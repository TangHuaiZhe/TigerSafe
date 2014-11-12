package com.OldHandsomeTiger.Aty;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.OldHandsomeTiger.net.AsyncNetRequest;
import com.OldHandsomeTiger.net.GetDataListener;
import com.OldHandsomeTiger.tigersafe.R;
import com.OldHandsomeTiger.util.Config;
import com.OldHandsomeTiger.util.NetStatusUtil;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.SynthesizerListener;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;

public class Aty_9_tuLing extends Activity {
	private static final String TAG = "Aty_9_tuLing";
	private EditText ed_info;
	private Button bt_submit;
	private TextView tv_content;
	private ProgressDialog pdDialog;
	private ImageButton imv_keda;
	private CheckBox ck_autoRead;
	private SharedPreferences sp;
	private Editor editor;
	// 语音听写对象
	private SpeechRecognizer mIat;
	// 语音听写UI
	private RecognizerDialog iatDialog;
	// 语音合成对象
	private SpeechSynthesizer mTts;
	// 引擎类型
	private String mEngineType = SpeechConstant.TYPE_CLOUD;
	
	
	private Toast mToast;
	
	
	
	/**
	 * 初始化监听器。
	 */
	private InitListener mInitListener = new InitListener() {

		@Override
		public void onInit(int code) {
			Log.d(TAG, "SpeechRecognizer init() code = " + code);
			if (code != ErrorCode.SUCCESS) {
				Toast.makeText(Aty_9_tuLing.this, "初始化监听器错误" + code,
						Toast.LENGTH_SHORT).show();
			}
		}
	};

