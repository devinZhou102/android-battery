package cn.edu.pkusz.battery.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import cn.edu.pkusz.battery.R;
import cn.edu.pkusz.battery.common.Constants;
import cn.edu.pkusz.battery.service.TimerService;

/**
 * 
 * xin
 * 
 */
public class SplashActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);

		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				startActivity(new Intent(SplashActivity.this,
						MainActivity.class));
				startService(new Intent(getApplicationContext(), TimerService.class));
				finish();
			}
		}, Constants.START_FULLSCREEN_TIME);
	}
}