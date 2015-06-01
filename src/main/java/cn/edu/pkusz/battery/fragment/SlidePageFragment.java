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
        double x [] = {1,2,3,4,5,6};
        double y [] = {100,75,60,20,50,90};
        mRenderer.setOrientation(XYMultipleSeriesRenderer.Orientation.HORIZONTAL);
        mRenderer.setXAxisMax(7);
        mRenderer.setYAxisMin(0);
        mRenderer.setXAxisMin(0);
        mRenderer.setYAxisMax(105);
        mRenderer.setYLabelsPadding(2);
        mRenderer.setYLabelsAlign(Paint.Align.RIGHT);
        mRenderer.setXLabels(5);
        mRenderer.setYLabels(10);
        //不可沿X轴和Y轴拖动
        mRenderer.setPanEnabled(false, false);
        //不可缩放
        mRenderer.setZoomEnabled(false, false);
        //不可点击
        mRenderer.setClickEnabled(false);
        mRenderer.setPointSize(5);

        mRenderer.setApplyBackgroundColor(true);
//        mRenderer.setBackgroundColor(Color.argb(255, 100, 100, 100));
        mRenderer.setBackgroundColor(Color.DKGRAY);
        mRenderer.setShowGrid(true);

        XYSeriesRenderer render = new XYSeriesRenderer();
        //设置线的颜色
        render.setColor(Color.GREEN);
        //设置点的类型
        render.setPointStyle(PointStyle.CIRCLE);
        //点是否是实心的
        render.setFillPoints(true);
        render.setLineWidth(2);
        //不显示legend
        render.setShowLegendItem(false);
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
