package com.unito.smapssdk.chart;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.gson.Gson;
import com.unito.smapssdk.R;
import com.unito.smapssdk.library.JsonUtils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


public class PieChartFrag extends BaseActivity {

    @SuppressWarnings("FieldCanBeLocal")
    private PieChart chart;
    private String dataType = "DAY";
    private int waterType = 1;
    private long mLastClickTime = 0;

    private TextView tvToday;
    private TextView tvLastWeek;
    private TextView tvLastMonth;
    private TextView tvLastYear;
    private View view1, view2, view3, view4, view5;
    private TextView textView1, textView2, textView3, textView4, textView5;
    private Switch aSwitch;

    //    public static final int[] COLORFUL_COLORS = new int[5];
    public static final List<Integer> COLORFUL_COLORS = new ArrayList();

    @Override
    public int defineLayoutResource() {
        return R.layout.frag_simple_pie;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    protected void initializeComponents() {
        chart = findViewById(R.id.pieChart1);
        chart.setUsePercentValues(true);
        chart.getDescription().setEnabled(false);
        chart.setExtraOffsets(5, 10, 5, 5);
        chart.setExtraOffsets(20.f, 0.f, 20.f, 0.f);
        chart.setDrawHoleEnabled(false);
        chart.setTransparentCircleColor(Color.WHITE);
        chart.setTransparentCircleAlpha(110);

        chart.setHoleRadius(58f);
        chart.setTransparentCircleRadius(61f);
        chart.setRotationAngle(0);
        // enable rotation of the chart by touch
        chart.setRotationEnabled(false);
        chart.setHighlightPerTapEnabled(false);

//        tf = ResourcesCompat.getFont(getActivity(), R.font.avant_garde_lt);
        Legend l = chart.getLegend();
        l.setEnabled(false);

        tvToday = findViewById(R.id.fragment_web_dashboard_tvToday);
        tvLastWeek = findViewById(R.id.fragment_web_dashboard_tvLastWeek);
        tvLastMonth = findViewById(R.id.fragment_web_dashboard_tvLastMonth);
        tvLastYear = findViewById(R.id.fragment_web_dashboard_tvLastYear);

        view1 = findViewById(R.id.view1);
        view2 = findViewById(R.id.view2);
        view3 = findViewById(R.id.view3);
        view4 = findViewById(R.id.view4);
        view5 = findViewById(R.id.view5);
        textView1 = findViewById(R.id.text1);
        textView2 = findViewById(R.id.text2);
        textView3 = findViewById(R.id.text3);
        textView4 = findViewById(R.id.text4);
        textView5 = findViewById(R.id.text5);
        aSwitch = findViewById(R.id.swi);
//        if (Unito.getInstance().isFlavourSupport()) {
//            aSwitch.setVisibility(View.VISIBLE);
//            aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                @Override
//                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                    if (isChecked) {
//                        waterType = 2;
//                    } else {
//                        waterType = 1;
//                    }
//                    responseCache();
//                }
//            });
//        } else {
//            aSwitch.setVisibility(View.GONE);
//        }

        tvToday.setOnClickListener(this);
        tvLastWeek.setOnClickListener(this);
        tvLastMonth.setOnClickListener(this);
        tvLastYear.setOnClickListener(this);

        tvToday.callOnClick();

        responseChartInfo();
    }

    private void responseChartInfo() {
//        final String hubToken = Unito.getInstance().getSharedPreferences().getString(PREF.HUB_TOKEN, Const.DEFULTVAL);
////        final String hubToken = "31F8FD8B-DBBB-4CCC-9AC9-E4E3677CDCCD";
//        final GetChartInfoModel getChartInfoModel = new GetChartInfoModel(hubToken, waterType,
//                dataType);
//        UtilsApi.getAPIService().getChartInfo(getChartInfoModel,
////                "05f1bbc37bb7d6b187481589cda8b092").subscribeOn(Schedulers.io()).observeOn(
//                Unito.getInstance().getSharedPreferences().getString(PREF.HUB_SERIAL_ID, Const.DEFULTVAL)).subscribeOn(Schedulers.io()).observeOn(
//                AndroidSchedulers.mainThread()).subscribe(new Observer<ChartInfoModel>() {
//            @Override
//            public void onSubscribe(Disposable d) {
//                disposable = d;
//            }
//
//            @Override
//            public void onNext(ChartInfoModel response) {
//                Utils.dismissFancyProgressDialog(progressDialog);
//                if (null != response && response.getCode() == 200) {
//                    Unito.getInstance().getSharedPreferences_cache().edit().putString("pieChart" + dataType + waterType, System.currentTimeMillis() + "~" + new Gson().toJson(response)).apply();
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
        String jsonString = "{\n" +
                "    \"msg\": \"success\",\n" +
                "    \"code\": 200,\n" +
                "    \"data\": {\n" +
                "        \"coldWaterPercentage\": 25,\n" +
                "        \"hotWaterPercentage\": 15,\n" +
                "        \"sodaWaterPercentage\": 35,\n" +
                "        \"filteredWaterPercentage\": 25\n" +
                "    }\n" +
                "}";
        setData(new Gson().fromJson(jsonString, ChartInfoModel.class));

    }

    private void responseCache() {
//        try {
//            String responseData = Unito.getInstance().getSharedPreferences_cache().getString("pieChart" + dataType + waterType, "");
//            if (responseData.isEmpty()) {
//                responseChartInfo();
//            } else {
//                String[] data = responseData.split("~");
//                if (dataType.equals("DAY")) {
//                    if (Math.abs(System.currentTimeMillis() - Long.parseLong(data[0])) > 1 * DAY_CACHA_TIME * 60 * 1000) {
//                        responseChartInfo();
//                    } else {
//                        setData(new Gson().fromJson(data[1], ChartInfoModel.class));
//                    }
//                } else {
//                    if (Math.abs(System.currentTimeMillis() - Long.parseLong(data[0])) > 1 * OTHER_CACHA_TIME * 60 * 1000) {
//                        responseChartInfo();
//                    } else {
//                        setData(new Gson().fromJson(data[1], ChartInfoModel.class));
//                    }
//                }
//            }
//        } catch (Exception e) {
//            responseChartInfo();
//            e.printStackTrace();
//        }
    }

    private void setData(ChartInfoModel response) {
//        chart.animateY(500, Easing.EaseInOutQuad);
        ArrayList<PieEntry> entries = new ArrayList<>();
        COLORFUL_COLORS.clear();
        if (waterType == 1) {
//            if (BLEConstant.isInfinityDevice()) {
            if (response.getData().getHotWaterPercentage() != 0) {
                COLORFUL_COLORS.add(Color.rgb(255, 78, 0));
                entries.add(new PieEntry(response.getData().getHotWaterPercentage()));
            }
            if (response.getData().getFilteredWaterPercentage() != 0) {
                COLORFUL_COLORS.add(Color.rgb(216, 163, 2));
                entries.add(new PieEntry(response.getData().getFilteredWaterPercentage()));
            }
            if (response.getData().getColdWaterPercentage() != 0) {
                COLORFUL_COLORS.add(Color.rgb(0, 132, 255));
                entries.add(new PieEntry(response.getData().getColdWaterPercentage()));
            }
            if (response.getData().getSodaWaterPercentage() != 0) {
                COLORFUL_COLORS.add(Color.rgb(10, 196, 120));
                entries.add(new PieEntry(response.getData().getSodaWaterPercentage()));
            }
            view1.setVisibility(View.VISIBLE);
            view2.setVisibility(View.VISIBLE);
            view3.setVisibility(View.VISIBLE);
            view4.setVisibility(View.VISIBLE);
            textView1.setVisibility(View.VISIBLE);
            textView2.setVisibility(View.VISIBLE);
            textView3.setVisibility(View.VISIBLE);
            textView4.setVisibility(View.VISIBLE);
//            } else if (BLEConstant.isSparkleDevice()) {
//                if (response.getData().getHotWaterPercentage() != 0) {
//                    COLORFUL_COLORS.add(Color.rgb(10, 196, 120));
//                    entries.add(new PieEntry(response.getData().getHotWaterPercentage()));
//                }
//                if (response.getData().getFilteredWaterPercentage() != 0) {
//                    COLORFUL_COLORS.add(Color.rgb(216, 163, 2));
//                    entries.add(new PieEntry(response.getData().getFilteredWaterPercentage()));
//                }
//                if (response.getData().getColdWaterPercentage() != 0) {
//                    COLORFUL_COLORS.add(Color.rgb(0, 132, 255));
//                    entries.add(new PieEntry(response.getData().getColdWaterPercentage()));
//                }
//                view2.setVisibility(View.VISIBLE);
//                view3.setVisibility(View.VISIBLE);
//                view4.setVisibility(View.VISIBLE);
//                textView2.setVisibility(View.VISIBLE);
//                textView3.setVisibility(View.VISIBLE);
//                textView4.setVisibility(View.VISIBLE);
//            } else if (BLEConstant.isLavaDevice()) {
//                if (response.getData().getHotWaterPercentage() != 0) {
//                    COLORFUL_COLORS.add(Color.rgb(255, 78, 0));
//                    entries.add(new PieEntry(response.getData().getHotWaterPercentage()));
//                }
//                if (response.getData().getMixedWaterPercentage() != 0) {
//                    COLORFUL_COLORS.add(Color.rgb(182, 53, 163));
//                    entries.add(new PieEntry(response.getData().getMixedWaterPercentage()));
//                }
//                if (response.getData().getFilteredWaterPercentage() != 0) {
//                    COLORFUL_COLORS.add(Color.rgb(216, 163, 2));
//                    entries.add(new PieEntry(response.getData().getFilteredWaterPercentage()));
//                }
//                view1.setVisibility(View.VISIBLE);
//                view2.setVisibility(View.VISIBLE);
//                view5.setVisibility(View.VISIBLE);
//                textView1.setVisibility(View.VISIBLE);
//                textView2.setVisibility(View.VISIBLE);
//                textView5.setVisibility(View.VISIBLE);
//            } else if (BLEConstant.isElementsDevice()) {
//                if (response.getData().getHotWaterPercentage() != 0) {
//                    COLORFUL_COLORS.add(Color.rgb(255, 78, 0));
//                    entries.add(new PieEntry(response.getData().getHotWaterPercentage()));
//                }
//                if (response.getData().getFilteredWaterPercentage() != 0) {
//                    COLORFUL_COLORS.add(Color.rgb(216, 163, 2));
//                    entries.add(new PieEntry(response.getData().getFilteredWaterPercentage()));
//                }
//                if (response.getData().getColdWaterPercentage() != 0) {
//                    COLORFUL_COLORS.add(Color.rgb(0, 132, 255));
//                    entries.add(new PieEntry(response.getData().getColdWaterPercentage()));
//                }
//                view1.setVisibility(View.VISIBLE);
//                view2.setVisibility(View.VISIBLE);
//                view3.setVisibility(View.VISIBLE);
//                textView1.setVisibility(View.VISIBLE);
//                textView2.setVisibility(View.VISIBLE);
//                textView3.setVisibility(View.VISIBLE);
//            }
//        } else {
//            if (response.getData().getHotFlavorWaterPercentage() != 0) {
//                COLORFUL_COLORS.add(Color.rgb(255, 78, 0));
//                entries.add(new PieEntry(response.getData().getHotFlavorWaterPercentage()));
//            }
//            if (response.getData().getFilteredFlavorWaterPercentage() != 0) {
//                COLORFUL_COLORS.add(Color.rgb(216, 163, 2));
//                entries.add(new PieEntry(response.getData().getFilteredFlavorWaterPercentage()));
//            }
//            if (response.getData().getColdFlavorWaterPercentage() != 0) {
//                COLORFUL_COLORS.add(Color.rgb(0, 132, 255));
//                entries.add(new PieEntry(response.getData().getColdFlavorWaterPercentage()));
//            }
//            if (response.getData().getSodaFlavorWaterPercentage() != 0) {
//                COLORFUL_COLORS.add(Color.rgb(10, 196, 120));
//                entries.add(new PieEntry(response.getData().getSodaFlavorWaterPercentage()));
//            }
//            if (response.getData().getMixedFlavorWaterPercentage() != 0) {
//                COLORFUL_COLORS.add(Color.rgb(182, 53, 163));
//                entries.add(new PieEntry(response.getData().getMixedFlavorWaterPercentage()));
//            }
//        }

            if (entries.size() == 0) {
                chart.setData(null);
                chart.invalidate();
                return;
            }

            PieDataSet dataSet = new PieDataSet(entries, "");
            dataSet.setSliceSpace(3f);
//        dataSet.setSelectionShift(5f);
            ArrayList<Integer> colors = new ArrayList<>();
            for (int c : COLORFUL_COLORS)
                colors.add(c);
            colors.add(ColorTemplate.getHoloBlue());
            dataSet.setColors(colors);
            //dataSet.setSelectionShift(0f);
            dataSet.setValueLinePart1OffsetPercentage(100.f);
            dataSet.setValueLinePart1Length(0.6f);
            dataSet.setValueLinePart2Length(0.8f);
            dataSet.setUsingSliceColorAsValueLineColor(true);

            //dataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
            dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);

            PieData data = new PieData(dataSet);
            data.setValueFormatter(new PercentFormatter1(chart));
            data.setValueTextSize(12f);
            data.setValueTextColor(Color.rgb(14, 113, 159));
            chart.setData(data);

            // undo all highlights
            chart.highlightValues(null);

            chart.invalidate();
//        isFirst = true;
        }
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

}
