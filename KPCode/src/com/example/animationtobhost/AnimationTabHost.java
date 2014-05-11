/*******************************************************************************
 *
 *    Copyright (c) whty Tech Co. Ltd
 *
 *    TYWicity
 * 
 *    AnimationTabHost.java
 *    AnimationTabHost.java everything instead of com.whty.wicity.china.views.AnimationTabActivity.java
 *
 *    @author: xjin
 *    @since:  2012
 *    @version: 1.0
 *
 ******************************************************************************/

package com.example.animationtobhost;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ActivityGroup;
import android.app.LocalActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.animationtobhost.ViewPager.OnPageChangeListener;
import com.whty.wicity.core.DisplayUtils;

/*
 * ?自定义
 */
public class AnimationTabHost extends LinearLayout {
	//动画相关？,tabhost导航栏
	private AnimationTabWidget animationTabWidget;
	//页面滚动分页事件
	private onPageChangedListener  pageChangeListener;
	//滑动？
	private ViewPager viewPager;
	static final String TAB = "tab_";
	private Context context;
	
	private int offset = 0; // 动画偏移量
	private int currIndex = 0; // 当前索引
	private int bmpW = 0; // 动画图片宽度
	
	private ImageView glide; //  // 动画图片，tabhost标题下的小图,此处不需要,,隐藏
	/**
	 * @param context
	 */
	public AnimationTabHost(Context context) {
		this(context, null);
	}

	/**
	 * @param context
	 * @param attrs
	 */
	public AnimationTabHost(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		//?
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.WicityTab);
		Drawable drawable = a.getDrawable(R.styleable.WicityTab_widget_background);
		int color = a.getColor(R.styleable.WicityTab_widget_color, Color.BLACK);
		//从animation_tab_host读取控件
		inflate(context, R.layout.animation_tab_host, this);
		animationTabWidget = (AnimationTabWidget) findViewById(R.id.vWidget);
		viewPager = (ViewPager) findViewById(R.id.vPager);
		animationTabWidget.setBackgroundDrawable(drawable);
		animationTabWidget.setWidgetColor(color);
	}
	
	
	/**
	 * 初始化动画,tabhost标题下的小图,此处不需要,隐藏
	 */
	private void InitImageView(int count) {
		// // 动画图片，tabhost标题下的小图,此处不需要,隐藏
		glide = animationTabWidget.getSlideImageView();
		//获取屏幕分辨率及DisplayMetrics
		DisplayMetrics dm = new DisplayMetrics();
		((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenW = dm.widthPixels;// 获取分辨率宽度
		bmpW = screenW / count;
		LayoutParams params = new LayoutParams(bmpW, DisplayUtils.dipToPixel(6));
		glide.setLayoutParams(params);
		offset = (screenW / count - bmpW) / 2;// 计算偏移量
		Matrix matrix = new Matrix();
		matrix.postTranslate(offset, 0);
		glide.setImageMatrix(matrix);// 设置动画初始位置
	}
	private List<Activity> activities;
	int currentTab=0;

	/**
	 * 初始tabHost 的意图
	 */
	public void setActivities(List<Intent> intents,int currentTab){
		this.currentTab=currentTab;
		List<View> pageViews = new ArrayList<View>();
		activities= new ArrayList<Activity>();
		if (!(context instanceof ActivityGroup)) {
			throw new IllegalArgumentException("the parent acivity must extend ActivityGroup");
		}
		ActivityGroup activityGroup = (ActivityGroup) context;
		int count  = intents.size();
		//tabhost标题下的小图,此处不需要,隐藏
		InitImageView(count);
		for (int i = 0; i < count; i++) {
			Intent intent = intents.get(i);
			//如果设置，并且这个Activity已经在当前的Task中运行，因此，不再是重新启动一个这个Activity的实例，
		//而是在这个Activity上方的所有Activity都将关闭，然后这个Intent会作为一个新的Intent投递到老的Activity（现在位于顶端）中。
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			//总的来说，实现“如何在一个Activity的一部分中显示其他Activity”除了LocalActivityManager还需要一个
			//或多个"容器"Layout(或者和Layout同级别的View)。
			//http://blog.sina.com.cn/s/blog_588508f801010g1r.html
			LocalActivityManager manager = activityGroup.getLocalActivityManager();
			final Window w = manager.startActivity(TAB+i, intent);
			View currentView = w.getDecorView();
			pageViews.add(currentView);
			activities.add((Activity) w.getContext());
		}
		viewPager.setAdapter(new MyPagerAdapter(pageViews));
		animationTabWidget.setActivities(activities, viewPager);
		//2014 my add
		  //Drawable dr = this.getResources().getDrawable(R.drawable.caidan_qianbg);
		//animationTabWidget.setBackgroundDrawable(dr);
		viewPager.setCurrentItem(currentTab);
		/*The method setOnPageChangeListener(ViewPager.OnPageChangeListener) in the type ViewPager is not applicable for
		 *  the arguments (AnimationTabHost.MyOnPageChangeListener)									  
		 */
		viewPager.setOnPageChangeListener(new MyOnPageChangeListener(count));
		//viewPager.setDescendantFocusability(FOCUS_BLOCK_DESCENDANTS);
		//viewPager.onInterceptTouchEvent(false);
		
	}
	
	public List<Activity> getActivies(){
		return activities;
	}
	public ViewPager getViewPager(){
		return viewPager;
	}
	
	/**
	 * 页卡切换监听
	 */
	public class MyOnPageChangeListener implements OnPageChangeListener {
		
		private int count;
		private int[] pagetabs;
		protected int tab = offset * 2 + bmpW;
		
		public MyOnPageChangeListener(int count){
			
			this.count = count;
			pagetabs = new int[count];
			for (int i = 0; i < count; i++) {
				pagetabs[i] = tab*i;
			}
			if(currentTab!=0){
				onPageSelected(currentTab);
			}
		}

		public void onPageSelected(int arg0) {
			if(pageChangeListener != null){
				pageChangeListener.onPageChange(arg0);
			}
			Animation animation = null;
			if (arg0 != 0) {
				pagetabs[0] = offset;
			}
			if (arg0 != currIndex) {
				animation = new TranslateAnimation(pagetabs[currIndex], pagetabs[arg0], 0, 0);
			}
			currIndex = arg0;
			animation.setFillAfter(true);// True:图片停在动画结束位置
			animation.setDuration(300);
			glide.startAnimation(animation);
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub
			
		}
	}


	/**
	 * 
	 * 页面滚动监听
	 *
	 */
	public interface onPageChangedListener{
		public void onPageChange(int index);
	}
	/*
	 * 分页
	 */
	public void setOnPageChnageListener(onPageChangedListener listener){
		pageChangeListener = listener;
	}
	class MyPagerAdapter extends PagerAdapter {
		public List<View> mListViews;

		public MyPagerAdapter(List<View> mListViews) {
			this.mListViews = mListViews;
		}

		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			((ViewPager) arg0).removeView(mListViews.get(arg1));
		}

		@Override
		public void finishUpdate(View arg0) {
		}

		@Override
		public int getCount() {
			return mListViews.size();
		}

		@Override
		public Object instantiateItem(View arg0, int arg1) {
            int count = getCount();
			if (arg1 < count) {
				((ViewPager) arg0).addView(mListViews.get(arg1 % count), 0);
			}
            View currentView = mListViews.get(arg1 % count);
            currentView.requestFocus();
            currentView.bringToFront();
			return currentView;
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == (arg1);
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {
		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View arg0) {
		}
	}

}
