package com.example.spice.ui.graphs;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.spice.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import java.util.ArrayList;
import java.util.List;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class GraphsFragment extends Fragment
{
    //initializing the bar char to be displayed at a later time in the code.
    BarChart bar;

    //Firebase realtime database retrieval information
    private FirebaseAuth auth;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private String currentUserId = user.getUid();
    private DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Member").child(currentUserId).child("Graph");

    //Strings that hold the float values form the database
    public String Blues = null;
    public String Classical = null;
    public String Country = null;
    public String Disco = null;
    public String HipHop = null;
    public String Jazz = null;
    public String Metal = null;
    public String Pop = null;
    public String Reggae = null;
    public String Rock = null;

    private ArrayList<String> categories = new ArrayList<String>();

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    public Float map[];

    public GraphsFragment() {}

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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) { super.onViewCreated(view, savedInstanceState); }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        //initializing the view and setting it to a certain id which we have as fragment_graphs
        View view =inflater.inflate(R.layout.fragment_graphs,container,false);
        //sending the view to a GroupBarChar() function that will add the char to the veiw and display it
        GroupBarChart(view);
        //returns the edited view for the application to use
        return view;
    }

    public void GroupBarChart(View view)
    {
        //setting the chart variable to a certain id which is "bar" in the application xml file
        bar = (BarChart) view.findViewById(R.id.bar);
        Params();
    }

    public void setData()
    {
        //x axis labels
        String[] labels = {"Blues", "Classical", "Country", "Disco", "Hip-Hop", "Jazz", "Metal", "Pop", "Reggae", "Rock"};
        bar.setDrawBarShadow(false);
        bar.getDescription().setEnabled(false);

        //allow user to zoom in and out with fingers
        bar.setPinchZoom(true);

        //grid in the background
        bar.setDrawGridBackground(true);

        //values above bars in chart
        bar.setDrawValueAboveBar(true);

        //maximum 10 bars allowed in chart
        bar.setMaxVisibleValueCount(10);

        //fits bars to be the same width
        bar.setFitBars(true);

        //sets the lowest possible value to be 0 (percentages)
        bar.getAxisLeft().setAxisMinimum(0f);

        //sets the background to be the same color as our application
        bar.setGridBackgroundColor(393939);

        //sets an offset of 10 so that the text will not be cut off at the bottom of the graph
        bar.setExtraOffsets(0,0, 0,10);

        //setting an x axis
        XAxis xaxis = bar.getXAxis();

        //rotating so there isn't any scrunching of words
        xaxis.setLabelRotationAngle(-30f);

        //x axis lines
        xaxis.setDrawGridLines(true);

        //sets x axis to bottom (we can change to top and what not)
        xaxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        //spacing for the label values
        xaxis.setGranularity(1f);

        //enabling values to be shown
        xaxis.setDrawLabels(true);

        //enabling a line to be shown
        xaxis.setDrawAxisLine(true);

        //adding the actual labels to the x axis
        xaxis.setValueFormatter(new IndexAxisValueFormatter(labels));

        //setting the x axis text to white
        xaxis.setTextColor(Color.WHITE);

        //setting the x axis text to a size of 14
        xaxis.setTextSize(14);

        //setting the x axis line color to black so it is a little visible in the output
        xaxis.setAxisLineColor(Color.BLACK);

        //initializing a y axis to edit
        YAxis leftAxis = bar.getAxisLeft();

        //setting y axis text color to white
        leftAxis.setTextColor(Color.WHITE);

        //setting the y axis text size to 12
        leftAxis.setTextSize(12);

        //setting the color of the line to black so it will be a bit visible
        leftAxis.setAxisLineColor(Color.BLACK);

        //setting the maximum percent to 100 which will almost never happen (values always have error or are part one category or another)
        leftAxis.setAxisMaximum(100);

        //enabling gridlines for y axis
        leftAxis.setDrawGridLines(true);

        //setting spacing
        leftAxis.setGranularity(2);

        //amount of y axis labels there will be
        leftAxis.setLabelCount(8, true);

        //positioning the y axis outside of the graph
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);

        //enabling the right axis
        bar.getAxisRight().setEnabled(false);

        //initializing an arraylist for the graph values to be put into
        ArrayList<BarEntry> valueSet = new ArrayList<BarEntry>();

        if(Blues == null) //error handling for if there wasn't anything in Blues from the database
        {
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
        }
        else //Inputting all values from the database into the chart
        {
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
        }

        //setting the bar color rgb values
        int orange = Color. rgb(236, 127, 55);

        //making a new bar set to put our values arraylist into to set the graph
        List<IBarDataSet> dataSets = new ArrayList<>();

        //creating a new dataset for the bargraph
        BarDataSet barDataSet = new BarDataSet(valueSet, "Data Set");

        //setting a constraint to the left
        barDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);

        //changing color to orange
        barDataSet.setColor(orange);

        //if clicked or pressed the bar will be a darker shade of orange
        barDataSet.setHighlightEnabled(true);
        barDataSet.setHighLightColor(Color.BLACK);
        barDataSet.setValueTextColor(Color.BLACK);

        //add the dataset to the graph
        dataSets.add(barDataSet);

        //creating a new BarData variable for the dataset to be placed into
        BarData barData = new BarData();
        barData.addDataSet(barDataSet);

        //setting the label count to be correct if it isn't already
        bar.getXAxis().setLabelCount(barDataSet.getEntryCount());

        //adding the finalized graph a finalized datavalue to set the graph
        BarData data = new BarData(barDataSet);

        //setting the graph with the finalized values
        bar.setData(data);

        //notify app that something was changed
        bar.notifyDataSetChanged();

        //refresh graph
        bar.invalidate();

        //animate the graph when clicked
        bar.animateX(300);
        bar.animateY(500);
    }

//This function will retrieve all song information used for the graph out of the database
    public void Params()
    {
        currentUserId = user.getUid();
        FirebaseDatabase.getInstance().getReference().child("Member").child(currentUserId).child("Graph")
        .addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                //retrieving all information from the database
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
                //sets the data in the graph
                setData();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {
            }
        });
    }
}

