package cn.edu.pkusz.battery.power;

import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.util.Random;

/**
 * Created by 陶世博 on 2015/6/1.
 */
public class BatteryLevelChart {

    public static XYMultipleSeriesRenderer buildRenderer() {
        XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();
        setRenderer(mRenderer);
        addSeriesRenderer(mRenderer);
        return mRenderer;
    }

    public static void setRenderer(XYMultipleSeriesRenderer mRenderer) {
        mRenderer.setOrientation(XYMultipleSeriesRenderer.Orientation.HORIZONTAL);
        mRenderer.setXAxisMin(1);
        mRenderer.setXAxisMax(7.2);
        mRenderer.setYAxisMin(0);
        mRenderer.setYAxisMax(102);
        mRenderer.setYLabelsPadding(2);
        mRenderer.setXLabelsAlign(Paint.Align.CENTER);
        mRenderer.setYLabelsAlign(Paint.Align.RIGHT);
        mRenderer.setXLabels(0);
        mRenderer.addXTextLabel(1,"00:00");
        mRenderer.addXTextLabel(2,"04:00");
        mRenderer.addXTextLabel(3,"08:00");
        mRenderer.addXTextLabel(4,"12:00");
        mRenderer.addXTextLabel(5,"16:00");
        mRenderer.addXTextLabel(6,"20:00");
        mRenderer.addXTextLabel(7,"24:00");
        mRenderer.setYLabels(0);
        mRenderer.addYTextLabel(20,"20");
        mRenderer.addYTextLabel(40,"40");
        mRenderer.addYTextLabel(60,"60");
        mRenderer.addYTextLabel(80,"80");
        mRenderer.addYTextLabel(100,"100");
        mRenderer.setShowGrid(true);
        //当自定义坐标轴标签时使用该方法可以显示网格
        mRenderer.setShowCustomTextGrid(true);
        //不可沿X轴和Y轴拖动
        mRenderer.setPanEnabled(false, false);
        //不可缩放
        mRenderer.setZoomEnabled(false, false);
        mRenderer.setExternalZoomEnabled(false);
        //不可点击
        mRenderer.setClickEnabled(false);
        mRenderer.setApplyBackgroundColor(true);
        mRenderer.setBackgroundColor(Color.DKGRAY);
        mRenderer.setPointSize(5f);
        //不显示legend(坐标轴的说明文字)
        mRenderer.setShowLegend(false);
    }

    public static void addSeriesRenderer(XYMultipleSeriesRenderer mRenderer) {
        XYSeriesRenderer seriesRenderer = new XYSeriesRenderer();
        //设置线的颜色
        seriesRenderer.setColor(Color.GREEN);
        //设置点的类型
        seriesRenderer.setPointStyle(PointStyle.CIRCLE);
        //点是否是实心的
        seriesRenderer.setFillPoints(false);
        seriesRenderer.setLineWidth(2);
        //设置空心点的线宽
        seriesRenderer.setPointStrokeWidth(3f);
        mRenderer.addSeriesRenderer(seriesRenderer);
    }

    public static XYMultipleSeriesDataset buildDataset(int pageNumber) {
        XYMultipleSeriesDataset mDataset = new XYMultipleSeriesDataset();
        double x [] = {1,2,3,4,5,6,7};
        double y [] = {100,75,60,20,50,90,80};
        Random random = new Random();
        random.setSeed(pageNumber*11+100);
        XYSeries series = new XYSeries(""+pageNumber);
        for(int i=0;i<x.length;i++){
            series.add(x[i], random.nextDouble() * 100);
        }
        mDataset.addSeries(series);
        return mDataset;
    }

}
