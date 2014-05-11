/*******************************************************************************
 *
 *    Copyright (c) whty Tech Co. Ltd
 *
 *    TYWicity
 * 
 *    AnimationTabWidget.java
 *    AnimationTabWidget.java everything instead of com.whty.wicity.china.views.viewpaper.AnimationTabWidget.java
 *
 *    @author: xjin
 *    @since:  2012
 *    @version: 1.0
 *
 ******************************************************************************/

package com.example.animationtobhost;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


/**
 * @author xjin,tabhost导航栏
 */
public class AnimationTabWidget extends LinearLayout implements OnClickListener {
	
	private ViewPager viewPager;
	private ImageView glide; // 动画图片，tabhost标题下的小图,此处不需要
	private LinearLayout linearLayout;
	private int color;
	private Context mContext;
	/**
	 * @param context
	 */
	public AnimationTabWidget(Context context) {
		this(context, null);
	}

	/**
	 * @param context
	 * @param attrs
	 */
	public AnimationTabWidget(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext=context;
		inflate(context, R.layout.animation_tab_widget, this);
		linearLayout = (LinearLayout) findViewById(R.id.widget_main);
		glide = (ImageView) findViewById(R.id.btn_glide);
	}

	public void setActivities(List<Activity> activities,ViewPager viewPager){
		this.viewPager = viewPager;
		initActivity(activities);
	}
	 // 动画图片，tabhost标题下的小图,此处不需要,,隐藏
	public ImageView getSlideImageView(){
		return glide;
	}

	/**
	 * @param activities,初始化标题栏
	 */
	private void initActivity(List<Activity> activities) {
		int count = activities.size();
		for (int i = 0; i < count; i++) {
			Activity activity = activities.get(i);
			TextView textView = new TextView(getContext());
			textView.setId(i);
			textView.setGravity(Gravity.CENTER);
			//textView.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
			 Drawable dr = this.getResources().getDrawable(R.drawable.caidan_lanbg); //取得图片资源
			textView.setBackgroundDrawable(dr);
			textView.setText(activity.getTitle());
			//textView.setTextColor(Color.BLACK);
			textView.setTextColor(Color.WHITE);
			textView.setTextSize(19);
			textView.setOnClickListener(this);
			//button.setPadding(0, 10, 0, 0);
			LayoutParams tVparams = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
			tVparams.weight = 1f;
			
			linearLayout.addView(textView, tVparams);
			/*if (i != count - 1) {
				ImageView imageView = new ImageView(getContext());
				LayoutParams iVparams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
				imageView.setImageResource(R.drawable.tab_line);
				linearLayout.addView(imageView, iVparams);
			}*/
		}
	}
	
	@Override
	public void onClick(View v){
		if (null != viewPager) {
			viewPager.setCurrentItem(v.getId());
		}
	}

	/**
	 * @param color
	 */
	public void setWidgetColor(int color) {
		this.color = color;
	}
}
