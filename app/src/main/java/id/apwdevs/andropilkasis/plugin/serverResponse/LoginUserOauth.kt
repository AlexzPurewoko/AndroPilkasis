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

package id.apwdevs.andropilkasis.plugin.serverResponse

import id.apwdevs.andropilkasis.params.PublicConfig
import id.apwdevs.andropilkasis.plugin.GetServerData
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import org.json.JSONException
import org.json.JSONObject

object LoginUserOauth {
    private const val TAG_NAME = "authResponse"
    private const val USER_AUTH_CONDITION_KEY = "isUserRegistered"
    private const val KEY_PASSWORD_VER_RESULT = "passwordVerification"

    /**
     * Authenticate User for login into application
     * Returns a LoginUserOauthData that indicate is success of not
     * besides
     */
    fun login(
        username: String,
        password: String,
        timeout: Int = PublicConfig.ServerConfig.SERVER_TIMEOUT
    ): Deferred<LoginUserOauthData> = GlobalScope.async {
        // build the data into hashmaps and send it into server

        val postParams = hashMapOf(
            PublicConfig.SharedKey.KEY_USERNAME to username,
            PublicConfig.SharedKey.KEY_PASSWORD to password
        )
        val oauthLogin =
            GetServerData.sendPostRequest(PublicConfig.ServerConfig.AUTH_LOGIN, postParams, timeout).await()
        val mJSONObject: JSONObject
        try {
            mJSONObject = JSONObject(oauthLogin).getJSONObject(TAG_NAME)
            val isRegistered = mJSONObject.getBoolean(USER_AUTH_CONDITION_KEY)
            var exceptionIfOccur: Throwable? = null
            if (isRegistered) {
                val pwdVerification = mJSONObject.getBoolean(KEY_PASSWORD_VER_RESULT)
                if (!pwdVerification) {
                    exceptionIfOccur = PasswordException("Password doesn't match with this account")
                }
            } else {
                exceptionIfOccur = UserAccountNotRegisteredException("Tidak ada Username $username dalam basis data")
            }
            LoginUserOauthData(isRegistered, exceptionIfOccur)

        } catch (e: JSONException) {
            LoginUserOauthData(false, e)
        }

    }
}

class PasswordException(message: String) : Exception(message)

class UserAccountNotRegisteredException(message: String) : Exception(message)

data class LoginUserOauthData(
    val isUserRegistered: Boolean,
    val exceptionIfOccur: Throwable? = null
)