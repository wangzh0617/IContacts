package com.young.icontacts.fragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.young.icontacts.R;
import com.young.icontacts.adapter.CallLogAdapter;
import com.young.icontacts.model.CallLogInfo;

import android.annotation.SuppressLint;
import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CallLog;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

@SuppressLint("HandlerLeak")
public class FoundFragment extends BaseFragment {
	
	private MyAsyncQueryHandlerTwo aHandler;
	
	private CallLogAdapter adapter;
	private List<CallLogInfo> list;
	
	ListView cListView;
	
	boolean only = true;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		aHandler = new MyAsyncQueryHandlerTwo(context.getContentResolver());
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.two_fragment_layout, container, false);
		context = getActivity();

		return view;
	}
	
	
	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		
		initui();
	}
	
	
	
	@Override
		public void setUserVisibleHint(boolean isVisibleToUser) {
			// TODO Auto-generated method stub
			super.setUserVisibleHint(isVisibleToUser);
			if (isVisibleToUser== true) {
				
				if (only == true) {
					Uri uri = CallLog.Calls.CONTENT_URI;
					String[] projection = { 
							CallLog.Calls.DATE,
							CallLog.Calls.NUMBER,
							CallLog.Calls.TYPE,
							CallLog.Calls.CACHED_NAME,
							CallLog.Calls._ID,
							CallLog.Calls.DURATION
					}; // 查询的列
					aHandler.startQuery(0, null, uri, projection, null, null, CallLog.Calls.DEFAULT_SORT_ORDER);
					
					only=false;
				}
				
				
			}
		}
	
	/**
	 * init UI
	 */
	private void initui() {
		// TODO Auto-generated method stub
		cListView = (ListView)view.findViewById(R.id.call_listview);
		
	}
	
	
	
	private class MyAsyncQueryHandlerTwo extends AsyncQueryHandler {

		public MyAsyncQueryHandlerTwo(ContentResolver cr) {
			super(cr);
		}
		@SuppressLint("SimpleDateFormat")
		protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
			if (cursor != null && cursor.getCount() > 0) {
				list = new ArrayList<CallLogInfo>();
				SimpleDateFormat sfd = new SimpleDateFormat("MM-dd hh:mm");
				Date date;
				cursor.moveToFirst();
				for (int i = 0; i < cursor.getCount(); i++) {			//only 30 
					cursor.moveToPosition(i);
					date = new Date(cursor.getLong(cursor.getColumnIndex(CallLog.Calls.DATE)));
//					String date = cursor.getLong(cursor.getColumnIndex(CallLog.Calls.DATE));
					String number = cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER));
					int type = cursor.getInt(cursor.getColumnIndex(CallLog.Calls.TYPE));
					String cachedName = cursor.getString(cursor.getColumnIndex(CallLog.Calls.CACHED_NAME));
					int id = cursor.getInt(cursor.getColumnIndex(CallLog.Calls._ID));
//					String time = cursor.getString(cursor.getColumnIndex(CallLog.Calls.DURATION));
				

					CallLogInfo clb = new CallLogInfo();
					clb.setId(id);
					clb.setNumber(number);
					clb.setName(cachedName);
					
					clb.setType(type);
					clb.setDate(sfd.format(date));
					
					list.add(clb);
				}
				if (list.size() > 0) {
					adapter = new CallLogAdapter(context, list);
					cListView.setAdapter(adapter);
				}
			}
		}
	}
}
