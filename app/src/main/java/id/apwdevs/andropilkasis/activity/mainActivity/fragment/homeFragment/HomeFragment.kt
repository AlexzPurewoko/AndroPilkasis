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

package id.apwdevs.andropilkasis.activity.mainActivity.fragment.homeFragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import id.apwdevs.andropilkasis.R
import id.apwdevs.andropilkasis.activity.mainActivity.model.HomeFragmentModel
import id.apwdevs.andropilkasis.activity.mainActivity.presenter.HomeFragmentPresenter
import id.apwdevs.andropilkasis.activity.mainActivity.viewAdapter.ListCandidateAdapter
import id.apwdevs.andropilkasis.params.PublicConfig
import id.apwdevs.andropilkasis.plugin.SimpleProgressDialog
import id.apwdevs.andropilkasis.plugin.serverResponse.DataCandidates
import id.apwdevs.andropilkasis.plugin.serverResponse.ListCandidates
import id.apwdevs.andropilkasis.plugin.serverResponse.UserPilkasisData


class HomeFragment : Fragment(), HomeFragmentModel {

    private val listCandidate: MutableList<DataCandidates> = mutableListOf()
    private lateinit var userDataSheet: UserPilkasisData
    private var hasSelectedLeader: Int = 0
    private var disableSubmitBtnTouch = false
    private lateinit var candidateAdapter: ListCandidateAdapter
    private lateinit var presenter: HomeFragmentPresenter

    // View space
    private lateinit var scrollView: ScrollView
    private lateinit var submitButton: FloatingActionButton
    private lateinit var recyclerView: RecyclerView
    private lateinit var emoteText: TextView
    private lateinit var stateDesc: TextView
    private lateinit var progressDialog: SimpleProgressDialog


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (listCandidate.isEmpty()) {
            val data = arguments!!
            userDataSheet = data.getParcelable(PublicConfig.SharedKey.KEY_USER_DATASHEET)!!
            listCandidate.addAll(data.getParcelable<ListCandidates>(PublicConfig.SharedKey.KEY_ALL_CANDIDATE_LIST)?.listCandidate!!)
            hasSelectedLeader = userDataSheet.selectedLeader
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.home_fragment_main, container, false)
        submitButton = rootView.findViewById(R.id.home_fragment_id_submit)
        recyclerView = rootView.findViewById(R.id.home_fragment_id_recyclerview)
        emoteText = rootView.findViewById(R.id.adapter_id_showstatus_emotetext)
        stateDesc = rootView.findViewById(R.id.adapter_id_showstatus_description)
        scrollView = rootView.findViewById(R.id.home_fragment_id_scroll)
        candidateAdapter = ListCandidateAdapter(listCandidate, hasSelectedLeader > 0, hasSelectedLeader) {
            hasSelectedLeader = it
        }
        recyclerView.adapter = candidateAdapter
        recyclerView.layoutManager = LinearLayoutManager(context)

        // submit button setting
        disableSubmitBtnTouch = hasSelectedLeader > 0
        submitButton.setOnClickListener {
            if (!disableSubmitBtnTouch) {
                // submit methods...
                presenter.submit(
                    userDataSheet.userName!!,
                    userDataSheet.password!!,
                    candidateAdapter.selectedLeaderPosition
                )
            } else {
                Snackbar.make(rootView, "Maaf kamu sudah memlih!", Snackbar.LENGTH_SHORT).show()
            }
        }

        // update the state
        updateState()

        // set the presenter
        presenter = HomeFragmentPresenter(this)

        // set up the progressDialog
        progressDialog = SimpleProgressDialog(context!!)
        progressDialog.stateTextContent = "Tunggu Sebentar..."
        scrollView.smoothScrollTo(0, 0)
        return rootView
    }

    override fun onResume() {
        super.onResume()
        scrollView.fullScroll(ScrollView.FOCUS_UP)
    }

    override fun onSubmitFailed(e: Throwable) {
        progressDialog.hideDialog()
        hasSelectedLeader = 0
        updateState()
        AlertDialog.Builder(context!!).apply {
            setTitle(e.javaClass.simpleName)
            setMessage(e.javaClass.simpleName)
            setPositiveButton("Okay") { dialog, _ ->
                dialog.dismiss()
            }
        }.show()
    }

    override fun onSubmitSuccess(state: String, idCandidate: Int) {
        progressDialog.hideDialog()
        hasSelectedLeader = idCandidate
        disableSubmitBtnTouch = true
        candidateAdapter.hasLockedLeaderSelection = true
        candidateAdapter.selectedLeaderPosition = idCandidate
        updateState()
        candidateAdapter.notifyDataSetChanged()
        disableSubmitBtnTouch = true

    }

    override fun onSubmit() {
        progressDialog.showDialog()
    }

    fun updateState() {
        when (hasSelectedLeader) {
            0 -> {
                emoteText.text = ":("
                stateDesc.text = "Yahhh kok Golput..\nMilih dong salah satu"
            }
            else -> {
                emoteText.text = ":)"
                stateDesc.text = "Horeee udah milih!\nJangan milih lagi yaaa.."
            }
        }

    }

    companion object {

        @JvmStatic
        fun newInstance(packet: Bundle) =
            HomeFragment().apply {
                arguments = packet
            }
    }
}
