package com.example.animationtobhost;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.animationtobhost.adapter.QuestionAdapter;
import com.example.animationtobhost.api.HttpConstants;
import com.example.animationtobhost.model.Question;
import com.example.animationtobhost.util.HttpUtil;
import com.example.animationtobhost.util.SharePreferenceUtil;
import com.example.animationtobhost.util.ToastUtil;
import com.example.animationtobhost.widget.RTPullListView;
import com.example.animationtobhost.widget.RTPullListView.OnRefreshListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class HomeActivity extends Activity {

	private long exitTime = 0;// 记录时间
	private PopupWindow mPopupwinow = null;// 弹出菜单
	private View llyPopView;// 弹出菜单显示的View
	private ImageView ivMenu;// 菜单按钮
	private ImageView ivSearch;// 查询按钮
	private View login_register_btn;// 底部导入按钮
	private String result = "";// json返回值
	private PopupWindow mPop;// menuWindow;
	private LayoutInflater inflater; // 这个是将xml中的布局显示在屏幕上的关键类
	private View layout;
	private TextView btnLogin, btnCancel;
	private ImageView imgLogin, imgCancel;
	private EditText txtLoginName, txtPwd;
	private RadioGroup radioGroup;
	private ViewPager viewPager;
	private List<View> views;
	private View view1, view2, view3;
	private int checkedIdArray[] = new int[] { R.id.rb_news, R.id.rb_question,
			R.id.rb_tag };
	private List<Question> questionList;
	private QuestionAdapter questionAdapter;
	private int pageIndex = 0;

	private RTPullListView lvQuestion;
	private ProgressBar moreProgressBar;
	private RelativeLayout footerView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.home);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title);
		initView();
		initButton();
		initPop();
		checkUser();
		initQuestion();
	}
	//初始化问题页面
	private void initQuestion() {
		pageIndex = 0;
		questionList = new ArrayList<Question>();
		loadQuestion(1);
		lvQuestion = (RTPullListView) view2.findViewById(R.id.lv_question);
		questionAdapter = new QuestionAdapter(HomeActivity.this, questionList);
		lvQuestion.setAdapter(questionAdapter);

		LayoutInflater inflater = LayoutInflater.from(this);
		View view = inflater.inflate(R.layout.list_footview, null);
		footerView = (RelativeLayout) view
				.findViewById(R.id.list_footview);
		moreProgressBar = (ProgressBar) view.findViewById(R.id.footer_progress);
		lvQuestion.addFooterView(footerView);

		// 下拉刷新监听器
		lvQuestion.setonRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				pageIndex = 0;
				if (!HttpUtil.checkConnection(HomeActivity.this)) {
					questionList.clear();
					ToastUtil.toastShort(HomeActivity.this, "无网络连接");
					questionAdapter.notifyDataSetChanged();
					lvQuestion.onRefreshComplete();
					return;
				}
				loadQuestion(1);
			}
		});

		// 获取跟多监听器
		footerView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				pageIndex++;
				moreProgressBar.setVisibility(View.VISIBLE);
				if (!HttpUtil.checkConnection(HomeActivity.this)) {
					ToastUtil.toastShort(HomeActivity.this, "无网络连接");
					moreProgressBar.setVisibility(View.GONE);
					questionAdapter.notifyDataSetChanged();
					lvQuestion.setSelectionfoot();
					return;
				}
				loadQuestion(2);
			}
		});
	}

	// 加载问题页面数据
	private void loadQuestion(final int type) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				Map<String, String> maps = new HashMap<String, String>();
				int st = 0 + 10 * pageIndex;
				int en = 9 + 10 * pageIndex;
				String start = st + "";
				String end = en + "";
				maps.put("start", start);
				maps.put("end", end);
				Message msg = new Message();
				try {
					result = HttpUtil.requestByPost(
							HttpConstants.HttpQuestions, maps, 8);
					if (result == null
							|| result.equals("") || result.equals("0")) {
						msg.what = 1;
					}else if(result.equals("[]")){ 
						msg.what = 4;
					}else {
						if (type == 1) {
							msg.what = 2;
						} else {
							msg.what = 3;
						}
						Log.i("QuestionJson", result);
					}
					
				} catch (Exception e) {
					e.printStackTrace();
					msg.what = 1;
				} finally {
					HomeActivity.this.LoadHandler.sendMessage(msg);
				}

			}
		}).start();
	}

	Handler LoadHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:// 加载数据失败
				questionList.clear();
				ToastUtil.toastShort(HomeActivity.this, "加载数据失败，请检查网络");
				moreProgressBar.setVisibility(View.GONE);
				questionAdapter.notifyDataSetChanged();
				lvQuestion.onRefreshComplete();
				lvQuestion.setSelectionfoot();
				break;
			case 2:// 重新加载
				// JSONArray jsonObjs;
				// JSONObject jsonObj;
				// jsonObjs = new JSONArray(result);
				// for (int i = 0; i < jsonObjs.length(); i++) {
				// jsonObj=(JSONObject) jsonObjs.opt(0);
				// String uid = jsonObj.getString("uId");
				// String username = jsonObj.getString("username");
				// Log.i("Questionuid", uid);
				// Log.i("Questionusername", username);
				// }
				footerView.setVisibility(View.VISIBLE);
				questionList.clear();
				try {
					Gson gson = new Gson();
					List<Question> newList = gson.fromJson(result,
							new TypeToken<List<Question>>() {
							}.getType());
					questionList.addAll(newList);
					Log.i("qsize", questionList.size() + "");
					questionAdapter.notifyDataSetChanged();
					lvQuestion.onRefreshComplete();
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			case 3:// 加载更多
				try {
					Gson gson = new Gson();
					List<Question> newList = gson.fromJson(result,
							new TypeToken<List<Question>>() {
							}.getType());
					questionList.addAll(newList);
					Log.i("qsize", questionList.size() + "");
					if(newList.size()==0)
						moreProgressBar.setVisibility(View.GONE);
					questionAdapter.notifyDataSetChanged();
					lvQuestion.setSelectionfoot();
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			case 4:
				footerView.setVisibility(View.GONE);
				moreProgressBar.setVisibility(View.GONE);
				questionAdapter.notifyDataSetChanged();
				lvQuestion.setSelectionfoot();
				break;
			default:
				break;
			}
		};
	};

	// 初始化页面控件
	private void initView() {
		// 页头切换事件
		radioGroup = (RadioGroup) this.findViewById(R.id.radio_group);
		RadioButton rbDefault = (RadioButton) HomeActivity.this
				.findViewById(R.id.rb_question);
		rbDefault.setBackgroundResource(R.drawable.caidan_qianbg);
		rbDefault.setTextColor(HomeActivity.this.getResources().getColor(
				R.color.black));
		viewPager = (ViewPager) this.findViewById(R.id.viewpager);
		viewPager.setAdapter(new MyPagerAdapter());
		view1 = LayoutInflater.from(HomeActivity.this).inflate(
				R.layout.my_contacts_list, null);
		view2 = LayoutInflater.from(HomeActivity.this).inflate(
				R.layout.question, null);
		view3 = LayoutInflater.from(HomeActivity.this).inflate(
				R.layout.my_contacts_list, null);
		views = new ArrayList<View>();
		views.add(view1);
		views.add(view2);
		views.add(view3);
		viewPager.setCurrentItem(1);
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				for (int i = 0; i < checkedIdArray.length; i++) {
					RadioButton rb = (RadioButton) HomeActivity.this
							.findViewById(checkedIdArray[i]);
					if (i == position) {
						rb.setBackgroundResource(R.drawable.caidan_qianbg);
						rb.setTextColor(HomeActivity.this.getResources()
								.getColor(R.color.black));
					} else {
						rb.setBackgroundResource(R.drawable.caidan_lanbg);
						rb.setTextColor(HomeActivity.this.getResources()
								.getColor(R.color.white));
					}
				}

			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});
		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {

				switch (checkedId) {
				case R.id.rb_news:
					viewPager.setCurrentItem(0);
					break;
				case R.id.rb_question:
					viewPager.setCurrentItem(1);
					break;
				case R.id.rb_tag:
					viewPager.setCurrentItem(2);
					break;
				default:
					break;
				}
			}
		});
	}

	class MyPagerAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return views.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			// TODO Auto-generated method stub
			return arg0 == arg1;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			// TODO Auto-generated method stub
			((ViewPager) container).addView(views.get(position), 0);
			return views.get(position);
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			// TODO Auto-generated method stub
			((ViewPager) container).removeView(views.get(position));
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
				Intent i2 = new Intent(HomeActivity.this,
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
			mPop.showAtLocation(HomeActivity.this.findViewById(R.id.main),
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

	// 登陆
	private void userLogin() {
		final String name = txtLoginName.getText().toString();
		final String pwd = txtPwd.getText().toString();
		if (name.equals("") || pwd.equals("")) {
			AlertDialog.Builder alert = new AlertDialog.Builder(
					HomeActivity.this);// Login.this);
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
		if (!HttpUtil.checkConnection(HomeActivity.this)) {
			ToastUtil.toastShort(HomeActivity.this, "无网络连接");
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
					if (result == null || result.equals("[]")
							|| result.equals("") || result.equals("0")) {
						msg.what = 2;
					} else {
						msg.what = 1;
						Log.i("jsonresult", result);
					}
					
				} catch (Exception e) {
					e.printStackTrace();
					msg.what = 2;
				} finally {
					HomeActivity.this.LoginHandler.sendMessage(msg);
				}
				

			}
		}).start();
	}

	Handler LoginHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:// 登录成功
				ToastUtil.toastShort(HomeActivity.this, "登陆成功");
				JSONObject jsonObj;
				try {
					jsonObj = new JSONObject(result);
					String uid = jsonObj.getString("uid");
					String email = jsonObj.getString("email");
					SharePreferenceUtil sharedPreferences = new SharePreferenceUtil(
							HomeActivity.this, "user");
					sharedPreferences.saveSharedPreferencesString("uid", uid);
					sharedPreferences.saveSharedPreferencesString("email",
							email);
					checkUser();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				break;
			case 2:// 登陆失败
				ToastUtil.toastShort(HomeActivity.this, "登陆失败");
			default:
				break;
			}
		};
	};

	// 根据是否登录显示隐藏
	public void checkUser() {
		SharePreferenceUtil sharedPreferences = new SharePreferenceUtil(
				HomeActivity.this, "user");
		String user = sharedPreferences.getSharedPreferenceString("uid");
		if (user == null) {
			ivMenu.setVisibility(View.GONE);
			ivSearch.setVisibility(View.GONE);
			login_register_btn.setVisibility(View.VISIBLE);
		} else {
			ivMenu.setVisibility(View.VISIBLE);
			ivSearch.setVisibility(View.VISIBLE);
			login_register_btn.setVisibility(View.GONE);
		}
	}

	// 初始化弹出菜单
	public void initPop() {
		llyPopView = LayoutInflater.from(HomeActivity.this).inflate(
				R.layout.popup_menu, null);
		ivMenu = (ImageView) findViewById(R.id.iv_menu);
		ivSearch = (ImageView) findViewById(R.id.iv_search);
		ivMenu.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				WindowManager wm = (WindowManager) HomeActivity.this
						.getSystemService(Context.WINDOW_SERVICE);
				int width = wm.getDefaultDisplay().getWidth();
				if (mPopupwinow == null) {
					mPopupwinow = new PopupWindow(llyPopView, width / 3,
							LayoutParams.WRAP_CONTENT, true);
					mPopupwinow.setBackgroundDrawable(new ColorDrawable(
							0x00000000));
				}
				Rect frame = new Rect();
				getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
				int statusBarHeight = frame.top;
				int contentTop = getWindow().findViewById(
						Window.ID_ANDROID_CONTENT).getTop();
				int titleBarHeight = contentTop - statusBarHeight;
				mPopupwinow.setAnimationStyle(android.R.style.Animation_Dialog);
				mPopupwinow.showAtLocation(ivMenu, Gravity.RIGHT
						| Gravity.TOP, 0, titleBarHeight);

				mPopupwinow.showAsDropDown(llyPopView);
			}
		});
		initPopListener();
	}

	// 弹出菜单监听事件
	public void initPopListener() {
		TextView tv_menu_msg = (TextView) llyPopView
				.findViewById(R.id.tv_menu_msg);
		TextView tv_menu_firend = (TextView) llyPopView
				.findViewById(R.id.tv_menu_firend);
		TextView tv_menu_question = (TextView) llyPopView
				.findViewById(R.id.tv_menu_question);
		TextView tv_menu_info = (TextView) llyPopView
				.findViewById(R.id.tv_menu_info);
		TextView tv_menu_about = (TextView) llyPopView
				.findViewById(R.id.tv_menu_about);
		TextView tv_menu_exit = (TextView) llyPopView
				.findViewById(R.id.tv_menu_exit);
		tv_menu_msg.setOnClickListener(menuClick);
		tv_menu_firend.setOnClickListener(menuClick);
		tv_menu_question.setOnClickListener(menuClick);
		tv_menu_info.setOnClickListener(menuClick);
		tv_menu_about.setOnClickListener(menuClick);
		tv_menu_exit.setOnClickListener(menuClick);
	}

	OnClickListener menuClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (mPopupwinow != null) {
				mPopupwinow.dismiss();
			}
			switch (v.getId()) {
			case R.id.tv_menu_msg:
				Toast.makeText(HomeActivity.this, "msg", Toast.LENGTH_SHORT)
						.show();
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
				SharePreferenceUtil sharedPreferences = new SharePreferenceUtil(
						HomeActivity.this, "user");
				sharedPreferences.removeSharedPreferencesString("uid");
				checkUser();
				ToastUtil.toastShort(HomeActivity.this, "注销成功");
				break;
			default:
				break;
			}

		}

	};

	protected void onResume() {
		super.onResume();
		checkUser();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			exit();
			return true;
		}
		return super.onKeyDown(keyCode, event);

	}

	// 退出
	public void exit() {
		if ((System.currentTimeMillis() - exitTime) > 2000) {
			ToastUtil.toastShort(HomeActivity.this, "再按一次退出系统");
			exitTime = System.currentTimeMillis();
		} else {
			System.exit(0);
		}
	}

}
