package com.young.icontacts.activity;

import java.util.List;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.young.icontacts.R;
import com.young.icontacts.adapter.ContactDetailAdapter;
import com.young.icontacts.fina.Contacts;
import com.young.icontacts.model.NumAndTypeInfo;
import com.young.icontacts.utils.ImageViewUtils;
import com.young.icontacts.utils.TispToastFactory;
import com.young.icontacts.view.CircleImageView;
import com.young.icontacts.view.OverScrollView;
import com.young.icontacts.view.OverScrollView.OverScrollListener;
import com.young.icontacts.view.OverScrollView.OverScrollTinyListener;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
public class ContactDetailsActivity extends BaseActivity implements TabListener,
		OverScrollListener, OverScrollTinyListener,OnClickListener {
	public Context context;
	/**
	 * 初始化填充值，控制图片被隐藏的边缘值
	 */
	private static final int PADDING = -100;
	
	
	/***actionbar***/
	//layout btn back
	LinearLayout layoutBtn_bar_back;
	CircleImageView img_bar_head;
	TextView tv_bar_name;
	
	
	/***layout***/
	private ImageView mHeaderImage;
	ListView listView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contact_details);
		context = this;
		
		// 自定义actionbar的布局
		setActionBarLayout( R.layout.contact_detail_actionbar_layout );
		
		init();
		
		
		

		// 隐藏图片的初始边界
		// 保证下拉能出现放大图片的效果
		scrollLoosen();
	}
	
	

	/**
	 * init UI
	 */
	private void init() {
		
		/**actionbar**/
		layoutBtn_bar_back = (LinearLayout)findViewById(R.id.contactDetail_actionbar_layoutback);
		layoutBtn_bar_back.setOnClickListener(this);
		
		img_bar_head = (CircleImageView)findViewById(R.id.contactDetail_actionbar_head);
		tv_bar_name = (TextView)findViewById(R.id.contactDetail_actionbar_name);
		
		//actionbar circle head
		int intId = getIntent().getIntExtra(Contacts.CONTACT_ID, 0);
		Uri uri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI,intId);
		ImageViewUtils.setImagefromUrl(context,uri.toString(),img_bar_head);
		//actionbar name
		tv_bar_name.setText(getIntent().getStringExtra(Contacts.DISNAME));
		
		mHeaderImage = (ImageView) findViewById(R.id.details_headimage);
		mHeaderImage.setOnClickListener(this);
		ImageViewUtils.setImagefromUrlTwo(context,uri.toString(),mHeaderImage);
		
		//listview
		listView = (ListView)findViewById(R.id.contactDetail_listview);
		
		@SuppressWarnings("unchecked")
		List<NumAndTypeInfo> list = (List<NumAndTypeInfo>) getIntent().getSerializableExtra(Contacts.NUMBERABDTYPE_LIST);
		ContactDetailAdapter cAdapter = new ContactDetailAdapter(this, list,dao);
		listView.setAdapter(cAdapter);
		
		OverScrollView scrollView = (OverScrollView) findViewById(R.id.layout);
		scrollView.setOverScrollListener(this);
		scrollView.setOverScrollTinyListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_contact_details_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		switch (item.getItemId()) {
		case R.id.action_edit:
			Intent i = new Intent(Intent.ACTION_EDIT);   
	        i.setData(ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI,getIntent().getIntExtra(Contacts.CONTACT_ID, 00)));   
	        context.startActivity(i);
//			TispToastFactory.showToast(context, "action_edit", 1);
			break;
		case R.id.action_delete:
			
			ShowDeleteDialog();
			
			
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	public void scrollDistance(int tinyDistance, int totalDistance) {
		if (totalDistance > 0 || tinyDistance == 0
				|| mHeaderImage.getPaddingBottom() == 0) {
			return;
		}
		int padding = PADDING - totalDistance / 2;
		if (padding > 0) {
			padding = 0;
		}
		mHeaderImage.setPadding(padding, 0, padding, padding);
	}

	@Override
	public void scrollLoosen() {
		int padding = PADDING;
		mHeaderImage.setPadding(padding, 0, padding, padding);

		// 滑动处理松开时回调
	}

	@Override
	public void headerScroll() {
//		Toast.makeText(getApplicationContext(), "开始刷新", Toast.LENGTH_SHORT)
//				.show();

		// 处理下拉超过一定临界值时 的回调
	}

	@Override
	public void footerScroll() {
		// 处理上拉超过一定临界值时 的回调
	}

	@Override
	public void onTabReselected(Tab arg0, FragmentTransaction arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTabSelected(Tab arg0, FragmentTransaction arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTabUnselected(Tab arg0, FragmentTransaction arg1) {
		// TODO Auto-generated method stub

	}
	
	
	/**
	 * 设置ActionBar的布局
	 * @param layoutId 布局Id
	 * 
	 * */
	public void setActionBarLayout( int layoutId ){
		ActionBar actionBar = getActionBar( );
		if( null != actionBar ){
			actionBar.setDisplayShowHomeEnabled( false );
			actionBar.setDisplayShowCustomEnabled(true);

			LayoutInflater inflator = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View v = inflator.inflate(layoutId, null);
			ActionBar.LayoutParams layout = new ActionBar.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			actionBar.setCustomView(v,layout);
		}
	}
	
	
	
	/**
	 * delete dialog
	 */
	@SuppressLint("InflateParams")
	private void ShowDeleteDialog() {
		final Dialog dialog = new Dialog(this, R.style.dialog);

		LayoutInflater fInflater = LayoutInflater.from(this);
		View diaView = fInflater.inflate(R.layout.delete_dialog_layout,
				null);
		
		//ok
		Button btn_ok = (Button) diaView
				.findViewById(R.id.btn_dialog_ok);
		btn_ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
//				System.out.println("id    "+getIntent().getIntExtra(Contacts.CONTACT_ID, 00));
//				getContentResolver().delete(
//		                ContentUris.withAppendedId(RawContacts.CONTENT_URI,
//		                		getIntent().getIntExtra(Contacts.CONTACT_ID, 00)), null, null);
				
				Uri deleteUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, getIntent().getIntExtra(Contacts.CONTACT_ID, 00));
				Uri lookupUri = ContactsContract.Contacts.getLookupUri(ContactDetailsActivity.this.getContentResolver(), deleteUri);
				if(lookupUri != Uri.EMPTY){
					ContactDetailsActivity.this.getContentResolver().delete(deleteUri, null, null);
				}
				finish();
				
				TispToastFactory.showToast(context, "删除成功", 1);
	            dialog.dismiss();
			}
		});
		
		//cancle
		Button btn_cancle = (Button) diaView
				.findViewById(R.id.btn_dialog_cancle);
		btn_cancle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
	            dialog.dismiss();
			}
		});
		

		dialog.setContentView(diaView);
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
	}
	

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.contactDetail_actionbar_layoutback:
			finish();
			break;
			
		case R.id.details_headimage:
			Intent i = new Intent(Intent.ACTION_EDIT);   
	        i.setData(ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI,getIntent().getIntExtra(Contacts.CONTACT_ID, 00)));   
	        context.startActivity(i);
			break;

		default:
			break;
		}
	}
	
	
	/**
	 * 返回键
	 */
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
		
			finish();
		} 
		
		return true;// 设置成false让back失效 ，true表示 不失效
	}
}
