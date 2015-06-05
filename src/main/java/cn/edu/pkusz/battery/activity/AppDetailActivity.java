package cn.edu.pkusz.battery.activity;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.BarChart;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.edu.pkusz.battery.R;
import cn.edu.pkusz.battery.network.TrafficChart;


public class AppDetailActivity extends FragmentActivity {
    /**
     启动这个Activity的对象应该同时传递一个Intent，Intent中包含了CPU_AMOUNT, DOWNLOAD_AMOUNT, UPLOAD_AMOUNT三个值
     */

    public static final String CPU_AMOUNT = "cpu_amount";
    public static final String DOWNLOAD_AMOUNT = "download_amount";
    public static final String UPLOAD_AMOUNT = "upload_amount";

    private GraphicalView mChartView;
    /** The main dataset that includes all the series that go into a chart. */
    private XYMultipleSeriesDataset mDataset = null;
    /** The main renderer that includes all the renderers customizing a chart. */
    private XYMultipleSeriesRenderer mRenderer = null;

    private TextView mCpuAmount;
    private TextView mTrafficUpload;
    private TextView mTrafficDownload;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       
        setContentView(R.layout.activity_app_detail);

        mCpuAmount = (TextView) findViewById(R.id.cpu_amount);
        mTrafficUpload = (TextView) findViewById(R.id.traffic_mount_upload);
        mTrafficDownload = (TextView) findViewById(R.id.traffic_mount_download);
        //处理CPU使用量
        setCpuAmount();
        //处理网络流量
        setTrafficAmount();
        //柱状图初始化
        mRenderer = TrafficChart.buildRenderer();
        mDataset = TrafficChart.buildDataset();
    }

    private void setCpuAmount() {
        //对接时需要给amount传递对应app所占用的cpu使用量,单位是ms
        int amount = getIntent().getIntExtra(AppDetailActivity.CPU_AMOUNT,1000);
        mCpuAmount.setText(amount+"");
    }

    private void setTrafficAmount() {
        //对接时需要在此处传递网络流量
        long upload_amount = getIntent().getIntExtra(AppDetailActivity.UPLOAD_AMOUNT,100);
        long download_amount = getIntent().getIntExtra(AppDetailActivity.DOWNLOAD_AMOUNT,100);
        mTrafficUpload.setText(upload_amount + "");
        mTrafficDownload.setText(download_amount+"");
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mChartView == null) {
            LinearLayout layout = (LinearLayout) findViewById(R.id.traffic_chart);
            mChartView = ChartFactory.getBarChartView(this, mDataset, mRenderer, BarChart.Type.DEFAULT);
            layout.addView(mChartView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));
        }
        else {
            mChartView.repaint();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_app_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
