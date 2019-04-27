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

object AllowedUser {
    const val JSON_TAG = "allow_access"
    const val KEY_HAS_PERMISSION = "hasAllowPermission"
    const val KEY_HAS_SELECTED_LEADER = "hasSelectedLeader"
    const val AUTH_CONDITION = "isAuthSuccess"
    const val KEY_GROUP_ID = "group_id"
    fun hashMapIsAllowed(username: String, passwd: String, group: String): Deferred<HashMap<String, Boolean>?> =
        GlobalScope.async {
            val postParams = hashMapOf(
                PublicConfig.SharedKey.KEY_USERNAME to username,
                PublicConfig.SharedKey.KEY_PASSWORD to passwd,
                KEY_GROUP_ID to group
            )
            val retHashMap = HashMap<String, Boolean>()
            try {
                val retJSONStr = GetServerData.sendPostRequest(
                    PublicConfig.ServerConfig.ALLOW_ACCESS_REALCOUNT,
                    postParams,
                    PublicConfig.ServerConfig.SERVER_TIMEOUT
                ).await()
                val jsonObject = JSONObject(retJSONStr).getJSONObject(JSON_TAG)
                val isAuthSuccess = jsonObject.getBoolean(AUTH_CONDITION)
                if (isAuthSuccess) {
                    retHashMap.put(
                        AUTH_CONDITION,
                        isAuthSuccess
                    )
                    retHashMap.put(
                        KEY_HAS_PERMISSION,
                        jsonObject.getBoolean(KEY_HAS_PERMISSION)
                    )
                    retHashMap.put(
                        KEY_HAS_SELECTED_LEADER,
                        jsonObject.getBoolean(KEY_HAS_SELECTED_LEADER)
                    )
                } else {
                    retHashMap.put(
                        AUTH_CONDITION,
                        false
                    )
                }
            } catch (e: Throwable) {
                e.printStackTrace()
            }
            retHashMap
        }
}