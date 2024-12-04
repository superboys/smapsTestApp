package com.unito.smapssdk.chart

import android.graphics.Color
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import kotlin.math.min
import kotlin.math.roundToInt

abstract class BaseFragment : Fragment(), View.OnClickListener {

    protected val TAG = this::class.java.simpleName
    private var lastClickedTime: Long = 0

    protected abstract fun defineLayoutResources(): Int
    protected abstract fun initializeComponent(view: View)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(defineLayoutResources(), container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        try {
            initializeComponent(view)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    /**
     * To change the Statusbar color, use this method
     *
     * @param activity Currently visible activity
     * @param color Color which is need to be applied to the status bar.
     */
    fun setStatusBarColor(activity: FragmentActivity?, color: Int) {
        if (activity == null) {
            return
        }

        val window = activity.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = ContextCompat.getColor(activity, color)
        window.navigationBarColor = manipulateColor(color, 0.8f)
    }

    /**
     * Based on provided color, generate new color which is being applied to navigation bar.
     */
    private fun manipulateColor(color: Int, factor: Float): Int {
        val a = Color.alpha(ContextCompat.getColor(requireActivity(), color))
        val r = (Color.red(ContextCompat.getColor(requireActivity(), color)) * factor).roundToInt()
        val g =
            (Color.green(ContextCompat.getColor(requireActivity(), color)) * factor).roundToInt()
        val b = (Color.blue(ContextCompat.getColor(requireActivity(), color)) * factor).roundToInt()
        return Color.argb(
            a,
            min(r, 255),
            min(g, 255),
            min(b, 255)
        )
    }
}