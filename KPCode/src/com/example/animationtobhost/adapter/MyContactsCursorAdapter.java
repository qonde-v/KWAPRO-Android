package com.example.animationtobhost.adapter;

import java.io.InputStream;
import java.util.ArrayList;

import android.content.ContentUris;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.animationtobhost.R;
import com.example.animationtobhost.model.ContactsBean;

public class MyContactsCursorAdapter extends BaseAdapter {
	private ArrayList<ContactsBean> mList;
	private Context mContext;

	/*
	 * 加载list
	 */
	public MyContactsCursorAdapter(ArrayList<ContactsBean> list,
			Context context) {
		mList = list;
		mContext = context;
	}

	public void refresh(ArrayList<ContactsBean> list) {
		mList = list;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder holder = null;
		if (convertView == null) {
			//?哪里的mContext 
			LayoutInflater inflater = LayoutInflater.from(mContext);
			convertView = inflater.inflate(R.layout.my_contacts_item, null);
			holder = new Holder();
			holder.mNameText = (TextView) convertView.findViewById(R.id.tvName);
			holder.mIDText = (TextView) convertView.findViewById(R.id.tvId);

			holder.mHeadImage = (ImageView) convertView
					.findViewById(R.id.ivHead);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}

		final int type = mList.get(getCount() - position - 1).getType();
		final String name = mList.get(getCount() - position - 1).getName();
		final String number = mList.get(getCount() - position - 1).getNumber();
		holder.mNameText.setText(type == 1 ? name : number);
		final Long id = mList.get(getCount() - position - 1).getId();
		holder.mIDText.setText(id + "");

		Long photoid = mList.get(getCount() - position - 1).getPhoneId();

		ImageView _view = holder.mHeadImage;
		_view.setImageResource(R.drawable.icon_contacts_avatar_default);


		// 得到联系人头像Bitamp
		Bitmap contactPhoto = null;
		// photoid 大于0 表示联系人有头像 如果没有给此人设置头像则给他一个默认的
		if (photoid != null && photoid > 0) {
			Uri uri = ContentUris.withAppendedId(
					ContactsContract.Contacts.CONTENT_URI, id);
			InputStream input = ContactsContract.Contacts
					.openContactPhotoInputStream(mContext.getContentResolver(),
							uri);
			contactPhoto = BitmapFactory.decodeStream(input);

			_view.setImageBitmap(contactPhoto);
		} else {
			_view.setImageResource(R.drawable.icon_contacts_avatar_default);
		}
		//添加点击事件
		convertView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				showToast(mContext,"click>>："+name);
			}

		});

		return convertView;
	}
/*
 * 自定义头部
 */
	class Holder {
		private TextView mNameText, mIDText;
		private ImageView mHeadImage;
	}
	
	private static Toast toast = null;

	public static void showToast(Context c, String msg) {
		if (toast == null) {
			toast = Toast.makeText(c, msg, Toast.LENGTH_LONG);
		} else {
			toast.setText(msg);
		}
		toast.show();
	}
}
