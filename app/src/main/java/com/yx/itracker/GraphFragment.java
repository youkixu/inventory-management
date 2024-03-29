package com.yx.itracker;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.yx.itracker.Class.Order;
import com.yx.itracker.Class.OrderCalculations;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;


public class GraphFragment extends Fragment {
    private OnGraphDataInteractionListener mListener;
    private LineChart chart;

    public GraphFragment() { }

    //https://github.com/PhilJay/MPAndroidChart/blob/5030b36c86cfa2d076bb6b86fa8fcb37ce58557f/MPChartExample/src/main/java/com/xxmassdeveloper/mpchartexample/LineChartTime.java
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View view = inflater.inflate(R.layout.fragment_graph, container, false);
        chart = (LineChart) view.findViewById(R.id.chart);

        try {
            List<Entry> entries = new ArrayList<Entry>();
            entries.add(new Entry(0,0));

//            long referenceTimestamp = 1541315708;
//            entries.add(new Entry(0,20));
//            entries.add(new Entry(77,50));
//            entries.add(new Entry(150,70));


            LineDataSet dataSet = new LineDataSet(entries, "Label"); // add entries to dataset
            dataSet.setLineWidth(2.5f);
            dataSet.setCircleRadius(4.5f);
            dataSet.setColor(Color.rgb(240, 99, 99));
            dataSet.setCircleColor(Color.rgb(240, 99, 99));
            dataSet.setHighLightColor(Color.rgb(190, 190, 190));
            dataSet.setValueTextSize(10f);

           //hart.setBackgroundColor(Color.rgb(104, 241, 175));

            LineData lineData = new LineData(dataSet);

            XAxis xAxis = chart.getXAxis();
            xAxis.setPosition(XAxis.XAxisPosition.TOP_INSIDE);
            xAxis.setTextSize(10f);
            xAxis.setTextColor(Color.WHITE);
            xAxis.setDrawAxisLine(false);
            xAxis.setDrawGridLines(true);
            xAxis.setTextColor(Color.rgb(255, 102, 102));
            xAxis.setCenterAxisLabels(true);
            xAxis.setGranularity(1f); // one hour
            //xAxis.setValueFormatter(new HourAxisValueFormatter(referenceTimestamp));

            YAxis leftAxis = chart.getAxisLeft();
            leftAxis.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
            leftAxis.setTextColor(ColorTemplate.getHoloBlue());
            leftAxis.setDrawGridLines(true);
            leftAxis.setGranularityEnabled(true);
            leftAxis.setAxisMinimum(0f);
            //leftAxis.setAxisMaximum(170f);
            leftAxis.setYOffset(-9f);
            leftAxis.setTextColor(Color.rgb(255, 102, 102));

            YAxis rightAxis = chart.getAxisRight();
            rightAxis.setEnabled(false);

            chart.getLegend().setTextColor(Color.WHITE);
            Description chartDescription = new Description();
            chartDescription.setText("");
            chart.setDescription(chartDescription);
            chart.setData(lineData);
            chart.invalidate(); // refresh

        } catch (Exception e) {
            Log.d("aa", e.getMessage());
        }

        return view;
    }


    private void setData(List<Order> orderList) {
        OrderCalculations oc = new OrderCalculations(orderList);
        HashMap<Long, Float> orderMap = oc.getTimeProfitMap();
        Log.d("aabb", "OM:"+ orderMap.toString());

        Map.Entry<Long, Float> entry = orderMap.entrySet().iterator().next();
        long referenceTimestamp = entry.getKey();

        XAxis xAxis = chart.getXAxis();
        xAxis.setValueFormatter(new HourAxisValueFormatter(referenceTimestamp));

        ArrayList<Entry> values = new ArrayList<>();

        for (Map.Entry<Long, Float> mapEntry : orderMap.entrySet()) {
            System.out.println("Key = " + mapEntry.getKey() + ", Value = " + mapEntry.getValue());
            long xNew = mapEntry.getKey() - referenceTimestamp;
            values.add(new Entry(xNew, mapEntry.getValue()));
        }

        // create a dataset and give it a type
        LineDataSet set1 = new LineDataSet(values, "Today's Profit");
        set1.setAxisDependency(YAxis.AxisDependency.LEFT);
        set1.setColor(ColorTemplate.getHoloBlue());
        set1.setValueTextColor(ColorTemplate.getHoloBlue());
        set1.setLineWidth(1.5f);
        set1.setDrawCircles(false);
        set1.setDrawValues(false);
        set1.setFillAlpha(65);
        set1.setFillColor(ColorTemplate.getHoloBlue());
        set1.setHighLightColor(Color.rgb(244, 117, 117));
        set1.setDrawCircleHole(false);

        // create a data object with the data sets
        LineData data = new LineData(set1);
        data.setValueTextColor(Color.WHITE);
        data.setValueTextSize(9f);

        // set data
        chart.setData(data);
    }

    public float getRandom(float range, float start) {
        return (float) (Math.random() * range) + start;
    }

    public void updateGraphData(List<Order> orderList) {
        setData(orderList);
        chart.getData().notifyDataChanged();
        chart.notifyDataSetChanged();
        chart.invalidate(); // refresh

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnGraphDataInteractionListener) {
            mListener = (OnGraphDataInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnGraphDataInteractionListener {
        // TODO: Update argument type and name
        List<Order> getOrderList();
    }
}
