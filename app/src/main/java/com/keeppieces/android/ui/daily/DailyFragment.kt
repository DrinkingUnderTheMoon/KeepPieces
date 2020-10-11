package com.keeppieces.android.ui.daily

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.keeppieces.android.R
import com.keeppieces.android.extension.getItemDecoration
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_daily.*


@AndroidEntryPoint
class DailyFragment: Fragment() {
    private val viewModel: DailyViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.d(TAG, "onCreateView")
        return inflater.inflate(R.layout.fragment_daily, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setUpRecyclerView()
        Log.d(TAG, "onActivityCreated")
    }

    private fun setUpRecyclerView() {
        dailyBills.apply {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            addItemDecoration(getItemDecoration())
            viewModel.billList.observe(viewLifecycleOwner) { billList->
                for (bill in billList) {
                    Log.d("Daily ${bill.billId}", bill.toString())
                }
                adapter = DailyAdapter(billList)
            }
        }
    }

    companion object {
        const val TAG = "DailyFragment"
        private const val KEY_DAY = "key-day"
        fun newInstance(day: Int): DailyFragment {
            return DailyFragment().apply {
                arguments = Bundle().apply { putInt(KEY_DAY, day) }
            }
        }
    }
}