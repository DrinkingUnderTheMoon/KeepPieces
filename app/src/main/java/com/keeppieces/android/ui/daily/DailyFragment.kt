package com.keeppieces.android.ui.daily

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.keeppieces.android.R
import com.keeppieces.android.extension.getItemDecoration
import com.keeppieces.android.extension.toCHINADFormatted
import com.keeppieces.android.logic.data.*
import com.keeppieces.android.ui.daily.adapter.DailyAccountOverviewAdapter
import com.keeppieces.android.ui.daily.adapter.DailyMemberOverviewAdapter
import com.keeppieces.android.ui.daily.adapter.DailyPrimaryOverviewAdapter
import com.keeppieces.android.ui.daily.adapter.DailyTypeOverviewAdapter
import com.keeppieces.android.ui.detail.DetailActivity
import com.keeppieces.pie_chart.PieAnimation
import com.keeppieces.pie_chart.PieData
import com.keeppieces.pie_chart.PiePortion
import kotlinx.android.synthetic.main.fragment_daily.*
import kotlinx.android.synthetic.main.layout_daily_account_overview.*
import kotlinx.android.synthetic.main.layout_daily_member_overview.*
import kotlinx.android.synthetic.main.layout_daily_primary_overview.*
import kotlinx.android.synthetic.main.layout_daily_type_overview.*
import java.time.LocalDate


