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

object GetServerParams {
    const val JSON_TAG = "parameters"
    const val KEY_PARAMS_NAME = "param"
    const val KEY_VALUE = "value"
    fun getAll(): Deferred<HashMap<String, String>?> = GlobalScope.async {
        val retHashMap = HashMap<String, String>()
        try {
            val retJSONStr = GetServerData.sendGetRequest(
                PublicConfig.ServerConfig.GET_LIST_PARAMETERS,
                PublicConfig.ServerConfig.SERVER_TIMEOUT
            ).await()
            val jsonObject = JSONObject(retJSONStr)
            val jsonArray = jsonObject.getJSONArray(JSON_TAG)
            var index = 0
            while (index < jsonArray.length()) {
                val paramObject = jsonArray.get(index++) as JSONObject
                retHashMap.put(
                    paramObject.getString(KEY_PARAMS_NAME),
                    paramObject.getString(KEY_VALUE)
                )
            }
        } catch (e: Throwable) {
            e.printStackTrace()
        }
        retHashMap
    }
}