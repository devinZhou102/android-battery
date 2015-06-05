package cn.edu.pkusz.battery.fragment;


import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import cn.edu.pkusz.battery.R;
import cn.edu.pkusz.battery.power.BatteryLevelChart;

/**
 * Created by 陶世博 on 2015/5/30.
 */
public class SlidePageFragment extends Fragment {
    private int mPageNumber;
    public static final String ARG_PAGE = "page";
    private GraphicalView mChartView;
    /** The main dataset that includes all the series that go into a chart. */
    private XYMultipleSeriesDataset mDataset = null;
    /** The main renderer that includes all the renderers customizing a chart. */
    private XYMultipleSeriesRenderer mRenderer = null;
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
        mRenderer = BatteryLevelChart.buildRenderer();
        mDataset = BatteryLevelChart.buildDataset(getmPageNumber());
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mChartView == null) {
            LinearLayout layout = (LinearLayout) rootView.findViewById(R.id.battery_level_chart);
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
