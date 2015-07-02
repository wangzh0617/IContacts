package com.young.icontacts.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.net.Uri;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.young.icontacts.R;
import com.young.icontacts.model.ContactBean;
import com.young.icontacts.utils.ImageViewUtils;
import com.young.icontacts.view.CircleImageView;

public class LoveCollectsAdapter extends BaseAdapter{
	private List<ContactBean> list = null;
	private Context mContext;
	
	
	public LoveCollectsAdapter(Context mContext, List<ContactBean> list) {
		this.mContext = mContext;
		this.list = list;
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
		if (view == null) {
			viewHolder = new ViewHolder();
			view = LayoutInflater.from(mContext).inflate(R.layout.love_collect_list_item_layout, null);
			viewHolder.tvname = (TextView) view.findViewById(R.id.tv_contact_name);
			viewHolder.imghead = (CircleImageView)view.findViewById(R.id.img_contact_head);
			
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}
	
		viewHolder.tvname.setText(list.get(position).getDisplayName());
//		long photoId = Integer.parseInt(list.get(position).getContactId());
		Uri uri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI,list.get(position).getContactId());
		ImageViewUtils.setImagefromUrl(mContext,uri.toString(), viewHolder.imghead);
		
		return view;

	}
	


	final static class ViewHolder {
		CircleImageView imghead;
		TextView tvname;
	}
}