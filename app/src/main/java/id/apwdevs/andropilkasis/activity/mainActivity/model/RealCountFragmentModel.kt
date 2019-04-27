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

package id.apwdevs.andropilkasis.activity.mainActivity.model

import id.apwdevs.andropilkasis.plugin.serverResponse.ListGroupData
import id.apwdevs.andropilkasis.plugin.serverResponse.RealCountData

interface RealCountFragmentModel {
    fun onLoadStarted()
    fun onLoadFinished(realCountData: RealCountData, groupList: ListGroupData)
    fun onLoadFailed(e: Throwable)
    fun onNotPermitedAccess()
}