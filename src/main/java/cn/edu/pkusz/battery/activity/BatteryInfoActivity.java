package cn.edu.pkusz.battery.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.Calendar;
import java.util.GregorianCalendar;

import cn.edu.pkusz.battery.R;
import cn.edu.pkusz.battery.common.Static;
import cn.edu.pkusz.battery.fragment.SlidePageFragment;
import cn.edu.pkusz.battery.fragment.TabFragment_1;
import cn.edu.pkusz.battery.indicator.CirclePageIndicator;
import cn.edu.pkusz.battery.indicator.PageIndicator;

public class BatteryInfoActivity extends FragmentActivity {
	private TextView mBattery_health, mBattery_scale, mBattery_level, mBattery_temperature, mBattery_voltage, mBattery_craft;
    private static TextView mBattery_level_date;
    public static final int NUM_PAGES = 3;
    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;
    private PageIndicator mIndicator;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_battery_info);
        mBattery_level_date = (TextView)findViewById(R.id.battery_level_date);
        // Instantiate a ViewPager and a PagerAdapter.
        mPager = (ViewPager) findViewById(R.id.battery_level_pager);
        mPagerAdapter = new SlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        mIndicator = (CirclePageIndicator)findViewById(R.id.indicator);
        mIndicator.setViewPager(mPager);
        mIndicator.setCurrentItem(mPagerAdapter.getCount() - 1);
        mPager.setCurrentItem(2);

        //设置电池信息
        mBattery_health = (TextView)findViewById(R.id.battery_health);
    	mBattery_scale = (TextView)findViewById(R.id.battery_scale);
    	mBattery_level = (TextView)findViewById(R.id.battery_level);
    	mBattery_temperature = (TextView)findViewById(R.id.battery_temperature);
    	mBattery_voltage = (TextView)findViewById(R.id.battery_voltage);
    	mBattery_craft = (TextView)findViewById(R.id.battery_craft);
        Intent intent = getIntent();
        mBattery_health.setText(intent.getStringExtra(TabFragment_1.BATTERY_HEALTH));
        mBattery_scale.setText(intent.getIntExtra(TabFragment_1.BATTERY_SCALE, 100) + "%");
        mBattery_level.setText((int) (intent.getIntExtra(TabFragment_1.BATTERY_LEVEL, 0) / (float) intent.getIntExtra(TabFragment_1.BATTERY_SCALE, 100) * 100) + "%");
        mBattery_temperature.setText(TabFragment_1.getFloatValue(intent.getIntExtra(TabFragment_1.BATTERY_TEMPERATURE,0) * 0.1) + "℃");
        mBattery_voltage.setText(TabFragment_1.getFloatValue(intent.getIntExtra(TabFragment_1.BATTERY_VOLTAGE,0)/1000.0) +"V");
        mBattery_craft.setText(intent.getStringExtra(TabFragment_1.BATTERY_CRAFT));
    }


    private class SlidePagerAdapter extends FragmentStatePagerAdapter {
        public SlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return SlidePageFragment.create(position);
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }

    /*
    根据ViewPager的当前Item设置日期
     */
    public static void setDate(int position) {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.add(Calendar.DAY_OF_MONTH,position-(NUM_PAGES-1));
        mBattery_level_date.setText(" "+Static.getDateFormatShort().format(calendar.getTime()));
    }
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
