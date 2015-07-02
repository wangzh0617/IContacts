package com.young.icontacts.fragment;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.young.icontacts.R;
import com.young.icontacts.activity.ContactDetailsActivity;
import com.young.icontacts.adapter.ContactCAdapter;
import com.young.icontacts.fina.Contacts;
import com.young.icontacts.model.ContactBean;
import com.young.icontacts.model.NumAndTypeInfo;
import com.young.icontacts.view.CharacterParser;
import com.young.icontacts.view.PinyinComparator;
import com.young.icontacts.view.SideBar;
import com.young.icontacts.view.SideBar.OnTouchingLetterChangedListener;

import android.annotation.SuppressLint;
import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class ContactsFragment extends BaseFragment{

	
	/*** contact list ***/
	private ListView contactListview;
	private SideBar sideBar;
	private TextView dialog;
	private ContactCAdapter adapter;
	
	/**
	 * 汉字转换成拼音的
	 */
	private CharacterParser characterParser;

	/**
	 * 根据拼音来排列ListView里面的数据类
	 */
	private PinyinComparator pinyinComparator;
	private AsyncQueryHandler asyncQuery;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view = inflater.inflate(R.layout.one_fragment_layout, container, false);
		context = getActivity();

		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		
		initui();
		SearchContact();
	}
	
	
	
	/**
	 * init UI
	 */
	private void initui() {
		// TODO Auto-generated method stub
		// 实例化汉字转拼音
				characterParser = CharacterParser.getInstance();
				pinyinComparator = new PinyinComparator();

				sideBar = (SideBar) view.findViewById(R.id.sidrbar);
				dialog = (TextView) view.findViewById(R.id.dialog);
				sideBar.setTextView(dialog);

				// 设置右侧触摸监听
				sideBar.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() {

					@Override
					public void onTouchingLetterChanged(String s) {
						
						
						// 该字母首次出现的位置
						int position = adapter.getPositionForSection(s.charAt(0));
						if (position != -1) {
							contactListview.setSelection(position);
						}

					}
				});

				// list
				contactListview = (ListView) view.findViewById(R.id.contact_list);
//				DisplayMetrics dm = new DisplayMetrics();
//				((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
//				int width = dm.widthPixels;//宽度
//				int px_width = DensityUtil.dip2px(context, 161);
//				contactListview.setSwipeOffRight(width-px_width);
				contactListview.setOnItemClickListener(new OnItemClickListener() {
		
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						adapter = (ContactCAdapter)parent.getAdapter();
						ContactBean contactBean = (ContactBean)adapter.getItem(position);
						
						Intent intent = new Intent(context,
								ContactDetailsActivity.class);
						intent.putExtra(Contacts.CONTACT_ID, contactBean.getContactId());
						intent.putExtra(Contacts.DISNAME, contactBean.getDisplayName());
						intent.putExtra(Contacts.NUMBERABDTYPE_LIST, (Serializable)contactBean.getList());
						
						startActivity(intent);
					}
				});
	}
	
	
	
	/**
	 * search contact
	 */
	private void SearchContact() {
		asyncQuery = new MyAsyncQueryHandler(context.getContentResolver());
		Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI; // 联系人的Uri
		String[] projection = { ContactsContract.CommonDataKinds.Phone._ID,
				ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
				ContactsContract.CommonDataKinds.Phone.DATA1, "sort_key",
				ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
				
				ContactsContract.CommonDataKinds.Phone.PHOTO_ID,
				ContactsContract.CommonDataKinds.Phone.LOOKUP_KEY,
				ContactsContract.CommonDataKinds.Phone.TYPE,
				}; // 查询的列
		asyncQuery.startQuery(0, null, uri, projection, null, null,
				"sort_key COLLATE LOCALIZED asc"); // 按照sort_key升序查询
	}

	/**
	 * 数据库异步查询类AsyncQueryHandler
	 * 
	 * @author administrator
	 * 
	 */
	@SuppressLint("HandlerLeak")
	private class MyAsyncQueryHandler extends AsyncQueryHandler {

		public MyAsyncQueryHandler(ContentResolver cr) {
			super(cr);
		}

		/**
		 * 查询结束的回调函
		 */
		@SuppressWarnings("rawtypes")
		@SuppressLint({ "UseSparseArrays", "DefaultLocale" })
		@Override
		protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
			List<ContactBean> sReturnList = null;
			if (cursor != null && cursor.getCount() > 0) {
//				SourceDateList = new ArrayList<ContactBean>();
				sReturnList = new ArrayList<ContactBean>();
				cursor.moveToFirst();
				for (int i = 0; i < cursor.getCount(); i++) {
					cursor.moveToPosition(i);
					String name = cursor.getString(1);
					String number = cursor.getString(2);
					// String sortKey = cursor.getString(3);
					int contactId = cursor.getInt(4);
					Long photoId = cursor.getLong(5);
					// String lookUpKey = cursor.getString(6);
					String typeString = cursor.getString(7);
//					String rowid = cursor.getString(8);
					
					ContactBean cb = new ContactBean();

					cb.setDisplayName(name);
					cb.setContactId(contactId);
					cb.setPhotoId(photoId);
					cb.setPhoneNum(number);
					cb.setNumberType(typeString);
					

					// 汉字转换成拼
					String pinyin = characterParser.getSelling(name);
					String sortString = pinyin.substring(0, 1).toUpperCase();

					// 正则表达式，判断首字母是否是英文字母
					if (sortString.matches("[A-Z]")) {
						cb.setSortLetters(sortString.toUpperCase());
					} else {
						cb.setSortLetters("#");
					}

					NumAndTypeInfo info = new NumAndTypeInfo();
					info.setContactId(contactId);
					info.number = number;
					info.numberType = typeString;
//					SourceDateList.add(cb);
					

					if (sReturnList.size() == 0) {
						sReturnList.add(cb);
					}
					for (int w = 0; w < sReturnList.size(); w++) {
						if (cb.getContactId() == sReturnList.get(w).getContactId()) {
							List<NumAndTypeInfo> list = sReturnList.get(w).getList();
							list.add(info);
						} else {
							Iterator iterator  = sReturnList.iterator();
							boolean flag = true;
							ContactBean a=null;
							while(iterator.hasNext()){
								a = (ContactBean)iterator.next();  
								if (cb.getContactId() == a.getContactId()){
									flag = false;
									List<NumAndTypeInfo> list = a.getList();
									list.add(info);
									break;
								}
							  }  
							if(flag){
								List<NumAndTypeInfo> list = new ArrayList<NumAndTypeInfo>();
								list.add(info);
								cb.setList(list);
								sReturnList.add(cb);
							}
							break;
						}
					}
				}
				
				if (sReturnList.size() > 0) {
					
					Collections.sort(sReturnList, pinyinComparator);
					adapter = new ContactCAdapter(context, 
							sReturnList,dao);
					contactListview.setAdapter(adapter);
				}
			}else {
				
				sideBar.setClickable(false);
			}
		}
	}
	
}
