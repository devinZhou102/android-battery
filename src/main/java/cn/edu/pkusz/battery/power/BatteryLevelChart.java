package cn.edu.pkusz.battery.power;

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Paint;

import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Random;

import cn.edu.pkusz.battery.R;
import cn.edu.pkusz.battery.activity.BatteryInfoActivity;
import cn.edu.pkusz.battery.common.Constants;
import cn.edu.pkusz.battery.common.Static;
import cn.edu.pkusz.battery.db.BatteryLevelEntry;
import cn.edu.pkusz.battery.db.DbManager;

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
        mRenderer.setXAxisMax(146);
        mRenderer.setYAxisMin(0);
        mRenderer.setYAxisMax(102);
        mRenderer.setYLabelsPadding(2);
        mRenderer.setXLabelsAlign(Paint.Align.CENTER);
        mRenderer.setYLabelsAlign(Paint.Align.RIGHT);
        mRenderer.setXLabels(0);
        setXTextLabel(mRenderer);
        mRenderer.setYLabels(0);
        mRenderer.addYTextLabel(20, "20");
        mRenderer.addYTextLabel(40, "40");
        mRenderer.addYTextLabel(60, "60");
        mRenderer.addYTextLabel(80, "80");
        mRenderer.addYTextLabel(100, "100");
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
        mRenderer.setBackgroundColor(Color.argb(255, 47, 54, 62));
        mRenderer.setMarginsColor(Color.argb(255,39,44,50));
        mRenderer.setPointSize(5f);
        //不显示legend(坐标轴的说明文字)
        mRenderer.setShowLegend(false);
    }

    /**
     * 每10分钟一个点，每天145个数据点
     * @param mRenderer
     */
    private static void setXTextLabel(XYMultipleSeriesRenderer mRenderer) {
        mRenderer.addXTextLabel(1, "00:00");
        mRenderer.addXTextLabel(25, "04:00");
        mRenderer.addXTextLabel(49, "08:00");
        mRenderer.addXTextLabel(73, "12:00");
        mRenderer.addXTextLabel(97, "16:00");
        mRenderer.addXTextLabel(121, "20:00");
        mRenderer.addXTextLabel(145, "24:00");
    }

    public static void addSeriesRenderer(XYMultipleSeriesRenderer mRenderer) {
        XYSeriesRenderer seriesRenderer = new XYSeriesRenderer();
        //设置线的颜色
        seriesRenderer.setColor(Color.GREEN);
        //设置点的类型
        seriesRenderer.setPointStyle(PointStyle.POINT);
        //点是否是实心的
        seriesRenderer.setFillPoints(false);
        seriesRenderer.setLineWidth(2);
        //设置空心点的线宽
        seriesRenderer.setPointStrokeWidth(3f);
        mRenderer.addSeriesRenderer(seriesRenderer);
    }

    public static XYMultipleSeriesDataset buildDataset(int pageIndex) {
        XYMultipleSeriesDataset mDataset = new XYMultipleSeriesDataset();
        //构造X轴数据点
        double x[] = new double[Constants.BATTERY_LEVEL_CHART_POINT_NUM];
        for (int i = 1; i <= Constants.BATTERY_LEVEL_CHART_POINT_NUM; i++) {
            x[i - 1] = i;
        }
        //查询Y轴数据点
        double y [] = queryLevelsFromDb(pageIndex);
        if (y == null) {
            y = randomY();
        }
        XYSeries series = new XYSeries("" + pageIndex);
        for (int i = 0; i < x.length; i++) {
            series.add(x[i], y[i]);
        }
        mDataset.addSeries(series);
        return mDataset;
    }

    private static double[] randomY() {
        double[] y =  new double[Constants.BATTERY_LEVEL_CHART_POINT_NUM];
        Random random = new Random(System.currentTimeMillis());
        for (int i = 0; i < y.length; i++) {
            y[i] = random.nextDouble() * 100;
        }
        smoothLevels(y);
        return y;
    }

    /*
    从数据库中查询电量数据
     */
    private static double[] queryLevelsFromDb(int pageIndex) {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.add(Calendar.DAY_OF_MONTH, pageIndex - BatteryInfoActivity.NUM_PAGES + 1);
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        long start = calendar.getTimeInMillis();
        calendar.add(Calendar.HOUR, 24);
        long end = calendar.getTimeInMillis();
        calendar.add(Calendar.HOUR, -24);
        DbManager dbManager = Static.getDbManager();
        List<BatteryLevelEntry> list = dbManager.battery_query(start, end);
        if (list.size() == 0) {
            return null;
        }
        double[] levels = new double[Constants.BATTERY_LEVEL_CHART_POINT_NUM];
        for (BatteryLevelEntry entry : list) {
            long timeInToday = entry.timestamp - calendar.getTimeInMillis();
            int index = (int)(timeInToday / Constants.INTERVAL )%Constants.BATTERY_LEVEL_CHART_POINT_NUM + 1;
            levels[index] = entry.level * 100;
        }
        //平滑曲线
        smoothLevels(levels);
        return levels;
    }

    /*
    对数据进行平滑，防止曲线抖动过于剧烈
     */
    private static void smoothLevels(double[] levels) {
        int index = levels.length-1;
        while (index >= 0 && levels[index] == 0) {
            index--;
        }
        while (index >= 1) {
            if (levels[index] == 0) {
                if (levels[index - 1] != 0) {
                    levels[index] = (levels[index - 1] + levels[index + 1]) / 2;
                } else {
                    levels[index] = levels[index + 1];
                }
            }
            index--;
        }
        if (levels[0] == 0) {
            levels[0] = levels[1];
        }
    }
}
