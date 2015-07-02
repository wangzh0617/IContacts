package com.young.icontacts.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.young.icontacts.R;
import com.young.icontacts.db.DatabaseDAO;
import com.young.icontacts.model.ContactBean;
import com.young.icontacts.model.NumAndTypeInfo;
import com.young.icontacts.utils.ImageViewUtils;
import com.young.icontacts.view.CircleImageView;

public class ContactCAdapter extends BaseAdapter implements SectionIndexer{
	private List<ContactBean> list = null;
	private Context mContext;
	
	DatabaseDAO dao;
	
	public ContactCAdapter(Context mContext, List<ContactBean> list,DatabaseDAO dao) {
		this.mContext = mContext;
		this.list = list;
		this.dao = dao;
	}
	
	/**
	 * 当ListView数据发生变化时,调用此方法来更新ListView
	 * @param list
	 */
	public void updateListView(List<ContactBean> list){
		this.list = list;
		notifyDataSetChanged();
	}

	public int getCount() {
		return this.list.size();
	}

	public Object getItem(int position) {
		return list.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	@SuppressLint("InflateParams")
	public View getView(final int position, View view, ViewGroup arg2) {
		ViewHolder viewHolder = null;
		final ContactBean mContent = list.get(position);
		if (view == null) {
			viewHolder = new ViewHolder();
			view = LayoutInflater.from(mContext).inflate(R.layout.contact_list_item_layout, null);
			viewHolder.tvname = (TextView) view.findViewById(R.id.tv_contact_name);
			viewHolder.tvLetter = (TextView) view.findViewById(R.id.catalog);
			viewHolder.imghead = (CircleImageView)view.findViewById(R.id.img_contact_head);
			
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}
		
		//根据position获取分类的首字母的Char ascii值
		int section = getSectionForPosition(position);
		
		//如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
		if(position == getPositionForSection(section)){
			viewHolder.tvLetter.setVisibility(View.VISIBLE);
			viewHolder.tvLetter.setText(mContent.getSortLetters());
		}else{
			viewHolder.tvLetter.setVisibility(View.GONE);
		}
	
		viewHolder.tvname.setText(list.get(position).getDisplayName());
//		long photoId = Integer.parseInt(list.get(position).getContactId());
		Uri uri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI,list.get(position).getContactId());
		ImageViewUtils.setImagefromUrl(mContext,uri.toString(), viewHolder.imghead);
		
	
		
		
		//phone
//		viewHolder.phone.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				if (list.get(position).getList().size() == 1) {
//					Intent intent = new Intent(Intent.ACTION_CALL,
//							Uri.parse("tel:"+list.get(position).getList().get(0).getNumber()));
//					mContext.startActivity(intent);
//				}else {
//					ShowNumForPhoneDialog(position);
//				}
//			}
//		});
		
		
		//msg
//		viewHolder.message.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				if (list.get(position).getList().size() == 1) {
////					Intent mIntent = new Intent(Intent.ACTION_VIEW);   
////			        mIntent.putExtra("address",list.get(position).getList().get(0).getNumber());   
////			        mIntent.putExtra("sms_body", ""); 
////			        mIntent.setType("vnd.android-dir/mms-sms");   
////			        mContext.startActivity(mIntent);
//					
//					Intent intent = new Intent();
//					intent.setAction(Intent.ACTION_SENDTO);
//					intent.setData(Uri.parse("smsto:"+list.get(position).getList().get(0).getNumber()));
//					mContext.startActivity(intent);
//				}else {
//					ShowNumForMsgDialog(position);
//				}
//			}
//		});
		
		
		return view;

	}
	


	final static class ViewHolder {
		CircleImageView imghead;
		TextView tvLetter;
		TextView tvname;
	}


	/**
	 * 根据ListView的当前位置获取分类的首字母的Char ascii值
	 */
	public int getSectionForPosition(int position) {
		return list.get(position).getSortLetters().charAt(0);
	}

	/**
	 * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
	 */
	@SuppressLint("DefaultLocale")
	public int getPositionForSection(int section) {
		for (int i = 0; i < getCount(); i++) {
			String sortStr = list.get(i).getSortLetters();
			char firstChar = sortStr.toUpperCase().charAt(0);
			if (firstChar == section) {
				return i;
			}
		}
		
		return -1;
	}
	
	@Override
	public Object[] getSections() {
		return null;
	}
	
	
	/**
	 * change number for msg dialog
	 */
	@SuppressLint("InflateParams")
	private void ShowNumForMsgDialog(int pos) {
		final Dialog dialog = new Dialog(mContext, R.style.dialog);

		LayoutInflater fInflater = LayoutInflater.from(mContext);
		View diaView = fInflater.inflate(R.layout.chance_number_dialog_layout,
				null);
		
		ListView listView = (ListView)diaView.findViewById(R.id.number_listview);
		ChanceNumberForMsgAdapter cAdapter = new ChanceNumberForMsgAdapter(mContext, list.get(pos).getList(),dao);
		listView.setAdapter(cAdapter);
		
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				ChanceNumberForPhoneAdapter cAdapter = (ChanceNumberForPhoneAdapter)parent.getAdapter();
				NumAndTypeInfo numAndTypeInfo = (NumAndTypeInfo)cAdapter.getItem(position);
				
				Intent intent = new Intent();
				intent.setAction(Intent.ACTION_SENDTO);
				intent.setData(Uri.parse("smsto:"+numAndTypeInfo.getNumber()));
				mContext.startActivity(intent);
			}
		});
		
		//ok
		Button btn_ok = (Button) diaView
				.findViewById(R.id.btn_dialog_ok);
		btn_ok.setOnClickListener(new OnClickListener() {

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
	
	
	
	/**
	 * change number for phone dialog
	 */
	@SuppressLint("InflateParams")
	private void ShowNumForPhoneDialog(int pos) {
		final Dialog dialog = new Dialog(mContext, R.style.dialog);

		LayoutInflater fInflater = LayoutInflater.from(mContext);
		View diaView = fInflater.inflate(R.layout.chance_number_dialog_layout,
				null);
		
		ListView listView = (ListView)diaView.findViewById(R.id.number_listview);
		
		ChanceNumberForPhoneAdapter cAdapter = new ChanceNumberForPhoneAdapter(mContext, list.get(pos).getList(),dao);
		listView.setAdapter(cAdapter);
		
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				ChanceNumberForPhoneAdapter cAdapter = (ChanceNumberForPhoneAdapter)parent.getAdapter();
				NumAndTypeInfo numAndTypeInfo = (NumAndTypeInfo)cAdapter.getItem(position);
				
				Intent intent = new Intent(Intent.ACTION_CALL,
						Uri.parse("tel:"+numAndTypeInfo.getNumber()));
				mContext.startActivity(intent);
			}
		});
		
		//ok
		Button btn_ok = (Button) diaView
				.findViewById(R.id.btn_dialog_ok);
		btn_ok.setOnClickListener(new OnClickListener() {

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
	
}