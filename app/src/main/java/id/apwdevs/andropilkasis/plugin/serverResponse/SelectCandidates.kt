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

object SelectCandidates {

    private const val JSON_TAG = "selectKandidat"
    private const val KEY_AUTH_CONDITION = "isAuthSuccess"
    private const val KEY_UPDATE_RESULT = "updateResult"
    private const val KEY_UPDATE_MSG = "updateMessage"
    private const val KEY_SELECTED_CANDIDATES = "id_calon_ketua"

    fun select(username: String, password: String, id_candidates: Int): Deferred<ReturnedResult> = GlobalScope.async {
        // build the data into hashmaps and send it into server

        val postParams = hashMapOf<String, String>(
            PublicConfig.SharedKey.KEY_USERNAME to username,
            PublicConfig.SharedKey.KEY_PASSWORD to password,
            KEY_SELECTED_CANDIDATES to "$id_candidates"

        )

        val userJSON = GetServerData.sendPostRequest(
            PublicConfig.ServerConfig.SELECT_CANDIDATE,
            postParams,
            PublicConfig.ServerConfig.SERVER_TIMEOUT
        ).await()

        try {
            val mRetJSON = JSONObject(userJSON).getJSONObject(JSON_TAG)
            val authSuccess = mRetJSON.getString(KEY_AUTH_CONDITION)?.toBoolean() ?: false

            if (authSuccess) {
                val updateResult = mRetJSON.getString(KEY_UPDATE_RESULT)?.toBoolean() ?: false
                val updateMessage = mRetJSON.getString(KEY_UPDATE_MSG)
                ReturnedResult(null, true, updateResult, updateMessage)
            } else {
                ReturnedResult(null, false, false, null)
            }
        } catch (e: Throwable) {
            ReturnedResult(e, false, false, null)
        }
    }
}

data class ReturnedResult(
    val exceptionIfOccur: Throwable?,
    val isAuthSuccess: Boolean,
    val updateResult: Boolean,
    val updateMessage: String?
)