package com.young.icontacts.fragment;

import com.young.icontacts.db.AssetsDatabaseManager;
import com.young.icontacts.db.DatabaseDAO;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class BaseFragment extends Fragment{

	/***Fragment***/
	static Context context;
	View view;
	
	public SQLiteDatabase sqliteDB;
	public  DatabaseDAO dao;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		initDB();
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		context = getActivity();
		return super.onCreateView(inflater, container, savedInstanceState);
	}
	
	private void initDB() {
		AssetsDatabaseManager.initManager(context);
		AssetsDatabaseManager mg = AssetsDatabaseManager.getAssetsDatabaseManager();
		sqliteDB = mg.getDatabase("number_location.zip");
		dao = new DatabaseDAO(sqliteDB);
	}
	
	
	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
		AssetsDatabaseManager.closeAllDatabase();
	}
}
