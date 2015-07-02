package com.young.icontacts.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class MyAdapter extends BaseAdapter{

	String YiDong [] = {"134","135","136","137","138","139","150","151","152","157","158",
			"159","182","188","147"};
	
	String LianTong [] = {"130","131","132","155","156","186","145"};
	
	String DianXin [] = {"133","153","189"};
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		return null;
	}

	/**得到输入区号中的前三位数字或前四位数字去掉首位为零后的数字。*/
	public String getAreaCodePrefix(String number){
		if (number.charAt(1) == '1' || number.charAt(1) == '2')
			return number.substring(1,3);
		return number.substring(1,4);
	}
	
	/**得到输入手机号码的前三位数字。*/
	public String getMobilePrefix(String number){
		return number.substring(0,3);
	}
	
	/**得到输入号码的中间四位号码，用来判断手机号码归属地。*/
	public String getCenterNumber(String number){
		return number.substring(3,7);
	}
	
	/**判断号码是否以零开头*/
	public boolean isZeroStarted(String number){
		if (number == null || number.isEmpty()){
			return false;
		}
		return number.charAt(0) == '0';
	}
	
	/**得到号码的长度*/
	public int getNumLength(String number){
		if (number == null || number.isEmpty()  )
			return 0;
		return number.length();
	}
	
	/**pd is +86*/
	public String DeleteESNumber(String number){
		String str_number;
		if (number.substring(0, 3).equals("+86")){
			str_number = number.substring(3, number.length());
			return str_number;
		}
		return number;
	}
}
