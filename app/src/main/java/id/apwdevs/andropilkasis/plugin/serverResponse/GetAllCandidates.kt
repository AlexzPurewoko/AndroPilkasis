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
import java.io.Serializable

object GetAllCandidates {
    const val JSON_TAG = "kandidat"
    const val AUTH_CONDITION = "isAuthSuccess"
    const val KEY_NUM_ID = "num_id"
    const val KEY_FULLNAME = "nama"
    const val KEY_USER_GROUPS = "kelas"
    const val KEY_VISI = "visi"
    const val KEY_MISSION = "misi"
    const val KEY_AVATAR = "avatar"

    fun getList(username: String, password: String): Deferred<ListCandidates> = GlobalScope.async {
        val postParams = hashMapOf(
            PublicConfig.SharedKey.KEY_USERNAME to username,
            PublicConfig.SharedKey.KEY_PASSWORD to password
        )
        val userJSON = GetServerData.sendPostRequest(
            PublicConfig.ServerConfig.GET_LIST_CANDIDATES,
            postParams,
            PublicConfig.ServerConfig.SERVER_TIMEOUT
        ).await()

        try {
            val mRetJSON = JSONObject(userJSON).getJSONObject(JSON_TAG)
            val authSuccess = mRetJSON.getString(AUTH_CONDITION)?.toBoolean() ?: false

            if (authSuccess) {
                val listData = mutableListOf<DataCandidates>()
                var index = 0
                while (mRetJSON.has(index.toString())) {
                    val dataObject = mRetJSON.getJSONObject(index.toString())
                    listData.add(
                        DataCandidates(
                            dataObject.getInt(KEY_NUM_ID),
                            dataObject.getString(KEY_FULLNAME),
                            dataObject.getString(KEY_USER_GROUPS),
                            dataObject.getString(KEY_VISI),
                            dataObject.getString(KEY_MISSION),
                            dataObject.getString(KEY_AVATAR)
                        )
                    )
                    index++
                }

                ListCandidates(null, authSuccess, listData)
            } else {
                ListCandidates(null, authSuccess, null)
            }
        } catch (e: Throwable) {
            ListCandidates(e, false, null)
        }
    }
}

data class DataCandidates(
    val num_id: Int,
    val nama: String?,
    val groups: String?,
    val visi: String?,
    val misi: String?,
    val avatar: String?
) : Serializable

@Parcelize
data class ListCandidates(
    val exceptionIfOccur: Throwable?,
    val isAuthSuccess: Boolean,
    val listCandidate: List<DataCandidates>?
) : Parcelable