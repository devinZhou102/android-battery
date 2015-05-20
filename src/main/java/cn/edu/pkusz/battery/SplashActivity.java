package cn.edu.pkusz.battery;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Intent;

/**
 * 
 * xin
 * 
 */
public class SplashActivity extends Activity {

	private final int START_FULLSCREEN_TIME = 5000;

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
				finish();
			}
		}, START_FULLSCREEN_TIME);
	}
}