package com.example.spice.ui.graphs;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.spice.R;
import com.example.spice.ui.login.login;
import com.example.spice.ui.profile.ChangePassword;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import java.util.ArrayList;
import java.util.List;

import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class GraphsFragment extends Fragment
{
    BarChart bar;

    private FirebaseAuth auth;
    private String currentUserId;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private DatabaseReference ref;

    public String Blues;
    public String Classical;
    public String Country;
    public String Disco;
    public String HipHop;
    public String Jazz;
    public String Metal;
    public String Pop;
    public String Reggae;
    public String Rock;

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    public Float map[];

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

        setData();
        bar.setFitBars(true);
    }

    public void setData()
    {
        String[] labels = {"Blues", "Classical", "Country", "Disco", "Hip-Hop", "Jazz", "Metal", "Pop", "Reggae", "Rock"};

        XAxis xaxis = bar.getXAxis();
        xaxis.setDrawGridLines(true);
        xaxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xaxis.setGranularity(1f);
        xaxis.setDrawLabels(true);
        xaxis.setDrawAxisLine(true);
        xaxis.setValueFormatter(new IndexAxisValueFormatter(labels));
        xaxis.setTextColor(Color.WHITE);
        xaxis.setTextSize(10);
        xaxis.setAxisLineColor(Color.BLACK);

        YAxis yAxisLeft = bar.getAxisLeft();
        yAxisLeft.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        yAxisLeft.setDrawGridLines(true);
        yAxisLeft.setDrawAxisLine(true);
        yAxisLeft.setEnabled(true);

        YAxis leftAxis = bar.getAxisLeft();
        leftAxis.setTextColor(Color.WHITE);
        leftAxis.setTextSize(8);
        leftAxis.setAxisLineColor(Color.WHITE);
        leftAxis.setDrawGridLines(true);
        leftAxis.setGranularity(2);
        leftAxis.setLabelCount(8, true);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);

        bar.getAxisRight().setEnabled(false);

        Legend legend = bar.getLegend();
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        legend.setOrientation(Legend.LegendOrientation.VERTICAL);
        legend.setDrawInside(false);
        legend.setTextColor(Color.WHITE);
        legend.setEnabled(false);

        ArrayList<BarEntry> valueSet = new ArrayList<BarEntry>();

        BarEntry entry = new BarEntry(0, 0);
        valueSet.add(entry);
        entry = new BarEntry(1, 0);
        valueSet.add(entry);
        entry = new BarEntry(2, 0);
        valueSet.add(entry);
        entry = new BarEntry(3, 0);
        valueSet.add(entry);
        entry = new BarEntry(4, 0);
        valueSet.add(entry);
        entry = new BarEntry(5, 0);
        valueSet.add(entry);
        entry = new BarEntry(6, 0);
        valueSet.add(entry);
        entry = new BarEntry(7, 0);
        valueSet.add(entry);
        entry = new BarEntry(8, 0);
        valueSet.add(entry);
        entry = new BarEntry(9, 0);
        valueSet.add(entry);

        /*

        currentUserId = user.getUid();
        FirebaseDatabase.getInstance().getReference().child("Member").child(currentUserId).child("Graph")
                .addValueEventListener(new ValueEventListener()
                {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                    {
                        Blues = dataSnapshot.child("Blues").getValue().toString();
                        Classical = dataSnapshot.child("Classical").getValue().toString();
                        Country = dataSnapshot.child("Country").getValue().toString();
                        Disco = dataSnapshot.child("Disco").getValue().toString();
                        HipHop = dataSnapshot.child("Hip-Hop").getValue().toString();
                        Jazz = dataSnapshot.child("Jazz").getValue().toString();
                        Metal = dataSnapshot.child("Metal").getValue().toString();
                        Pop = dataSnapshot.child("Pop").getValue().toString();
                        Reggae = dataSnapshot.child("Reggae").getValue().toString();
                        Rock = dataSnapshot.child("Rock").getValue().toString();
                        System.out.print(Blues);
                        System.out.print(Blues);
                        System.out.print(Blues);
                        System.out.print(Blues);
                        System.out.print(Blues);
                        System.out.print(Blues);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError)
                    {
                    }
                });

        BarEntry entry = new BarEntry(0, Float.valueOf(Blues)*100);
        valueSet.add(entry);
        entry = new BarEntry(1, Float.valueOf(Classical)*100);
        valueSet.add(entry);
        entry = new BarEntry(2, Float.valueOf(Country)*100);
        valueSet.add(entry);
        entry = new BarEntry(3, Float.valueOf(Disco)*100);
        valueSet.add(entry);
        entry = new BarEntry(4, Float.valueOf(HipHop)*100);
        valueSet.add(entry);
        entry = new BarEntry(5, Float.valueOf(Jazz)*100);
        valueSet.add(entry);
        entry = new BarEntry(6, Float.valueOf(Metal)*100);
        valueSet.add(entry);
        entry = new BarEntry(7, Float.valueOf(Pop)*100);
        valueSet.add(entry);
        entry = new BarEntry(8, Float.valueOf(Reggae)*100);
        valueSet.add(entry);
        entry = new BarEntry(9, Float.valueOf(Rock)*100);
        valueSet.add(entry);

         */


        List<IBarDataSet> dataSets = new ArrayList<>();
        BarDataSet barDataSet = new BarDataSet(valueSet, "Data Set");
        barDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        barDataSet.setColor(Color.RED);
        barDataSet.setHighlightEnabled(true);
        barDataSet.setHighLightColor(Color.BLACK);
        barDataSet.setValueTextColor(Color.BLACK);
        barDataSet.setDrawValues(false);
        barDataSet.setHighlightEnabled(true);
        dataSets.add(barDataSet);

        bar.getDescription().setText("Classifier Percentages");

        bar.getXAxis().setLabelCount(barDataSet.getEntryCount());
        bar.getDescription().setTextSize(16);
        bar.getDescription().setTextColor(Color.WHITE);
        BarData data = new BarData(barDataSet);

        bar.setData(data);
        bar.notifyDataSetChanged();
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

}
