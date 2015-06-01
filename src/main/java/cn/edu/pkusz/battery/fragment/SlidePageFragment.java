package cn.edu.pkusz.battery.fragment;


import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import cn.edu.pkusz.battery.R;

/**
 * Created by 陶世博 on 2015/5/30.
 */
public class SlidePageFragment extends Fragment {
    private int mPageNumber;
    public static final String ARG_PAGE = "page";
    private GraphicalView mChartView;
    /** The main dataset that includes all the series that go into a chart. */
    private XYMultipleSeriesDataset mDataset = new XYMultipleSeriesDataset();
    /** The main renderer that includes all the renderers customizing a chart. */
    private XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();
    private ViewGroup rootView;

    public static SlidePageFragment create(int pageNumber) {
        SlidePageFragment fragment = new SlidePageFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, pageNumber);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPageNumber = getArguments().getInt(ARG_PAGE);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout containing a title and body text.
        rootView = (ViewGroup) inflater
                .inflate(R.layout.fragment_screen_slide_page, container, false);
        initChart();

        return rootView;
    }

    private void initChart() {
        double x [] = {1,2,3,4,5,6,7};
        double y [] = {100,75,60,20,50,90,80};
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

        XYSeriesRenderer render = new XYSeriesRenderer();
        //设置线的颜色
        render.setColor(Color.GREEN);
        //设置点的类型
        render.setPointStyle(PointStyle.CIRCLE);
        //点是否是实心的
        render.setFillPoints(false);
        render.setLineWidth(2);
        //设置空心点的线宽
        render.setPointStrokeWidth(3f);
        mRenderer.addSeriesRenderer(render);
        XYSeries series = new XYSeries(""+getmPageNumber());
        for(int i=0;i<x.length;i++){
            series.add(x[i], y[i]);
        }
        mDataset.addSeries(series);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mChartView == null) {
            LinearLayout layout = (LinearLayout) rootView.findViewById(R.id.chart);
            mChartView = ChartFactory.getLineChartView(getActivity(), mDataset, mRenderer);
            layout.addView(mChartView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));
        }
        else {
            mChartView.repaint();
        }
    }

    public int getmPageNumber() {
        return mPageNumber;
    }
}
