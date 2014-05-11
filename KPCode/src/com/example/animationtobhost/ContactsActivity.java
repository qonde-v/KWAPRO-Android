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
import android.graphics.drawable.BitmapDrawable;
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
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;

import com.example.animationtobhost.AnimationTabHost.onPageChangedListener;
import com.example.animationtobhost.util.APIUtil;
import com.example.animationtobhost.util.MyHttpclient;
//import android.view.KeyEvent;
//import android.widget.LinearLayout;
//import android.widget.Toast;
//import android.graphics.Color;

@SuppressLint("HandlerLeak")
public class ContactsActivity extends ActivityGroup implements
		onPageChangedListener, OnClickListener {

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
		// btn_back = (Button) findViewById(R.id.btn_back);
		// btn_back.setOnClickListener(this);

		animationTabHost = (AnimationTabHost) findViewById(R.id.tabHost);
		animationTabHost.setOnPageChnageListener(this);

		// layout = View.inflate(this, R.layout.login, null);

		Button btnRegister_btm = (Button) this
				.findViewById(R.id.btnRegister_btm);
		Button btnLogin_btm = (Button) this.findViewById(R.id.btnLogin_btm);
		ImageView imgRegister_btm = (ImageView) this
				.findViewById(R.id.imgRegister_btm);
		ImageView imgLogin_btm = (ImageView) this
				.findViewById(R.id.imgLogin_btm);
		// RelativeLayout rlbtnRegister = (RelativeLayout)
		// findViewById(R.id.rlbtnRegister);
		// RelativeLayout rlbtnLogin = (RelativeLayout)
		// findViewById(R.id.rlbtnLogin);
		btnRegister_btm.setOnClickListener(hander);
		btnLogin_btm.setOnClickListener(hander);
		imgRegister_btm.setOnClickListener(hander);
		imgLogin_btm.setOnClickListener(hander);
		// rlbtnLogin.setOnClickListener(hander);
		// rlbtnRegister.setOnClickListener(hander);
	}

	View.OnClickListener hander = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub

			switch (v.getId()) {
			case R.id.btnLogin_btm:
			case R.id.imgLogin_btm:
				// case R.id.rlbtnLogin:
				// Intent i = new Intent(ContactsActivity.this, Login.class);
				// startActivity(i);
				// Toast.makeText(ContactsActivity.this, "m2",
				// Toast.LENGTH_LONG).show();
				popWindow();
				break;
			case R.id.btnRegister_btm:
			case R.id.imgRegister_btm:
				// case R.id.rlbtnRegister:
				Intent i2 = new Intent(ContactsActivity.this, Register.class);
				// Intent i2 = new Intent(ContactsActivity.this, Index.class);
				startActivity(i2);
				break;
			}
		}
	};

	/**
	 * 弹窗
	 */
	private void popWindow() {

		initPopWindow();
		// mPop.showAsDropDown(this.layout);// 以这个Button为anchor（可以理解为锚，基准），在下方弹出
		// mPop.showAtLocation(this.findViewById(R.id.main),
		// Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
		// //设置layout在PopupWindow中显示的位置
//		mPop.showAtLocation(ContactsActivity.this.findViewById(R.id.main),
//				Gravity.CENTER, 0, 0);// 在屏幕居中，无偏移

		// Toast.makeText(ContactsActivity.this, "1",Toast.LENGTH_LONG).show();
		// // if(menu_display){ //如果 Menu已经打开 ，先关闭Menu
		// // menuWindow.dismiss();
		// // menu_display = false;
		// // }
		//
		// //获取LayoutInflater实例
		// //inflater =
		// (LayoutInflater)this.getSystemService(LAYOUT_INFLATER_SERVICE);
		// //这里的main布局是在inflate中加入的哦，以前都是直接this.setContentView()的吧？呵呵
		// //该方法返回的是一个View的对象，是布局中的根
		// layout = inflater.inflate(R.layout.contacts_main, null);
		// try
		// {
		// // TODO Auto-generated method stub
		// //下面我们要考虑了，我怎样将我的layout加入到PopupWindow中呢？？？很简单
		// menuWindow = new PopupWindow(layout,LayoutParams.FILL_PARENT,
		// LayoutParams.WRAP_CONTENT); //后两个参数是width和height
		// //menuWindow.showAsDropDown(layout); //设置弹出效果
		// //menuWindow.showAsDropDown(null, 0, layout.getHeight());
		// //this.findViewById(R.id.btn_bg);
		// menuWindow.showAtLocation(this.findViewById(R.id.main),
		// Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
		// //设置layout在PopupWindow中显示的位置
		//
		// Intent intent = new Intent();
		// intent.setClass(ContactsActivity.this,Login.class);
		// startActivity(intent);
		// menuWindow.dismiss(); //响应点击事件之后关闭Menu
		// }
		// catch (Exception e) {
		// Toast.makeText(ContactsActivity.this,
		// e.toString(),Toast.LENGTH_LONG).show();
		// System.out.println(e.toString());
		// // throw e;
		// } finally {
		// System.out.println("finally " );
		// }

	}

	private void initPopWindow() {
		if (mPop == null) {
			 inflater =(LayoutInflater)this.getSystemService(LAYOUT_INFLATER_SERVICE);
			// layout = View.inflate(this, R.layout.test2, null);
			//layout = View.inflate(this, R.layout.login, null);
			 layout = inflater.inflate( R.layout.login, null);
			// layout =
			// getLayoutInflater().inflate(R.layout.hotel_sort_popview,null);
			// mPop= new PopupWindow(layout,LayoutParams.FILL_PARENT,
			// LayoutParams.WRAP_CONTENT); //后两个参数是width和height
			
			//mPop = new PopupWindow(layout, 300,500);
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

	// 弹窗zhong按钮事件
	View.OnClickListener handerPop = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub

			switch (v.getId()) {
			// btnCancel.setOnClickListener(hander);
			// imgCancel.setOnClickListener(hander);
			// btnLogin.setOnClickListener(hander);
			// imgLogin.setOnClickListener(hander);
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
					result=MyHttpclient.requestByPost(APIUtil.loginUrl, maps, 8);
					Message msg=new Message();
					msg.what=1;
					ContactsActivity.this.MyHandler.sendMessage(msg);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
		}).start();
		
		
		// else {
		// // // 调用服务登录。。
		// // // Toast.makeText(Login.this, "1",Toast.LENGTH_LONG
		// // // ).show();
		// // // 回传参
		// // Toast.makeText(Login.this, "1", Toast.LENGTH_LONG).show();
		// // Intent data = new Intent();
		// //
		// // // Intent intent = new Intent();
		// // // intent.setClass(Login.this, MainActivity.class);
		// // // startActivity(intent);
		// // setResult(RESULT_OK, data);
		// // // setResult(RESULT_OK);
		// //
		// // finish();// 此处一定要调用finish()方法
		//
		// }
	}
	
	Handler MyHandler=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				Log.i("result1", result);
				break;

			default:
				break;
			}
		};
	};

	@Override
	public void onClick(View v) {
		// switch (v.getId()) {
		// case R.id.btn_back:
		// // 返回
		// finish();
		// break;
		//
		// default:
		// break;
		// }
	}

	// @Override
	// public boolean onKeyDown(int keyCode, KeyEvent event) {
	// if (keyCode == KeyEvent.KEYCODE_BACK) {
	// finish();
	// }
	// return super.onKeyDown(keyCode, event);
	// }

	/**
	 * ui主线程
	 */
	// Handler mHandler = new Handler() {
	// public void handleMessage(Message msg) {
	// int state = msg.what;
	//
	// };
	// };

	@Override
	public void onPageChange(int index) {
	}
}
