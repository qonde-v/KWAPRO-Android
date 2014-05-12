package com.example.animationtobhost;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.annotation.SuppressLint;
import android.app.ActivityGroup;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;

import com.example.animationtobhost.AnimationTabHost.onPageChangedListener;
import com.example.animationtobhost.api.HttpConstants;
import com.example.animationtobhost.util.HttpUtil;
import com.example.animationtobhost.util.SharePreferenceUtil;

public class ContactsActivity extends ActivityGroup implements
		onPageChangedListener, OnClickListener {
	
	private PopupWindow mPopupwinow = null;//弹出菜单
	private View llyPopView;//弹出菜单显示的View
	private ImageView ivMenu;//菜单按钮
	private ImageView ivSearch;
	private RelativeLayout rlBottom;//底部按钮
//	private RelativeLayout rlbtnRegister;//注册按钮layout
//	private RelativeLayout rlbtnLogin;//登录按钮layout
	private static final String TAG = "WeiboActivity";
	private static final int NETWORK_ERROR = 1;
	private Context mContext;
	private String result="";
	private AnimationTabHost animationTabHost;
	private Button btnRegister_btm, btnLogin_btm;
	// private RelativeLayout rlbtnLogin, rlbtnRegister;
	private ImageView imgRegister_btm, imgLogin_btm;

	// private Button btn_back;
	private PopupWindow mPop;// menuWindow;
	// private boolean menu_display = false;
	private LayoutInflater inflater; // 这个是将xml中的布局显示在屏幕上的关键类
	private View layout;
	private TextView btnLogin, btnCancel;
	private ImageView imgLogin, imgCancel;
	private EditText txtLoginName, txtPwd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.contacts_main);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title);
		mContext = this;

		getView();
		initView();
		
		initButton();
		initPop();
		
		checkUser();
	}

	/**
	 * 初始化tabhost
	 */
	private void initView() {
		List<Intent> intents = new ArrayList<Intent>();

		// MyContactsList是电话薄类，自定义的
		// 传给MyContactsList显示
		Intent intent0 = new Intent(this, MyContactsList.class);
		// 向MyContactsList传参数
		intent0.putExtra("num", 0);
		intent0.putExtra("title_name", "RSS新闻");
		intents.add(intent0);

		Intent intent1 = new Intent(this, MyContactsList.class);
		intent1.putExtra("num", 1);
		intent1.putExtra("title_name", "最新问题");
		intents.add(intent1);

		Intent intent3 = new Intent(this, MyContactsList.class);
		intent3.putExtra("num", 3);
		intent3.putExtra("title_name", "热门标签");
		intents.add(intent3);

		// setActivities自己的方法
		animationTabHost.setActivities(intents, 0);
		animationTabHost.setVisibility(View.VISIBLE);
	}

	/**
	 * getview
	 */
	private void getView() {

		animationTabHost = (AnimationTabHost) findViewById(R.id.tabHost);
		animationTabHost.setOnPageChnageListener(this);

		
	}
	//初始化登陆注册按钮事件
	private void initButton(){
//		rlbtnRegister=(RelativeLayout) this.findViewById(R.id.rlbtnRegister);
//		rlbtnLogin=(RelativeLayout) this.findViewById(R.id.rlbtnLogin);
		rlBottom=(RelativeLayout) this.findViewById(R.id.rlBottom);
		Button btnRegister_btm = (Button) this
				.findViewById(R.id.btnRegister_btm);
		Button btnLogin_btm = (Button) this.findViewById(R.id.btnLogin_btm);
		ImageView imgRegister_btm = (ImageView) this
				.findViewById(R.id.imgRegister_btm);
		ImageView imgLogin_btm = (ImageView) this
				.findViewById(R.id.imgLogin_btm);
		btnRegister_btm.setOnClickListener(buttonClickListener);
		btnLogin_btm.setOnClickListener(buttonClickListener);
		imgRegister_btm.setOnClickListener(buttonClickListener);
		imgLogin_btm.setOnClickListener(buttonClickListener);
	}
	View.OnClickListener buttonClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btnLogin_btm:
			case R.id.imgLogin_btm:
				initPopWindow();//登录弹窗
				break;
			case R.id.btnRegister_btm:
			case R.id.imgRegister_btm:
				Intent i2 = new Intent(ContactsActivity.this, RegisterActivity.class);//跳转到注册页面
				startActivity(i2);
				break;
			}
		}
	};

	//登录弹窗
	private void initPopWindow() {
		if (mPop == null) {
			inflater =(LayoutInflater)this.getSystemService(LAYOUT_INFLATER_SERVICE);
			layout = inflater.inflate( R.layout.login, null);
			mPop = new PopupWindow(layout, LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT);
			mPop.setFocusable(true);
			mPop.setOutsideTouchable(true);
			btnCancel = (TextView) layout.findViewById(R.id.btnCalcel);
			imgCancel = (ImageView) layout.findViewById(R.id.imgCancel);
			btnLogin = (TextView) layout.findViewById(R.id.btnLogin);
			imgLogin = (ImageView) layout.findViewById(R.id.imgLogin);
			txtLoginName = (EditText) layout.findViewById(R.id.txtLoginName);
			txtPwd = (EditText) layout.findViewById(R.id.txtPwd);
			btnCancel.setOnClickListener(handerPop);
			imgCancel.setOnClickListener(handerPop);
			btnLogin.setOnClickListener(handerPop);
			imgLogin.setOnClickListener(handerPop);
			BitmapDrawable drawable=new BitmapDrawable();
			mPop.setBackgroundDrawable(drawable);// 需要设置一下此参数，点击外边可消失
			mPop.setAnimationStyle(android.R.style.Animation_Toast);
            //监听窗口消失
            mPop.setOnDismissListener(new OnDismissListener() {
				@Override
				public void onDismiss() {
					WindowManager.LayoutParams lp = getWindow().getAttributes();
		            lp.alpha = 1.0f; //0.0-1.0
		            getWindow().setAttributes(lp);
		            mPop=null;
					
				}
			});
            mPop.showAtLocation(ContactsActivity.this.findViewById(R.id.main),
    				Gravity.CENTER, 0, 0);
    		mPop.showAsDropDown(layout);//显示窗口
    		//背景透明度
			WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.alpha = 0.8f; //0.0-1.0
            getWindow().setAttributes(lp);
		}
		
	}

	// 弹窗中按钮事件
	View.OnClickListener handerPop = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btnCalcel:
			case R.id.imgCancel:
				if (mPop.isShowing()) {
					mPop.dismiss();
				}
				break;
			case R.id.btnLogin:
			case R.id.imgLogin:
				userLogin();
				break;
			}
		}
	};
	//登陆
	private void userLogin(){
		final String name=txtLoginName.getText().toString();
		final String pwd=txtPwd.getText().toString();
		if (name.equals("")||pwd.equals(""))
		{
			AlertDialog.Builder alert = new AlertDialog.Builder(
					ContactsActivity.this);// Login.this);
			alert.setTitle("友情提醒")
					.setMessage("用户名密码不能为空")
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(
										DialogInterface dialog,
										int which) {
								}
							}).show();
			return;
		}
		if (mPop.isShowing()&&mPop!=null) {
			mPop.dismiss();
		}
		new Thread(new Runnable() {
			@Override
			public void run() {
				
				Map<String, String> maps=new HashMap<String, String>();
				maps.put("user", name);
				maps.put("pwd", pwd);
				
				try {
					result=HttpUtil.requestByPost(HttpConstants.HttpLogin, maps, 8);
					Message msg=new Message();
					msg.what=1;
					ContactsActivity.this.MyHandler.sendMessage(msg);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
		}).start();
	}
	
	Handler MyHandler=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1://登录成功
				Log.i("result1", result);
				SharePreferenceUtil sharedPreferences=new SharePreferenceUtil(ContactsActivity.this, "share");
				sharedPreferences.saveSharedPreferencesString("user", "test");
				checkUser();
			    
				break;

			default:
				break;
			}
		};
	};

	@Override
	public void onClick(View v) {
		
	}

	@Override
	public void onPageChange(int index) {
	}
	
	//根据是否登录显示隐藏
	public void checkUser(){
		SharePreferenceUtil sharedPreferences=new SharePreferenceUtil(ContactsActivity.this, "share");
		String user=sharedPreferences.getSharedPreferenceString("user");
		if(user==null){
        	ivMenu.setVisibility(View.GONE);
        	ivSearch.setVisibility(View.GONE);
        	rlBottom.setVisibility(View.VISIBLE);
        }else{
        	ivMenu.setVisibility(View.VISIBLE);
        	ivSearch.setVisibility(View.VISIBLE);
        	rlBottom.setVisibility(View.GONE);
        }
	}
	
		//初始化弹出菜单
		public void initPop(){
		    llyPopView = LayoutInflater.from(ContactsActivity.this).inflate(
					R.layout.popup_menu, null);
	        ivMenu=(ImageView) findViewById(R.id.iv_menu);
	        ivSearch=(ImageView) findViewById(R.id.iv_search);
	        ivMenu.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					WindowManager wm=(WindowManager) ContactsActivity.this.getSystemService(Context.WINDOW_SERVICE);
					int width=wm.getDefaultDisplay().getWidth();
					if (mPopupwinow == null) {
						mPopupwinow = new PopupWindow(llyPopView,width/3,LayoutParams.WRAP_CONTENT, true);
						mPopupwinow.setBackgroundDrawable(new ColorDrawable(0x00000000));
					}
					Rect frame = new Rect();
					getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
					int statusBarHeight = frame.top;
					int contentTop = getWindow().findViewById(Window.ID_ANDROID_CONTENT).getTop();
					int titleBarHeight = contentTop - statusBarHeight;
					mPopupwinow.setAnimationStyle(android.R.style.Animation_Dialog);
					mPopupwinow.showAtLocation(llyPopView,
		    				Gravity.RIGHT|Gravity.TOP, 0, titleBarHeight+50);
					
					mPopupwinow.showAsDropDown(llyPopView);
				}
	       });
	       initPopListener();
		}
		
		//弹出菜单监听事件
		public void initPopListener(){
			TextView tv_menu_msg=(TextView) llyPopView.findViewById(R.id.tv_menu_msg);
			TextView tv_menu_firend=(TextView) llyPopView.findViewById(R.id.tv_menu_firend);
			TextView tv_menu_question=(TextView) llyPopView.findViewById(R.id.tv_menu_question);
			TextView tv_menu_info=(TextView) llyPopView.findViewById(R.id.tv_menu_info);
			TextView tv_menu_about=(TextView) llyPopView.findViewById(R.id.tv_menu_about);
			TextView tv_menu_exit=(TextView) llyPopView.findViewById(R.id.tv_menu_exit);
			tv_menu_msg.setOnClickListener(menuClick);
			tv_menu_firend.setOnClickListener(menuClick);
			tv_menu_question.setOnClickListener(menuClick);
			tv_menu_info.setOnClickListener(menuClick);
			tv_menu_about.setOnClickListener(menuClick);
			tv_menu_exit.setOnClickListener(menuClick);
		}
		OnClickListener menuClick=new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(mPopupwinow!=null){
					mPopupwinow.dismiss();
				}
				switch (v.getId()) {
				case R.id.tv_menu_msg:
					Toast.makeText(ContactsActivity.this, "msg", Toast.LENGTH_SHORT).show();
					
					break;
				case R.id.tv_menu_firend:
					
					break;
				case R.id.tv_menu_question:
								
					break;
				case R.id.tv_menu_info:
					
					break;
				case R.id.tv_menu_about:
					
					break;
				case R.id.tv_menu_exit:
					SharePreferenceUtil sharedPreferences=new SharePreferenceUtil(ContactsActivity.this, "share");
					sharedPreferences.removeSharedPreferencesString("user");
					checkUser();
					break;
				default:
					break;
				}
				
			}
			
		};
}
