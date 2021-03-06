package cn.edu.pkusz.battery.activity;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
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
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
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


public class TabFragment_3_1 extends Activity
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
        setContentView(R.layout.tab_fragment_3_1);
        
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
		
		
		
		
		//-------------------------------------------Wifi-----------------------------------------------------------
        mWifiButton = (Switch)findViewById(R.id.XinSwitcher4);
        Log.i("lin", "lin4");
        refreshButton4();
        mWifiButton.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean isChecked4) {
				if(mWifiManager.isWifiEnabled())
				{
					mWifiManager.setWifiEnabled(false);
				}
				else
				{
					mWifiManager.setWifiEnabled(true);
				}				
			}
		});
        //-------------------------------------------Wifi-----------------------------------------------------------
        
        //-------------------------------------------Bluetooth-----------------------------------------------------------		
		mBluetooth = (Switch)findViewById(R.id.XinSwitcher5);
		Log.i("lin", "lin5");
		refreshButton5();
		mBluetooth.setOnCheckedChangeListener(new OnCheckedChangeListener() {			
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean isChecked5) {
				switch (getBluetoothStatus()) //获取蓝牙当前状态
				{
				case BluetoothAdapter.STATE_ON:  //当前开启状态，则改变其为关闭状态
					 mBluetoothAdapter.disable();
					break;
				case BluetoothAdapter.STATE_TURNING_ON: //当前正在开启状态，则改变其为关闭状态
					mBluetoothAdapter.disable();
					break;
				case BluetoothAdapter.STATE_OFF: //当前关闭状态，则改变其为开启状态
					mBluetoothAdapter.enable();
					break;
				case BluetoothAdapter.STATE_TURNING_OFF: //当前正在关闭状态，则改变其为开启状态
					mBluetoothAdapter.enable();
					break;
				}
			}
		});
		//-------------------------------------------Bluetooth-----------------------------------------------------------
		
		//-------------------------------------------Sync-----------------------------------------------------------
		mSyncButton = (Switch)findViewById(R.id.XinSwitcher6);
        refreshButton6();        
        mSyncButton.setOnCheckedChangeListener(new OnCheckedChangeListener() {			
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean isChecked6) {
		    	if(getSyncStatus(TabFragment_3_1.this))  //这里应该引用外部的类
				{
					setSyncStatus(false);
				}
				else
				{
					setSyncStatus(true);
				}
			}
		});
        //-------------------------------------------Sync-----------------------------------------------------------
		
        //-------------------------------------------Silent-----------------------------------------------------------       
        mSilentButton = (Switch)findViewById(R.id.XinSwitcher7);
        refreshButton7();
        mSilentButton.setOnCheckedChangeListener(new OnCheckedChangeListener() {			
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean isChecked7) {				
		  		switch (getSilentStatus())
		      	{
		  		case AudioManager.RINGER_MODE_SILENT:
		  			mAudioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
		  			break;
		  		case AudioManager.RINGER_MODE_NORMAL:
		  			mAudioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
		  			break;
		  		case AudioManager.RINGER_MODE_VIBRATE:
		  			mAudioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
		  			break;
		  		default:
		  			break;
		      	}	
		  		refreshButton8();
			}
		});
        //-------------------------------------------Silent-----------------------------------------------------------
        
        //-------------------------------------------Vibrate-----------------------------------------------------------       
        mVibrateButton = (Switch)findViewById(R.id.XinSwitcher8);
        refreshButton8();
        mVibrateButton.setOnCheckedChangeListener(new OnCheckedChangeListener() {			
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean isChecked8) {				
		  		switch (getSilentStatus())
		      	{
		      	case AudioManager.RINGER_MODE_SILENT:
		  			mAudioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
		  			break;
		      	case AudioManager.RINGER_MODE_VIBRATE:
		  			mAudioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
		  			break;
		      	case AudioManager.RINGER_MODE_NORMAL:
		  			mAudioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
		  			break;
		  		default:
		  			break;
		      	}		
		  		refreshButton7();
			}
		});
        //-------------------------------------------Vibrate-----------------------------------------------------------
        
        //-------------------------------------------MobileData-----------------------------------------------------------
		mMobileDataButton = (Switch) findViewById(R.id.XinSwitcher3);
		refreshButton3();
		mMobileDataButton.setOnCheckedChangeListener(new OnCheckedChangeListener() {			
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean isChecked3) {
				if (getMobileDataStatus())
				{
					setMobileDataStatus(false);
				}
				else
				{
					setMobileDataStatus(true);
				}
			}
		});
		//-------------------------------------------MobileData-----------------------------------------------------------
        
		//-------------------------------------------LockScreen-----------------------------------------------------------
		mLockScreenButton = (Button)findViewById(R.id.XinSwitcher2);
		refreshButton2();
		mLockScreenButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// 显示对话框
                showDialog(DIALOG);
			}
		});
		//-------------------------------------------LockScreen-----------------------------------------------------------
		
		//-------------------------------------------Brightness-----------------------------------------------------------
		SeekBar seekBar = (SeekBar) findViewById(R.id.XinSwitcher1);
        //进度条绑定最大亮度，255是最大亮度
        seekBar.setMax(255);
        //取得当前亮度
        ContentResolver cr = getContentResolver();
        int light = android.provider.Settings.System.getInt(cr,
				Settings.System.SCREEN_BRIGHTNESS, -1);
        
        //SCREEN_BRIGHTNESS_MODE_AUTOMATIC 将自动调节屏幕亮度变成手动调节
        try {
			if(Settings.System.getInt(cr, Settings.System.SCREEN_BRIGHTNESS_MODE)
					== Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC)
				stopAutoBrightness(getContentResolver());
		} catch (SettingNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        //进度条绑定当前亮度
        seekBar.setProgress(light);
        //根据进度条事件改变亮度
        seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				//取得当前进度
				int tmpInt = seekBar.getProgress();
				
				//当进度小于10时，设置成10，防止太黑看不见的效果。
				if (tmpInt < 10) {
					tmpInt = 10;
				}				
				//根据当前进度改变亮度
				setLight(tmpInt);
				setScreenLightValue(getContentResolver(), tmpInt);
			}
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
			}			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub
			}
		});
		//-------------------------------------------Brightness-----------------------------------------------------------
		
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
	
	//-------------------------------------------Wifi----------------------------------------------------------------
	//根据当前WiFi的状态，来更新按钮：Wifi开启时，按钮显示wifi关闭；Wifi关闭时，按钮显示Wifi开启。
    private void refreshButton4()
    {
    	if(mWifiManager.isWifiEnabled())
    	{
    		mWifiButton.setChecked(true);
    	}
    	else
    	{
    		mWifiButton.setChecked(false);
    	}
        //mWifiButton.setText(mWifiManager.isWifiEnabled() ? R.string.wifi_off : R.string.wifi_on);
    }
    //-------------------------------------------Wifi----------------------------------------------------------------
    
    //-------------------------------------------Bluetooth-----------------------------------------------------------
    //更新按钮状态
  	private void refreshButton5()
  	{
  		switch (getBluetoothStatus()) 
  		{
  		case BluetoothAdapter.STATE_ON:
  			mBluetooth.setChecked(true);
  			break;
  		case BluetoothAdapter.STATE_TURNING_ON:
  			mBluetooth.setChecked(true);
  			break;
  		case BluetoothAdapter.STATE_OFF:
  			mBluetooth.setChecked(false);
  			break;
  		case BluetoothAdapter.STATE_TURNING_OFF:
  			mBluetooth.setChecked(false);
  			break;
  		}
  	}
  	
  	//获取蓝牙当前状态
  	private int getBluetoothStatus()
  	{
  		return mBluetoothAdapter.getState();
  	}
    //-------------------------------------------Bluetooth-----------------------------------------------------------
    
    //-------------------------------------------Sync-----------------------------------------------------------
    //更新按钮状态
    private void refreshButton6()
    {
    	//Log.i("lin", this.toString());
    	if(getSyncStatus(this))
    	{
    		mSyncButton.setChecked(true);
    	}
    	else
    	{
    		mSyncButton.setChecked(false);
    	}
        //mSyncButton.setText(getSyncStatus(this) ? R.string.sync_off : R.string.sync_on);
    }
    
    private boolean getSyncStatus(Context context)
    {
    	ContentResolver resolver = context.getContentResolver();
    	Boolean on = resolver.getMasterSyncAutomatically();
    	ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return connManager.getBackgroundDataSetting() && ContentResolver.getMasterSyncAutomatically();
    }
    
    private void setSyncStatus(boolean enbled)
    {
    	/*getMasterSyncAutomatically和setMasterSyncAutomatically为抽象类ContentResolver的静态函数，
    	 * 所以可以直接通过类来调用
    	 */
    	ContentResolver.setMasterSyncAutomatically(enbled);
    }
    //-------------------------------------------Sync-----------------------------------------------------------
    
    //-------------------------------------------Silent-----------------------------------------------------------
    //更新按钮
  	private void refreshButton7()
  	{
  		switch (getSilentStatus())
      	{
  		case AudioManager.RINGER_MODE_SILENT:
  			mSilentButton.setChecked(true);
  			break;
  		case AudioManager.RINGER_MODE_NORMAL:
  			mSilentButton.setChecked(false);
  			break;  
  		case AudioManager.RINGER_MODE_VIBRATE:
  			mSilentButton.setChecked(true);
  			break;	  		
  		default:
  			break;
      	}
  	}

  	//获取手机当前的静音模式状态
  	private int getSilentStatus()
    {
  		return mAudioManager.getRingerMode();
    }
    //-------------------------------------------Silent-----------------------------------------------------------
    
    //-------------------------------------------Vibrate-----------------------------------------------------------
    //更新按钮
  	private void refreshButton8()
  	{
  		
  		switch (getSilentStatus())
      	{
  		case AudioManager.RINGER_MODE_NORMAL:
  			mVibrateButton.setChecked(true);
  			break;
  		case AudioManager.RINGER_MODE_VIBRATE:
  			mVibrateButton.setChecked(true);
  			break;
  		case AudioManager.RINGER_MODE_SILENT:
  			mVibrateButton.setChecked(false);
  			break;	
  		default:
  			break;
      	}
  	}
  	
    //-------------------------------------------Vibrate-----------------------------------------------------------
    
    //-------------------------------------------MobileData-----------------------------------------------------------
  	private void refreshButton3()
	{
    	if(getMobileDataStatus())
    	{
    		mMobileDataButton.setChecked(true);
    	}
    	else
    	{
    		mMobileDataButton.setChecked(false);
    	}
		//mMobileDataButton.setText(getMobileDataStatus() ? R.string.mobile_data_off : R.string.mobile_data_on);
	}

	private boolean getMobileDataStatus()
	{
		String methodName = "getMobileDataEnabled";
		Class cmClass = mConnectivityManager.getClass();
		Boolean isOpen = null;
		
		try 
		{
			Method method = cmClass.getMethod(methodName, new Class[0]);
			isOpen = (Boolean) method.invoke(mConnectivityManager, new Object[]{});
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return isOpen;
	}
		
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
    
	//-------------------------------------------LockScreen-----------------------------------------------------------
	/**
     * 创建单选按钮对话框
     */
    @Override
    protected Dialog onCreateDialog(int id) {
        Dialog dialog=null;
        switch (id) 
        {
	        case DIALOG:
	            Builder builder=new AlertDialog.Builder(this);
	            //设置对话框的标题
	            builder.setTitle("自动锁屏");
	            //0: 默认第一个单选按钮被选中
	            builder.setSingleChoiceItems(items, 0, new android.content.DialogInterface.OnClickListener(){
	                public void onClick(DialogInterface dialog, int which) {	
	                	if(which == 0)
	                		FLAG = 15;
	                	else if(which == 1)
	                		FLAG = 30;
	                	else if(which == 2)
	                		FLAG = 60;
	                	else if(which == 3)
	                		FLAG = 120;
	                	else if(which == 4)
	                		FLAG = 600;
	                	else if(which == 5)
	                		FLAG = 1800;
	                	else
	                		FLAG = -1;
	                	//String hoddy=getResources().getStringArray(R.array.hobby)[which];
	                	//editText.setText("您选择了： "+hoddy);
	                }
	            });            
	            //添加一个确定按钮
	            builder.setPositiveButton("确认", new DialogInterface.OnClickListener(){
	                public void onClick(DialogInterface dialog, int which) {
	                	Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT, FLAG*1000);
	                    refreshButton2();
	                }
	            });
	            //添加一个取消按钮
	            builder.setNegativeButton("取消", new DialogInterface.OnClickListener(){
	                public void onClick(DialogInterface dialog, int which) {
	                    
	                }
	            });
	            //创建一个单选按钮对话框
	            dialog=builder.create();
	            break;
        }
        return dialog;
    }
	
	private void refreshButton2()
  	{
		if(getScreenOffTime() < 60)
			mLockScreenButton.setText(String.valueOf(getScreenOffTime()) + "秒");
		else
			mLockScreenButton.setText(String.valueOf(getScreenOffTime()/60) + "分钟");
  	}
	
	private int getScreenOffTime() 
	{
		int screenOffTime = 0;
		try 
		{
		screenOffTime = Settings.System.getInt(getContentResolver(),
		Settings.System.SCREEN_OFF_TIMEOUT);
		} 
		catch (Exception localException) {
		}
		return screenOffTime/1000;
	}
	//-------------------------------------------LockScreen-----------------------------------------------------------
    
	
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

