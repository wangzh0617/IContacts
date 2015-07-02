package com.young.icontacts.adapter;

import java.util.List;


import com.young.icontacts.R;
import com.young.icontacts.model.CallLogInfo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CallLogAdapter extends BaseAdapter{
	
	private Context context;
	private List<CallLogInfo> list;
	
	public CallLogAdapter(Context context,List<CallLogInfo> list){
		this.context = context;
		this.list = list;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.two_fragment_list_item_layout, null);
			holder = new ViewHolder();
			holder.call_type = (ImageView) convertView.findViewById(R.id.img_call_type);
			holder.name = (TextView) convertView.findViewById(R.id.tv_call_name);
			holder.number = (TextView) convertView.findViewById(R.id.tv_call_number);
			holder.time = (TextView) convertView.findViewById(R.id.tv_call_time);
			
			holder.img_call = (ImageView) convertView.findViewById(R.id.img_call);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		CallLogInfo clb = list.get(position);
		switch (clb.getType()) {
		case 1:
			holder.call_type.setBackgroundResource(R.drawable.call_type_incoming_call);
			break;
		case 2:
			holder.call_type.setBackgroundResource(R.drawable.call_type_outgoing_call);
			break;
		case 3:
			holder.call_type.setBackgroundResource(R.drawable.call_type_missed_call);
			break;
		}
		if (clb.getName() == null) {
			holder.name.setText("陌生人");
		}else {
			holder.name.setText(clb.getName());
		}
		holder.number.setText(clb.getNumber());
		holder.time.setText(clb.getDate());
		
		holder.img_call.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Intent.ACTION_CALL,
				Uri.parse("tel:"+list.get(position).getNumber()));
				context.startActivity(intent);
			}
		});
		
		return convertView;
	}

	
	private static class ViewHolder {
		ImageView call_type,img_call;
		TextView name;
		TextView number;
		TextView time;
	}
}
