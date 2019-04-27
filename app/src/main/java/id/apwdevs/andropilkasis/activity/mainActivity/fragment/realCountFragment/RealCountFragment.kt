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

package id.apwdevs.andropilkasis.activity.mainActivity.fragment.realCountFragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.snackbar.Snackbar
import id.apwdevs.andropilkasis.R
import id.apwdevs.andropilkasis.activity.mainActivity.model.RealCountFragmentModel
import id.apwdevs.andropilkasis.activity.mainActivity.presenter.RealCountFragmentPresenter
import id.apwdevs.andropilkasis.activity.mainActivity.viewAdapter.RealCountCandidateAdapter
import id.apwdevs.andropilkasis.params.PublicConfig
import id.apwdevs.andropilkasis.plugin.gone
import id.apwdevs.andropilkasis.plugin.serverResponse.CandidateCountData
import id.apwdevs.andropilkasis.plugin.serverResponse.ListGroupData
import id.apwdevs.andropilkasis.plugin.serverResponse.RealCountData
import id.apwdevs.andropilkasis.plugin.serverResponse.UserPilkasisData
import id.apwdevs.andropilkasis.plugin.visible

class RealCountFragment : Fragment(), RealCountFragmentModel {
    // View
    private lateinit var stateText: TextView
    private lateinit var stateEmote: TextView
    private lateinit var stateLayout: LinearLayout
    private lateinit var linearCount: LinearLayout
    private lateinit var userCountProgress: ProgressBar
    private lateinit var userCountPercent: TextView
    private lateinit var userCountDesc: TextView

    private lateinit var golputCountProgress: ProgressBar
    private lateinit var golputCountPercent: TextView
    private lateinit var golputCountDesc: TextView
    private lateinit var spinnerCategory: Spinner
    private lateinit var recyclerView: RecyclerView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    //Data objects
    private val listUserGroup: MutableList<String> = mutableListOf(ALL_CATEGORIES)
    private val listCandidate: MutableList<CandidateCountData> = mutableListOf()
    private lateinit var userDataSheet: UserPilkasisData
    private var userProgress: Int = 0
        set(value) {
            userCountProgress.progress = value
            userCountPercent.text = "$value %"
            field = value
        }
    private var golputProgress: Int = 0
        set(value) {
            golputCountProgress.progress = value
            golputCountPercent.text = "$value %"
            field = value
        }

    //Presenter
    private lateinit var presenter: RealCountFragmentPresenter

    //Adapter
    private lateinit var realCountAdapter: RealCountCandidateAdapter
    private lateinit var listGroupAdapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userDataSheet = arguments!![PublicConfig.SharedKey.KEY_USER_DATASHEET] as UserPilkasisData
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val parentLayout = inflater.inflate(R.layout.realcount_fragment_main, container, false)
        // for progress indicators
        userCountProgress = parentLayout.findViewById(R.id.adapter_progress_id_progress1)
        userCountPercent = parentLayout.findViewById(R.id.adapter_progress_id_textpercent1)
        userCountDesc = parentLayout.findViewById(R.id.adapter_progress_id_desc1)
        golputCountProgress = parentLayout.findViewById(R.id.adapter_progress_id_progress2)
        golputCountPercent = parentLayout.findViewById(R.id.adapter_progress_id_textpercent2)
        golputCountDesc = parentLayout.findViewById(R.id.adapter_progress_id_desc2)

        userCountProgress.max = 100
        userProgress = 0
        userCountDesc.text = "Telah Memilih"
        golputCountProgress.max = 100
        golputProgress = 0
        golputCountDesc.text = "Golput"

        //////
        stateText = parentLayout.findViewById(R.id.adapter_id_showstatus_description)
        stateEmote = parentLayout.findViewById(R.id.adapter_id_showstatus_emotetext)
        stateLayout = parentLayout.findViewById(R.id.realcount_fragment_id_state_notpermit)
        linearCount = parentLayout.findViewById(R.id.realcount_fragment_id_statelinear)
        spinnerCategory = parentLayout.findViewById(R.id.realcount_fragment_id_spinner)
        recyclerView = parentLayout.findViewById(R.id.realcount_fragment_id_recyclerview)
        swipeRefreshLayout = parentLayout.findViewById(R.id.realcount_fragment_id_parentlayout)

        spinnerCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedSpinner = listUserGroup[spinnerCategory.selectedItemPosition]
                presenter.refresh(
                    userDataSheet.userName!!,
                    userDataSheet.password!!,
                    selectedSpinner,
                    userDataSheet.userClass!!
                )
            }
        }
        listGroupAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, listUserGroup)
        spinnerCategory.adapter = listGroupAdapter
        spinnerCategory.setSelection(0, true)

        // sets the RecyclerView
        realCountAdapter = RealCountCandidateAdapter(listCandidate, 0)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = realCountAdapter

        // sets the presenter
        presenter = RealCountFragmentPresenter(this, Handler(Looper.getMainLooper()))

        swipeRefreshLayout.setOnRefreshListener {
            val selectedSpinner = listUserGroup[spinnerCategory.selectedItemPosition]
            presenter.refresh(
                userDataSheet.userName!!,
                userDataSheet.password!!,
                selectedSpinner,
                userDataSheet.userClass!!
            )
        }

        presenter.refresh(
            userDataSheet.userName!!,
            userDataSheet.password!!,
            listUserGroup[0],
            userDataSheet.userClass!!
        )
        return parentLayout
    }


    override fun onLoadStarted() {
        stateLayout.gone()
        if (!swipeRefreshLayout.isRefreshing)
            swipeRefreshLayout.isRefreshing = true

    }

    override fun onLoadFinished(realCountData: RealCountData, groupList: ListGroupData) {
        // Enabling the view
        spinnerCategory.visible()
        linearCount.visible()
        recyclerView.visible()
        stateLayout.gone()
        // update the dataSet
        swipeRefreshLayout.isRefreshing = false
        //realCountAdapter.countGolput = realCountData.countGolput
        realCountAdapter.countTotalUser = realCountData.countTotalUser
        // try to change all
        listCandidate.clear()
        listCandidate.addAll(realCountData.listCandidates!!)
        realCountAdapter.notifyDataSetChanged()

        listUserGroup.clear()
        listUserGroup.add(PublicConfig.ALL_CATEGORIES)
        listUserGroup.addAll(groupList.listGroup!!)
        listGroupAdapter.notifyDataSetChanged()
        // update state percent
        userProgress = (realCountData.countTotalUser - realCountData.countGolput) * 100 / realCountData.countTotalUser
        golputProgress =
            if (realCountData.countGolput == 0) 0
            else
                realCountData.countGolput * 100 / realCountData.countTotalUser
    }

    override fun onLoadFailed(e: Throwable) {
        swipeRefreshLayout.isRefreshing = false
        recyclerView.gone()
        linearCount.gone()
        spinnerCategory.gone()
        Snackbar.make(swipeRefreshLayout, "${e.javaClass.simpleName} : ${e.message}", Snackbar.LENGTH_LONG).show()
    }

    override fun onNotPermitedAccess() {
        swipeRefreshLayout.isRefreshing = false
        stateLayout.visible()
        stateText.text = "Sorry!...\nAccess Denied"
        stateEmote.text = ":("
        recyclerView.gone()
        linearCount.gone()
        spinnerCategory.gone()
    }

    companion object {
        private const val ALL_CATEGORIES = "All"
        @JvmStatic
        fun newInstance(userDataSheet: UserPilkasisData): RealCountFragment =
            RealCountFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(PublicConfig.SharedKey.KEY_USER_DATASHEET, userDataSheet)
                }
            }
    }
}