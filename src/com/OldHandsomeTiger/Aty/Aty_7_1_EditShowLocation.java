package com.OldHandsomeTiger.Aty;
/*
 * 编辑来电归属地文本框的位置
 * 
 */
import com.OldHandsomeTiger.tigersafe.R;
import com.OldHandsomeTiger.util.Config;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.TextView;
import android.widget.RelativeLayout.LayoutParams;

public class Aty_7_1_EditShowLocation extends Activity implements OnTouchListener {
	private static final String TAG = "Aty_ShowLocation";
	private TextView tv;
	private  SharedPreferences sp;
	
	// 点击了归属地Textview
	int startX = 0;
	int startY = 0;

	int deletaX;
	int deletaY;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		sp=getSharedPreferences(Config.KEY_CONFIG, MODE_PRIVATE);
		setContentView(R.layout.show_location);
		tv = (TextView) findViewById(R.id.tv_location);
		tv.setOnTouchListener(this);
		
		
		int x = sp.getInt(Config.KEY_LAST_X, 0);
		int y = sp.getInt(Config.KEY_LAST_Y, 0);
		Log.i(TAG, "lastx="+x);
		Log.i(TAG, "lasty="+y);
		//为什么下面的命令不行？？是BUG吗？
//		tv.layout(tv.getLeft()+x, tv.getTop()+y, tv.getRight()+x, tv.getBottom()+y);
//		tv.invalidate();
		android.widget.LinearLayout.LayoutParams params = 
				(android.widget.LinearLayout.LayoutParams) tv.getLayoutParams();
		params.leftMargin = x;
		params.topMargin = y;
		tv.setLayoutParams(params);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch (v.getId()) {
		case R.id.tv_location:
	

			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				// 获取到第一次触屏时候的X和Y
				startX = (int) event.getRawX();
				startY = (int) event.getRawY();
				Log.i(TAG,"ACTION_DOWN_X:"+ startX);
				Log.i(TAG,"ACTION_DOWN_Y:"+ startY);
				break;
			case MotionEvent.ACTION_MOVE:

				int currentX = (int) event.getRawX();
				int currentY = (int) event.getRawY();
				int left = tv.getLeft();
				int top = tv.getTop();
				int right = tv.getRight();
				int bottom = tv.getBottom();
//				Log.i(TAG,"Start1X:"+ startX);
//				Log.i(TAG,"Start1Y:"+ startY);
				
				Log.i(TAG, "当前的Left"+left);
				Log.i(TAG, "当前的top"+top);
				Log.i(TAG, "当前的right"+right);
				Log.i(TAG, "当前的bottom"+bottom);
				Log.i(TAG,"DX:"+ (currentX-startX));
				Log.i(TAG,"DY:"+ (currentY-startY));
				tv.layout(left + currentX - startX, top + currentY - startY,
						right + currentX - startX, bottom + currentY - startY);
				tv.invalidate();
				startX = (int) event.getRawX();
				startY = (int) event.getRawY();
//				Log.i(TAG,"Start2X:"+ startX);
//				Log.i(TAG,"Start2Y:"+ startY);
				break;

			case MotionEvent.ACTION_UP:
				Log.i(TAG, "手指离开屏幕……S");
				int lasty = tv.getTop();
				int lastx = tv.getLeft();
				Editor editor = sp.edit();
				editor.putInt(Config.KEY_LAST_X, lastx);
				editor.putInt(Config.KEY_LAST_Y, lasty);
				editor.commit();
				break;

			default:
				break;
			}

			break;

		default:
			break;
		}

		// True if the listener has consumed the event, false otherwise.
		return true;
	}

}
