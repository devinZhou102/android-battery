package cn.edu.pkusz.battery.fragment;

import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import cn.edu.pkusz.battery.R;
import cn.edu.pkusz.battery.activity.AppDetailActivity;
import cn.edu.pkusz.battery.info.ApplicationInfo;
import cn.edu.pkusz.battery.info.AppInfoFormat;

/**
 * 
 * xin
 * 
 */
public class TabFragment_4 extends Fragment {
	// private final int PROGRESS_DIALOG_ID = 1;

	private TextView batterySummary;
	private ListView listView;
	private customAdapter adapter;
	private ApplicationInfo info;
	// private ProgressDialog progressDialog;
	private List<AppInfoFormat> mList;
	private String mBatterySummary;
	public double tt = ApplicationInfo.time;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		getActivity().getActionBar().setTitle(R.string.title_ranking);
		
		View view = inflater.inflate(R.layout.tab_fragment_4, container, false);
		return view;
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
		if (this.isVisible()) {
			getActivity().getActionBar().setTitle(R.string.title_ranking);
		} 
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		// onCreateDialog(PROGRESS_DIALOG_ID);
		getStart();
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);

	}

	// ------------------------------------------------------------------
	private void getStart() {
		batterySummary = (TextView) getActivity().findViewById(
				R.id.batterySummary);
		listView = (ListView) getActivity().findViewById(R.id.listview);
		adapter = new customAdapter();
		listView.setAdapter(adapter);

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				System.out.println(id + "item clicked");

				AppInfoFormat bsipper = adapter.getItem(position);

				double cputime = bsipper.getValue();

				String Name = bsipper.getName();
				double Rate = bsipper.getPercentOfTotal();

				Drawable icon = bsipper.getIcon();
				Bitmap bt = drawableToBitmap(icon);

				long download_amount = bsipper.getTcpBytesReceived();
				long upload_amount = bsipper.getTcpBytesSent();

				Intent intent = new Intent(getActivity(),
						AppDetailActivity.class);
				intent.putExtra("packageName", bsipper.getPackageName());
				intent.putExtra("isSystemApp", bsipper.getIssystemapp());
				intent.putExtra("name", Name);// 应用名称
				intent.putExtra("cpuRate", Rate);// CPU使用率
				intent.putExtra("cpu_amount", cputime);// CPU占用率
				intent.putExtra("icon", bt);// 图标

				intent.putExtra("download_amount", download_amount);// 下载流量
				intent.putExtra("upload_amount", upload_amount);// 上传流量

				startActivity(intent);
			}
		});

		info = new ApplicationInfo(getActivity());
		info.setMinPercentOfTotal(0);
		getBatteryStats();

	}

	private void getBatteryStats() {
		// getActivity().showDialog(PROGRESS_DIALOG_ID);
		new Thread() {
			public void run() {
				mList = info.getBatteryStats();
				mHandler.sendEmptyMessage(1);
			}
		}.start();
	}

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {

			switch (msg.what) {
			case 1:
				if (getActivity().isFinishing())
					return;
				// progressDialog.dismiss();
				batterySummary.setText("信息获取方式"
						+ (info.testType == 1 ? "(根据记录文件)" : "(根据CPU使用时间)"));
				adapter.setData(mList);

				break;
			}
		}
	};

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	class Holder {
		ImageView appIcon;
		TextView appName;
		TextView txtProgress;
		ProgressBar progress;
		Button clBtn;
	}

	private String format(double size) {
		return String.format("%1$.2f%%", size);
	}

	class customAdapter extends BaseAdapter {
		private List<AppInfoFormat> list;
		private LayoutInflater inflater;

		public customAdapter() {
			inflater = LayoutInflater.from(getActivity());
		}

		public void setData(List<AppInfoFormat> list) {
			this.list = list;

			for (int i = list.size() - 1; i >= 0; i--) {
				final AppInfoFormat sipper = list.get(i);
				String name = sipper.getName();
				Drawable icon = sipper.getIcon();
				if (name != null) {
					sipper.setName(name);
					if (icon == null) {
						PackageManager pm = getActivity().getPackageManager();
						icon = pm.getDefaultActivityIcon();
					}
					sipper.setIcon(icon);
				} else {
					list.remove(i);
				}
			}
			notifyDataSetInvalidated();
		}

		@Override
		public int getCount() {
			return list == null ? 0 : list.size();
		}

		@Override
		public AppInfoFormat getItem(int position) {
			return list == null ? null : list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Holder holder = null;
			if (convertView == null) {
				holder = new Holder();
				convertView = inflater.inflate(R.layout.listview_item, null);
				holder.appIcon = (ImageView) convertView
						.findViewById(R.id.appIcon);
				holder.appName = (TextView) convertView
						.findViewById(R.id.appName);
				holder.txtProgress = (TextView) convertView
						.findViewById(R.id.txtProgress);
				convertView.setTag(holder);
			} else {
				holder = (Holder) convertView.getTag();
			}

			final AppInfoFormat sipper = getItem(position);

			holder.appName.setText(sipper.getName());
			holder.appIcon.setImageDrawable(sipper.getIcon());

			double percentOfTotal = sipper.getPercentOfTotal();
			holder.txtProgress.setText(format(percentOfTotal));

			holder.clBtn = (Button) convertView.findViewById(R.id.ImBtn);
			
			int issys = sipper.getIssystemapp();
			if (issys != 0)
			{
				holder.clBtn.setText(R.string.check);
                holder.clBtn.setBackgroundResource(R.drawable.button_deep_green);
                holder.clBtn.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        String packageName = sipper.getPackageName();
                        Log.e("check", packageName);
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        intent.setData(Uri.fromParts("package", packageName, null));
                        startActivity(intent);
                    }
                });
			}
			else
			{
				 holder.clBtn.setText(R.string.shutdown);
	                holder.clBtn.setBackgroundResource(R.drawable.button_light_green);
	                final Holder holdertmp = holder;
	                holder.clBtn.setOnClickListener(new OnClickListener() {
	                    @Override
	                    public void onClick(View v) {
	                        Log.e("shutdown","shutdown");
	                        String packageName = sipper.getPackageName();
	                        ActivityManager am = (ActivityManager) getActivity().getSystemService(Context.ACTIVITY_SERVICE);
	                        am.killBackgroundProcesses(packageName);
	                        holdertmp.clBtn.setBackgroundResource(R.drawable.button_shutdown_hidden);
	                    }
	                });
			}
			if (sipper.getName().equals("Android OS"))
			{
				 holder.clBtn.setText(R.string.check);
	                holder.clBtn.setBackgroundResource(R.drawable.button_deep_green);
	                holder.clBtn.setOnClickListener(new OnClickListener() {
	                    @Override
	                    public void onClick(View v) {
	                        // TODO Auto-generated method stub
	                    	String packageName = sipper.getPackageName();
	                    	Log.e("check", packageName);
	                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
	                        intent.setData(Uri.fromParts("package", packageName, null));
	                        startActivity(intent);
	                    }
	                });
			}	

			// ===================================================================================================
			return convertView;
		}
	}

	public static Bitmap drawableToBitmap(Drawable drawable) {
		// 取 drawable 的长宽
		int w = drawable.getIntrinsicWidth();
		int h = drawable.getIntrinsicHeight();
		// 取 drawable 的颜色格式
		Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
				: Bitmap.Config.RGB_565;
		// 建立对应 bitmap
		Bitmap bitmap = Bitmap.createBitmap(w, h, config);
		// 建立对应 bitmap 的画布
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, w, h);
		// 把 drawable 内容画到画布中
		drawable.draw(canvas);
		return bitmap;
	}
}// end of fragment
