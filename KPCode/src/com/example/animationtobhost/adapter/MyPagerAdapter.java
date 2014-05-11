package com.example.animationtobhost.adapter;

import java.util.List;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

public class MyPagerAdapter extends PagerAdapter {
	   //界面列表   
    private List<View> views;  
      
    public MyPagerAdapter (List<View> views){  
        this.views = views;  
    }  
    
    //销毁arg1位置的界面   
    @Override  
    public void destroyItem(View arg0, int arg1, Object arg2) {  
        ((ViewPager) arg0).removeView(views.get(arg1));       
    } 
    
    //获得当前界面数   
    @Override  
    public int getCount() {  
        if (views != null)  
        {  
            return views.size();  
        }  
          
        return 0;  
    }  
    
    //初始化arg1位置的界面   
    @Override  
    public Object instantiateItem(View arg0, int arg1) {  
          
        ((ViewPager) arg0).addView(views.get(arg1), 0);  
          
        return views.get(arg1);  
    }  
	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		  return (arg0 == arg1);  
	}
		
  }
