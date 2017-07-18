package com.torch.chainmanage.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.torch.chainmanage.R;

public class BaseFragmentActivity extends FragmentActivity {

	protected Context mContext;
	protected Activity mActivity;
	private TextView mTitleBackText;
	private TextView mTitleNameText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		mActivity = this;
		requestWindowFeature(Window.FEATURE_NO_TITLE); // 隐藏标题栏
	}

	@Override
	public void finish() {
		super.finish();
	}

	@Override
	public void startActivity(Intent intent) {
		super.startActivity(intent);
	}

	@SuppressLint("NewApi")
	@Override
	public void setContentView(int layoutResID) {
		super.setContentView(layoutResID);
		if (VERSION.SDK_INT >= VERSION_CODES.KITKAT) {
			getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		}

		if (VERSION.SDK_INT >= VERSION_CODES.ICE_CREAM_SANDWICH) {
			View v = findViewById(R.id.activity_main);
			if (v != null) {
				v.setFitsSystemWindows(true);
			}
		}
		if(VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) { 
            Window window = getWindow();  
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);  
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN  
                            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);  
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);  
            window.setStatusBarColor(Color.TRANSPARENT);
        }  
		mTitleBackText = (TextView) findViewById(R.id.title_bar_back);
		mTitleNameText = (TextView) findViewById(R.id.title_bar_name);
		if(mTitleBackText != null){
			mTitleBackText.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					finish();
				}
			});
		}
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}
	
	public void setTitleName(String str){
		if(mTitleNameText != null){
			mTitleNameText.setText(str);
		}
	}
}