class DailyFragment(var date: LocalDate) : Fragment() {
    private val viewModel: DailyViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_daily, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setUpView(date.toString())
        dailyLeftArrow.setOnClickListener {
            date.plusDays(-1)
            setUpView(date.toString())
        }
        dailyRightArrow.setOnClickListener {
            date.plusDays(1)
            setUpView(date.toString())
        }
    }

    private fun setUpView(date: String) {
        viewModel.billList(date).observe(viewLifecycleOwner) { billList ->
            val bills = if (billList.isEmpty()) tempList else billList
            setUpPieView(bills)
            setUpTypeCard(bills)
            setUpPrimaryCard(bills)
            setUpAccountCard(bills)
            setUpMemberCard(bills)
        }

        dailyDetailBtn.setOnClickListener {
            DetailActivity.start(it.context, LocalDate.now(), LocalDate.now(), R.color.dark_green)
        }
    }

    private fun setUpPieView(bills: List<Bill>) {
        val dailyOverview = viewModel.dailyOverview(bills, "green")
        dailyAmount.text = dailyOverview.total.toCHINADFormatted()
        val piePortions = dailyOverview.bills.map {
            PiePortion(
                it.secondaryCategory, it.amount, ContextCompat.getColor(requireContext(), it.color)
            )
        }.toList()

        val pieData = PieData(portions = piePortions)
        val pieAnimation = PieAnimation(pieChart).apply {
            duration = 600
        }
        pieChart.setPieData(pieData = pieData, animation = pieAnimation)
    }

    private fun setUpPrimaryCard(bills: List<Bill>) {
        val primaryList = viewModel.dailyPrimaryList(bills, "blue")
        dailyPrimaryDetailRecycler.apply {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            addItemDecoration(getItemDecoration())
            adapter = DailyPrimaryOverviewAdapter(primaryList)
        }
        setUpPrimaryPieView(primaryList)
    }

    private fun setUpPrimaryPieView(primaryList: List<DailyPrimary>) {
        daily_primary_title.text = "分类"
        val piePortions = primaryList.map {
            PiePortion(
                it.primaryCategory, it.amount, ContextCompat.getColor(requireContext(), it.color)
            )
        }.toList()

        val pieData = PieData(portions = piePortions)
        val pieAnimation = PieAnimation(dailyPrimaryOverviewPie).apply {
            duration = 600
        }

        dailyPrimaryOverviewPie.setPieData(pieData = pieData, animation = pieAnimation)
    }

    private fun setUpAccountCard(bills: List<Bill>) {
        val accountList = viewModel.dailyAccountList(bills, "purple")
        dailyAccountDetailRecycler.apply {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            addItemDecoration(getItemDecoration())
            adapter = DailyAccountOverviewAdapter(accountList)
        }
        setUpAccountPieView(accountList)
    }

    private fun setUpAccountPieView(accountList: List<DailyAccount>) {
        dailyAccountTitle.text = "账户"
        val piePortions = accountList.map {
            PiePortion(
                it.account, it.amount, ContextCompat.getColor(requireContext(), it.color)
            )
        }.toList()

        val pieData = PieData(portions = piePortions)
        val pieAnimation = PieAnimation(dailyAccountOverviewPie).apply {
            duration = 600
        }

        dailyAccountOverviewPie.setPieData(pieData = pieData, animation = pieAnimation)
    }

    private fun setUpMemberCard(bills: List<Bill>) {
        val memberList = viewModel.dailyMemberList(bills, "orange")
        dailyMemberDetailRecycler.apply {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            addItemDecoration(getItemDecoration())
            adapter = DailyMemberOverviewAdapter(memberList)
        }
        setUpMemberPieView(memberList)
    }

    private fun setUpMemberPieView(memberList: List<DailyMember>) {
        dailyMemberTitle.text = "成员"
        val piePortions = memberList.map {
            PiePortion(
                it.member, it.amount, ContextCompat.getColor(requireContext(), it.color)
            )
        }.toList()

        val pieData = PieData(portions = piePortions)
        val pieAnimation = PieAnimation(dailyMemberOverviewPie).apply {
            duration = 600
        }

        dailyMemberOverviewPie.setPieData(pieData = pieData, animation = pieAnimation)
    }

    private fun setUpTypeCard(bills: List<Bill>) {
        val typeList = viewModel.dailyTypeList(bills)
        dailyTypeDetaiRecycler.apply {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            addItemDecoration(getItemDecoration())
            adapter = DailyTypeOverviewAdapter(typeList)
        }
        setUpTypePieView(typeList)
    }

    private fun setUpTypePieView(typeList: List<DailyType>) {
        dailyTypeTitle.text = "收支"
        val piePortions = typeList.map {
            PiePortion(
                it.type, it.amount, ContextCompat.getColor(requireContext(), it.color)
            )
        }.toList()

        val pieData = PieData(portions = piePortions)
        val pieAnimation = PieAnimation(dailyTypeOverviewPie).apply {
            duration = 600
        }

        dailyTypeOverviewPie.setPieData(pieData = pieData, animation = pieAnimation)
    }

    companion object {
        @RequiresApi(Build.VERSION_CODES.O)
        val tempList = listOf(
            Bill(
                date = LocalDate.now().toString(),
                amount = 6.60,
                account = "校园卡",
                member = "Me",
                primaryCategory = "Food",
                secondaryCategory = "Breakfast",
                type = "支出"
            ),
            Bill(
                date = LocalDate.now().toString(),
                amount = 23.60,
                account = "Wechat",
                member = "Me",
                primaryCategory = "Food",
                secondaryCategory = "Lunch",
                type = "支出"
            ),
            Bill(
                date = LocalDate.now().toString(),
                amount = 47.09,
                account = "校园卡",
                member = "Me",
                primaryCategory = "Food",
                secondaryCategory = "Dinner",
                type = "支出"
            ),
            Bill(
                date = LocalDate.now().toString(),
                amount =1299.00,
                account = "Alipay",
                member = "Mom",
                primaryCategory = "Wearing",
                secondaryCategory = "Shoes",
                type = "支出"
            ),
            Bill(
                date = LocalDate.now().toString(),
                amount = 229.90,
                account = "Cash",
                member = "boy friend",
                primaryCategory = "人情",
                secondaryCategory = "礼物",
                type = "支出"
            ),
            Bill(
                date = LocalDate.now().toString(),
                amount = 999.0,
                account = "Wechat",
                member = "Me",
                primaryCategory = "Wearing",
                secondaryCategory = "Shoes",
                type = "支出"
            ),
        )
    }
}