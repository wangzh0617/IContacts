package com.young.icontacts;


import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Intents.Insert;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.young.icontacts.activity.BaseActivity;
import com.young.icontacts.adapter.LoveCollectsAdapter;
import com.young.icontacts.fragment.ContactsFragment;
import com.young.icontacts.fragment.FoundFragment;
import com.young.icontacts.menu.ActionBarDrawerToggle;
import com.young.icontacts.menu.DrawerArrowDrawable;
import com.young.icontacts.model.ContactBean;
import com.young.icontacts.utils.PagerSlidingTabStrip;

public class MainActivity extends BaseActivity implements OnClickListener,
		OnLongClickListener {
	
	/****side menu****/
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;
	private DrawerArrowDrawable drawerArrow;
	
	
	/**
	 * PagerSlidingTabStrip的实例
	 */
	private PagerSlidingTabStrip tabs;

	/**
	 * 获取当前屏幕的密度
	 */
	private DisplayMetrics dm;
	
	/**
	 * 联系人列表的Fragment
	 */
	private ContactsFragment contactsFragment;

	/**
	 * 通话记录的Fragment
	 */
	private FoundFragment foundFragment;
	

	/*** t9 btn ***/
	ImageButton btn_t9;

	/*** layout t9 ***/
	RelativeLayout layoutT9;

	/*** in t9 layout ***/
	ImageButton imgBtn_showOrhide, imgBtn_callphone,
			imgBtn_delete,imgBtn_add;
	LinearLayout layout_top;

	// textview phone number
	TextView tv_phoneNumber;
	// pd t9number show or hide
	boolean up_down = false;
	// pop
	private PopupWindow mPopupWindow;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		getActionBar().setTitle("iContacts");
		getActionBar().setIcon(R.drawable.icon);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);
		getActionBar().setDisplayShowHomeEnabled(false);

		initui();
		initPop();

	}
	
	
	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		
