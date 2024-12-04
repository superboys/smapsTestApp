package com.unito.smapssdk.chart;

import android.graphics.Color;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.LargeValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.google.gson.Gson;
import com.unito.smapssdk.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class BarChartFrag extends BaseActivity implements OnChartValueSelectedListener {

    private BarChart chart;
    private String dataType = "DAY";
    private boolean isFirst = true;
    List<String> listAxi = new ArrayList<>();
    private long mLastClickTime = 0;

    private TextView tvToday;
    private TextView tvLastWeek;
    private TextView tvLastMonth;
    private TextView tvLastYear;
    private LinearLayout linearBoiler;

    @Override
    public int defineLayoutResource() {
        return R.layout.activity_barchart;
    }

    @Override
    protected void initializeComponents() {
        setupActionBar();
        chart = findViewById(R.id.chart1);
        chart.setOnChartValueSelectedListener(this);
        chart.getDescription().setEnabled(false);
        // scaling can now only be done on x- and y-axis separately
        chart.setPinchZoom(true);
        chart.setDrawBarShadow(false);
        chart.setDrawGridBackground(false);
        chart.setDrawValueAboveBar(false);
        chart.getViewPortHandler().setMaximumScaleX(2f);
        chart.getViewPortHandler().setMaximumScaleY(1f);
        Legend l = chart.getLegend();
        l.setEnabled(false);
        tvToday = findViewById(R.id.fragment_web_dashboard_tvToday);
        tvLastWeek = findViewById(R.id.fragment_web_dashboard_tvLastWeek);
        tvLastMonth = findViewById(R.id.fragment_web_dashboard_tvLastMonth);
        tvLastYear = findViewById(R.id.fragment_web_dashboard_tvLastYear);
        linearBoiler = findViewById(R.id.linear_boiler);

        tvToday.setOnClickListener(this);
        tvLastWeek.setOnClickListener(this);
        tvLastMonth.setOnClickListener(this);
        tvLastYear.setOnClickListener(this);

        tvToday.callOnClick();
//        if (BLEConstant.isSparkleDevice()) {
//            linearBoiler.setVisibility(View.GONE);
//        }

        String jsonString = "{\n" +
                "    \"msg\": \"success\",\n" +
                "    \"code\": 200,\n" +
                "    \"data\": [\n" +
                "       {\n" +
                "        \"statisticsTime\": \"2024-04-01\",\n" +
                "        \"heaterPowerConsumption\": 2.0,\n" +
                "        \"compressorPowerConsumption\": 0.13\n" +
                "        }\n" +
                "    ]\n" +
                "    \n" +
                "}";
        setData(new Gson().fromJson(jsonString, BarInfoModel.class));

    }

    private void setData(BarInfoModel response) {
        float groupSpace = 0.24f;
        float barSpace = 0.1f; // x4 DataSet
        float barWidth = 0.28f; // x4 DataSet

        int groupCount = response.getData().size();

        ArrayList<BarEntry> values1 = new ArrayList<>();
        ArrayList<BarEntry> values2 = new ArrayList<>();
        listAxi.clear();
        for (int i = 0; i < response.getData().size(); i++) {
            if (dataType.equals("MONTH")) {
                String[] week = response.getData().get(i).getStatisticsTime().split("-");
                listAxi.add(week[0] + " Week:" + week[1]);
            } else if (dataType.equals("WEEK")) {
                String[] week = response.getData().get(i).getStatisticsTime().split("-");
                listAxi.add(week[1] + "-" + week[2]);
            } else {
                listAxi.add(response.getData().get(i).getStatisticsTime());
            }
//            if (BLEConstant.isLavaDevice()) {
//                values1.add(new BarEntry(i, (float) response.getData().get(i).getHeaterPowerConsumption()));
//                values2.add(new BarEntry(i, 0));
//            } else if (BLEConstant.isSparkleDevice()) {
//                values2.add(new BarEntry(i, (float) response.getData().get(i).getCompressorPowerConsumption()));
//                values1.add(new BarEntry(i, 0));
//            } else {
            values2.add(new BarEntry(i, (float) response.getData().get(i).getCompressorPowerConsumption()));
            values1.add(new BarEntry(i, (float) response.getData().get(i).getHeaterPowerConsumption()));
//            }
        }

        XAxis xAxis = chart.getXAxis();
        xAxis.setGranularity(1f);
//        xAxis.setCenterAxisLabels(true);
        xAxis.setLabelRotationAngle(-45);
        xAxis.setTextColor(getResources().getColor(R.color.white));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawAxisLine(false);
        //不显示X轴网格线
        xAxis.setDrawGridLines(false);
        IndexAxisValueFormatter formatter = new IndexAxisValueFormatter(listAxi);
        xAxis.setValueFormatter(formatter);//设置自定义格式，在绘制之前动态调整x的值。
        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setTextColor(getResources().getColor(R.color.white));
        leftAxis.setDrawGridLines(true);
        leftAxis.setEnabled(true);
        leftAxis.setGridLineWidth(1);
        leftAxis.setDrawAxisLine(false);

        leftAxis.setLabelCount(listAxi.size(), false);
        leftAxis.setGridColor(getResources().getColor(R.color.white));
        leftAxis.setSpaceTop(35f);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)
        leftAxis.setDrawAxisLine(false);
        chart.getAxisRight().setEnabled(false);
        chart.getAxisRight().setDrawAxisLine(false);

        BarDataSet set1, set2;

        if (chart.getData() != null && chart.getData().getDataSetCount() > 0) {
//            if (BLEConstant.isLavaDevice()) {
//                set1 = (BarDataSet) chart.getData().getDataSetByIndex(0);
//                set1.setValues(values1);
//            } else if (BLEConstant.isSparkleDevice()) {
//                set2 = (BarDataSet) chart.getData().getDataSetByIndex(1);
//                set2.setValues(values2);
//            } else {
            set1 = (BarDataSet) chart.getData().getDataSetByIndex(0);
            set2 = (BarDataSet) chart.getData().getDataSetByIndex(1);
            set1.setValues(values1);
            set2.setValues(values2);
//            }
            chart.getData().notifyDataChanged();
            chart.notifyDataSetChanged();

        } else {
            // create 4 DataSets
            BarData data;
//            if (BLEConstant.isLavaDevice()) {
//                set1 = new BarDataSet(values1, getString(R.string.bar_boiler));
//                set1.setColor(Color.rgb(255, 78, 0));
//                set1.setBarBorderWidth(1);
//                set1.setDrawValues(false);
//                set1.setBarBorderColor(getResources().getColor(R.color.white));
//                data = new BarData(set1);
//            } else if (BLEConstant.isSparkleDevice()) {
//                set2 = new BarDataSet(values2, getString(R.string.bar_chiller));
//                set2.setColor(Color.rgb(0, 132, 255));
//                set2.setBarBorderWidth(1);
//                set2.setDrawValues(false);
//                set2.setBarBorderColor(getResources().getColor(R.color.white));
//                data = new BarData(set2);
//            } else {
            set1 = new BarDataSet(values1, getString(R.string.bar_boiler));
            set1.setColor(Color.rgb(255, 78, 0));
            set1.setBarBorderWidth(1);
            set1.setDrawValues(false);
            set1.setBarBorderColor(getResources().getColor(R.color.white));
            set2 = new BarDataSet(values2, getString(R.string.bar_chiller));
            set2.setColor(Color.rgb(0, 132, 255));
            set2.setBarBorderWidth(1);
            set2.setDrawValues(false);
            set2.setBarBorderColor(getResources().getColor(R.color.white));
            data = new BarData(set1, set2);

            data.setValueFormatter(new LargeValueFormatter());
            chart.setData(data);
        }

        // create a custom MarkerView (extend MarkerView) and specify the layout
        // to use for it
        BarMarkerView mv = new BarMarkerView(this, R.layout.custom_marker_view, formatter);
        mv.setChartView(chart); // For bounds control
        chart.setMarker(mv); // Set the marker to the chart
        //设置动画效果
//        chart.animateY(1000, Easing.Linear);
//        chart.animateX(1000, Easing.Linear);
        // specify the width each bar should have
        chart.getBarData().setBarWidth(barWidth);
        // restrict the x-axis range
        chart.getXAxis().setAxisMinimum(0);
//        chart.getXAxis().setAxisMaximum(8);
        chart.getXAxis().setAxisMaximum(0 + chart.getBarData().getGroupWidth(groupSpace, barSpace) * groupCount);
        chart.groupBars(0, groupSpace, barSpace);
        chart.invalidate();
//        isFirst = true;
    }


    private void responseBarInfo() {
//        progressDialog = Utils.displayFancyProgressDialog(getActivity(), "");
//        final String hubToken = Unito.getInstance().getSharedPreferences().getString(PREF.HUB_TOKEN, Const.DEFULTVAL);
////        final String hubToken = "31F8FD8B-DBBB-4CCC-9AC9-E4E3677CDCCD";
//        final GetChartInfoModel getChartInfoModel = new GetChartInfoModel(hubToken, 0,
//                dataType);
//        UtilsApi.getAPIService().getBarInfo(getChartInfoModel,
////                "05f1bbc37bb7d6b187481589cda8b092").subscribeOn(Schedulers.io()).observeOn(
//                Unito.getInstance().getSharedPreferences().getString(PREF.HUB_SERIAL_ID, Const.DEFULTVAL)).subscribeOn(Schedulers.io()).observeOn(
//                AndroidSchedulers.mainThread()).subscribe(new Observer<BarInfoModel>() {
//            @Override
//            public void onSubscribe(Disposable d) {
//                disposable = d;
//            }
//
//            @Override
//            public void onNext(BarInfoModel response) {
//                Utils.dismissFancyProgressDialog(progressDialog);
//                if (null != response && response.getCode() == 200) {
//                    Unito.getInstance().getSharedPreferences().edit().putString("barChart" + dataType, System.currentTimeMillis() + "~" + new Gson().toJson(response)).apply();
//                    setData(response);
//                } else {
////                    isFirst = true;
//                }
////                parseJSONResponse(new Gson().toJson(response.data));
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                Utils.dismissFancyProgressDialog(progressDialog);
////                isFirst = true;
//            }
//
//            @Override
//            public void onComplete() {
//                Utils.dismissFancyProgressDialog(progressDialog);
////                isFirst = true;
//            }
//        });
    }

    private void setupActionBar() {
//        try {
//            homeActivity.getLlBack().setVisibility(View.VISIBLE);
//            homeActivity.getTvAction().setVisibility(View.GONE);
//            homeActivity.getImgLogoLeft().setVisibility(View.GONE);
//            homeActivity.getImgLogoRight().setVisibility(View.GONE);
//
//            homeActivity.getTvToolbarHeader().setText(R.string.bar_power_consumption);
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
    }

    private void responseCache() {
//        try {
//            String responseData = Unito.getInstance().getSharedPreferences().getString("barChart" + dataType, "");
//            if (responseData.isEmpty()) {
//                responseBarInfo();
//            } else {
//                String[] data = responseData.split("~");
//                if (dataType.equals("DAY")) {
//                    if (Math.abs(System.currentTimeMillis() - Long.parseLong(data[0])) > 1 * DAY_CACHA_TIME * 60 * 1000) {
//                        responseBarInfo();
//                    } else {
//                        setData(new Gson().fromJson(data[1], BarInfoModel.class));
//                    }
//                } else {
//                    if (Math.abs(System.currentTimeMillis() - Long.parseLong(data[0])) > 1 * OTHER_CACHA_TIME * 60 * 1000) {
//                        responseBarInfo();
//                    } else {
//                        setData(new Gson().fromJson(data[1], BarInfoModel.class));
//                    }
//                }
//            }
//        } catch (Exception e) {
//            responseBarInfo();
//            e.printStackTrace();
//        }
    }

