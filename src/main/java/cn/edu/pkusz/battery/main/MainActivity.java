package cn.edu.pkusz.battery.main;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import cn.edu.pkusz.battery.R;
import cn.edu.pkusz.battery.fragment.TabFragment_1;
import cn.edu.pkusz.battery.fragment.TabFragment_2;
import cn.edu.pkusz.battery.fragment.TabFragment_3;
import cn.edu.pkusz.battery.fragment.TabFragment_4;

/**
 * 
 * xin
 * 
 */
public class MainActivity extends FragmentActivity implements OnClickListener {

	private View tab_view_1, tab_view_2, tab_view_3, tab_view_4, tab_view_5;
	private ImageView tab_icon_1, tab_icon_2, tab_icon_3, tab_icon_4,
			tab_icon_5;
	private TextView tab_text_1, tab_text_2, tab_text_3, tab_text_4,
			tab_text_5;
	private int tab_status = -1;

	/**
	 * 用于对Fragment进行管理
	 */
	private FragmentManager fragmentManager;

	private TabFragment_1 tabFragment_1;
	private TabFragment_2 tabFragment_2;
	private TabFragment_3 tabFragment_3;
	private TabFragment_4 tabFragment_4;
	//private TabFragment_5 tabFragment_5;
	static boolean flag = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		findViewById();
		fragmentManager = getSupportFragmentManager();
		// 第一次启动时选中第0个tab
		setTabSelection(0);
	}

	private void setTabSelection(int index) {
		// TODO Auto-generated method stub
		// 开启一个Fragment事务
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		// 切换设置动画
		setAnimations(transaction, index);
		// 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
		hideFragments(transaction);
		// 清除
		clearBackground();
		switch (index) {
		case 0:
			tab_view_1.setBackgroundResource(R.drawable.tab_left_pressed);
			tab_icon_1.setImageResource(R.drawable.battery_tab_icon01_pressed);
			tab_text_1.setTextColor(Color.WHITE);
			if (tabFragment_1 == null) {
				// 如果Fragment为空，则创建一个并添加到界面上
				tabFragment_1 = new TabFragment_1();
				transaction.add(R.id.content, tabFragment_1);
			} else {
				// 如果Fragment不为空，则直接将它显示出来
				transaction.show(tabFragment_1);
			}
			break;
		case 1:
			tab_view_2.setBackgroundResource(R.drawable.tab_middle_pressed);
			tab_icon_2.setImageResource(R.drawable.battery_tab_icon02_pressed);
			tab_text_2.setTextColor(Color.WHITE);
			if (tabFragment_2 == null) {
				// 如果Fragment为空，则创建一个并添加到界面上
				tabFragment_2 = new TabFragment_2();
				transaction.add(R.id.content, tabFragment_2);
			} else {
				// 如果Fragment不为空，则直接将它显示出来
				transaction.show(tabFragment_2);
			}
			break;
		case 2:
			tab_view_3.setBackgroundResource(R.drawable.tab_middle_pressed);
			tab_icon_3.setImageResource(R.drawable.battery_tab_icon03_pressed);
			tab_text_3.setTextColor(Color.WHITE);
			if (tabFragment_3 == null) {
				// 如果Fragment为空，则创建一个并添加到界面上
				tabFragment_3 = new TabFragment_3();
				transaction.add(R.id.content, tabFragment_3);
			} else {
				// 如果Fragment不为空，则直接将它显示出来
				transaction.show(tabFragment_3);
			}
			break;
		case 3:
			tab_view_4.setBackgroundResource(R.drawable.tab_middle_pressed);
			tab_icon_4.setImageResource(R.drawable.battery_tab_icon04_pressed);
			tab_text_4.setTextColor(Color.WHITE);
			if (tabFragment_4 == null) {
				// 如果Fragment为空，则创建一个并添加到界面上
				tabFragment_4 = new TabFragment_4();
				transaction.add(R.id.content, tabFragment_4);
			} else {
				// 如果Fragment不为空，则直接将它显示出来
				transaction.show(tabFragment_4);
			}
//			Intent i = new Intent(this,Consumer.class);
//			startActivity(i);
//			finish();
			break;
		default:
			break;
		}
		transaction.commit();
	}

	/**
	 * 设置Fragment切换动画
	 * 
	 * @param transaction
	 */
	private void setAnimations(FragmentTransaction transaction, int index) {
		// TODO Auto-generated method stub
		if (tab_status < index || tab_status == -1) {
			transaction.setCustomAnimations(R.anim.push_left_in,
					R.anim.push_left_out);
		} else if (tab_status > index) {
			transaction.setCustomAnimations(R.anim.back_left_in,
					R.anim.back_right_out);
		}
		tab_status = index;
	}

	/**
	 * 隐藏Fragment
	 * 
	 * @param transaction
	 */
	private void hideFragments(FragmentTransaction transaction) {
		// TODO Auto-generated method stub
		if (tabFragment_1 != null) {
			transaction.hide(tabFragment_1);
		}
		if (tabFragment_2 != null) {
			transaction.hide(tabFragment_2);
		}
		if (tabFragment_3 != null) {
			transaction.hide(tabFragment_3);
		}
		if (tabFragment_4 != null) {
			transaction.hide(tabFragment_4);
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.linearLayout_tab_1:
			setTabSelection(0);
			break;
		case R.id.linearLayout_tab_2:
			setTabSelection(1);
			break;
		case R.id.linearLayout_tab_3:
			setTabSelection(2);
			break;
		case R.id.linearLayout_tab_4:
			setTabSelection(3);
			break;
		/*
		case R.id.linearLayout_tab_5:
			setTabSelection(4);
			break;
		*/
		default:
			break;
		}
	}

	private void findViewById() {
		tab_view_1 = findViewById(R.id.linearLayout_tab_1);
		tab_view_2 = findViewById(R.id.linearLayout_tab_2);
		tab_view_3 = findViewById(R.id.linearLayout_tab_3);
		tab_view_4 = findViewById(R.id.linearLayout_tab_4);
		//tab_view_5 = findViewById(R.id.linearLayout_tab_5);

		tab_icon_1 = (ImageView) findViewById(R.id.tab_icon_1);
		tab_icon_2 = (ImageView) findViewById(R.id.tab_icon_2);
		tab_icon_3 = (ImageView) findViewById(R.id.tab_icon_3);
		tab_icon_4 = (ImageView) findViewById(R.id.tab_icon_4);
		//tab_icon_5 = (ImageView) findViewById(R.id.tab_icon_5);

		tab_text_1 = (TextView) findViewById(R.id.tab_text_1);
		tab_text_2 = (TextView) findViewById(R.id.tab_text_2);
		tab_text_3 = (TextView) findViewById(R.id.tab_text_3);
		tab_text_4 = (TextView) findViewById(R.id.tab_text_4);
		//tab_text_5 = (TextView) findViewById(R.id.tab_text_5);

		tab_text_1.setTextColor(Color.GRAY);
		tab_text_2.setTextColor(Color.GRAY);
		tab_text_3.setTextColor(Color.GRAY);
		tab_text_4.setTextColor(Color.GRAY);
		//tab_text_5.setTextColor(Color.GRAY);

		tab_view_1.setOnClickListener(this);
		tab_view_2.setOnClickListener(this);
		tab_view_3.setOnClickListener(this);
		tab_view_4.setOnClickListener(this);
		//tab_view_5.setOnClickListener(this);
	}

	/**
	 * 清除点击效果
	 */
	private void clearBackground() {
		tab_view_1.setBackgroundResource(0);
		tab_view_2.setBackgroundResource(0);
		tab_view_3.setBackgroundResource(0);
		tab_view_4.setBackgroundResource(0);
		//tab_view_5.setBackgroundResource(0);

		tab_icon_1.setImageResource(R.drawable.battery_tab_icon01_normal);
		tab_icon_2.setImageResource(R.drawable.battery_tab_icon02_normal);
		tab_icon_3.setImageResource(R.drawable.battery_tab_icon03_normal);
		tab_icon_4.setImageResource(R.drawable.battery_tab_icon04_normal);
		//tab_icon_5.setImageResource(R.drawable.battery_tab_icon05_normal);

		tab_text_1.setTextColor(Color.GRAY);
		tab_text_2.setTextColor(Color.GRAY);
		tab_text_3.setTextColor(Color.GRAY);
		tab_text_4.setTextColor(Color.GRAY);
		//tab_text_5.setTextColor(Color.GRAY);

	}
}
