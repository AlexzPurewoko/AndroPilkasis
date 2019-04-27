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
import org.json.JSONObject

object GetRealCount {
    private const val JSON_TAG = "realcount"
    private const val AUTH_CONDITION = "isAuthSuccess"
    private const val KEY_COUNT_TOTAL_USER = "count_user_total"
    private const val KEY_COUNT_GOLPUT = "count_golput"
    private const val KEY_COUNT_SELECTED = "count_selected"
    private const val KEY_NUM_ID = "num_id"
    private const val KEY_NAME = "nama"
    private const val KEY_GROUPS = "group"
    private const val KEY_VISI = "visi"
    private const val KEY_MISI = "misi"
    private const val KEY_AVATAR = "avatar"
    const val CATEGORY_FILTER_KEY = "category_filter"
    fun getRealCount(username: String, password: String): Deferred<RealCountData> =
        getResult(
            hashMapOf(
                PublicConfig.SharedKey.KEY_USERNAME to username,
                PublicConfig.SharedKey.KEY_PASSWORD to password
            ),
            PublicConfig.ServerConfig.REALCOUNT_ALL
        )

    fun getRealCountByCategory(username: String, password: String, groupFilterName: String): Deferred<RealCountData> =
        getResult(
            hashMapOf(
                PublicConfig.SharedKey.KEY_USERNAME to username,
                PublicConfig.SharedKey.KEY_PASSWORD to password,
                CATEGORY_FILTER_KEY to groupFilterName
            ),
            PublicConfig.ServerConfig.REALCOUNT_BY_CATEGORY
        )

    private fun getResult(params: HashMap<String, String>, urlString: String): Deferred<RealCountData> =
        GlobalScope.async {
            val userJSON = GetServerData.sendPostRequest(
                urlString,
                params,
                PublicConfig.ServerConfig.SERVER_TIMEOUT
            ).await()
            try {
                val mRetJSON = JSONObject(userJSON).getJSONObject(JSON_TAG)
                val authSuccess = mRetJSON.getString(AUTH_CONDITION)?.toBoolean() ?: false

                if (authSuccess) {
                    val listData = mutableListOf<CandidateCountData>()
                    val countTotalUser = mRetJSON.getString(KEY_COUNT_TOTAL_USER).toInt()
                    val countGolput = mRetJSON.getString(KEY_COUNT_GOLPUT).toInt()

                    var index = 0
                    while (mRetJSON.has(index.toString())) {
                        val dataObject = mRetJSON.getJSONObject(index.toString())
                        listData.add(
                            CandidateCountData(
                                dataObject.getInt(KEY_NUM_ID),
                                dataObject.getString(KEY_NAME),
                                dataObject.getString(KEY_GROUPS),
                                dataObject.getString(KEY_VISI),
                                dataObject.getString(KEY_MISI),
                                dataObject.getString(KEY_AVATAR),
                                dataObject.getInt(KEY_COUNT_SELECTED)
                            )
                        )
                        index++
                    }


                    RealCountData(
                        null,
                        true,
                        countTotalUser,
                        countGolput,
                        listData
                    )
                } else {
                    RealCountData(
                        null,
                        false,
                        0,
                        0,
                        null
                    )
                }
            } catch (e: Throwable) {
                RealCountData(
                    e,
                    false,
                    0,
                    0,
                    null
                )
            }
        }
}

data class CandidateCountData(
    val numID: Int,
    val nama: String?,
    val group: String?,
    val visi: String?,
    val misi: String?,
    val avatar: String?,
    val countSelected: Int
)

data class RealCountData(
    val exceptionIfOccur: Throwable?,
    val isAuthSuccess: Boolean,
    val countTotalUser: Int,
    val countGolput: Int,
    val listCandidates: List<CandidateCountData>?
)