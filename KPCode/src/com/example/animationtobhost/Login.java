package com.example.animationtobhost;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Login extends Activity {
	EditText txtLoginName, txtPwd;
	TextView btnLogin, btnCancel;
	ImageView imgLogin, imgCancel;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);

		//findViews();
		//ViewClickListener();
	}

	private void ViewClickListener() {
		// TODO Auto-generated method stub
		// btnLogin.setOnClickListener(btnLoginrListener);
		btnLogin.setOnClickListener(hander);

	}

	private void findViews() {
		// TODO Auto-generated method stub
		// btnLogin=(Button)findViewById(R.id.btnLogin);
		btnLogin = (TextView) findViewById(R.id.btnLogin);
		btnCancel = (TextView) findViewById(R.id.btnCalcel);
		imgLogin = (ImageView) findViewById(R.id.imgLogin);
		imgCancel = (ImageView) findViewById(R.id.imgCancel);
		txtLoginName = (EditText) findViewById(R.id.txtLoginName);
		txtPwd = (EditText) findViewById(R.id.txtPwd);
	}

	View.OnClickListener hander = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			// try {
			// Toast.makeText(Login.this, "1",Toast.LENGTH_LONG ).show();
			switch (v.getId()) {
			case R.id.btnLogin|R.id.imgLogin:
				if (txtLoginName.getText().length() == 0
						|| txtPwd.getText().length() == 0) {

					AlertDialog.Builder alert = new AlertDialog.Builder(
							Login.this);
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
				else {
					// 调用服务登录。。
					// Toast.makeText(Login.this, "1",Toast.LENGTH_LONG
					// ).show();
					// 回传参
					Toast.makeText(Login.this, "1", Toast.LENGTH_LONG).show();
					Intent data = new Intent();

					// Intent intent = new Intent();
					// intent.setClass(Login.this, MainActivity.class);
					// startActivity(intent);
					setResult(RESULT_OK, data);
					// setResult(RESULT_OK);

					finish();// 此处一定要调用finish()方法

				}
				break;
			case R.id.btnCalcel|R.id.imgCancel:
				
				break;
			}
			
			// } catch (Exception ex) {
			// AlertDialog.Builder ad = new AlertDialog.Builder(Login.this);
			// ad.setTitle("alert")
			// .setMessage(ex.toString())
			// .setPositiveButton("确定",
			// new DialogInterface.OnClickListener() {
			// @Override
			// public void onClick(DialogInterface dialog,
			// int which) {
			// // TODO Auto-generated method stub
			//
			// }
			// }).show();
			// }
		}
	};

	// private OnClickListener btnLoginrListener=new OnClickListener(){
	//
	// @Override
	// public void onClick(View v) {
	// // TODO Auto-generated method stub
	// if(txtLoginName.getText().length()==0||
	// txtPwd.getText().length()==0){
	// AlertDialog.Builder alert=new AlertDialog.Builder(Login.this);
	// alert.setTitle("友情提醒")
	// .setMessage("用户名密码不能为空")
	// .setPositiveButton("确定", new DialogInterface.OnClickListener() {
	//
	// @Override
	// public void onClick(DialogInterface dialog, int which) {
	// // TODO Auto-generated method stub
	//
	// }
	// }).show();
	// //return;
	// }
	// else{
	// //调用服务登录。。
	// //contentprovider
	// //Toast.makeText(Login.this, "1",Toast.LENGTH_LONG ).show();
	// //回传参
	//
	// Intent data=new Intent();
	// /*data.putExtra("a", "a_value");
	// data.putExtra("b", "b_value"); */
	// //intent为A传来的带有Bundle的intent，当然也可以自己定义新的Bundle
	// /*Bundle b=new Bundle();
	// b.putString("a", "a_value");*/
	//
	// //Intent intent = new Intent();
	// //intent.setClass(Login.this, MainActivity.class);
	// //startActivity(intent);
	// setResult(RESULT_OK, data);
	// //setResult(RESULT_OK);
	//
	// finish();//此处一定要调用finish()方法
	//
	// //Toast.makeText(Login.this, "2", Toast.LENGTH_LONG).show();
	// Log.d("login", "f");
	// }
	// }
	//
	// };

}
