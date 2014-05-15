package com.example.animationtobhost;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.animationtobhost.api.HttpConstants;
import com.example.animationtobhost.util.HttpUtil;
import com.example.animationtobhost.util.SharePreferenceUtil;
import com.example.animationtobhost.util.ToastUtil;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
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
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author Administrator
 *
 */
public class RegisterActivity extends Activity {
	private View login_register_btn;//底部导入按钮
	private String result="";//json返回值
	
	//ctrl+shift+o
	private Button btnRegisterFinish;
	private EditText txtLoginName,txtPwd,txtEmail;
	private ImageView imgbtnMale,imgbtnFemale,imgMale,imgFemale;
	
	private PopupWindow mPop;// menuWindow;
	private PopupWindow mPopPic;// menuWindow;
	private LayoutInflater inflater; // 这个是将xml中的布局显示在屏幕上的关键类
	private View layout;
	private TextView btnLogin, btnCancel;
	private ImageView imgLogin, imgCancel;
	private EditText txtLoginName_pop, txtPwd_pop;
	
	private ImageView btnCammer,btnPhotoChoose;
	
	 private static int RESULT_LOAD_IMAGE = 1;
	 private static final int SCALE = 5;//缩放图片
	 
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		//无标题,放在setContentView(R.layout.register);前
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		
		setContentView(R.layout.register);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.register_title);

		findViews();
		
		ViewClickListener();
		
		initButton();
	}
	
	
	// 加载事件
	public void ViewClickListener() {
		btnRegisterFinish.setOnClickListener(hander);
		imgbtnMale.setOnClickListener(hander);//男按钮
		imgbtnFemale.setOnClickListener(hander);//女按钮
		imgFemale.setOnClickListener(hander);//男图
		imgMale.setOnClickListener(hander);//女图 
	}
	View.OnClickListener hander = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub

			switch (v.getId()) {
			case R.id.imgbtnMale://男按钮
				 //Toast.makeText(Register.this, "ok",
				 //Toast.LENGTH_LONG).show();
				changbtnBgcolor(1);
				break;
			case R.id.imgbtnFemale://女按钮
				changbtnBgcolor(2);
				break;
			case R.id.imgFemale://男图
				PopWindowPic();
				break;
			case R.id.imgMale://nv图
				PopWindowPic();
				
				break;
			case R.id.btnRegisterFinish://注册验证
				userRegister();
				break;
			}
		}
		
		//注册
		private void userRegister(){
			final String name=txtLoginName.getText().toString();
			final String pwd= txtPwd.getText().toString();
			final String email=txtEmail.getText().toString();
			if(name.equals("")||pwd.equals("")||email.equals("")){
				AlertDialog.Builder alert =new AlertDialog.Builder(RegisterActivity.this);
				alert.setTitle("友情提醒")
				.setMessage("用户名密码邮箱不能为空")
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						
					}
				}).show();
				return;
			}
			new Thread(new Runnable() {
				@Override
				public void run() {

					Map<String, String> maps = new HashMap<String, String>();
					maps.put("user", name);
					maps.put("pwd", pwd);
					maps.put("email", pwd);
					Message msg = new Message();
					try {
						result = HttpUtil.requestByPost(HttpConstants.HttpRegister,
								maps, 8);
						if(result==null||result.equals("[]")||result.equals("")||result.equals("0")||result.equals("1")){
							msg.what = 2;
						}else{
							msg.what = 1;
						}
						
					} catch (Exception e) {
						e.printStackTrace();
						msg.what = 2;
					}finally{
						RegisterActivity.this.MyHandler.sendMessage(msg);
					}
					

				}
			}).start();
		}
