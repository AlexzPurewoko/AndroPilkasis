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

package id.apwdevs.andropilkasis.activity.mainActivity.fragment.userFragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import com.squareup.picasso.Picasso
import id.apwdevs.andropilkasis.R
import id.apwdevs.andropilkasis.params.PublicConfig
import id.apwdevs.andropilkasis.plugin.serverResponse.UserPilkasisData
import info.abdolahi.CircularMusicProgressBar


class UserFragment : Fragment() {

    private lateinit var userPhoto: CircularMusicProgressBar
    private lateinit var userFullName: TextView
    private lateinit var userGroups: TextView
    private lateinit var cardLogout: CardView

    private lateinit var userDataSheet: UserPilkasisData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userDataSheet = arguments!![PublicConfig.SharedKey.KEY_USER_DATASHEET] as UserPilkasisData
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val layout = inflater.inflate(R.layout.user_fragment_main, container, false)
        userPhoto = layout.findViewById(R.id.user_fragment_id_userimage)
        userFullName = layout.findViewById(R.id.user_fragment_id_fullname)
        userGroups = layout.findViewById(R.id.user_fragment_id_groups)
        cardLogout = layout.findViewById(R.id.user_fragment_id_cardlogout)

        initUI()
        return layout
    }

    private fun initUI() {
        userFullName.text = userDataSheet.name
        userGroups.text = userDataSheet.userClass
        if (userDataSheet.avatar != PublicConfig.DEFAULT_USER_AVATAR) {
            Picasso.get()
                .load("${PublicConfig.ServerConfig.USER_IMG_PATH}/${userDataSheet.userClass}/${userDataSheet.avatar}")
                .into(userPhoto)
        }
        cardLogout.setOnClickListener {
            context?.getSharedPreferences(PublicConfig.SHARED_PREF_NAME, Context.MODE_PRIVATE)?.edit(commit = true) {
                remove(PublicConfig.SharedKey.KEY_USERNAME)
                remove(PublicConfig.SharedKey.KEY_PASSWORD)
            }
            Toast.makeText(context, "Merestart Aplikasi", Toast.LENGTH_LONG).show()
            val i = activity?.packageManager?.getLaunchIntentForPackage(activity!!.packageName)
            i?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            i?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            activity?.startActivity(i)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(userDataSheet: UserPilkasisData): UserFragment =
            UserFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(PublicConfig.SharedKey.KEY_USER_DATASHEET, userDataSheet)
                }
            }
    }
}