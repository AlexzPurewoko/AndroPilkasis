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

package id.apwdevs.andropilkasis.activity.mainActivity.viewAdapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import id.apwdevs.andropilkasis.R
import id.apwdevs.andropilkasis.activity.mainActivity.viewHolder.HomeCandidateCardVH
import id.apwdevs.andropilkasis.plugin.serverResponse.DataCandidates

class ListCandidateAdapter(
    private val listCandidates: List<DataCandidates>,
    var hasLockedLeaderSelection: Boolean = false,
    var selectedLeaderPosition: Int = 0,
    private val onItemClick: (position: Int) -> Unit
) : RecyclerView.Adapter<HomeCandidateCardVH>() {

    private var notifyUpdateState: Boolean = false

    init {
        setHasStableIds(true)
    }

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getItemViewType(position: Int): Int = position
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeCandidateCardVH {
        val cardView = LayoutInflater.from(parent.context).inflate(R.layout.home_adapter_candidate_card, parent, false)
        return HomeCandidateCardVH(cardView)
    }

    override fun getItemCount(): Int {
        return listCandidates.size
    }

    override fun onBindViewHolder(holder: HomeCandidateCardVH, position: Int) {

        if (notifyUpdateState && holder.isSelected) {
            notifyUpdateState = false
            holder.isSelected = false
        } else {
            val dataCandidates = listCandidates[position]
            holder.setUp(dataCandidates, selectedLeaderPosition == position + 1)
            val card: CardView = holder.itemView as CardView
            card.setOnClickListener {
                if (!hasLockedLeaderSelection) {
                    if (holder.isSelected) {
                        holder.isSelected = false
                        selectedLeaderPosition = 0
                    } else {
                        if (selectedLeaderPosition > 0) {
                            notifyUpdateState = true
                            notifyItemChanged(selectedLeaderPosition - 1)
                        }
                        selectedLeaderPosition = position + 1
                        holder.isSelected = true
                    }
                }
            }
        }
    }

}