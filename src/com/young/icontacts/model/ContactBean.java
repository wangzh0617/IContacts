package com.young.icontacts.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class ContactBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7578446968300497010L;
//	private String contactId;
//	private String photoId;
	public String displayName;
	
	private String pinyin;
	private String sortLetters;  //显示数据拼音的首字母
	
	private int contactId;
	private long photoId;
	public String phoneNum;
	private String numberType;
	
	private List<NumAndTypeInfo> list = new ArrayList<NumAndTypeInfo>();
	
	
	public List<NumAndTypeInfo> getList() {
		return list;
	}
	public void setList(List<NumAndTypeInfo> list) {
		this.list = list;
	}
	//	public String getContactId() {
//		return contactId;
//	}
//	public void setContactId(String contactId) {
//		this.contactId = contactId;
//	}
//	public String getPhotoId() {
//	return photoId;
//}
//public void setPhotoId(String photoId) {
//	this.photoId = photoId;
//}
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public String getPhoneNum() {
		return phoneNum;
	}
	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}
	
	public String getPinyin() {
		return pinyin;
	}
	public void setPinyin(String pinyin) {
		this.pinyin = pinyin;
	}
	public String getSortLetters() {
		return sortLetters;
	}
	public void setSortLetters(String sortLetters) {
		this.sortLetters = sortLetters;
	}
	public int getContactId() {
		return contactId;
	}
	public void setContactId(int contactId) {
		this.contactId = contactId;
	}
	public long getPhotoId() {
		return photoId;
	}
	public void setPhotoId(long photoId) {
		this.photoId = photoId;
	}
	public String getNumberType() {
		return numberType;
	}
	public void setNumberType(String numberType) {
		this.numberType = numberType;
	}
	
	
}
