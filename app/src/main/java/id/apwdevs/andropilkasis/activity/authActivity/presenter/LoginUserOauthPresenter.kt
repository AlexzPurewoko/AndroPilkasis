/**
 * Copyright @2019 by Alexzander Purwoko Widiantoro <purwoko908@gmail.com>
 * @author APWDevs
 *
 * Licensed under GNU GPLv3
 *
 * This module is provided by "AS IS" and if you want to take
 * a copy or modifying this module, you must include this @author
 * Thanks! Happy Coding!
 */

package id.apwdevs.andropilkasis.activity.authActivity.presenter

import android.content.Context
import android.os.Bundle
import android.os.Handler
import androidx.core.content.edit
import id.apwdevs.andropilkasis.activity.authActivity.LoginUserOauthActivity
import id.apwdevs.andropilkasis.activity.authActivity.model.LoginCallback
import id.apwdevs.andropilkasis.activity.authActivity.model.LoginUserOauthModel
import id.apwdevs.andropilkasis.params.PublicConfig
import id.apwdevs.andropilkasis.plugin.CheckConnections
import id.apwdevs.andropilkasis.plugin.CoroutineContextProvider
import id.apwdevs.andropilkasis.plugin.serverResponse.GatherUserData
import id.apwdevs.andropilkasis.plugin.serverResponse.GetAllCandidates
import id.apwdevs.andropilkasis.plugin.serverResponse.LoginUserOauth
import kotlinx.coroutines.*
import java.lang.ref.WeakReference
import java.net.UnknownHostException

class LoginUserOauthPresenter(
    ctx: LoginUserOauthActivity,
    model: LoginUserOauthModel,
    private val uiThread: Handler,
    private val contextPool: CoroutineContextProvider = CoroutineContextProvider()
) : LoginCallback {

    private val activity = WeakReference<LoginUserOauthActivity>(ctx)
    private val modelWeak = WeakReference<LoginUserOauthModel>(model)
    private val uiThreadWeak = WeakReference<Handler>(uiThread)
    private val sharedPref = activity.get()?.getSharedPreferences(PublicConfig.SHARED_PREF_NAME, Context.MODE_PRIVATE)

    fun run() {
        modelWeak.get()?.initializeUI()
        modelWeak.get()?.onLoadingOauth()
        GlobalScope.launch(contextPool.main) {
            val isConnected =
                CheckConnections().isConnected(activity.get(), PublicConfig.ServerConfig.SERVER_URL, 8000).await()
            if (isConnected) {
                // check if the user have login in a past time
                val username = sharedPref?.getString(PublicConfig.SharedKey.KEY_USERNAME, null)
                val password = sharedPref?.getString(PublicConfig.SharedKey.KEY_PASSWORD, null)

                if (username == null && password == null) {
                    modelWeak.get()?.requestLogin()
                } else {
                    login(username!!, password!!, false).await()
                }
            } else post {
                modelWeak.get()
                    ?.onFailedOauth(UnknownHostException("Cannot tell into the server! where the URL is ${PublicConfig.ServerConfig.SERVER_URL}"))
            }
        }
    }

    private fun collectData(username: String, password: String): Deferred<Bundle?> = GlobalScope.async {
        val userData = GatherUserData.getUserData(username, password).await()
        val allCandidates = GetAllCandidates.getList(username, password).await()
        when {
            userData.exceptionIfOccur != null -> {
                post {
                    modelWeak.get()?.onFailedOauth(userData.exceptionIfOccur)
                }
                return@async null
            }
            allCandidates.exceptionIfOccur != null -> {
                post {
                    modelWeak.get()?.onFailedOauth(allCandidates.exceptionIfOccur)
                }
                return@async null
            }
        }

        if (userData.isAuthSuccess) {
            val data = Bundle()
            data.putParcelable(PublicConfig.SharedKey.KEY_USER_DATASHEET, userData)
            data.putParcelable(PublicConfig.SharedKey.KEY_ALL_CANDIDATE_LIST, allCandidates)
            delay(1000)
            post {
                modelWeak.get()?.onFinished(data)
            }
            return@async data
        }
        null
    }

    fun login(username: String, password: String, isFirstLogin: Boolean) = GlobalScope.async {
        val oauthResult = LoginUserOauth.login(username, password).await()
        if (!oauthResult.isUserRegistered || oauthResult.exceptionIfOccur != null) post {
            modelWeak.get()?.onFailedOauth(oauthResult.exceptionIfOccur)
        } else {
            //modelWeak.get()?.onOauthSuccess()
            val data = collectData(username, password).await()
            if (data != null && isFirstLogin) {
                sharedPref?.edit {
                    putString(PublicConfig.SharedKey.KEY_USERNAME, username)
                    putString(PublicConfig.SharedKey.KEY_PASSWORD, password)
                }
                return@async
            }
        }
    }

    fun post(run: () -> Unit?) {
        uiThreadWeak.get()?.post {
            run(run)
        }
    }

    override fun submitLogin(username: String, password: String) {
        GlobalScope.launch(contextPool.main) {
            post {
                modelWeak.get()?.onLoadingOauth()
            }
            delay(1200)

            login(username, password, true).await()
        }
    }

}