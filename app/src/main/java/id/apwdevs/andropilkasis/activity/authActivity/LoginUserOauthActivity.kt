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

package id.apwdevs.andropilkasis.activity.authActivity

import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import id.apwdevs.andropilkasis.R
import id.apwdevs.andropilkasis.activity.authActivity.fragment.FillFormOauthFragment
import id.apwdevs.andropilkasis.activity.authActivity.fragment.LoadingOauthFragment
import id.apwdevs.andropilkasis.activity.authActivity.model.LoginCallback
import id.apwdevs.andropilkasis.activity.authActivity.model.LoginUserOauthModel
import id.apwdevs.andropilkasis.activity.authActivity.model.OnUserFailedLogin
import id.apwdevs.andropilkasis.activity.authActivity.presenter.LoginUserOauthPresenter
import id.apwdevs.andropilkasis.activity.mainActivity.MainUserActivity
import id.apwdevs.andropilkasis.plugin.serverResponse.PasswordException
import id.apwdevs.andropilkasis.plugin.serverResponse.UserAccountNotRegisteredException
import kotlinx.android.synthetic.main.login_user_oauth.*

class LoginUserOauthActivity : AppCompatActivity(), LoginUserOauthModel, LoginCallback {
    val fragments = arrayOf(
        LoadingOauthFragment(),
        FillFormOauthFragment.newInstance(this)
    )
    private lateinit var presenter: LoginUserOauthPresenter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_user_oauth)

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        presenter = LoginUserOauthPresenter(this, this, Handler(Looper.getMainLooper()))
        presenter.run()
    }

    fun switchFragment(fragment: Fragment, fragmentTag: String) {
        val fragmentManager = supportFragmentManager.beginTransaction()
        fragmentManager.replace(R.id.oauthlogin_id_frameholder, fragment, fragmentTag)
        fragmentManager.commit()
    }

    override fun initializeUI() {
        val colorDark =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                getColor(R.color.colorPrimaryDark)
            else
                resources.getColor(R.color.colorPrimaryDark)
        // Tinting status bar
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            window.statusBarColor = colorDark
        // drawing a AndroPilkasis text's
        val titleText = oauthlogin_id_title.text
        val spannableString = SpannableString(titleText)
        spannableString.setSpan(
            ForegroundColorSpan(
                colorDark
            ),
            5,
            titleText.length,
            0
        )
        spannableString.setSpan(
            android.text.style.StyleSpan(Typeface.BOLD),
            0, titleText.length, 0
        )
        oauthlogin_id_title.text = spannableString
    }

    override fun onLoadingOauth() {
        switchFragment(fragments[0], "LoadingFragment")
    }

    override fun requestLogin() {
        switchFragment(fragments[1], "RequestLogin")
    }

    override fun onFailedOauth(ex: Throwable?) {
        switchFragment(fragments[1], "RequestLogin")
        if (ex == null) return
        when (ex) {
            is UserAccountNotRegisteredException -> {
                (fragments[1] as OnUserFailedLogin).onUserNotRegistered(ex.message!!)
            }
            is PasswordException -> {
                (fragments[1] as OnUserFailedLogin).onUnMatchPassword(ex.message!!)
            }
            else -> {
                AlertDialog.Builder(this).apply {
                    setIcon(android.R.drawable.ic_dialog_alert)
                    setTitle(ex.javaClass.simpleName)
                    setMessage(ex.message)
                    setPositiveButton("Okay") { dialog, _ ->
                        dialog.dismiss()
                        finish()
                    }
                }.show()
            }
        }

    }

    override fun onOauthSuccess() {

    }

    override fun onFinished(data: Bundle) {
        val intent = Intent(this, MainUserActivity::class.java)
        intent.putExtras(data)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)

    }

    override fun submitLogin(username: String, password: String) {
        presenter.submitLogin(username, password)
    }
}
