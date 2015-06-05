package cn.edu.pkusz.battery.activity;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Switch;
import cn.edu.pkusz.battery.R;
//-------------------------------------------Wifi-----------------------------------------------------------
//-------------------------------------------Wifi-----------------------------------------------------------
//-------------------------------------------Bluetooth-----------------------------------------------------------
//-------------------------------------------Bluetooth-----------------------------------------------------------
//-------------------------------------------Sync-----------------------------------------------------------
//-------------------------------------------Sync-----------------------------------------------------------
//-------------------------------------------Silent-----------------------------------------------------------
//-------------------------------------------Silent-----------------------------------------------------------
//-------------------------------------------MobileData-----------------------------------------------------------
//-------------------------------------------MobileData-----------------------------------------------------------
//-------------------------------------------LockScreen-----------------------------------------------------------
//-------------------------------------------LockScreen-----------------------------------------------------------
//-------------------------------------------Brightness-----------------------------------------------------------

//-------------------------------------------Brightness-----------------------------------------------------------


public class TabFragment_3_2 extends Activity
{
	//-------------------------------------------Wifi-----------------------------------------------------------
    private WifiManager mWifiManager;
    private Switch mWifiButton;
    //Wifi设置改变系统发送的广播
    public static final String WIFI_STATE_CHANGED = "android.net.wifi.WIFI_STATE_CHANGED";
    private TestChange mTestChange;
	private IntentFilter mIntentFilter;
	//-------------------------------------------Wifi-----------------------------------------------------------
	
	//-------------------------------------------Bluetooth-----------------------------------------------------------
	private Switch mBluetooth;
	private BluetoothAdapter mBluetoothAdapter;
	//private TestChange mTestChange;
	//private IntentFilter mIntentFilter;
	public static final String BLUETOOTH_STATE_CHANGED = "android.bluetooth.adapter.action.STATE_CHANGED";
	private static final String BLUETOOTH_ACTION = "android.bluetooth.a2dp.profile.action.CONNECTION_STATE_CHANGED";
	//-------------------------------------------Bluetooth-----------------------------------------------------------
	
	//-------------------------------------------Sync-----------------------------------------------------------
    private Switch mSyncButton;
    //同步模式改变系统发送的广播
  	private static final String SYNC_CONN_STATUS_CHANGED = "com.android.sync.SYNC_CONN_STATUS_CHANGED";
    //-------------------------------------------Sync-----------------------------------------------------------
    
    //-------------------------------------------Silent-----------------------------------------------------------
  	private AudioManager mAudioManager;
	private Switch mSilentButton;
	//静音模式改变系统发送的广播
	public static final String RINGER_MODE_CHANGED = "android.media.RINGER_MODE_CHANGED";
	//-------------------------------------------Silent-----------------------------------------------------------

    //-------------------------------------------Vibrate-----------------------------------------------------------
	private Switch mVibrateButton;
	//-------------------------------------------Vibrate-----------------------------------------------------------
	
    //-------------------------------------------MobileData-----------------------------------------------------------
	private ConnectivityManager mConnectivityManager;
	private Switch mMobileDataButton;
	private static final String NETWORK_CHANGE = "android.intent.action.ANY_DATA_STATE";
	//-------------------------------------------MobileData-----------------------------------------------------------
	
	//-------------------------------------------LockScreen-----------------------------------------------------------
	private Button mLockScreenButton;
	private PowerManager mPowerManager;
	private final static int DIALOG=1;
	private static int FLAG=-1;
	String[] items = new String[] { 
			"15秒", "30秒",
			"1分钟", "2分钟",
			"10分钟", "30分钟" };
	//-------------------------------------------LockScreen-----------------------------------------------------------
	
