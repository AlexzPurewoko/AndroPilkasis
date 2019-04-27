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

package id.apwdevs.andropilkasis.activity.mainActivity

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.set
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import id.apwdevs.andropilkasis.R
import id.apwdevs.andropilkasis.activity.mainActivity.fragment.homeFragment.HomeFragment
import id.apwdevs.andropilkasis.activity.mainActivity.fragment.realCountFragment.RealCountFragment
import id.apwdevs.andropilkasis.activity.mainActivity.fragment.userFragment.UserFragment
import id.apwdevs.andropilkasis.params.PublicConfig
import id.apwdevs.andropilkasis.plugin.serverResponse.UserPilkasisData
import kotlinx.android.synthetic.main.main_user_activity.*

class MainUserActivity : AppCompatActivity() {

    private lateinit var fragmentSectionAdapter: FragmentSectionAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_user_activity)
        // tint the status bar
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            window.statusBarColor = Color.GRAY

        // set the action bar
        main_toolbar.background = window.decorView.background
        val span = SpannableString("AndroPilkasis")
        span.set(0, 5, ForegroundColorSpan(Color.GRAY))
        span.set(5, span.length, ForegroundColorSpan(getColorRes(R.color.colorPrimaryDark)))
        main_toolbar.title = span
        setSupportActionBar(main_toolbar)

        fragmentSectionAdapter = FragmentSectionAdapter(supportFragmentManager, intent.extras)
        main_id_viewpager_holder.adapter = fragmentSectionAdapter
        main_id_viewpager_holder.currentItem = 0
        main_id_viewpager_holder.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
                main_bottom_nav.selectedItemId = when (position) {
                    0 -> R.id.navigation_home
                    1 -> R.id.navigation_realcount
                    else -> R.id.navigation_user
                }
            }

        })

        main_bottom_nav.setOnNavigationItemSelectedListener {

            main_id_viewpager_holder.currentItem = when (it.itemId) {
                R.id.navigation_home -> 0
                R.id.navigation_realcount -> 1
                else -> 2
            }
            true
        }


    }

    private fun getColorRes(resId: Int): Int =
        when {
            Build.VERSION.SDK_INT > Build.VERSION_CODES.M ->
                getColor(resId)
            else -> resources.getColor(resId)
        }

    inner class FragmentSectionAdapter(fm: FragmentManager, extraBundle: Bundle) : FragmentStatePagerAdapter(fm) {

        private val fragments = arrayOf(
            HomeFragment.newInstance(extraBundle),
            RealCountFragment.newInstance(extraBundle[PublicConfig.SharedKey.KEY_USER_DATASHEET] as UserPilkasisData),
            UserFragment.newInstance(extraBundle[PublicConfig.SharedKey.KEY_USER_DATASHEET] as UserPilkasisData)
        )

        override fun getItem(position: Int): Fragment = fragments[position]

        override fun getCount(): Int = fragments.size

    }
}