//		SearchContact();
		
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_add) {

			Intent intent = new Intent(Intent.ACTION_INSERT);
			intent.setType("vnd.android.cursor.dir/person");
			intent.setType("vnd.android.cursor.dir/contact");
			intent.setType("vnd.android.cursor.dir/raw_contact");
			startActivityForResult(intent, 1000);

			return true;
		}
		else if (id == android.R.id.home) {
			if (mDrawerLayout.isDrawerOpen(mDrawerList)) {
				mDrawerLayout.closeDrawer(mDrawerList);
			} else {
				mDrawerLayout.openDrawer(mDrawerList);
			}
		}
		return super.onOptionsItemSelected(item);
	}
	
	
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onPostCreate(savedInstanceState);
		mDrawerToggle.syncState();
	}

	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
		mDrawerToggle.onConfigurationChanged(newConfig);
	}
	
	/**
	 * init UI
	 */
	private void initui() {
		// TODO Auto-generated method stub
		
		//menu
		mDrawerLayout = (DrawerLayout) findViewById(R.id.main_relative);
		mDrawerList = (ListView) findViewById(R.id.navdrawer);
		drawerArrow = new DrawerArrowDrawable(this) {
			@Override
			public boolean isLayoutRtl() {
				return false;
			}
		};
		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				drawerArrow, R.string.drawer_open, R.string.drawer_close) {

			public void onDrawerClosed(View view) {
				super.onDrawerClosed(view);
				getActionBar().setTitle("iContacts");
				invalidateOptionsMenu();
			}

			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);
				getActionBar().setTitle("收藏夹");
				invalidateOptionsMenu();
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);
		mDrawerToggle.syncState();
		
		//two pager
		dm = getResources().getDisplayMetrics();
		ViewPager pager = (ViewPager) findViewById(R.id.pager);
		pager.setOffscreenPageLimit(2);
		tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
		pager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
		tabs.setViewPager(pager);
		setTabsValue();

		// image btn t9
		btn_t9 = (ImageButton) findViewById(R.id.btn_contact_keybord);
		btn_t9.setOnClickListener(this);

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		getKeepedContacts();
	}

	/**
	 * init popwindow
	 */
	@SuppressLint("InflateParams")
	private void initPop() {
		// init pop
		View popupView = getLayoutInflater().inflate(
				R.layout.t9keybord_layout, null);
		mPopupWindow = new PopupWindow(popupView, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, true);
		mPopupWindow.setAnimationStyle(R.style.AnimationPreview);
		mPopupWindow.setFocusable(true);
		mPopupWindow.setOutsideTouchable(true);
//		mPopupWindow.setTouchable(true);
		mPopupWindow.setBackgroundDrawable(new BitmapDrawable(getResources(),
				(Bitmap) null)); // back dismiss
		mPopupWindow.update();

		// delete
		imgBtn_delete = (ImageButton) popupView
				.findViewById(R.id.img_btn_delete);
		imgBtn_delete.setOnClickListener(this);
		imgBtn_delete.setOnLongClickListener(this);
		
		//show or side
		imgBtn_showOrhide = (ImageButton)popupView.findViewById(R.id.img_btn_hide);
		imgBtn_showOrhide.setOnClickListener(this);

		// text number
		tv_phoneNumber = (TextView) popupView.findViewById(R.id.phone_number);
		tv_phoneNumber.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				if (count == 0) {
					imgBtn_delete.setVisibility(View.GONE);
					imgBtn_showOrhide.setVisibility(View.VISIBLE);
				}else {
					
					imgBtn_showOrhide.setVisibility(View.GONE);
					imgBtn_delete.setVisibility(View.VISIBLE);
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});					

		// btn call
		imgBtn_callphone = (ImageButton) popupView
				.findViewById(R.id.img_btn_callPhone);
		imgBtn_callphone.setOnClickListener(this);
		
		// btn add
		imgBtn_add = (ImageButton) popupView
						.findViewById(R.id.img_btn_add);
		imgBtn_add.setOnClickListener(this);

		// layout btn
		for (int i = 0; i < 12; i++) {
			View v = popupView.findViewById(R.id.dialNum1 + i);
			v.setOnClickListener(this);
		}
	}

	@SuppressLint("HandlerLeak")
	private Handler popupHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				if (mPopupWindow.isShowing()) {
					mPopupWindow.dismiss();
				} else {
					mPopupWindow.showAtLocation(
							MainActivity.this.findViewById(R.id.main_relative),
							Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 1);
				}
				break;
			}
		}
	};
	
	
	
	/**
	 * 对PagerSlidingTabStrip的各项属性进行赋值。
	 */
	private void setTabsValue() {
		// 设置Tab是自动填充满屏幕的
		tabs.setShouldExpand(true);
		// 设置Tab的分割线是透明的
		tabs.setDividerColor(Color.TRANSPARENT);
		// 设置Tab底部线的高度
		tabs.setUnderlineHeight((int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 1, dm));
		// 设置Tab Indicator的高度
		tabs.setIndicatorHeight((int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 4, dm));
		// 设置Tab标题文字的大小
		tabs.setTextSize((int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_SP, 18, dm));
		// 设置Tab Indicator的颜色
		tabs.setIndicatorColor(Color.parseColor("#2e94e5"));
		// 设置选中Tab文字的颜色 (这是我自定义的一个方法)
		tabs.setSelectedTextColor(Color.parseColor("#2e94e5"));
		// 取消点击Tab时的背景色
		tabs.setTabBackground(0);
	}

	public class MyPagerAdapter extends FragmentPagerAdapter {

		public MyPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		private final String[] titles = { "联系人", "通话记录"};

		@Override
		public CharSequence getPageTitle(int position) {
			return titles[position];
		}

		@Override
		public int getCount() {
			return titles.length;
		}

		@Override
		public Fragment getItem(int position) {
			switch (position) {
			case 0:
				if (contactsFragment == null) {
					contactsFragment = new ContactsFragment();
				}
				return contactsFragment;
			case 1:
				if (foundFragment == null) {
					foundFragment = new FoundFragment();
				}
				return foundFragment;
			
			default:
				return null;
			}
		}

	}
	
	

	/**
	 * btn click
	 */
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_contact_keybord: // right bottom btn
			// layoutT9.setVisibility(View.VISIBLE);
			// btn_t9.setVisibility(View.GONE);
			popupHandler.sendEmptyMessageDelayed(0, 0);
			break;

		// key bord btn
		case R.id.dialNum1:
			if (tv_phoneNumber.getText().length() < 11) {
				tv_phoneNumber.setText(tv_phoneNumber.getText().toString()
						+ v.getTag().toString());
			}
			break;
		case R.id.dialNum2:
			if (tv_phoneNumber.getText().length() < 11) {
				tv_phoneNumber.setText(tv_phoneNumber.getText().toString()
						+ v.getTag().toString());
			}
			break;
		case R.id.dialNum3:
			if (tv_phoneNumber.getText().length() < 11) {
				tv_phoneNumber.setText(tv_phoneNumber.getText().toString()
						+ v.getTag().toString());
			}
			break;
		case R.id.dialNum4:
			if (tv_phoneNumber.getText().length() < 11) {
				tv_phoneNumber.setText(tv_phoneNumber.getText().toString()
						+ v.getTag().toString());
			}
			break;
		case R.id.dialNum5:
			if (tv_phoneNumber.getText().length() < 11) {
				tv_phoneNumber.setText(tv_phoneNumber.getText().toString()
						+ v.getTag().toString());
			}
			break;
		case R.id.dialNum6:
			if (tv_phoneNumber.getText().length() < 11) {
				tv_phoneNumber.setText(tv_phoneNumber.getText().toString()
						+ v.getTag().toString());
			}
			break;
		case R.id.dialNum7:
			if (tv_phoneNumber.getText().length() < 11) {
				tv_phoneNumber.setText(tv_phoneNumber.getText().toString()
						+ v.getTag().toString());
			}
			break;
		case R.id.dialNum8:
			if (tv_phoneNumber.getText().length() < 11) {
				tv_phoneNumber.setText(tv_phoneNumber.getText().toString()
						+ v.getTag().toString());
			}
			break;
		case R.id.dialNum9:
			if (tv_phoneNumber.getText().length() < 11) {
				tv_phoneNumber.setText(tv_phoneNumber.getText().toString()
						+ v.getTag().toString());
			}
			break;
		case R.id.dialNum10:
			if (tv_phoneNumber.getText().length() < 11) {
				tv_phoneNumber.setText(tv_phoneNumber.getText().toString()
						+ v.getTag().toString());
			}
			break;
		case R.id.dialNum11:
			if (tv_phoneNumber.getText().length() < 11) {
				tv_phoneNumber.setText(tv_phoneNumber.getText().toString()
						+ v.getTag().toString());
			}
			break;
		case R.id.dialNum12:
			if (tv_phoneNumber.getText().length() < 11) {
				tv_phoneNumber.setText(tv_phoneNumber.getText().toString()
						+ v.getTag().toString());
			}
			break;

		case R.id.img_btn_delete: // delete
			String strnum = tv_phoneNumber.getText().toString();
			if (strnum.length() > 0) {
				tv_phoneNumber
						.setText(strnum.substring(0, strnum.length() - 1));
			}
			break;
			
		case R.id.img_btn_hide: // hide
			
				mPopupWindow.dismiss();
			
			break;

		 case R.id.img_btn_callPhone: // call
			 
			 if (!tv_phoneNumber.getText().toString().equals("")) {
				 Intent intent = new Intent(Intent.ACTION_CALL,
							Uri.parse("tel:"+tv_phoneNumber.getText().toString().trim()));
					startActivity(intent);
			 		}
			 
		 break;
		 
		 case R.id.img_btn_add: // add
			 
			 if (!tv_phoneNumber.getText().toString().equals("")) {
				
				 Intent intent = new Intent(Intent.ACTION_INSERT);
				 intent.setType("vnd.android.cursor.dir/person");
				 intent.setType("vnd.android.cursor.dir/contact");
				 intent.setType("vnd.android.cursor.dir/raw_contact");
				 intent.putExtra(Insert.PHONE, tv_phoneNumber.getText().toString().trim());
				 startActivity(intent);
			}
			 
			 
		 break;

		default:
			break;
		}
	}

	@Override
	public boolean onLongClick(View v) {
		// TODO Auto-generated method stub

		tv_phoneNumber.setText("");

		return false;
	}
	
	
	/**
	* 获得收藏夹的联系人
	*/
	private void getKeepedContacts(){
	Cursor cur = getContentResolver().query(  
	                ContactsContract.Contacts.CONTENT_URI, null, ContactsContract.Contacts.STARRED + " =  1 " , null, null);  
//	        startManagingCursor(cur);  
//	        int num = cur.getCount();
//	        System.out.println(num + "");
//	        int count = 0;
	List<ContactBean> lBeans = new ArrayList<ContactBean>();
	        while (cur.moveToNext()) {  
//	        count ++;
	   
	            long id = cur.getLong(cur.getColumnIndex("_id"));  
	            Cursor pcur = getContentResolver().query(  
	                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,  
	                    null,  
	                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "="  
	                            + Long.toString(id), null, null);  
	   
	            // 处理多个号码的情况  
	            String phoneNumbers = "";  
	            while (pcur.moveToNext()) {  
	                String strPhoneNumber = pcur  
	                        .getString(pcur  
	                                .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));  
//	                phoneNumbers += strPhoneNumber + ",";
	                
	                phoneNumbers = strPhoneNumber;
	                
	            }  
	            pcur.close();
	            String name = cur.getString(cur.getColumnIndex("display_name"));
	            System.out.println("name===   "+name);
	            System.out.println("numbers===   "+phoneNumbers);
	            
	            ContactBean cb = new ContactBean();

				cb.setDisplayName(name);
//				cb.setContactId(contactId);
				cb.setPhotoId(id);
				cb.setPhoneNum(phoneNumbers);
				
				lBeans.add(cb);
	        }  
	        cur.close();
	        
	        if (lBeans.size() > 0) {
				
				LoveCollectsAdapter adapter = new LoveCollectsAdapter(this,lBeans);
				mDrawerList.setAdapter(adapter);
			}
	}
	
	
	
	/**
	 * 返回键
	 */
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
		
//			Intent intent = new Intent(Intent.ACTION_MAIN);
//			intent.addCategory(Intent.CATEGORY_HOME);
//			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//			startActivity(intent);
//			android.os.Process.killProcess(android.os.Process.myPid()); // 杀掉进程
			if (mDrawerLayout.isDrawerOpen(mDrawerList)) {
				mDrawerLayout.closeDrawer(mDrawerList);
			} else {
				finish();
			}
//			System.exit(0);
		} 
		
		return true;// 设置成false让back失效 ，true表示 不失效
	}
}
