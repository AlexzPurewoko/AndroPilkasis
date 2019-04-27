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
import androidx.recyclerview.widget.RecyclerView
import id.apwdevs.andropilkasis.R
import id.apwdevs.andropilkasis.activity.mainActivity.viewHolder.RealCountCandidateVH
import id.apwdevs.andropilkasis.plugin.serverResponse.CandidateCountData

class RealCountCandidateAdapter(
    private val realCountData: MutableList<CandidateCountData>,
    var countTotalUser: Int
) : RecyclerView.Adapter<RealCountCandidateVH>() {

    init {
        setHasStableIds(true)
    }

    override fun getItemId(position: Int): Long = position.toLong()
    override fun getItemViewType(position: Int): Int = position

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RealCountCandidateVH {
        val layout = LayoutInflater.from(parent.context).inflate(R.layout.realcount_adapter_candidates, parent, false)
        return RealCountCandidateVH(layout)
    }

    override fun getItemCount(): Int = realCountData.size

    override fun onBindViewHolder(holder: RealCountCandidateVH, position: Int) {
        val selectedCandidate = realCountData[position]
        holder.setUp(selectedCandidate, countTotalUser)
    }

}