/**
 * 弹照相窗
 */
		private void PopWindowPic() {
			
			// TODO Auto-generated method stub
			if(mPopPic==null){
				//inflater=(LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//				inflater=(LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				inflater=LayoutInflater.from(RegisterActivity.this);
				layout=inflater.inflate(R.layout.photo,null);
				btnCammer=(ImageView)layout.findViewById(R.id.btnCammer);
				btnPhotoChoose=(ImageView)layout.findViewById(R.id.btnPhotoChoose);
				mPopPic = new PopupWindow(layout, LayoutParams.WRAP_CONTENT,
						LayoutParams.WRAP_CONTENT);
				mPopPic.setFocusable(true);// // 使其聚集
				mPopPic.setOutsideTouchable(true);// 设置允许在外点击消失
				BitmapDrawable drawable=new BitmapDrawable();
				mPopPic.setBackgroundDrawable(drawable);// 需要设置一下此参数，点击外边可消失
				mPopPic.setAnimationStyle(android.R.style.Animation_Toast);
				//背景透明度
				WindowManager.LayoutParams lp = getWindow().getAttributes();
	            lp.alpha = 0.8f; //0.0-1.0
	            getWindow().setAttributes(lp);
	            //监听窗口消失
	            mPopPic.setOnDismissListener(new OnDismissListener() {
					
					@Override
					public void onDismiss() {
						WindowManager.LayoutParams lp = getWindow().getAttributes();
			            lp.alpha = 1.0f; //0.0-1.0
			            getWindow().setAttributes(lp);
			            mPopPic=null;
					}
				});
				
				
				//照相
				btnCammer.setOnClickListener(new OnClickListener() { 
		             
			            @Override 
			            public void onClick(View v) { 
			                String path="sdcard/DCIM/Camera/kp/"+System.currentTimeMillis()+".png";
			                final File file=new File(path);
			                final Uri uri=Uri.fromFile(file);
			                Log.v("camera","uri创建ok");
			                Intent i=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			                //intent.putExtra("3", "3");
			                i.putExtra(android.provider.MediaStore.EXTRA_OUTPUT,uri);
			                // A.java 是主界面，B.java 是子功能模块，要从A启动B，B干完活之后把结果汇报给A
			                startActivityForResult(i,12);
			                Log.v("Camera", "相机调用成功。");
			                Log.v("Camera", "图片保存的绝对路径是："+path);
			            } 
			        });
				//相册选择
				btnPhotoChoose.setOnClickListener(new OnClickListener(){
					public void onClick(View v){
//						Intent i = new Intent(Intent.ACTION_GET_CONTENT);
//						i.setDataAndType(
//								MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
//								"image/*");
						Intent i=new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
						startActivityForResult(i,RESULT_LOAD_IMAGE);
					}
				});
				mPopPic.showAtLocation(RegisterActivity.this.findViewById(R.id.main),
		    				Gravity.CENTER, 0, 0);
				mPopPic.showAsDropDown(layout);//显示窗口
			}
			
			
		}

		
	};
