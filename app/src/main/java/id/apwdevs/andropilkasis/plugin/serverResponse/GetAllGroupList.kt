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

object GetAllGroupList {
    const val JSON_TAG = "groups"
    const val AUTH_CONDITION = "isAuthSuccess"
    const val GROUP_NAMELIST = "group_list"
    fun getAll(username: String, password: String): Deferred<ListGroupData> = GlobalScope.async {
        val postParams = hashMapOf(
            PublicConfig.SharedKey.KEY_USERNAME to username,
            PublicConfig.SharedKey.KEY_PASSWORD to password
        )
        val userJSON = GetServerData.sendPostRequest(
            PublicConfig.ServerConfig.LIST_GROUPS,
            postParams,
            PublicConfig.ServerConfig.SERVER_TIMEOUT
        ).await()

        try {
            val mRetJSON = JSONObject(userJSON).getJSONObject(JSON_TAG)
            val authSuccess = mRetJSON.getString(AUTH_CONDITION)?.toBoolean() ?: false

            if (authSuccess) {
                val listData = mRetJSON.getJSONArray(GROUP_NAMELIST)
                val listGroup = mutableListOf<String>()

                var index = 0
                while (index < listData.length()) {
                    listGroup.add(listData.getString(index))
                    index++
                }

                ListGroupData(authSuccess, null, listGroup)
            } else {
                ListGroupData(false, null, null)
            }
        } catch (e: Throwable) {
            ListGroupData(false, e, null)
        }
    }
}


data class ListGroupData(
    val isAuthSuccess: Boolean,
    val exceptionIfOccur: Throwable?,
    val listGroup: List<String>?
)