//    @Override
//    public void onClick(@NotNull View view) {
//        super.onClick(view);
//
//        if (SystemClock.elapsedRealtime() - mLastClickTime < 100) {
//            return;
//        }
//        mLastClickTime = SystemClock.elapsedRealtime();
//        if (view == tvToday) {
//            if (!view.isSelected()) {
//                dataType = "DAY";
//                Log.e("onCheckedChanged---》", "DAY");
//                responseCache();
//            }
//        } else if (view == tvLastWeek) {
//            if (!view.isSelected()) {
//                dataType = "WEEK";
//                Log.e("onCheckedChanged---》", "WEEK");
//                responseCache();
//            }
//        } else if (view == tvLastMonth) {
//            if (!view.isSelected()) {
//                dataType = "MONTH";
//                Log.e("onCheckedChanged---》", "MONTH");
//                responseCache();
//            }
//        } else if (view == tvLastYear) {
//            if (!view.isSelected()) {
//                dataType = "YEAR";
//                Log.e("onCheckedChanged---》", "YEAR");
//                responseCache();
//            }
//        }
//        disableAllTextView();
//        view.setSelected(true);
//    }

    private void disableAllTextView() {
        tvToday.setSelected(false);
        tvLastWeek.setSelected(false);
        tvLastMonth.setSelected(false);
        tvLastYear.setSelected(false);
    }

    @Override
    public void onValueSelected(Entry entry, Highlight highlight) {

    }

    @Override
    public void onNothingSelected() {

    }


    @Override
    public void onClick(View v) {

    }

}
