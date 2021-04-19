package com.example.spice.ui.graphs;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.spice.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import java.util.ArrayList;
import java.util.List;

import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.data.BarDataSet;

public class GraphsFragment extends Fragment
{
    BarChart bar;
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public GraphsFragment() { }


    public static GraphsFragment newInstance()
    {
        return new GraphsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view =inflater.inflate(R.layout.fragment_graphs,container,false);
        GroupBarChart(view);
        return view;
    }


    public void GroupBarChart(View view){
        bar = (BarChart) view.findViewById(R.id.bar);
        bar.setDrawBarShadow(false);
        bar.getDescription().setEnabled(false);
        bar.setPinchZoom(false);
        bar.setDrawGridBackground(true);
        bar.setDrawValueAboveBar(true);
        bar.setMaxVisibleValueCount(10);

        setData(10);
        bar.setFitBars(true);
    }

    private void setData(int count)
    {
        String[] labels = {"Blues", "Classical", "Country", "Disco", "Hip-Hop", "Jazz", "Metal", "Pop", "Reggae", "Rock"};

        XAxis xaxis = bar.getXAxis();
        xaxis.setDrawGridLines(false);
        xaxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xaxis.setGranularity(1f);
        xaxis.setDrawLabels(true);
        xaxis.setDrawAxisLine(false);
        xaxis.setValueFormatter(new IndexAxisValueFormatter(labels));

        YAxis yAxisLeft = bar.getAxisLeft();
        yAxisLeft.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        yAxisLeft.setDrawGridLines(false);
        yAxisLeft.setDrawAxisLine(false);
        yAxisLeft.setEnabled(false);

        bar.getAxisRight().setEnabled(false);

        Legend legend = bar.getLegend();
        legend.setEnabled(false);

        ArrayList<BarEntry> valueSet1 = new ArrayList<BarEntry>();

        for (int i = 0; i < 10; ++i) {
            BarEntry entry = new BarEntry(i, (i+1)*10);
            valueSet1.add(entry);
        }

        List<IBarDataSet> dataSets = new ArrayList<>();
        BarDataSet barDataSet = new BarDataSet(valueSet1, "Data Set");
        barDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        barDataSet.setColor(Color.RED);
        barDataSet.setHighlightEnabled(true);
        barDataSet.setHighLightColor(Color.WHITE);
        barDataSet.setValueTextColor(Color.WHITE);
        barDataSet.setDrawValues(false);
        barDataSet.setHighlightEnabled(true);
        dataSets.add(barDataSet);

        bar.getDescription().setText("Classifier Percentages");

        bar.getXAxis().setLabelCount(barDataSet.getEntryCount());
        bar.getDescription().setTextSize(16);
        bar.getDescription().setTextColor(Color.WHITE);
        BarData data = new BarData(barDataSet);

        bar.setData(data);
        bar.invalidate();
        bar.animateY(500);
    }
    /*

    public void GroupBarChart(View view){
        bar = (BarChart) view.findViewById(R.id.bar);
        bar.setDrawBarShadow(false);
        bar.getDescription().setEnabled(false);
        bar.setPinchZoom(false);
        bar.setDrawGridBackground(true);
        // empty labels so that the names are spread evenly
        String[] labels = {"", "Classical", "Rock", "Reggae", "Hip-hop", "Pop", ""};
        XAxis xAxis = bar.getXAxis();
        xAxis.setCenterAxisLabels(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(true);
        xAxis.setGranularity(1f); // only intervals of 1 day
        xAxis.setTextColor(Color.WHITE);
        xAxis.setTextSize(12);
        xAxis.setAxisLineColor(Color.WHITE);
        xAxis.setAxisMinimum(1f);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));

        YAxis leftAxis = bar.getAxisLeft();
        leftAxis.setTextColor(Color.WHITE);
        leftAxis.setTextSize(12);
        leftAxis.setAxisLineColor(Color.WHITE);
        leftAxis.setDrawGridLines(true);
        leftAxis.setGranularity(2);
        leftAxis.setLabelCount(8, true);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);

        bar.getAxisRight().setEnabled(false);
        bar.getLegend().setEnabled(false);

        float[] valOne = {10, 20, 30, 40, 50};

        ArrayList<BarEntry> barOne = new ArrayList<>();
        ArrayList<BarEntry> barTwo = new ArrayList<>();
        for (int i = 0; i < valOne.length; i++) {
            barOne.add(new BarEntry(i, valOne[i]));
        }

        BarDataSet set1 = new BarDataSet(barOne, "barOne");
        set1.setColor(Color.RED);
        BarDataSet set2 = new BarDataSet(barTwo, "barTwo");
        set2.setColor(Color.BLUE);

        set1.setHighlightEnabled(false);
        set2.setHighlightEnabled(false);
        set1.setDrawValues(false);
        set2.setDrawValues(false);

        ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
        dataSets.add(set1);
        dataSets.add(set2);
        BarData data = new BarData(dataSets);
        float groupSpace = 0.4f;
        float barSpace = 0f;
        float barWidth = 0.3f;
        // (barSpace + barWidth) * 2 + groupSpace = 1
        data.setBarWidth(barWidth);
        // so that the entire chart is shown when scrolled from right to left
        xAxis.setAxisMaximum(labels.length - 1.1f);
        bar.setData(data);
        bar.setScaleEnabled(false);
        bar.setVisibleXRangeMaximum(6f);
        bar.groupBars(1f, groupSpace, barSpace);
        bar.invalidate();
    }

 */



    /*
   public void GroupBarChart(View view){
        bar = (BarChart) view.findViewById(R.id.bar);
        bar.setDrawBarShadow(false);
        bar.getDescription().setEnabled(false);
        bar.setPinchZoom(false);
        bar.setDrawGridBackground(true);
        // empty labels so that the names are spread evenly
        String[] labels = {"", "Classical", "Rock", "Reggae", "Hip-hop", "Pop", ""};
        XAxis xAxis = bar.getXAxis();
        xAxis.setCenterAxisLabels(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(true);
        xAxis.setGranularity(1f); // only intervals of 1 day
        xAxis.setTextColor(Color.WHITE);
        xAxis.setTextSize(12);
        xAxis.setAxisLineColor(Color.WHITE);
        xAxis.setAxisMinimum(1f);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));

        YAxis leftAxis = bar.getAxisLeft();
        leftAxis.setTextColor(Color.WHITE);
        leftAxis.setTextSize(12);
        leftAxis.setAxisLineColor(Color.WHITE);
        leftAxis.setDrawGridLines(true);
        leftAxis.setGranularity(2);
        leftAxis.setLabelCount(8, true);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);

        bar.getAxisRight().setEnabled(false);
        bar.getLegend().setEnabled(false);

        setData(10);
        bar.setFitBars(true);
    }

    private void setData(int count)
    {
        ArrayList<BarEntry> yVals = new ArrayList<>();

        for(int i = 0; i < count; i++)
        {
            float value = (float) (Math.random()*100);
            yVals.add(new BarEntry(i, (int) value));
        }

        BarDataSet set = new BarDataSet(yVals,"Data Set");
        set.setColors(ColorTemplate.MATERIAL_COLORS);
        set.setDrawValues(true);
        set.setHighlightEnabled(false);
        set.setDrawValues(false);

        BarData data = new BarData(set);

        bar.setData(data);
        bar.invalidate();
        bar.animateY(500);
    }

     */
}
