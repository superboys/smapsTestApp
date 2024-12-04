//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.unito.smapssdk.chart;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;


import java.text.DecimalFormat;

public class PercentFormatter1 extends ValueFormatter {
    public DecimalFormat mFormat;
    private PieChart pieChart;

    public PercentFormatter1() {
        this.mFormat = new DecimalFormat("###,###,##0");
    }

    public PercentFormatter1(PieChart pieChart) {
        this();
        this.pieChart = pieChart;
    }

    public String getFormattedValue(float value) {
        return this.mFormat.format((int)value) + "%";
    }

    public String getPieLabel(float value, PieEntry pieEntry) {
        return this.pieChart != null && this.pieChart.isUsePercentValuesEnabled() ? this.getFormattedValue(value) : this.mFormat.format((double)value);
    }
}
