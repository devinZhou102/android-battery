package cn.edu.pkusz.battery.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import cn.edu.pkusz.battery.AppDetailActivity;
import cn.edu.pkusz.battery.BatteryInfoActivity;
import cn.edu.pkusz.battery.R;

/**
 * 
 * xin
 * 
 */
public class TabFragment_4 extends Fragment {
	private View view;
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.tab_fragment_4, container, false);
		((Button)view.findViewById(R.id.button)).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(getActivity(), AppDetailActivity.class);
				startActivity(intent);
			}
		});
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);

	}

	/**
	 * 获取用户安装的应用程序
	 * 
	 * @return
	 */
	@SuppressWarnings("unused")
	private List<Map<String, Object>> getAppListData() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		// 得到PackageManager对象
		PackageManager packageManager = getActivity().getPackageManager();
		// 得到系统安装的所有程序包的PackageInfo对象
		List<PackageInfo> packageList = packageManager.getInstalledPackages(0);
		for (PackageInfo packageInfo : packageList) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			// 非系统应用程序
			if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
				// 图标
				map.put("icon",
						packageInfo.applicationInfo.loadIcon(packageManager));
				// 应用程序名称
				map.put("appName",
						packageInfo.applicationInfo.loadLabel(packageManager));
				// 应用程序包名
				map.put("packageName", packageInfo.applicationInfo.packageName);
				// 循环读取并存到HashMap中，再添加到List上
				list.add(map);
			} else {
				// 系统应用程序
			}
		}
		return list;
	}
}
