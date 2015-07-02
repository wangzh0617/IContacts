package com.young.icontacts.adapter;

import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.young.icontacts.R;
import com.young.icontacts.db.DatabaseDAO;
import com.young.icontacts.model.NumAndTypeInfo;

public class ChanceNumberForPhoneAdapter extends MyAdapter{

	DatabaseDAO dao;
	
	private LayoutInflater mInflater;
	Context context;
	List<NumAndTypeInfo> list;
	
	public ChanceNumberForPhoneAdapter (Context context,List<NumAndTypeInfo> list,DatabaseDAO dao){
		this.context = context;
		this.list = list;
		this.dao = dao;
		mInflater = LayoutInflater.from(context);
	}
	
	@SuppressLint("InflateParams")
	@Override
	public View getView(int pos, View convertView, ViewGroup parentView) {

		// TODO Auto-generated method stub

		ViewHolder viewHolder = null;
		if (convertView == null) {
				convertView = mInflater.inflate(
						R.layout.chance_number_forphone_item_layout, null);
			
			viewHolder = new ViewHolder();
			viewHolder.tv_number = (TextView) convertView
					.findViewById(R.id.tv_item_number);
			viewHolder.tv_numberAddress = (TextView) convertView
					.findViewById(R.id.tv_item_numberAddress);
			
			viewHolder.tv_numberYLD = (TextView) convertView
					.findViewById(R.id.tv_item_numberYLD);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		viewHolder.tv_number.setText(list.get(pos).getNumber());
		
		String prefix, center;
		Map<String,String> map = null;
		
		final String phoneNumber = DeleteESNumber(list.get(pos).getNumber());
		
		if (isZeroStarted(phoneNumber) && getNumLength(phoneNumber) > 2){
			prefix = getAreaCodePrefix(phoneNumber);
			map = dao.queryAeraCode(prefix);
			
		}else if (!isZeroStarted(phoneNumber) && getNumLength(phoneNumber) > 6){
			prefix = getMobilePrefix(phoneNumber);
			center = getCenterNumber(phoneNumber);
			map = dao.queryNumber(prefix, center);
		}
		
		if (map == null) {
			viewHolder.tv_numberAddress.setText("未知");
		}else {
			
			String province = map.get("province");
			String city = map.get("city");
			if (province == null || city == null || province.isEmpty() || city.isEmpty()){
				viewHolder.tv_numberAddress.setText("未知");
			}else if ( province.equals(city))
				viewHolder.tv_numberAddress.setText(province);
			else
				viewHolder.tv_numberAddress.setText(province + " " + city);
		}
		
		String three  = phoneNumber.substring(0, 3);
		for (int i = 0; i < YiDong.length; i++) {
			if (three.equals(YiDong[i])) {
				viewHolder.tv_numberYLD.setText(" 移动");
				break;
			}
		}
		for (int i = 0; i < LianTong.length; i++) {
			if (three.equals(LianTong[i])) {
				viewHolder.tv_numberYLD.setText(" 联通");
				break;
			}
		}
		for (int i = 0; i < DianXin.length; i++) {
			if (three.equals(DianXin[i])) {
				viewHolder.tv_numberYLD.setText(" 电信");
				break;
			}
		}
		
		return convertView;
	
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
	
	private static class ViewHolder {
		public TextView tv_number,tv_numberAddress,tv_numberYLD;
	}
}
