package com.example.animationtobhost;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;

//import com.example.animationtobhost.AnimationTabHost.MyPagerAdapter;
import com.example.animationtobhost.adapter.MyPagerAdapter;

public class Index extends Activity implements OnClickListener,OnPageChangeListener 
{
	private static final int[] pics = { R.drawable.huangying_bg };// ,
																	// R.drawable.jieshao2};
	private List<View> views;
	private ViewPager viewpage;
	private MyPagerAdapter pagerAdapter;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.index);
		//getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title);
		

		SharedPreferences sharedPreferences = this.getSharedPreferences(
				"share", MODE_PRIVATE);
		boolean isFirstRun = sharedPreferences.getBoolean("isFirstRun", true);
		Editor editor = sharedPreferences.edit();
		//isFirstRun=true;
		if (isFirstRun) {
			Log.d("debug", "第一次运行");
			editor.putBoolean("isFirstRun", false);
			editor.commit();
			 ViewPager();
		} else {
			Log.d("debug", "不是第一次运行");
			Intent intent = new Intent(Index.this, ContactsActivity.class);
			startActivity(intent);
		}
	}
	
	public void ViewPager() {
		views = new ArrayList<View>();

		LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		for (int i = 0; i < pics.length; i++) {
			ImageView iv = new ImageView(this);
			iv.setLayoutParams(mParams);
			iv.setImageResource(pics[i]);
			views.add(iv);
			if(i == pics.length-1){
			iv.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(Index.this,
							ContactsActivity.class);
					startActivity(intent);
				}
			});
			}
		}
		
		viewpage = (ViewPager) findViewById(R.id.viewpager);
		pagerAdapter = new MyPagerAdapter(views);
		viewpage.setAdapter(pagerAdapter);
		// 绑定回调
		viewpage.setOnPageChangeListener(this);

	}
	/**
	 * 设置当前的引导页
	 */
	private void setCurView(int position) {
		if (position < 0 || position >= pics.length) {
			return;
		}

		viewpage.setCurrentItem(position);
	}
	
	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPageSelected(int arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int position = (Integer) v.getTag();
		setCurView(position);
	}
}

