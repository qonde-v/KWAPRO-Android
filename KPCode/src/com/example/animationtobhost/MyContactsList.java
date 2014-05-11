package com.example.animationtobhost;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Contacts;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import com.example.animationtobhost.adapter.MyContactsCursorAdapter;
import com.example.animationtobhost.model.ContactsBean;

public class MyContactsList extends Activity {

	private ListView mListView;
	private ArrayList<ContactsBean> mList;
	private MyContactsCursorAdapter mAdapter;
	private String title_name;
	private int num_type;//区分是哪个tab

	// private Button btnRegister;
	// private Button btnLogin;
	private TextView txt;
	// private Handler handler;  
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_contacts_list);
		// 获取ContactsActivity传来的参数
		title_name = getIntent().getStringExtra("title_name");
		setTitle(title_name); // 设置标头
		num_type=getIntent().getIntExtra("num", -1);
		// Button btnRegister=(Button) this.findViewById(R.id.btnRegister);
		// Button btnLogin=(Button) this.findViewById(R.id.btnLogin);
		// btnLogin.setOnClickListener(hander);
		// btnRegister.setOnClickListener(hander);

		loadData();
	}

	public void loadData() {
		mListView = (ListView) findViewById(R.id.listView);
		mList = new ArrayList<ContactsBean>();

		
		if(num_type==0)//rss新闻
		{
		
		}
		if(num_type==1)//最新问题
		{
			StringBuilder builder = new StringBuilder();

			try {
				//1
//				HttpPost mypost = new HttpPost(
//				"http://58.255.32.228/KWAPRO/apiweb/questions");
//		List<NameValuePair> params = new ArrayList<NameValuePair>();
//		params.add(new BasicNameValuePair("start", "0"));
//		params.add(new BasicNameValuePair("end", "9"));
//				mypost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
//				HttpResponse response = new DefaultHttpClient().execute(mypost);
//				if (response.getStatusLine().getStatusCode() == 200) {
//					BufferedReader reader = new BufferedReader(
//							new InputStreamReader(response.getEntity().getContent()));
//					for (String s = reader.readLine(); s != null; s = reader
//							.readLine()) {
//						builder.append(s);
//					}
//////					if (builder.length() > 3) {
//////						json(builder.toString());
//////					} else {
//////						message = handler.obtainMessage(2);
//////						handler.sendMessage(message);
//////					}
//					txt= (TextView) findViewById(R.id.textView1);
//					txt.setText(builder.toString());
//					Log.v("AAA", builder.toString());
//				}
				
//3
// new MyThread(handler,"http://58.255.32.228/KWAPRO/apiweb/questions",2).start();
				
//2				
//			        String path = "http://58.255.32.228/KWAPRO/apiweb/questions";  
//			        URL url = new URL(path);  
//			        HttpURLConnection conn = (HttpURLConnection) url.openConnection();  
//			        conn.setReadTimeout(5000);  
//			        conn.setRequestMethod("GET");  
//			        if (200 == conn.getResponseCode())  
//			        {  
//			            InputStream instream = conn.getInputStream();  
//			            byte[] data =new IOUtils().read(instream);  
//			            String jsonStr = new String(data);  
//			            JSONArray array = new JSONArray(jsonStr);  
//			            for (int i = 0; i < array.length(); i++)  
//			            {  
//			                JSONObject jsonObj = (JSONObject) array.getJSONObject(i);  
//			                builder.append(jsonObj.getString("tags"));
//			            }  
//			        }  
			  
			    	txt= (TextView) findViewById(R.id.textView1);
					txt.setText(builder.toString());
					Log.v("AAA", builder.toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if(num_type==2)//热门标签
		{
			/*
			 * ContentProvider-----数据提供给第三方应用程序使用时使用
			 * 因为在Android系统里面，数据库是私有的。一般情况下外部应用程序是没有权限读取其他应用程序的数据。
			 * 如果你想公开你自己的数据，你有两个选择：你可以创建你自己的内容提供器（一个ContentProvider子类）
			 * 或者你可以给已有的提供器添加数据-如果存在一个控制同样类型数据的内容提供器且你拥有写的权限。
			 */
			Cursor cursor = getContentResolver().query(Contacts.CONTENT_URI, null,
					ContactsContract.Contacts.HAS_PHONE_NUMBER + " > 0 ", null,
					Phone.DISPLAY_NAME + " desc ");
			if (cursor.moveToFirst()) {
				do {
					ContactsBean cb = new ContactsBean();
					// 遍历所有的电话号码
					// Phone.DISPLAY_NAME, Contacts.PHOTO_ID,Contacts._ID
					cb.setName(cursor.getString(cursor
							.getColumnIndex(Phone.DISPLAY_NAME)));
					cb.setId(cursor.getLong(cursor.getColumnIndex(Contacts._ID)));
					cb.setPhoneId(cursor.getLong(cursor
							.getColumnIndex(Contacts.PHOTO_ID)));
					cb.setType(1);
					mList.add(cb);
				} while (cursor.moveToNext());
			}

			mAdapter = new MyContactsCursorAdapter(mList, MyContactsList.this);
			mListView.setAdapter(mAdapter);
		}
	}
 
	public class IOUtils  
	{  
	    /** 
	     * 读取输入流为byte[]数组 
	     */  
	    //public static byte[] read(InputStream instream) throws IOException
		public  byte[] read(InputStream instream) throws IOException
	    {  
	        ByteArrayOutputStream bos = new ByteArrayOutputStream();  
	        byte[] buffer = new byte[1024];  
	        int len = 0;  
	        while ((len = instream.read(buffer)) != -1)  
	        {  
	            bos.write(buffer, 0, len);  
	        }  
	        return bos.toByteArray();  
	    }  
	}
//	public void json(String obj) {
//		try {
//			JSONObject jsonObject = new JSONObject(obj);
//			int uid = jsonObject.getInt("uid");
//			String permission = jsonObject.getString("permission");
//			String companyCode = jsonObject.getString("companyCode");
//			String companyName = jsonObject.getString("companyName");
//			String lawPerson = jsonObject.getString("lawPerson");
//			String annualOutput = jsonObject.getString("annualOutput");
//			String address = jsonObject.getString("address");
//			String tel = jsonObject.getString("tel");
//			Company com = new Company(uid, txtname.getText().toString(), txtpwd
//					.getText().toString(), permission, companyCode,
//					companyName, lawPerson, annualOutput, address, tel, End);
//			cur = pork_data.customerselect();
//			cur.requery();
//			if (cur.getCount() == 0) {
//				pork_data.customerinsert(com);
//			} else {
//				pork_data.customerupdate(com);
//				
//			}
//			if (permission.equals("1")) {
//				intent = new Intent(ConnectivityActivity.this,
//						AdminActivity.class);
//				startActivity(intent);
//			} else if (permission.equals("2")) {
//				intent = new Intent(ConnectivityActivity.this,
//						MaininterfaceActivity.class);
//				startActivity(intent);
//			}
//			Log.v("url response", "true=" + permission);
//			Log.v("url response", "true=" + companyCode);
//		} catch (Exception e) {
//			Log.v("url response", "false");
//			e.printStackTrace();
//		}
//	}
	
	
	// View.OnClickListener hander=new OnClickListener(){
	//
	// @Override
	// public void onClick(View v) {
	// // TODO Auto-generated method stub
	// Intent i;
	// switch(v.getId()){
	// case R.id.btnLogin:
	// Intent ii=new Intent(MyContactsList.this,Login.class);
	// startActivity(ii);
	// break;
	// case R.id.btnRegister:
	// i=new Intent(MyContactsList.this, Register.class);
	// startActivity(i);
	// break;
	// }
	// }
	// };
}


