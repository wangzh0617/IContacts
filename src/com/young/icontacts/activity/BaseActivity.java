package com.young.icontacts.activity;


import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.young.icontacts.db.AssetsDatabaseManager;
import com.young.icontacts.db.DatabaseDAO;


public class BaseActivity extends FragmentActivity{

	public SQLiteDatabase sqliteDB;
	public  DatabaseDAO dao;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		initDB();
	}
	
	private void initDB() {
		AssetsDatabaseManager.initManager(this);
		AssetsDatabaseManager mg = AssetsDatabaseManager.getAssetsDatabaseManager();
		sqliteDB = mg.getDatabase("number_location.zip");
		dao = new DatabaseDAO(sqliteDB);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		AssetsDatabaseManager.closeAllDatabase();
	}
	
}
