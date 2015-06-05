package cn.edu.pkusz.battery.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.widget.TextView;
import cn.edu.pkusz.battery.R;


public class TabFragment_1_1 extends Activity
{
	private int BatteryS;		//电池总电量
	private int BatteryN;		//目前电量
	private int BatteryV;		//电池电压
	private double BatteryT;		//电池温度
	private String BatteryStatus;	//电池状态
	private String BatteryTemp;		//电池使用情况
	private String Batterycraft;		//电池工艺
	TextView TV1, TV2, TV3, TV4, TV5, TV6;
	
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_fragment_1_1);	
        
        TV1 = (TextView)findViewById(R.id.TV1);
    	TV2 = (TextView)findViewById(R.id.TV2);
    	TV3 = (TextView)findViewById(R.id.TV3);
    	TV4 = (TextView)findViewById(R.id.TV4);
    	TV5 = (TextView)findViewById(R.id.TV5);
    	TV6 = (TextView)findViewById(R.id.TV6);
        // 注册一个系统 BroadcastReceiver，作为访问电池计量之用这个不能直接在AndroidManifest.xml中注册
     	registerReceiver(mBatInfoReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
    }
    
    @Override
	protected void onDestroy() 
	{
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	protected void onResume()
	{
		// TODO Auto-generated method stub
		super.onResume();
	}
	
	
	private BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver() 
	{
		public void onReceive(Context context, Intent intent) 
		{
			String action = intent.getAction();

			if (Intent.ACTION_BATTERY_CHANGED.equals(action)) 
			{
				BatteryS = intent.getIntExtra("scale", 0);	//电池总电量
				BatteryN = intent.getIntExtra("level", 0);	  //目前电量
				BatteryV = intent.getIntExtra("voltage", 0);  //电池电压
				BatteryT = intent.getIntExtra("temperature", 0);  //电池温度
				
				switch (intent.getIntExtra("status", BatteryManager.BATTERY_STATUS_UNKNOWN)) 
				{
                case BatteryManager.BATTERY_STATUS_CHARGING:
                    BatteryStatus = "充电状态";
                    break;
                case BatteryManager.BATTERY_STATUS_DISCHARGING:
                    BatteryStatus = "放电状态";
                    break;
                case BatteryManager.BATTERY_STATUS_NOT_CHARGING:
                    BatteryStatus = "未充电";
                    break;
                case BatteryManager.BATTERY_STATUS_FULL:
                    BatteryStatus = "充满电";
                    break;
                case BatteryManager.BATTERY_STATUS_UNKNOWN:
                    BatteryStatus = "未知道状态";
                    break;
				}
				
				switch (intent.getIntExtra("health", BatteryManager.BATTERY_HEALTH_UNKNOWN)) 
				{
                case BatteryManager.BATTERY_HEALTH_UNKNOWN:
                    BatteryTemp = "未知错误";
                    break;
                case BatteryManager.BATTERY_HEALTH_GOOD:
                    BatteryTemp = "状态良好";
                    break;
                case BatteryManager.BATTERY_HEALTH_DEAD:
                    BatteryTemp = "电池没有电";
                    break;
                case BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE:
                    BatteryTemp = "电池电压过高";
                    break;
                case BatteryManager.BATTERY_HEALTH_OVERHEAT:
                    BatteryTemp =  "电池过热";
                    break;
                }
				
				//TV2.setText("充电状态" + BatteryStatus);
				TV1.setText(BatteryTemp);
				TV2.setText(BatteryS + "%");
				TV3.setText(BatteryN + "%");
				TV4.setText((BatteryT*0.1) + "℃");
				TV5.setText((BatteryV/1000.0) + "V");				
				TV6.setText("Li-ion");
			}
		}
	};
	
}

