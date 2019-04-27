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

package id.apwdevs.andropilkasis.activity.authActivity.model

import android.os.Bundle

interface LoginUserOauthModel {
    fun initializeUI()
    fun onLoadingOauth()
    fun requestLogin()
    fun onFailedOauth(ex: Throwable?)
    fun onOauthSuccess()
    fun onFinished(data: Bundle)
}