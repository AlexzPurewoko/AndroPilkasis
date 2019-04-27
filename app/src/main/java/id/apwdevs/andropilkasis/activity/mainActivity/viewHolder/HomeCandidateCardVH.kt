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
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import id.apwdevs.andropilkasis.R
import id.apwdevs.andropilkasis.params.PublicConfig
import id.apwdevs.andropilkasis.plugin.serverResponse.DataCandidates
import info.abdolahi.CircularMusicProgressBar

class HomeCandidateCardVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var isSelected: Boolean = false
        set(value) {
            if (field) setColor(UNSELECTED_COLOR)
            else setColor(SELECTED_COLOR)
            field = value
        }
    private val cardView: CardView = itemView.findViewById(R.id.home_adapter_id_candidate_layout)
    private val candidateID: TextView = itemView.findViewById(R.id.home_adapter_id_candidate_id)
    private val candidateName: TextView = itemView.findViewById(R.id.home_adapter_id_candidate_name)
    private val candidateClass: TextView = itemView.findViewById(R.id.home_adapter_id_candidate_class)
    private val buttonVisiMisi: Button = itemView.findViewById(R.id.home_adapter_id_candidate_visimisi)
    private val candidateImage: CircularMusicProgressBar = itemView.findViewById(R.id.home_adapter_id_candidate_img)

    fun setUp(candidateData: DataCandidates, selectedLeader: Boolean) {
        candidateID.text = candidateData.num_id.toString()
        candidateName.text = candidateData.nama
        candidateClass.text = candidateData.groups
        buttonVisiMisi.setOnClickListener {
            AlertDialog.Builder(itemView.context).apply {
                setTitle("Visi dan Misi")
                setMessage("Visi : \n${candidateData.visi}\n\nMisi : \n${candidateData.misi}")
                setPositiveButton("Oke") { dialog, _ ->
                    dialog.dismiss()
                }
            }.show()
        }
        // Load the image if the image isn't default
        if (!candidateData.avatar!!.equals(PublicConfig.DEFAULT_CANDIDATE_AVATAR)) {
            Picasso.get().load("${PublicConfig.ServerConfig.CANDIDATE_IMG_PATH}/${candidateData.avatar}")
                .into(candidateImage)
        }

        setColor(if (selectedLeader) SELECTED_COLOR else UNSELECTED_COLOR)
    }

    private fun setColor(contentColor: ContentColor) {
        candidateID.setTextColor(contentColor.textColor)
        candidateName.setTextColor(contentColor.textColor)
        candidateClass.setTextColor(contentColor.textColor)
        buttonVisiMisi.setTextColor(contentColor.textColor)
        buttonVisiMisi.setBackgroundColor(contentColor.buttonColor)
        cardView.setCardBackgroundColor(contentColor.cardBg)
    }

    private val UNSELECTED_COLOR = ContentColor(Color.parseColor("#FFFD1717"), Color.WHITE, Color.parseColor("#A21111"))
    private val SELECTED_COLOR = ContentColor(Color.parseColor("#FF2196F3"), Color.WHITE, Color.parseColor("#FF0B77CA"))

    private data class ContentColor(
        val cardBg: Int,
        val textColor: Int,
        val buttonColor: Int
    )
}