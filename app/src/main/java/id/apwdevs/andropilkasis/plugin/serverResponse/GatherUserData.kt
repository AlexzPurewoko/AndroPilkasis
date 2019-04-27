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

import android.os.Parcelable
import id.apwdevs.andropilkasis.params.PublicConfig
import id.apwdevs.andropilkasis.plugin.GetServerData
import kotlinx.android.parcel.Parcelize
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import org.json.JSONObject

object GatherUserData {
    private const val JSON_TAG = "userDatasheet"
    private const val AUTH_CONDITION = "isAuthSuccess"
    private const val KEY_NUM_ID = "num_id"
    private const val KEY_NAME = "nama"
    private const val KEY_USERCLASS = "kelas"
    private const val KEY_USERNAME = "username"
    private const val KEY_AVATAR = "avatar"
    private const val KEY_SELECTED_LEADER = "pilih_ketua_id"

    fun getUserData(username: String, password: String): Deferred<UserPilkasisData> =
        GlobalScope.async {
            // build the data into hashmaps and send it into server

            val postParams = hashMapOf<String, String>(
                PublicConfig.SharedKey.KEY_USERNAME to username,
                PublicConfig.SharedKey.KEY_PASSWORD to password
            )

            val userJSON = GetServerData.sendPostRequest(
                PublicConfig.ServerConfig.GET_USER_DATASHEET,
                postParams,
                PublicConfig.ServerConfig.SERVER_TIMEOUT
            ).await()

            try {
                val mRetJSON = JSONObject(userJSON).getJSONObject(JSON_TAG)
                val authSuccess = mRetJSON.getString(AUTH_CONDITION)?.toBoolean() ?: false

                if (authSuccess) {
                    val listData = mRetJSON.getJSONObject("0")
                    val numID = listData.getInt(KEY_NUM_ID)
                    val name = listData.getString(KEY_NAME)
                    val userClass = listData.getString(KEY_USERCLASS)
                    val userName = listData.getString(KEY_USERNAME)
                    val avatar = listData.getString(KEY_AVATAR)
                    val selectedLeader = listData.getInt(KEY_SELECTED_LEADER)

                    UserPilkasisData(
                        authSuccess,
                        numID,
                        name,
                        userClass,
                        userName,
                        password,
                        avatar,
                        selectedLeader,
                        null
                    )
                } else {
                    UserPilkasisData(authSuccess, 0, null, null, null, null, null, 0, null)
                }
            } catch (e: Throwable) {
                UserPilkasisData(false, 0, null, null, null, null, null, 0, e)
            }
        }
}

@Parcelize
data class UserPilkasisData(
    val isAuthSuccess: Boolean,
    val userID: Int,
    val name: String?,
    val userClass: String?,
    val userName: String?,
    val password: String?,
    val avatar: String?,
    val selectedLeader: Int,
    val exceptionIfOccur: Throwable?
) : Parcelable