	//-------------------------------------------Brightness-----------------------------------------------------------
	private BrightObserver mBrightObserver;
	//-------------------------------------------Brightness-----------------------------------------------------------
	
    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_fragment_3_2);
        
        //-------------------------------------------Wifi-----------------------------------------------------------
        mWifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        mTestChange = new TestChange();
		mIntentFilter = new IntentFilter();
		// 添加广播接收器过滤的广播
		mIntentFilter.addAction("android.net.wifi.WIFI_STATE_CHANGED");
		//-------------------------------------------Wifi-----------------------------------------------------------
		
        //-------------------------------------------Bluetooth-----------------------------------------------------------
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		// 添加广播接收器过滤的广播
		mIntentFilter.addAction("android.bluetooth.adapter.action.STATE_CHANGED");
		mIntentFilter.addAction("android.bluetooth.a2dp.profile.action.CONNECTION_STATE_CHANGED");
		//-------------------------------------------Bluetooth-----------------------------------------------------------	
		
		//-------------------------------------------Sync-----------------------------------------------------------
		//添加广播接收器过滤的广播
        mIntentFilter.addAction("com.android.sync.SYNC_CONN_STATUS_CHANGED");
        //-------------------------------------------Sync-----------------------------------------------------------
		
        //-------------------------------------------Silent-----------------------------------------------------------
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        //添加广播接收器过滤的广播
        mIntentFilter.addAction("android.media.RINGER_MODE_CHANGED");
        //-------------------------------------------Silent-----------------------------------------------------------
        
        //-------------------------------------------MobileData-----------------------------------------------------------
		mConnectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
		mIntentFilter.addAction("android.intent.action.ANY_DATA_STATE");
		//-------------------------------------------MobileData-----------------------------------------------------------
		
		//-------------------------------------------Brightness-----------------------------------------------------------
		mPowerManager = (PowerManager)getSystemService(Context.POWER_SERVICE);        
        mBrightObserver = new BrightObserver(new Handler());
		//-------------------------------------------Brightness-----------------------------------------------------------

        
       
		
		//-------------------------------------------XinButton2-----------------------------------------------------------
		Button SaveButton2 = (Button) findViewById(R.id.SaveButton2);
		SaveButton2.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View arg0) {
				
				//-------------------------------------------Brightness-----------------------------------------------------------
				ContentResolver cr = getContentResolver();
		        //SCREEN_BRIGHTNESS_MODE_AUTOMATIC 将自动调节屏幕亮度变成手动调节
		        try {
					if(Settings.System.getInt(cr, Settings.System.SCREEN_BRIGHTNESS_MODE)
							== Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC)
						stopAutoBrightness(getContentResolver());
				} catch (SettingNotFoundException e) {
					e.printStackTrace();
				}
		        setScreenLightValue(getContentResolver(), 38);
		        //-------------------------------------------Brightness-----------------------------------------------------------
		        
		        //-------------------------------------------LockScreen-----------------------------------------------------------
		        Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT, 15000);
		        //-------------------------------------------LockScreen-----------------------------------------------------------
		        
		        //-------------------------------------------MobileData-----------------------------------------------------------
		        setMobileDataStatus(true);
		        //-------------------------------------------MobileData-----------------------------------------------------------
		        
		        //-------------------------------------------Silent-----------------------------------------------------------
		        mAudioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
		        //-------------------------------------------Silent-----------------------------------------------------------
		        
		        //-------------------------------------------Sync-----------------------------------------------------------
		        setSyncStatus(false);
		        //-------------------------------------------Sync-----------------------------------------------------------
		        
		        //-------------------------------------------Bluetooth-----------------------------------------------------------
		        mBluetoothAdapter.disable();
		        //-------------------------------------------Bluetooth-----------------------------------------------------------
		        
		        //-------------------------------------------Wifi-----------------------------------------------------------
		        mWifiManager.setWifiEnabled(false);
		        //-------------------------------------------Wifi-----------------------------------------------------------
			}	        
		});
		
		//-------------------------------------------XinButton2-----------------------------------------------------------
		
    }
    
    @Override
	protected void onDestroy() 
	{
		// TODO Auto-generated method stub
		super.onDestroy();
		//-------------------------------------------Wifi-----------------------------------------------------------
		// 解除广播接收器
		unregisterReceiver(mTestChange);
		//-------------------------------------------Wifi-----------------------------------------------------------
		//-------------------------------------------Brightness-----------------------------------------------------------
		mBrightObserver.stopObserver();
		//-------------------------------------------Brightness-----------------------------------------------------------
	}

	@Override
	protected void onResume()
	{
		// TODO Auto-generated method stub
		super.onResume();
		//-------------------------------------------Wifi-----------------------------------------------------------
		// 注册广播接收器
		registerReceiver(mTestChange, mIntentFilter);
		//-------------------------------------------Wifi-----------------------------------------------------------
		//-------------------------------------------Brightness-----------------------------------------------------------
		mBrightObserver.startObserver();
		//-------------------------------------------Brightness-----------------------------------------------------------
	}
	

    
    //-------------------------------------------Sync-----------------------------------------------------------
    private void setSyncStatus(boolean enbled)
    {
    	/*getMasterSyncAutomatically和setMasterSyncAutomatically为抽象类ContentResolver的静态函数，
    	 * 所以可以直接通过类来调用
    	 */
    	ContentResolver.setMasterSyncAutomatically(enbled);
    }
    //-------------------------------------------Sync-----------------------------------------------------------

    
    //-------------------------------------------MobileData-----------------------------------------------------------
	private void setMobileDataStatus(boolean enabled) 
	{
		try 
		{
			Class<?> conMgrClass = Class.forName(mConnectivityManager.getClass().getName());
			Field iConMgrField = conMgrClass.getDeclaredField("mService");
			iConMgrField.setAccessible(true);
			Object iConMgr = iConMgrField.get(mConnectivityManager);
			Class<?> iConMgrClass = Class.forName(iConMgr.getClass().getName());

			Method setMobileDataEnabledMethod = iConMgrClass.getDeclaredMethod(
					"setMobileDataEnabled", Boolean.TYPE);
			setMobileDataEnabledMethod.setAccessible(true);

			setMobileDataEnabledMethod.invoke(iConMgr, enabled);
		} catch (ClassNotFoundException e) 
		{
			e.printStackTrace();
		} catch (NoSuchFieldException e) 
		{
			e.printStackTrace();
		} catch (SecurityException e) 
		{
			e.printStackTrace();
		} catch (NoSuchMethodException e) 
		{
			e.printStackTrace();
		} catch (IllegalArgumentException e) 
		{
			e.printStackTrace();
		} catch (IllegalAccessException e) 
		{
			e.printStackTrace();
		} catch (InvocationTargetException e) 
		{
			e.printStackTrace();
		}
	}
    //-------------------------------------------MobileData-----------------------------------------------------------
    

    
	
	//-------------------------------------------Brightness-----------------------------------------------------------
	/*因为PowerManager提供的函数setBacklightBrightness接口是隐藏的，
	 * 所以在基于第三方开发调用该函数时，只能通过反射实现在运行时调用
	 */
	private void setLight(int light)
	{
		try
        {
			//得到PowerManager类对应的Class对象
            Class<?> pmClass = Class.forName(mPowerManager.getClass().getName());
            //得到PowerManager类中的成员mService（mService为PowerManagerService类型）
            Field field = pmClass.getDeclaredField("mService");
            field.setAccessible(true);
            //实例化mService
            Object iPM = field.get(mPowerManager);
            //得到PowerManagerService对应的Class对象
            Class<?> iPMClass = Class.forName(iPM.getClass().getName());
            /*得到PowerManagerService的函数setBacklightBrightness对应的Method对象，
             * PowerManager的函数setBacklightBrightness实现在PowerManagerService中
             */
            Method method = iPMClass.getDeclaredMethod("setBacklightBrightness", int.class);
            method.setAccessible(true);
            //调用实现PowerManagerService的setBacklightBrightness
            method.invoke(iPM, light);
        }
        catch (ClassNotFoundException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (NoSuchFieldException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (IllegalArgumentException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (IllegalAccessException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (NoSuchMethodException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (InvocationTargetException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

	}
	
	//启动自动调节亮度
	public void startAutoBrightness(ContentResolver cr) 
	{
		Settings.System.putInt(cr, Settings.System.SCREEN_BRIGHTNESS_MODE,
				Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC);
	}
	
	//关闭自动调节亮度
	public void stopAutoBrightness(ContentResolver cr) 
	{
		Settings.System.putInt(cr, Settings.System.SCREEN_BRIGHTNESS_MODE,
				Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
	}
	
	//设置改变亮度值
	public void setScreenLightValue(ContentResolver resolver, int value)
	{
		android.provider.Settings.System.putInt(resolver, Settings.System.SCREEN_BRIGHTNESS,
				value);
	}
	
	private class BrightObserver extends ContentObserver
	{
		ContentResolver mResolver;
		
		public BrightObserver(Handler handler)
		{
			super(handler);
			mResolver = getContentResolver();
		}

		@Override
		public void onChange(boolean selfChange) 
		{
			// TODO Auto-generated method stub
			super.onChange(selfChange);
			//refreshButton();
		}
		
		//注册观察
		public void startObserver()
		{
			mResolver.registerContentObserver(Settings.System
					.getUriFor(Settings.System.SCREEN_BRIGHTNESS), false,
					this);
			mResolver.registerContentObserver(Settings.System
					.getUriFor(Settings.System.SCREEN_BRIGHTNESS_MODE), false,
					this);
		}
		
		//解除观察
		public void stopObserver()
		{
			mResolver.unregisterContentObserver(this);
		}
	}
	//-------------------------------------------Brightness-----------------------------------------------------------
    
	
	
    private class TestChange extends BroadcastReceiver
	{
		@Override
		public void onReceive(Context context, Intent intent) 
		{
			// TODO Auto-generated method stub
			String action = intent.getAction();
			/*  xin 修改
			if (WIFI_STATE_CHANGED.equals(action))
			{
				//refreshButton();
				Toast.makeText(TabFragment_3_1.this, "Wifi设置有改变",
						Toast.LENGTH_SHORT).show();
			}
			*/
		}

	}
    
    
}

