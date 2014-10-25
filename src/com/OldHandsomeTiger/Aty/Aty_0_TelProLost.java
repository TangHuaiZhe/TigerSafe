package com.OldHandsomeTiger.Aty;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap.Config;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.OldHandsomeTiger.tigersafe.R;
import com.OldHandsomeTiger.util.SecurityUtil;

public class Aty_0_TelProLost extends Activity {
	public static final String TAG = "Aty_0_TelProLost";
	SharedPreferences sp;
	String CashPassWord;
	String CashPassWord_MD5;
	String InputPassWord1;
	String InputPassWord1_MD5;
	String InputPassWord2;
	String InputPassWord2_MD5;
	String InputPassWord3;
	String InputPassWord3_MD5;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		sp = getSharedPreferences(com.OldHandsomeTiger.util.Config.KEY_CONFIG, MODE_PRIVATE);
		CashPassWord_MD5 = sp.getString(
				com.OldHandsomeTiger.util.Config.KEY_PASSWORD_MD5, "");

		System.out.println("系统内部密码为：" + CashPassWord_MD5);

		if (CashPassWord_MD5 != "") {
			Log.i(TAG, "showNormalLoginDialog");
			showNormalLoginDialog();
		} else {
			Log.i(TAG, "showFirstLoginDialog");
			showFirstLoginDialog();
		}

	}

	private void showFirstLoginDialog() {
		View view = LayoutInflater.from(this).inflate(
				R.layout.first_entry_dialog, null);
		final AlertDialog alertDialog = new AlertDialog.Builder(
				Aty_0_TelProLost.this).setView(view).create();

		final EditText et_password = (EditText) view
				.findViewById(R.id.et_first_entry_pwd);
		final EditText et_passwordConfirm = (EditText) view
				.findViewById(R.id.et_first_entry_pwd_confirm);
		Button bt_confirm = (Button) view.findViewById(R.id.bt_first_dialog_ok);
		Button bt_cancel = (Button) view
				.findViewById(R.id.bt_first_dialog_cancle);

		/*
		 * 注意，这里为什么不直接用AlertDialog，因为只要点击它的按钮，这个对话框就会消失
		 * 所以还不如直接setView之后自己定义View
		 * 当然如果用于挑战该难度……可以使用反射机制……
		 */
		
		
		bt_confirm.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				InputPassWord1 = et_password.getText().toString();
				InputPassWord2 = et_passwordConfirm.getText().toString();
				InputPassWord1_MD5 = SecurityUtil.strToMD5(InputPassWord1);
				InputPassWord2_MD5 = SecurityUtil.strToMD5(InputPassWord2);
				System.out.println("********************");
				System.out.println("输入的密码是:");
				System.out.println(InputPassWord1);
				System.out.println(InputPassWord2);
				System.out.println("********************");
				if (InputPassWord1.equals("")) {
					Log.i(TAG, "输入的密码为空，需要重新输入");
					Toast.makeText(Aty_0_TelProLost.this, "密码不能为空，请重新输入",
							Toast.LENGTH_SHORT).show();
					return;
				}
				if (InputPassWord1.equals(InputPassWord2)) {

					Editor editor = sp.edit();
					editor.putString(
							com.OldHandsomeTiger.util.Config.KEY_PASSWORD_MD5,
							InputPassWord1_MD5);
					editor.commit();
					System.out.println("设置的密码MD5是"
							+ sp.getString(
									com.OldHandsomeTiger.util.Config.KEY_PASSWORD_MD5,
									""));
					Log.i(TAG, "密码设置成功！");
					Toast.makeText(Aty_0_TelProLost.this, "密码设置成功！",
							Toast.LENGTH_SHORT).show();

					Intent intent = new Intent(Aty_0_TelProLost.this,
							Aty_0_Config1.class);
					startActivity(intent);
					finish();

				} else {
					Log.i(TAG, "两次输入密码不一致，请重新输入,之前的密码是:");
					System.out.println(InputPassWord1);
					System.out.println(InputPassWord2);
					Toast.makeText(Aty_0_TelProLost.this,
							"两次输入密码不一致，或密码为空，请重新输入", Toast.LENGTH_SHORT).show();
				}
			}

		});

		bt_cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.i(TAG, "点击了取消按钮");
				alertDialog.dismiss();
				finish();
			}
		});
		alertDialog.show();

	}

	private void showNormalLoginDialog() {

		View view = LayoutInflater.from(this).inflate(
				R.layout.normal_entry_dialog, null);
		final AlertDialog alertDialog = new AlertDialog.Builder(
				Aty_0_TelProLost.this).setView(view).create();

		final EditText et_Password = (EditText) view
				.findViewById(R.id.et_normal_entry_pwd);
		Button bt_Confirm = (Button) view
				.findViewById(R.id.bt_normal_dialog_ok);
		Button bt_Cancel = (Button) view
				.findViewById(R.id.bt_normal_dialog_cancle);
		bt_Confirm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				InputPassWord3 = et_Password.getText().toString();
				InputPassWord3_MD5 = SecurityUtil.strToMD5(InputPassWord3);
				CashPassWord_MD5 = sp.getString(
						com.OldHandsomeTiger.util.Config.KEY_PASSWORD_MD5,
						"");
				System.out.println("系统存储的密码MD5是:" + CashPassWord_MD5);
				System.out.println("用户输入的密码MD5是:" + InputPassWord3_MD5);
				if (InputPassWord3_MD5.equals(CashPassWord_MD5)) {

				boolean issetup = sp.getBoolean(
							com.OldHandsomeTiger.util.Config.KEY_IS_SETUP,
							false);
				System.out.println("*********");
				System.out.println(issetup);
					if (issetup) {
						alertDialog.dismiss();
						startActivity(new Intent(Aty_0_TelProLost.this,
								LostProtectedSettingActivity.class));
						finish();
					} else {
						Log.i(TAG, "密码一致，进入手机防盗界面");
						Toast.makeText(Aty_0_TelProLost.this, "密码一致，进入手机防盗界面",
								Toast.LENGTH_SHORT).show();
						Intent intent = new Intent(Aty_0_TelProLost.this,
								Aty_0_Config1.class);
						startActivity(intent);
						alertDialog.dismiss();
						finish();
					}

				} else {
					Log.i(TAG, "密码错误，请重新输入！");
					Toast.makeText(Aty_0_TelProLost.this, "密码错误，请重新输入！",
							Toast.LENGTH_SHORT).show();
					et_Password.setText("");
				}
			}
		});

		bt_Cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.i(TAG, "点击了取消按钮");
				alertDialog.dismiss();
				finish();
			}
		});
		alertDialog.show();

	}

}