/**
 * 显示图片
 */
	protected void  onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode==RESULT_OK){
			//照相
			if(requestCode==12){
				
			}
			//选择图片
			if(requestCode==RESULT_LOAD_IMAGE && data!=null){
				ContentResolver resolver = getContentResolver();
				 //照片的原始资源地址  
				Uri uri=data.getData();
			
				try {
					////imgFemale.
	            	 //使用ContentProvider通过URI获取原始图片  
					Bitmap photo=MediaStore.Images.Media.getBitmap(resolver, uri);
					if(photo!=null){
						 //为防止原始图片过大导致内存溢出，这里先缩小原图显示，然后释放原始Bitmap占用的内存
						Bitmap smallBitmap=ImageTools.zoomBitmap(photo,photo.getWidth()/5,photo.getHeight()/5);
					}
					if (photo != null) {
						 //为防止原始图片过大导致内存溢出，这里先缩小原图显示，然后释放原始Bitmap占用的内存  					
						Bitmap smallBitmap = ImageTools.zoomBitmap(photo, photo.getWidth() / SCALE, photo.getHeight() / SCALE);
						//释放原始图片占用的内存，防止out of memory异常发生  
						photo.recycle();
						
						if(imgFemale.VISIBLE==View.VISIBLE)	
							imgFemale.setImageBitmap(smallBitmap);
						if(imgMale.VISIBLE==View.VISIBLE)
							imgMale.setImageBitmap(smallBitmap);
					}
				} catch (FileNotFoundException e) {
				    e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			}
		}
	}


	
	private void findViews() {
		btnRegisterFinish=(Button)findViewById(R.id.btnRegisterFinish);
		txtLoginName=(EditText)findViewById(R.id.txtLoginName);
		txtPwd=(EditText)findViewById(R.id.txtPassword);
		txtEmail=(EditText)findViewById(R.id.txtEmail);
		
		imgMale=(ImageView)findViewById(R.id.imgMale);
		imgFemale=(ImageView)findViewById(R.id.imgFemale);
		imgbtnMale=(ImageView)findViewById(R.id.imgbtnMale);
		imgbtnFemale=(ImageView)findViewById(R.id.imgbtnFemale);
	};

	
	
	/** 改变anniu背景和换图
	 * @param type 1男2女
	 */
	private void changbtnBgcolor(int type){
		switch(type){
		case 1://男
			imgMale.setVisibility(View.VISIBLE);
			imgFemale.setVisibility(View.GONE);  
			
			imgbtnMale.setBackgroundResource(R.drawable.zhuce_d_nan_an);  
			imgbtnFemale.setBackgroundResource(R.drawable.zhuce_d_nv);  
			break;
		case 2://女
			imgMale.setVisibility(View.GONE);
			imgFemale.setVisibility(View.VISIBLE);  
			imgbtnMale.setBackgroundResource(R.drawable.zhuce_d_nan);  
			imgbtnFemale.setBackgroundResource(R.drawable.zhuce_d_nv_an);  
			break;
		}
	}
	
	// 初始化登陆注册按钮事件
		private void initButton() {
			login_register_btn = this.findViewById(R.id.login_register_btn);
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
					initPopWindow();// 登录弹窗
					break;
				case R.id.btnRegister_btm:
				case R.id.imgRegister_btm:
					Intent i2 = new Intent(RegisterActivity.this,
							RegisterActivity.class);// 跳转到注册页面
					startActivity(i2);
					break;
				}
			}
		};

		// 登录弹窗
		private void initPopWindow() {
			if (mPop == null) {
				inflater = (LayoutInflater) this
						.getSystemService(LAYOUT_INFLATER_SERVICE);
				layout = inflater.inflate(R.layout.login, null);
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
				BitmapDrawable drawable = new BitmapDrawable();
				mPop.setBackgroundDrawable(drawable);// 需要设置一下此参数，点击外边可消失
				mPop.setAnimationStyle(android.R.style.Animation_Toast);
				// 监听窗口消失
				mPop.setOnDismissListener(new OnDismissListener() {
					@Override
					public void onDismiss() {
						WindowManager.LayoutParams lp = getWindow().getAttributes();
						lp.alpha = 1.0f; // 0.0-1.0
						getWindow().setAttributes(lp);
						mPop = null;

					}
				});
				mPop.showAtLocation(RegisterActivity.this.findViewById(R.id.main),
						Gravity.CENTER, 0, 0);
				mPop.showAsDropDown(layout);// 显示窗口
				// 背景透明度
				WindowManager.LayoutParams lp = getWindow().getAttributes();
				lp.alpha = 0.8f; // 0.0-1.0
				getWindow().setAttributes(lp);
			}
		}

		// 弹窗中按钮事件
		View.OnClickListener handerPop = new OnClickListener() {
			@Override
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.btnCalcel:
				case R.id.imgCancel://点击取消
					if (mPop.isShowing()) {
						mPop.dismiss();
					}
					break;
				case R.id.btnLogin:
				case R.id.imgLogin://点击登陆
					userLogin();
					break;
				}
			}
		};

		// 登陆
		private void userLogin() {
			final String name = txtLoginName.getText().toString();
			final String pwd = txtPwd.getText().toString();
			if (name.equals("") || pwd.equals("")) {
				AlertDialog.Builder alert = new AlertDialog.Builder(
						RegisterActivity.this);// Login.this);
				alert.setTitle("友情提醒")
						.setMessage("用户名密码不能为空")
						.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
									}
								}).show();
				return;
			}
			if (mPop.isShowing() && mPop != null) {
				mPop.dismiss();
			}
			if (!HttpUtil.checkConnection(RegisterActivity.this)) {
				ToastUtil.toastShort(RegisterActivity.this, "无网络连接");
				return;
			}
			new Thread(new Runnable() {
				@Override
				public void run() {

					Map<String, String> maps = new HashMap<String, String>();
					maps.put("user", name);
					maps.put("pwd", pwd);
					Message msg = new Message();
					try {
						result = HttpUtil.requestByPost(HttpConstants.HttpLogin,
								maps, 8);
						if(result==null||result.equals("[]")||result.equals("")||result.equals("0")){
							msg.what = 2;
						}else{
							msg.what = 1;
							Log.i("jsonresult", result);
						}
					} catch (Exception e) {
						e.printStackTrace();
						msg.what = 2;
					}finally{
						RegisterActivity.this.MyHandler.sendMessage(msg);
					}
					
				}
			}).start();
		}

		Handler MyHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 1:// 登录成功
					ToastUtil.toastShort(RegisterActivity.this, "登陆成功");
					JSONObject jsonObj;
					try {
						jsonObj = new JSONObject(result);
						String uid = jsonObj.getString("uid");
						String email = jsonObj.getString("email");
						SharePreferenceUtil sharedPreferences = new SharePreferenceUtil(
								RegisterActivity.this, "user");
						sharedPreferences.saveSharedPreferencesString("uid", uid);
						sharedPreferences.saveSharedPreferencesString("email", email);
						finish();
					} catch (JSONException e) {
						e.printStackTrace();
					}
					break;
				case 2://登陆失败
					ToastUtil.toastShort(RegisterActivity.this, "登陆失败");
				default:
					break;
				}
			};
		};
	

		
}