	/**
	 * 听写监听器。
	 */
	private RecognizerListener recognizerListener = new RecognizerListener() {
		@Override
		public void onBeginOfSpeech() {
		}
		@Override
		public void onError(SpeechError error) {
			Toast.makeText(Aty_9_tuLing.this, "录音错误：" + error.toString(),
					Toast.LENGTH_SHORT).show();
		}
		@Override
		public void onEndOfSpeech() {
		}
		@Override
		public void onResult(RecognizerResult results, boolean isLast) {
			String text = com.OldHandsomeTiger.util.JsonParser
					.parseIatResult(results.getResultString());
			ed_info.append(text);
			ed_info.setSelection(ed_info.length());
			if (isLast) {
				Toast.makeText(Aty_9_tuLing.this, "听写完毕……", Toast.LENGTH_SHORT)
						.show();
				iatDialog.dismiss();
			}
			
		
		}

		@Override
		public void onVolumeChanged(int volume) {
		}

		@Override
		public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
		}
	};

	/**
	 * 听写UI监听器
	 */
	private RecognizerDialogListener recognizerDialogListener = new RecognizerDialogListener() {
		public void onResult(RecognizerResult results, boolean isLast) {
		}

		/**
		 * 识别回调错误.
		 */
		public void onError(SpeechError error) {
			Toast.makeText(Aty_9_tuLing.this, "发生错误" + error,
					Toast.LENGTH_SHORT).show();
		}

	};
	
	/**
	 * 初期化监听。
	 */
	private InitListener mTtsInitListener = new InitListener() {
		@Override
		public void onInit(int code) {
			Log.d(TAG, "InitListener init() code = " + code);
			if (code != ErrorCode.SUCCESS) {
        		showTip("初始化失败,错误码："+code);
        	}		
		}
	};
	
	
	/**
	 * 合成回调监听。
	 */
	private SynthesizerListener mTtsListener = new SynthesizerListener() {
		@Override
		public void onSpeakBegin() {
			showTip("开始播放");
		}

		@Override
		public void onSpeakPaused() {
			showTip("暂停播放");
		}

		@Override
		public void onSpeakResumed() {
			showTip("继续播放");
		}

		@Override
		public void onBufferProgress(int percent, int beginPos, int endPos,
				String info) {
//			mPercentForBuffering = percent;
//			mToast.setText(String.format(getString(R.string.tts_toast_format),
//					mPercentForBuffering, mPercentForPlaying));
//			
//			mToast.show();
		}

		@Override
		public void onSpeakProgress(int percent, int beginPos, int endPos) {
//			mPercentForPlaying = percent;
//			showTip(String.format(getString(R.string.tts_toast_format),
//					mPercentForBuffering, mPercentForPlaying));
		}

		@Override
		public void onCompleted(SpeechError error) {
			if(error == null)
			{
				showTip("播放完成");
			}
			else if(error != null)
			{
				showTip(error.getPlainDescription(true));
			}
		}

		@Override
		public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
			
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tuling);
		mToast = Toast.makeText(this,"",Toast.LENGTH_SHORT);
		
		
		boolean isConnected=NetStatusUtil.isNetworkConnected(this);
		if(!isConnected){
			showTip("此功能必须连接网络，请开启移动/Wifi网络！");
		}
		
		
		SpeechUtility.createUtility(this, "appid="+"5462ae9b");
		ed_info = (EditText) findViewById(R.id.ed_info);
		tv_content = (TextView) findViewById(R.id.tv_content);
		bt_submit = (Button) findViewById(R.id.bt_submit);
		imv_keda = (ImageButton) findViewById(R.id.imv_keda);
		
		
		// 初始化识别对象
		mIat = SpeechRecognizer.createRecognizer(this, mInitListener);
		// 初始化听写Dialog,如果只使用有UI听写功能,无需创建SpeechRecognizer
		iatDialog = new RecognizerDialog(this, mInitListener);
		// 初始化合成对象
		mTts = SpeechSynthesizer.createSynthesizer(this, mTtsInitListener);
		mEngineType = SpeechConstant.TYPE_CLOUD;

		ck_autoRead = (CheckBox) findViewById(R.id.ck_autoRead);
		/** 科大讯飞自动读 */
		sp = getSharedPreferences(Config.KEY_CONFIG, MODE_PRIVATE);
		editor = sp.edit();
		ck_autoRead.setChecked(sp.getBoolean(Config.KEY_AUTOREAD, false));

		final GetDataListener listener = new GetDataListener() {
			@Override
			public String getData(String data) {
				// Log.i("Result", "结果是"+data);
				String result = null;
				try {
					JSONObject jsonObject = new JSONObject(data);
					result = jsonObject.getString("text");
				} catch (JSONException e) {
					e.printStackTrace();
				}
				pdDialog.dismiss();
				tv_content.setText(result);
				
				/**自动朗读*/
				if(ck_autoRead.isChecked()){
					String text = tv_content.getText().toString();
					// 设置参数
					setParam();
					int code = mTts.startSpeaking(text, mTtsListener);
				}
				
				return result;
			}
		};

		bt_submit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(!NetStatusUtil.isNetworkConnected(Aty_9_tuLing.this)){
					showTip("请开启网络……否则无法使用");
					return;
				}
				askTuLing(listener);
			}
		});
		
		ck_autoRead.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				ck_autoRead.setChecked(isChecked);
				editor.putBoolean(Config.KEY_AUTOREAD, isChecked);
				editor.commit();
			}
		});

		/** 语言服务开启 */
		imv_keda.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(!NetStatusUtil.isNetworkConnected(Aty_9_tuLing.this)){
					showTip("请开启网络……否则无法使用");
					return;
				}
				ed_info.setText(null);// 清空显示内容
				// 设置参数
				setParam();
				// 显示听写对话框
				iatDialog.setListener(recognizerDialogListener);
				iatDialog.show();
				mIat.startListening(recognizerListener);
				Toast.makeText(Aty_9_tuLing.this, "听写开始", Toast.LENGTH_SHORT)
						.show();
			}
		});

	}

	/**
	 * 参数设置
	 * @param param
	 * @return
	 */
	@SuppressLint("SdCardPath")
	public void setParam() {
		mIat.setParameter(SpeechConstant.DOMAIN, "iat");
		// 设置语言
			mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
			// 设置语言区域
			mIat.setParameter(SpeechConstant.ACCENT, "mandarin");
	}
	
	public void showTip(final String str){
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				mToast.setText(str);
				mToast.show();
			}
		});
	}
	
	public void askTuLing(final GetDataListener listener) {
		pdDialog = ProgressDialog.show(Aty_9_tuLing.this, "title",
				"老帅虎想问题中……");
		String content = ed_info.getText().toString();
		if (content == null) {
			Toast.makeText(Aty_9_tuLing.this, "问题不能为空~",
					Toast.LENGTH_SHORT).show();
			return;
		}
		String url = null;
		try {
			/** 需要注意url的拼接，因为是get方式……不能有空格，也不要把之前的符号转化 */
			String urlInfo = URLEncoder.encode(content, "UTF-8");
			url = Config.urlString + "?"
					+ "key=b24230c7a31a7b9b7e80822e68689b94&info="
					+ urlInfo;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		Log.i(TAG, "当前请求图灵机器人的url是:" + url);
		AsyncNetRequest asyncNetRequest = new AsyncNetRequest(url,
				listener);
		asyncNetRequest.execute();
	}


}
