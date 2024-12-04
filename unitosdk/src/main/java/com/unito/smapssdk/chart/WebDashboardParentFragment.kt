//package com.unito.smapssdk.chart
//
//import android.os.Bundle
//import android.os.SystemClock
//import android.view.View
//import android.widget.LinearLayout
//import androidx.appcompat.widget.AppCompatImageView
//import com.unito.smapssdk.R
//
///**
// * USAGE :
// * Created by R.S.
// */
//class WebDashboardParentFragment : BaseFragment() {
//    private var lastClickedTime = 0L
//
//    override fun defineLayoutResources(): Int {
//        return R.layout.fragment_web_dashboard_parent
//    }
//
//    override fun initializeComponent(view: View) {
//        setupActionBar()
//
//        val llWater: LinearLayout = view.findViewById(R.id.fragment_web_dashboard_parent_llWater)
//        val llPower: LinearLayout = view.findViewById(R.id.fragment_web_dashboard_parent_llPower)
//        val llBoiler: LinearLayout = view.findViewById(R.id.fragment_web_dashboard_parent_llBoiler)
//        val llChiller: LinearLayout = view.findViewById(R.id.fragment_web_dashboard_parent_llChiller)
//
//        val ivWater: AppCompatImageView = view.findViewById(R.id.fragment_web_dashboard_parent_ivWater)
//        val ivPower: AppCompatImageView = view.findViewById(R.id.fragment_web_dashboard_parent_ivPower)
//        val ivBoiler: AppCompatImageView = view.findViewById(R.id.fragment_web_dashboard_parent_ivBoiler)
//        val ivChiller: AppCompatImageView = view.findViewById(R.id.fragment_web_dashboard_parent_ivChiller)
//
//        llWater.visibility = View.VISIBLE
//        llPower.visibility = View.VISIBLE
//        llBoiler.visibility = View.VISIBLE
//        llChiller.visibility = View.VISIBLE
//
//        if (BLEConstant.connectionType == BLEConstant.CONNECTION_TYPE_LAVA) {
//            ivWater.setImageResource(R.drawable.ic_dashboard_lava_water)
//            ivPower.setImageResource(R.drawable.ic_dashboard_lava_power)
//            ivBoiler.setImageResource(R.drawable.ic_dashboard_lava_boiler)
//            llChiller.visibility = View.GONE
//        } else if (BLEConstant.connectionType == BLEConstant.CONNECTION_TYPE_SPARKLE) {
//            ivWater.setImageResource(R.drawable.ic_dashboard_sparkle_water)
//            ivPower.setImageResource(R.drawable.ic_dashboard_sparkle_power)
//            ivChiller.setImageResource(R.drawable.ic_dashboard_sparkle_chiller)
//            llBoiler.visibility = View.GONE
//        } else if (BLEConstant.connectionType == BLEConstant.CONNECTION_TYPE_ELEMENTS) {
//            ivWater.setImageResource(R.drawable.ic_dashboard_elements_water)
//            ivPower.setImageResource(R.drawable.ic_dashboard_infinity_power)
//            ivChiller.setImageResource(R.drawable.ic_dashboard_infinity_chiller)
//            ivBoiler.setImageResource(R.drawable.ic_dashboard_infinity_boiler)
//        } else {
//            ivWater.setImageResource(R.drawable.ic_dashboard_infinity_water)
//            ivPower.setImageResource(R.drawable.ic_dashboard_infinity_power)
//            ivBoiler.setImageResource(R.drawable.ic_dashboard_infinity_boiler)
//            ivChiller.setImageResource(R.drawable.ic_dashboard_infinity_chiller)
//        }
//
//        llWater.setOnClickListener(this)
//        llPower.setOnClickListener(this)
//        llBoiler.setOnClickListener(this)
//        llChiller.setOnClickListener(this)
//    }
//
//    private fun setupActionBar() {
//        try {
//            homeActivity.llBack.visibility = View.VISIBLE
//            homeActivity.tvAction.visibility = View.GONE
//            homeActivity.imgLogoLeft.visibility = View.GONE
//            homeActivity.imgLogoRight.visibility = View.GONE
//
//            homeActivity.tvToolbarHeader.setText(R.string.dashboard_dashboard)
//        } catch (ex:Exception) {
//            ex.printStackTrace()
//        }
//
//    }
//
//    override fun onHiddenChanged(hidden: Boolean) {
//        super.onHiddenChanged(hidden)
//        if (!hidden) {
//            setupActionBar()
//        }
//    }
//
//    override fun onClick(v: View) {
//        super.onClick(v)
//        if (SystemClock.elapsedRealtime() - lastClickedTime < Const.MAX_CLICK_INTERVAL) {
//            return
//        }
//        lastClickedTime = SystemClock.elapsedRealtime()
//
//        when (v.id) {
//            R.id.fragment_web_dashboard_parent_llWater -> {
//                addFragment(
//                    R.id.activity_home_fldashboard,
//                    this@WebDashboardParentFragment,
//                    PieChartFrag(),
//                    requiredAnimation = false,
//                    commitAllowingStateLoss = false
//                )
//            }
//            R.id.fragment_web_dashboard_parent_llPower -> {
//                addFragment(
//                    R.id.activity_home_fldashboard,
//                    this@WebDashboardParentFragment,
//                    BarChartFrag(),
//                    requiredAnimation = false,
//                    commitAllowingStateLoss = false
//                )
//            }
//            R.id.fragment_web_dashboard_parent_llBoiler -> {
//                addFragment(
//                    R.id.activity_home_fldashboard,
//                    this@WebDashboardParentFragment,
//                    BoilerLineChartFrag(),
//                    requiredAnimation = false,
//                    commitAllowingStateLoss = false
//                )
//            }
//            R.id.fragment_web_dashboard_parent_llChiller -> {
//                addFragment(
//                    R.id.activity_home_fldashboard,
//                    this@WebDashboardParentFragment,
//                    ChillerLineChartFrag(),
//                    requiredAnimation = false,
//                    commitAllowingStateLoss = false
//                )
//            }
//        }
//    }
//
//    private fun openWebDashboardFragment(type: Int): WebDashboardFragment {
//        val webDashboardFragment = WebDashboardFragment()
//        val bundle = Bundle()
//        bundle.putInt(WebDashboardFragment.BUNDLE_TYPE, type)
//        webDashboardFragment.arguments = bundle
//        return webDashboardFragment
//    }
//}