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

package id.apwdevs.andropilkasis.activity.mainActivity.presenter

import android.os.Handler
import id.apwdevs.andropilkasis.activity.mainActivity.model.RealCountFragmentModel
import id.apwdevs.andropilkasis.params.PublicConfig
import id.apwdevs.andropilkasis.plugin.CoroutineContextProvider
import id.apwdevs.andropilkasis.plugin.serverResponse.AllowedUser
import id.apwdevs.andropilkasis.plugin.serverResponse.GetAllGroupList
import id.apwdevs.andropilkasis.plugin.serverResponse.GetRealCount
import id.apwdevs.andropilkasis.plugin.serverResponse.GetServerParams
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.lang.ref.WeakReference

class RealCountFragmentPresenter(
    model: RealCountFragmentModel,
    private val uiHandler: Handler,
    private val coroutineContext: CoroutineContextProvider = CoroutineContextProvider()
) {
    private val modelWeak: WeakReference<RealCountFragmentModel> = WeakReference(model)
    fun refresh(username: String, password: String, category: String, userGroups: String) {
        modelWeak.get()?.onLoadStarted()
        GlobalScope.launch(coroutineContext.main) {
            // check user is allowed or not accessing RealCount data
            if (allowUserToAccessRealCount(username, password, userGroups).await()) {
                val listGroup = GetAllGroupList.getAll(username, password).await()
                val serverResponse = (
                        if (category == PublicConfig.ALL_CATEGORIES)
                            GetRealCount.getRealCount(username, password)
                        else
                            GetRealCount.getRealCountByCategory(username, password, category)
                        ).await()
                post {
                    when {
                        listGroup.exceptionIfOccur != null ->
                            modelWeak.get()?.onLoadFailed(listGroup.exceptionIfOccur)
                        serverResponse.exceptionIfOccur != null ->
                            modelWeak.get()?.onLoadFailed(serverResponse.exceptionIfOccur)
                        else ->
                            modelWeak.get()?.onLoadFinished(serverResponse, listGroup)
                    }

                }

            } else post {
                modelWeak.get()?.onNotPermitedAccess()
            }
        }
    }

    private fun allowUserToAccessRealCount(username: String, password: String, userGroups: String): Deferred<Boolean> =
        GlobalScope.async {
            val allowedUser = AllowedUser.hashMapIsAllowed(username, password, userGroups).await()
            val serverResponse = GetServerParams.getAll().await()
            if (serverResponse == null) return@async false
            val allowAccess = serverResponse[PublicConfig.ServerParams.KEY_ALLOW_ACCESS_REALCOUNT]
            if (allowAccess == null) return@async false
            if (allowedUser != null) {
                if (allowedUser[AllowedUser.KEY_HAS_PERMISSION]!! && allowedUser[AllowedUser.KEY_HAS_SELECTED_LEADER]!!)
                    return@async true
                else if (allowAccess.toBoolean() && allowedUser[AllowedUser.KEY_HAS_SELECTED_LEADER]!!) {
                    return@async true
                }
            }

            false
        }

    private fun post(run: () -> Unit) {
        uiHandler.post(run)
    }
}