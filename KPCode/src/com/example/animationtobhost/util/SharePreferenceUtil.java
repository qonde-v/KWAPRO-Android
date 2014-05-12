package com.example.animationtobhost.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SharePreferenceUtil { 
    private final SharedPreferences sharedpreferences; 
 
    public SharePreferenceUtil(Context context, String fileName) { 
        sharedpreferences = context.getSharedPreferences(fileName, 0);
    } 
    public boolean saveSharedPreferencesString(String key, String value) { 
    	SharedPreferences.Editor editor = sharedpreferences.edit(); 
    	editor.putString(key, value); 
    	return editor.commit(); 
	} 
    public String getSharedPreferenceString(String key){
    	 String str = null; 
    	 try
    	 {
	           str = sharedpreferences.getString(key, null); 
    	 } catch (Exception e) { 
    		 e.printStackTrace(); 
    	 } 
    	 return str;
    }
    public boolean removeSharedPreferencesString(String key){
    	SharedPreferences.Editor editor = sharedpreferences.edit(); 
    	editor.remove(key);
    	return editor.commit();
    }
}
