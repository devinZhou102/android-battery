package cn.edu.pkusz.battery.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import cn.edu.pkusz.battery.R;
import cn.edu.pkusz.battery.activity.TabFragment_3_1;
import cn.edu.pkusz.battery.activity.TabFragment_3_2;
import cn.edu.pkusz.battery.activity.TabFragment_3_3;



/**
 * 
 * xin
 * 
 */
public class TabFragment_3 extends Fragment 
{
	Button XinButton1, XinButton2, XinButton3, XinButton10;
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.tab_fragment_3, container, false);
		XinButton1 = (Button)view.findViewById(R.id.XinButton1);
		XinButton1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
					Intent intent = new Intent(getActivity(), TabFragment_3_1.class);
				    startActivity(intent);
			}
		});
		
		XinButton2 = (Button)view.findViewById(R.id.XinButton2);
		XinButton2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
					Intent intent = new Intent(getActivity(), TabFragment_3_2.class);
				    startActivity(intent);
			}
		});
		
		XinButton3 = (Button)view.findViewById(R.id.XinButton3);
		XinButton3.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
					Intent intent = new Intent(getActivity(), TabFragment_3_3.class);
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
}
