package cn.edu.pkusz.battery.network;

import android.graphics.Color;
import android.graphics.Paint;

import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.util.Random;

/**
 * Created by 陶世博 on 2015/6/1.
 */
public class TrafficChart {

    public static final int pointNumber = 24 * 60 / 10;
    public static XYMultipleSeriesRenderer buildRenderer() {
        XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();
        setRenderer(mRenderer);
        addSeriesRenderer(mRenderer);
        return mRenderer;
    }

    public static void setRenderer(XYMultipleSeriesRenderer mRenderer) {
        mRenderer.setOrientation(XYMultipleSeriesRenderer.Orientation.HORIZONTAL);
        mRenderer.setXAxisMin(1);
        mRenderer.setXAxisMax(144);
        mRenderer.setYAxisMin(0);
        mRenderer.setYAxisMax(102);
        mRenderer.setYLabelsPadding(2);
        mRenderer.setXLabelsAlign(Paint.Align.CENTER);
        mRenderer.setYLabelsAlign(Paint.Align.RIGHT);
        mRenderer.setXLabels(0);
        mRenderer.addXTextLabel(1, "18:00");
        mRenderer.addXTextLabel(37, "00:00");
        mRenderer.addXTextLabel(73, "06:00");
        mRenderer.addXTextLabel(109, "12:00");
        mRenderer.addXTextLabel(144, "18:00");
        mRenderer.setYLabels(0);
        mRenderer.addYTextLabel(20, "");
        mRenderer.addYTextLabel(40, "");
        mRenderer.addYTextLabel(60, "");
        mRenderer.addYTextLabel(80, "");
        mRenderer.addYTextLabel(100, "");
        mRenderer.setShowGrid(true);
        //当自定义坐标轴标签时使用该方法可以显示网格
        mRenderer.setShowCustomTextGrid(true);
        //不可沿X轴和Y轴拖动
        mRenderer.setPanEnabled(false, false);
        //不可缩放
        mRenderer.setZoomEnabled(false, false);
        mRenderer.setExternalZoomEnabled(false);
        //可点击
        mRenderer.setClickEnabled(false);
        mRenderer.setSelectableBuffer(2);
        mRenderer.setApplyBackgroundColor(true);
        mRenderer.setBackgroundColor(Color.argb(255, 47, 54, 62));
        mRenderer.setMarginsColor(Color.argb(255, 39, 44, 50));
        mRenderer.setBarWidth(1.2f);
        mRenderer.setBarSpacing(0.5f);
        //不显示legend(坐标轴的说明文字)
        mRenderer.setShowLegend(false);
    }

    public static void addSeriesRenderer(XYMultipleSeriesRenderer mRenderer) {
        XYSeriesRenderer seriesRenderer = new XYSeriesRenderer();
        //设置线的颜色
        seriesRenderer.setColor(Color.GREEN);
        //设置是否在柱状条或点的上方显示值
        //seriesRenderer.setDisplayChartValues(true);
        mRenderer.addSeriesRenderer(seriesRenderer);
    }

    public static XYMultipleSeriesDataset buildDataset() {
        XYMultipleSeriesDataset mDataset = new XYMultipleSeriesDataset();
        Random random = new Random();
        //新建一系列点，标题为空
        double data[] = new double[pointNumber];
        data[100]=20.0;
        data[101]=30.0;
        data[102]=40.0;
        data[103]=50.0;
        data[104]=30.0;
        data[105]=20.0;
        data[106]=10.0;
        XYSeries series = new XYSeries("");
        for(int i=1;i<=pointNumber;i++){
            series.add((double)i, data[i-1]);
        }
        mDataset.addSeries(series);
        return mDataset;
    }

    public static XYMultipleSeriesDataset buildDataset(double cpumount) {
        XYMultipleSeriesDataset mDataset = new XYMultipleSeriesDataset();
        Random random = new Random((long) (cpumount*2));
        //新建一系列点，标题为空
        double data[] = new double[pointNumber];
        data[100]=20.0;
        data[101]=30.0;
        data[102]=40.0;
        data[103]=50.0;
        data[104]=30.0;
        data[105]=20.0;
        data[106]=10.0;
        XYSeries series = new XYSeries("");
        for(int i=1;i<=pointNumber;i++){
            series.add((double)i, random.nextDouble()*100 % 100);
        }
        mDataset.addSeries(series);
        return mDataset;
    }
}
