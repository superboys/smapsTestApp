package com.unito.smapssdk.chart;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.google.gson.Gson;
import com.unito.smapssdk.R;

import java.util.ArrayList;
import java.util.List;

public class BoilerLineChartFrag extends BaseActivity implements OnChartValueSelectedListener{

    private CombinedChart chart;
    private String dataType = "DAY";
    private boolean isFirst = true;
    private long mLastClickTime = 0;
    List<String> listAxi = new ArrayList<>();
    private TextView tvToday;
    private TextView tvLastWeek;
    private TextView tvLastMonth;
    private TextView tvLastYear;
    private int maxmum;

    XAxis xAxis;

    @Override
    public int defineLayoutResource() {
        return R.layout.activity_boilerlinechart;
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
        chart.getViewPortHandler().setMaximumScaleX(4f);
        chart.getViewPortHandler().setMaximumScaleY(2f);
        tvToday = findViewById(R.id.fragment_web_dashboard_tvToday);
        tvLastWeek = findViewById(R.id.fragment_web_dashboard_tvLastWeek);
        tvLastMonth = findViewById(R.id.fragment_web_dashboard_tvLastMonth);
        tvLastYear = findViewById(R.id.fragment_web_dashboard_tvLastYear);

        Legend l = chart.getLegend();
        l.setEnabled(false);

        tvToday.setOnClickListener(this);
        tvLastWeek.setOnClickListener(this);
        tvLastMonth.setOnClickListener(this);
        tvLastYear.setOnClickListener(this);

        tvToday.callOnClick();

        String jsonString = "{\n" +
                "    \"msg\": \"success\",\n" +
                "    \"code\": 200,\n" +
                "    \"data\": [\n" +
                "        {\n" +
                "            \"temperature\": 102,\n" +
                "            \"statisticsTime\": \"00:00\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"temperature\": 101,\n" +
                "            \"statisticsTime\": \"00:30\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"temperature\": 99,\n" +
                "            \"statisticsTime\": \"01:00\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"temperature\": 102,\n" +
                "            \"statisticsTime\": \"01:30\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"temperature\": 100,\n" +
                "            \"statisticsTime\": \"02:00\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"temperature\": 99,\n" +
                "            \"statisticsTime\": \"02:30\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"temperature\": 102,\n" +
                "            \"statisticsTime\": \"03:00\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"temperature\": 100,\n" +
                "            \"statisticsTime\": \"03:30\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"temperature\": 101,\n" +
                "            \"statisticsTime\": \"04:00\"\n" +
                "        }\n" +
                "    ]\n" +
                "}";
        setData(new Gson().fromJson(jsonString, LineInfoModel.class));
    }

    private void setData(LineInfoModel response) {
        xAxis = chart.getXAxis();
        xAxis.setGranularity(1f);
//        xAxis.setCenterAxisLabels(true);
        xAxis.setLabelRotationAngle(-45);
        xAxis.setTextColor(getResources().getColor(R.color.white));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawAxisLine(false);
        //不显示X轴网格线
        xAxis.setDrawGridLines(false);

        LineData d = new LineData();

        ArrayList<Entry> entries = new ArrayList<>();
        listAxi.clear();
        maxmum = 0;
        for (int index = 0; index < response.getData().size(); index++) {
            if (dataType.equals("MONTH")) {
                xAxis.setLabelRotationAngle(-70);
                String[] week = response.getData().get(index).getStatisticsTime().split("-");
                listAxi.add(week[0] + " Week:" + week[1]);
            } else if (dataType.equals("WEEK")) {
                String[] week = response.getData().get(index).getStatisticsTime().split("-");
                listAxi.add(week[1] + "-" + week[2]);
            } else {
                listAxi.add(response.getData().get(index).getStatisticsTime());
            }
            int temp = response.getData().get(index).getTemperature();
            if (maxmum < temp) {
                maxmum = temp;
            }
            entries.add(new Entry(index, temp));
        }
        LineDataSet set = new LineDataSet(entries, "Line DataSet");
        set.setColor(Color.rgb(255, 78, 0));
        set.setLineWidth(2.5f);
        set.setCircleColor(Color.rgb(255, 78, 0));
        set.setDrawCircleHole(false);
        set.setCircleRadius(0f);
        set.setFillColor(Color.rgb(255, 78, 0));
        set.setMode(LineDataSet.Mode.LINEAR);
        set.setDrawValues(false);
        set.setValueTextSize(10f);
        set.setValueTextColor(Color.rgb(255, 78, 0));
        set.setDrawHorizontalHighlightIndicator(false);
        set.setDrawVerticalHighlightIndicator(false);

        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        d.addDataSet(set);
        IndexAxisValueFormatter formatter = new IndexAxisValueFormatter(listAxi);
        xAxis.setValueFormatter(formatter);//设置自定义格式，在绘制之前动态调整x的值。
        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setTextColor(getResources().getColor(R.color.white));
        leftAxis.setDrawGridLines(true);
        leftAxis.setEnabled(true);
        leftAxis.setGridLineWidth(1);
        leftAxis.setDrawAxisLine(false);

//        leftAxis.setLabelCount(6);
        leftAxis.setGridColor(getResources().getColor(R.color.white));
        leftAxis.setSpaceTop(35f);
//        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)
        leftAxis.setDrawAxisLine(false);
        chart.getAxisRight().setEnabled(false);
        chart.getAxisRight().setDrawAxisLine(false);
        CombinedData data = new CombinedData();

        // create a custom MarkerView (extend MarkerView) and specify the layout
        // to use for it
//        NewMarkerView_temp mv = new NewMarkerView_temp(getActivity(), R.layout.temp_marker_view,formatter,1,maxmum);
        XYMarkerView mv = new XYMarkerView(this,formatter,1,maxmum);
        mv.setChartView(chart); // For bounds control
        chart.setMarker(mv); // Set the marker to the chart

        data.setData(d);
//        xAxis.setAxisMaximum(data.getXMax() + 0.25f);
        chart.setData(data);
        chart.invalidate();
//        isFirst = true;
    }

