package com.example.mobilesensorapplication.OperationalClasses;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;
import android.widget.LinearLayout;

import com.example.mobilesensorapplication.ObjectClasses.AccelerometerAxisData;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

public class DataGraph {
    Context mContext;
    View mView;
    LinearLayout chartLayout;

    private GraphicalView mChart;


    private XYMultipleSeriesDataset mDataset = new XYMultipleSeriesDataset();

    private XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();

    private XYSeries mCurrentSeries;
    private XYSeries yCurrentSeries;
    private XYSeries zCurrentSeries;

    private XYSeriesRenderer xCurrentRenderer;
    private XYSeriesRenderer yCurrentRenderer;
    private XYSeriesRenderer zCurrentRenderer;


    public DataGraph(Context mContext, View mView, LinearLayout chartLayout) {
        this.mContext = mContext;
        this.mView = mView;
        this.chartLayout = chartLayout;
    }

    public void initChart() {
        mCurrentSeries = new XYSeries("X-axis");
        yCurrentSeries = new XYSeries("Y-axis");
        zCurrentSeries = new XYSeries("Z-axis");
        mDataset.addSeries(mCurrentSeries);
        mDataset.addSeries(yCurrentSeries);
        mDataset.addSeries(zCurrentSeries);

        xCurrentRenderer = new XYSeriesRenderer();
        yCurrentRenderer = new XYSeriesRenderer();
        zCurrentRenderer = new XYSeriesRenderer();

        // X-axis
        xCurrentRenderer.setLineWidth(2);
        xCurrentRenderer.setColor(Color.GREEN);
        xCurrentRenderer.setDisplayBoundingPoints(true);
        xCurrentRenderer.setPointStyle(PointStyle.CIRCLE);
        xCurrentRenderer.setPointStrokeWidth(1);

        // Y-axis
        yCurrentRenderer.setLineWidth(2);
        yCurrentRenderer.setColor(Color.RED);
        yCurrentRenderer.setDisplayBoundingPoints(true);
        yCurrentRenderer.setPointStyle(PointStyle.CIRCLE);
        yCurrentRenderer.setPointStrokeWidth(1);

        // Z-axis
        zCurrentRenderer.setLineWidth(2);
        zCurrentRenderer.setColor(Color.BLUE);
        zCurrentRenderer.setDisplayBoundingPoints(true);
        zCurrentRenderer.setPointStyle(PointStyle.CIRCLE);
        zCurrentRenderer.setPointStrokeWidth(1);

        mRenderer.addSeriesRenderer(xCurrentRenderer);
        mRenderer.addSeriesRenderer(yCurrentRenderer);
        mRenderer.addSeriesRenderer(zCurrentRenderer);

        // Chart styling
        mRenderer.setMarginsColor(Color.WHITE);
        mRenderer.setPanEnabled(false, false);
        mRenderer.setShowGrid(true);
        mRenderer.setYLabelsAlign(Paint.Align.RIGHT);
        mRenderer.setXLabelsColor(Color.BLACK);
        mRenderer.setYLabelsColor(0, Color.BLACK);
        mRenderer.setLabelsTextSize(20);

    }

    public void setUpdata(AccelerometerAxisData accelerometerAxisData) {



        if (mChart == null) {

            mCurrentSeries.add(0, accelerometerAxisData.getC_xValue());
            yCurrentSeries.add(0, accelerometerAxisData.getD_yValue());
            zCurrentSeries.add(0, accelerometerAxisData.getE_zValue());
            mChart = ChartFactory.getCubeLineChartView(mContext, mDataset, mRenderer, 0);
            chartLayout.addView(mChart);
        } else {
            mChart.repaint();
        }
        mCurrentSeries.add(0, accelerometerAxisData.getC_xValue());
        yCurrentSeries.add(0, accelerometerAxisData.getD_yValue());
        zCurrentSeries.add(0, accelerometerAxisData.getE_zValue());

    }

}

