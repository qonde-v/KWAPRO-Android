package com.example.animationtobhost;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
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
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author Administrator
 *
 */
public class Register extends Activity {
	
	//ctrl+shift+o
	private Button btnRegister,btnRegisterFinish,btnLogin;
	private EditText txtLoginName,txtPwd,txtEmail;
	private ImageView imgbtnMale,imgbtnFemale,imgMale,imgFemale;
	
	private PopupWindow mPop;// menuWindow;
	private PopupWindow mPopPic;// menuWindow;
	private LayoutInflater inflater; // 这个是将xml中的布局显示在屏幕上的关键类
	private View layout;
	private TextView btnLogin_pop, btnCancel_pop;
	//private ImageView imgLogin, imgCancel;
	private EditText txtLoginName_pop, txtPwd_pop;
	
	private ImageView btnCammer,btnPhotoChoose;
	
	 private static int RESULT_LOAD_IMAGE = 1;
	 private static final int SCALE = 5;//缩放图片
	 
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		//无标题,放在setContentView(R.layout.register);前
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		
		setContentView(R.layout.register);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title);

		findViews();
		
		ViewClickListener();
	}
	
	
	// 加载事件
	public void ViewClickListener() {
		btnRegister.setOnClickListener(hander);
		btnLogin.setOnClickListener(hander);
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
			case R.id.btnLogin://登录窗
				//Intent i = new Intent(Register.this, Login.class);
				//startActivity(i);
				// Toast.makeText(ContactsActivity.this, "m2",
				// Toast.LENGTH_LONG).show();
				popWinndow();
				break;
			case R.id.btnRegister://注册窗
				Intent i2 = new Intent(Register.this, Register.class);
				startActivity(i2);
				
				break;
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
				if(txtLoginName.getText().length()==0|| 
						   txtPwd.getText().length()==0||
						   txtEmail.getText().length()==0)
						{
							//AlertDialog.Builder alert = new AlertDialog.Builder(ConnectivityActivity.this);
							AlertDialog.Builder alert =new AlertDialog.Builder(Register.this);
							alert.setTitle("友情提醒")
							.setMessage("用户名密码邮箱不能为空")
							.setPositiveButton("确定", new DialogInterface.OnClickListener() {
								
								@Override
								public void onClick(DialogInterface dialog, int which) {
									// TODO Auto-generated method stub
									
								}
							}).show();
							return;
						}
//						else{
//							Intent i=new Intent();
//							setResult(RESULT_OK,i);
//							finish();
//							finish();
//						}
						
				break;
			}
		}
/**
 * 弹照相窗
 */
		private void PopWindowPic() {
			
			// TODO Auto-generated method stub
			if(mPopPic==null){
				//inflater=(LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//				inflater=(LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				inflater=LayoutInflater.from(Register.this);
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
				mPopPic.showAtLocation(Register.this.findViewById(R.id.main),
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
	
	/**
	 * 弹窗登录
	 */
	private void popWinndow() {

		initPopWindow();
		// mPop.showAsDropDown(this.layout);// 以这个Button为anchor（可以理解为锚，基准），在下方弹出
		// mPop.showAtLocation(this.findViewById(R.id.main),
		// Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
		// //设置layout在PopupWindow中显示的位置
//		mPop.showAtLocation(Register.this.findViewById(R.id.main),
//				Gravity.CENTER, 0, 0);// 在屏幕居中，无偏移

	

	}
	
	private void initPopWindow() {
		if (mPop == null) {
			inflater=(LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			// layout = View.inflate(this, R.layout.test2, null);
			//layout = View.inflate(this, R.layout.login, null);
			layout = inflater.inflate(R.layout.login, null);
			// layout =
			// getLayoutInflater().inflate(R.layout.hotel_sort_popview,null);
			// mPop= new PopupWindow(layout,LayoutParams.FILL_PARENT,
			// LayoutParams.WRAP_CONTENT); //后两个参数是width和height
			mPop = new PopupWindow(layout, LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT);
			mPop.setFocusable(true);
			mPop.setOutsideTouchable(true);
			
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
           
            

			btnCancel_pop = (TextView) layout.findViewById(R.id.btnCalcel);
			//imgCancel = (ImageView) layout.findViewById(R.id.imgCancel);
			btnLogin_pop = (TextView) layout.findViewById(R.id.btnLogin);
			//imgLogin = (ImageView) layout.findViewById(R.id.imgLogin);
			txtLoginName_pop = (EditText) layout.findViewById(R.id.txtLoginName);
			txtPwd_pop = (EditText) layout.findViewById(R.id.txtPwd);
			btnCancel_pop.setOnClickListener(handerPop);
			//imgCancel.setOnClickListener(handerPop);
			btnLogin_pop.setOnClickListener(handerPop);
			//imgLogin.setOnClickListener(handerPop);
			
			 mPop.showAtLocation(Register.this.findViewById(R.id.main),
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
			//case R.id.imgCancel:
				if (mPop.isShowing()) {
					mPop.dismiss();
				}
				break;

			case R.id.btnLogin:
			//case R.id.imgLogin:
				if (txtLoginName.getText().toString() == "用户名"
						|| txtPwd.getText().toString() == "密    码"
//						|| txtLoginName.getText().toString() == ""
//						|| txtPwd.getText().toString() == ""
							) 
				{
					AlertDialog.Builder alert = new AlertDialog.Builder(
							Register.this);// Login.this);
					alert.setTitle("友情提醒")
							.setMessage("用户名密码不能为空")
							.setPositiveButton("确定",
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											// TODO Auto-generated method
											// stub

										}
									}).show();
					return;
				}
				
				break;
			}
		}
	};

	
	private void findViews() {
		// TODO Auto-generated method stub
		btnRegister=(Button)findViewById(R.id.btnRegister);
		btnRegisterFinish=(Button)findViewById(R.id.btnRegisterFinish);
		btnLogin=(Button)findViewById(R.id.btnLogin);
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
}
