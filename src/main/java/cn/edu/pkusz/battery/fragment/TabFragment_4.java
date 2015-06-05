package cn.edu.pkusz.battery.fragment;

import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
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
import cn.edu.pkusz.battery.info.BatteryInfo;
import cn.edu.pkusz.battery.info.BatterySipper;
import cn.edu.pkusz.battery.info.Utils;

/**
 * 
 * xin
 * 
 */
public class TabFragment_4 extends Fragment {
	private final int PROGRESS_DIALOG_ID = 1;

	private TextView batterySummary;
	private ListView listView;
	private customAdapter adapter;
	private BatteryInfo info;
	private ProgressDialog progressDialog;
	private List<BatterySipper> mList;
	private String mBatterySummary;
	private Button closeBtn;
	public double tt = BatteryInfo.time;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.tab_fragment_4, container, false);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		onCreateDialog(PROGRESS_DIALOG_ID);
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

		// 设置item点击事件 跳转给博士页面
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				System.out.println(id + "item clicked");
				BatterySipper bsipper = adapter.getItem(position);

				double cputime = bsipper.getPercentOfTotal() * tt * 10;

				String Name = bsipper.getName();
				double Rate = bsipper.getPercentOfTotal();

				Drawable icon = bsipper.getIcon();
				Bitmap bt = drawableToBitmap(icon);

				long download_amount = bsipper.getTcpBytesReceived();
				long upload_amount = bsipper.getTcpBytesSent();

				Intent intent = new Intent(getActivity(),
						AppDetailActivity.class);
				intent.putExtra("name", Name);
				intent.putExtra("cpuRate", Rate);
				intent.putExtra("cputime", cputime);
				intent.putExtra("icon", bt);

				intent.putExtra("download_amount", download_amount);
				intent.putExtra("upload_amount", upload_amount);

				startActivity(intent);
			}
		});

		info = new BatteryInfo(getActivity());
		info.setMinPercentOfTotal(0);
		getActivity().registerReceiver(mBatteryInfoReceiver,
				new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
		getBatteryStats();

	}

	private void getBatteryStats() {
		getActivity().showDialog(PROGRESS_DIALOG_ID);
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
				progressDialog.dismiss();
				batterySummary.setText(mBatterySummary + "\n信息获取方式"
						+ (info.testType == 1 ? "(根据记录文件)" : "(根据CPU使用时间)"));
				adapter.setData(mList);

				break;
			}
		}
	};

	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case PROGRESS_DIALOG_ID:
			progressDialog = new ProgressDialog(getActivity());
			progressDialog.setMessage("请稍候...");
			return progressDialog;
		}
		return null;
	};

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		getActivity().unregisterReceiver(mBatteryInfoReceiver);
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

	private BroadcastReceiver mBatteryInfoReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (Intent.ACTION_BATTERY_CHANGED.equals(action)) {
				String batteryLevel = Utils.getBatteryPercentage(intent);
				String batteryStatus = Utils.getBatteryStatus(getActivity()
						.getResources(), intent);
				mBatterySummary = context.getResources().getString(
						R.string.power_usage_level_and_status, batteryLevel,
						batteryStatus);
			}
		}
	};

	// =====================================================================

	class customAdapter extends BaseAdapter {
		private List<BatterySipper> list;
		private LayoutInflater inflater;

		public customAdapter() {
			inflater = LayoutInflater.from(getActivity());
		}

		public void setData(List<BatterySipper> list) {
			this.list = list;

			for (int i = list.size() - 1; i >= 0; i--) {
				final BatterySipper sipper = list.get(i);
				String name = sipper.getName();
				if (name == null) {
					Drawable icon = sipper.getIcon();
					switch (sipper.getDrainType()) {
					case CELL:
						name = getString(R.string.power_cell);
						icon = getResources().getDrawable(
								R.drawable.ic_settings_cell_standby);
						break;
					case IDLE:
						name = getString(R.string.power_idle);
						icon = getResources().getDrawable(
								R.drawable.ic_settings_phone_idle);
						break;
					case BLUETOOTH:
						name = getString(R.string.power_bluetooth);
						icon = getResources().getDrawable(
								R.drawable.ic_settings_bluetooth);
						break;
					case WIFI:
						name = getString(R.string.power_wifi);
						icon = getResources().getDrawable(
								R.drawable.ic_settings_wifi);
						break;
					case SCREEN:
						name = getString(R.string.power_screen);
						icon = getResources().getDrawable(
								R.drawable.ic_settings_display);
						break;
					case PHONE:
						name = getString(R.string.power_phone);
						icon = getResources().getDrawable(
								R.drawable.ic_settings_voice_calls);
						break;
					case KERNEL:
						name = getString(R.string.process_kernel_label);
						icon = getResources().getDrawable(
								R.drawable.ic_power_system);
						break;
					case MEDIASERVER:
						name = getString(R.string.process_mediaserver_label);
						icon = getResources().getDrawable(
								R.drawable.ic_power_system);
						break;
					default:
						break;
					}

					if (name != null) {
						sipper.setName(name);
						if (icon == null) {
							PackageManager pm = getActivity()
									.getPackageManager();// getPackageManager();

							icon = pm.getDefaultActivityIcon();
						}
						sipper.setIcon(icon);
					} else {
						list.remove(i);
					}
				}
			}
			notifyDataSetInvalidated();
		}

		@Override
		public int getCount() {
			return list == null ? 0 : list.size();
		}

		@Override
		public BatterySipper getItem(int position) {
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

			BatterySipper sipper = getItem(position);

			holder.appName.setText(sipper.getName());
			holder.appIcon.setImageDrawable(sipper.getIcon());

			double percentOfTotal = sipper.getPercentOfTotal();
			holder.txtProgress.setText(format(percentOfTotal));

			// holder.progress.setProgress((int) percentOfTotal);
			// ===================================================================================================
			// 关闭按钮点击事件 给博士
			holder.clBtn = (Button) convertView.findViewById(R.id.ImBtn);
			// holder.clBtn.setBackgroundColor(Color.GREEN);
			int issys = sipper.getIssystemapp();
			if (issys != 0)
				holder.clBtn.setText("查看");
			else
				holder.clBtn.setText("关闭");
			if (sipper.getName().equals("Android OS"))
				holder.clBtn.setText("查看");
			holder.clBtn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					System.out.println("btn click");
				}
			});
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
