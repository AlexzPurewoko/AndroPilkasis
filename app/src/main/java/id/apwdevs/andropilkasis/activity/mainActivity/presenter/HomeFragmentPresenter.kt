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

import id.apwdevs.andropilkasis.activity.mainActivity.model.HomeFragmentModel
import id.apwdevs.andropilkasis.plugin.CoroutineContextProvider
import id.apwdevs.andropilkasis.plugin.serverResponse.SelectCandidates
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.lang.ref.WeakReference

class HomeFragmentPresenter(
    model: HomeFragmentModel,
    private val coroutineContext: CoroutineContextProvider = CoroutineContextProvider()
) {
    private val modelWeak: WeakReference<HomeFragmentModel> = WeakReference(model)

    fun submit(username: String, password: String, selectedID: Int) {
        modelWeak.get()?.onSubmit()
        GlobalScope.launch(coroutineContext.main) {
            val updateResult = SelectCandidates.select(username, password, selectedID).await()
            delay(1000)
            //post {
            if (updateResult.updateResult) {
                modelWeak.get()?.onSubmitSuccess(updateResult.updateMessage!!, selectedID)
            } else {
                if (updateResult.exceptionIfOccur != null)
                    modelWeak.get()?.onSubmitFailed(updateResult.exceptionIfOccur)
                else
                    modelWeak.get()?.onSubmitFailed(UnknownError("An unknown error ocurred when saving your selected leader"))
            }
            //}
        }
    }

}