    private void responseLineInfo() {
//        progressDialog = Utils.displayFancyProgressDialog(getActivity(), "");
//        final String hubToken = Unito.getInstance().getSharedPreferences().getString(PREF.HUB_TOKEN, Const.DEFULTVAL);
////        final String hubToken = "31F8FD8B-DBBB-4CCC-9AC9-E4E3677CDCCD";
//        final GetLineInfoModel getLineInfoModel = new GetLineInfoModel(hubToken, 1,
//                dataType);
//        UtilsApi.getAPIService().getLineInfo(getLineInfoModel,
////                "05f1bbc37bb7d6b187481589cda8b092").subscribeOn(Schedulers.io()).observeOn(
//                Unito.getInstance().getSharedPreferences().getString(PREF.HUB_SERIAL_ID, Const.DEFULTVAL)).subscribeOn(Schedulers.io()).observeOn(
//                AndroidSchedulers.mainThread()).subscribe(new Observer<LineInfoModel>() {
//            @Override
//            public void onSubscribe(Disposable d) {
//                disposable = d;
//            }
//
//            @Override
//            public void onNext(LineInfoModel response) {
//                Utils.dismissFancyProgressDialog(progressDialog);
//                if (null != response && response.getCode() == 200) {
//                    Unito.getInstance().getSharedPreferences().edit().putString("boilerLineChart"+dataType, System.currentTimeMillis()+"~"+new Gson().toJson(response)).apply();
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
//            homeActivity.getTvToolbarHeader().setText(R.string.boiler_linechart_temperature);
//        } catch (Exception ex) {
//            ex.printStackTrace();
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

    private void responseCache() {
//        try {
//            String responseData = Unito.getInstance().getSharedPreferences().getString("boilerLineChart" + dataType, "");
//            if (responseData.isEmpty()) {
//                responseLineInfo();
//            } else {
//                String[] data = responseData.split("~");
//                if (dataType.equals("DAY")) {
//                    if (Math.abs(System.currentTimeMillis() - Long.parseLong(data[0])) > 1 * DAY_CACHA_TIME * 60 * 1000) {
//                        responseLineInfo();
//                    } else {
//                        setData(new Gson().fromJson(data[1], LineInfoModel.class));
//                    }
//                } else {
//                    if (Math.abs(System.currentTimeMillis() - Long.parseLong(data[0])) > 1 * OTHER_CACHA_TIME * 60 * 1000) {
//                        responseLineInfo();
//                    } else {
//                        setData(new Gson().fromJson(data[1], LineInfoModel.class));
//                    }
//                }
//            }
//        } catch (Exception e) {
//            responseLineInfo();
//            e.printStackTrace();
//        }
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
