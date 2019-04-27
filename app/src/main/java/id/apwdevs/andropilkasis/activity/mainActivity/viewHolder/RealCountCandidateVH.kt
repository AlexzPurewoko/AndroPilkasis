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

package id.apwdevs.andropilkasis.activity.mainActivity.viewHolder

import android.graphics.Color
import android.view.View
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import id.apwdevs.andropilkasis.R
import id.apwdevs.andropilkasis.params.PublicConfig
import id.apwdevs.andropilkasis.plugin.serverResponse.CandidateCountData
import info.abdolahi.CircularMusicProgressBar

class RealCountCandidateVH(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val candidateID: TextView = itemView.findViewById(R.id.realcount_adapter_id_candidate_id)
    private val candidateName: TextView = itemView.findViewById(R.id.realcount_adapter_id_candidate_name)
    private val candidateClass: TextView = itemView.findViewById(R.id.realcount_adapter_id_candidate_class)
    private val description: TextView = itemView.findViewById(R.id.realcount_adapter_id_candidate_description)
    private val percentage: TextView = itemView.findViewById(R.id.realcount_adapter_id_candidate_percentage)
    private val candidateImage: CircularMusicProgressBar =
        itemView.findViewById(R.id.realcount_adapter_id_candidate_img)
    private val candidateCard: CardView = itemView.findViewById(R.id.realcount_adapter_id_candidate_layout)

    companion object {
        private val LESS_PERCENTAGE = ContentColor(Color.RED, Color.WHITE, Color.WHITE)
        private val MIDDLE_PERCENTAGE = ContentColor(Color.parseColor("#FFFF8400"), Color.WHITE, Color.WHITE)
        private val TOP_PERCENTAGE = ContentColor(Color.BLUE, Color.YELLOW, Color.WHITE)
    }

    fun setUp(realCountData: CandidateCountData, countTotalUser: Int) {

        candidateID.text = realCountData.numID.toString()
        candidateName.text = realCountData.nama
        candidateClass.text = realCountData.group
        description.text = "Terpilih ${realCountData.countSelected} suara dari $countTotalUser suara"

        val percent =
            if (realCountData.countSelected == 0) 0
            else
                realCountData.countSelected * 100 / countTotalUser
        percentage.text = "$percent %"
        candidateImage.setValue(percent.toFloat())

        // Load the image if the image isn't default
        if (realCountData.avatar!! != PublicConfig.DEFAULT_CANDIDATE_AVATAR) {
            Picasso.get().load("${PublicConfig.ServerConfig.CANDIDATE_IMG_PATH}/${realCountData.avatar}")
                .into(candidateImage)
        }
        // sets the color of cardView and another View
        setContentColor(
            when {
                percent in 0..40 -> LESS_PERCENTAGE
                percent in 42..69 -> MIDDLE_PERCENTAGE
                else -> TOP_PERCENTAGE
            }
        )
    }

    private fun setContentColor(contentColor: ContentColor) {
        candidateID.setTextColor(contentColor.textColor)
        candidateName.setTextColor(contentColor.textColor)
        candidateClass.setTextColor(contentColor.textColor)
        percentage.setTextColor(contentColor.textColor)
        description.setTextColor(contentColor.textColor)
        candidateImage.setBorderProgressColor(contentColor.progressBgColor)
        candidateCard.setCardBackgroundColor(contentColor.cardBgColor)
    }

    private data class ContentColor(
        val cardBgColor: Int,
        val progressBgColor: Int,
        val textColor: Int
    )
}