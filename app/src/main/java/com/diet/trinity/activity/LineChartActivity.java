package com.diet.trinity.activity;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;

import com.diet.trinity.R;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LineChartActivity extends AppCompatActivity {
    private static final Random RANDOM = new Random();
    private LineGraphSeries<DataPoint> series;
    private int lastX = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_chart);
        // we get graph view instance
        GraphView graph = (GraphView) findViewById(R.id.graph);
        // data
        series = new LineGraphSeries<DataPoint>();
        series.appendData(new DataPoint(0,1), true, 10);
        series.appendData(new DataPoint(1,1), true, 10);
        series.appendData(new DataPoint(2,3), true, 10);
        series.appendData(new DataPoint(3,1), true, 10);
        series.appendData(new DataPoint(4,3), true, 10);
        series.appendData(new DataPoint(5,2), true, 10);
        series.appendData(new DataPoint(6,4), true, 10);
        series.appendData(new DataPoint(7,3), true, 10);
        series.appendData(new DataPoint(8,2), true, 10);
        series.appendData(new DataPoint(10,1), true, 10);
        series.appendData(new DataPoint(12,1), true, 10);
        series.setColor(Color.CYAN);
        series.setDrawDataPoints(true);
        series.setDataPointsRadius(10);
        series.setThickness(8);
        graph.addSeries(series);

        // styling grid/labels
        graph.getGridLabelRenderer().setVerticalLabelsVAlign(GridLabelRenderer.VerticalLabelsVAlign.ABOVE);
        graph.getGridLabelRenderer().reloadStyles();
        graph.getGridLabelRenderer().setHorizontalAxisTitleColor(Color.GREEN);

        // customize a little bit viewport
        Viewport viewport = graph.getViewport();
        viewport.setYAxisBoundsManual(true);

        viewport.setXAxisBoundsManual(true);
        viewport.scrollToEnd();
        viewport.setMinY(0);
        viewport.setMaxY(10);

        viewport.setScrollable(true);
        viewport.setScalable(true);
    }
}