package cn.edu.pkusz.battery.activity;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.BarChart;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.edu.pkusz.battery.R;
import cn.edu.pkusz.battery.network.TrafficChart;

public class AppDetailActivity extends FragmentActivity {
	/**
	 * 启动这个Activity的对象应该同时传递一个Intent，Intent中包含了CPU_AMOUNT, DOWNLOAD_AMOUNT,
	 * UPLOAD_AMOUNT三个值
	 */

	public static final String CPU_AMOUNT = "cpu_amount";
	public static final String DOWNLOAD_AMOUNT = "download_amount";
	public static final String UPLOAD_AMOUNT = "upload_amount";

	private GraphicalView mChartView;
	/** The main dataset that includes all the series that go into a chart. */
	private XYMultipleSeriesDataset mDataset = null;
	/** The main renderer that includes all the renderers customizing a chart. */
	private XYMultipleSeriesRenderer mRenderer = null;

	private Button mUninstall;
	private Button mShutdownButton;
	private TextView mCpuAmount;
	private TextView mTrafficUpload;
	private TextView mTrafficDownload;
	private TextView nameTxt;
	private TextView ratecpuTxt;
	private ImageView iv;
	Intent intent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_app_detail);
		intent = getIntent();
		mUninstall= (Button)findViewById(R.id.uninstall);
		mShutdownButton= (Button)findViewById(R.id.shutdown);
		mCpuAmount = (TextView) findViewById(R.id.cpu_amount);
		mTrafficUpload = (TextView) findViewById(R.id.traffic_mount_upload);
		mTrafficDownload = (TextView) findViewById(R.id.traffic_mount_download);

		nameTxt = (TextView) findViewById(R.id.Name);
		ratecpuTxt = (TextView) findViewById(R.id.txtRate);
		iv = (ImageView) findViewById(R.id.Icon);

		String name = intent.getStringExtra("name");
		nameTxt.setText(name);

		double cpurate = intent.getDoubleExtra("cpuRate", 0.00);
		ratecpuTxt.setText(format(cpurate));
		Bitmap btmap = intent.getParcelableExtra("icon");
		iv.setImageBitmap(btmap);

		if(intent.getIntExtra("isSystemApp", 0)!=0)
		{
			mUninstall.setBackgroundResource(R.drawable.button_uninstall_hidden);
		}else {
			mUninstall.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					String packageName = intent.getStringExtra("packageName");
					Log.e("uninstall:", packageName);
		            Intent intent = new Intent();
		            intent.setAction(Intent.ACTION_DELETE);
		            intent.setData(Uri.parse("package:" + packageName));
		            startActivity(intent);
				}
			});
		}
		mShutdownButton.setText(R.string.check);
		mShutdownButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.e("shutdown","shutdown");
				String packageName = intent.getStringExtra("packageName");
				Log.e("check", packageName);
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                intent.setData(Uri.fromParts("package", packageName, null));
                startActivity(intent);
			}
		});
		// 处理CPU使用量
		setCpuAmount();
		// 处理网络流量
		setTrafficAmount();
		// 柱状图初始化
		mRenderer = TrafficChart.buildRenderer();
		mDataset = TrafficChart.buildDataset(intent.getDoubleExtra(CPU_AMOUNT, 108));
	}

	private void setCpuAmount() {
		// 对接时需要给amount传递对应app所占用的cpu使用量,单位是ms

		// int amount =
		// getIntent().getIntExtra(AppDetailActivity.CPU_AMOUNT,1000);
		double amount = intent.getDoubleExtra(CPU_AMOUNT, 108);
		Log.e("cpuuse", String.valueOf(amount));
		mCpuAmount.setText(amount + "");
	}

	private void setTrafficAmount() {
		// 对接时需要在此处传递网络流量
		// int upload_amount =
		// getIntent().getIntExtra(AppDetailActivity.UPLOAD_AMOUNT,100);
		// int download_amount =
		// getIntent().getIntExtra(AppDetailActivity.DOWNLOAD_AMOUNT,100);

		long upload_amount = intent.getLongExtra(UPLOAD_AMOUNT, 123) / 1024;
		long download_amount = intent.getLongExtra(DOWNLOAD_AMOUNT, 321) / 1024;
		mTrafficUpload.setText(upload_amount + "");
		mTrafficDownload.setText(download_amount + "");
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (mChartView == null) {
			LinearLayout layout = (LinearLayout) findViewById(R.id.traffic_chart);
			mChartView = ChartFactory.getBarChartView(this, mDataset,
					mRenderer, BarChart.Type.DEFAULT);
			layout.addView(mChartView, new ViewGroup.LayoutParams(
					ViewGroup.LayoutParams.MATCH_PARENT,
					ViewGroup.LayoutParams.MATCH_PARENT));
		} else {
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

		// noinspection SimplifiableIfStatement
		if (id == R.id.action_settings) {
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	private String format(double size) {
		return String.format("%1$.2f%%", size);
	}
}
