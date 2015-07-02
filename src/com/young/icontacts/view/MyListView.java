package com.young.icontacts.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

public class MyListView extends ListView {

	public MyListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public MyListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public MyListView(Context context) {
		super(context);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}

//	@Override
//		public boolean onInterceptTouchEvent(MotionEvent ev) {
//			// TODO Auto-generated method stub
//		
//		 switch 
//		 (ev.getAction()) {
//		             case 
//		 MotionEvent.ACTION_DOWN:
//		                 
//		 setParentScrollAble(false);//当手指触到listview的时候，让父ScrollView交出ontouch权限，也就是让父scrollview停住不能滚动
//		
//		             case MotionEvent.ACTION_MOVE:
//		                 
//		                 break;
//		         
//		     case MotionEvent.ACTION_UP:
//		             case 
//		 MotionEvent.ACTION_CANCEL:
//		                 
//		                
//		  setParentScrollAble(true);//当手指松开时，让父ScrollView重新拿到onTouch权限
//		         
//		         break;
//		             default:
//		                 break;
//		      
//		    }
//		
//			return super.onInterceptTouchEvent(ev);
//		}
}
