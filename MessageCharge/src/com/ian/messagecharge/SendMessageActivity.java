package com.ian.messagecharge;

import java.io.File;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.telephony.SmsManager;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.ian.utils.FileUtil;

public class SendMessageActivity extends Activity {
	
	private static String TAG = "SendMessageActivity";
	
	private static final int REQUEST_EX = 1;
	private static final String sdcardDir = Environment.getExternalStorageDirectory().getPath();
	
	// 
	private EditText etTelNo;
	private EditText etMessage; 
	private Button btnSend ;
	private ImageButton btnSelect ;
	private ImageButton btnSetting;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.send_message_layout);
		
		// 发送号码
		etTelNo = (EditText) findViewById(R.id.etTelNo);
		// 发送信息
		etMessage = (EditText) findViewById(R.id.etMessage);
		etMessage.setMovementMethod(ScrollingMovementMethod.getInstance());
		// 选择按钮
		btnSelect = (ImageButton) this.findViewById(R.id.btnSelect);
		btnSelect.setOnClickListener(selectListener);
		// 发送按钮
		btnSend = (Button) this.findViewById(R.id.btnSend);
		btnSend.setOnClickListener(sendListener);
		// 设置按钮
		btnSetting = (ImageButton) this.findViewById(R.id.btnSetting);
		btnSetting.setOnClickListener(settingListener);
	}
	
	/**
	 * 选择并读取文件
	 */
	private OnClickListener selectListener = new View.OnClickListener() {
		public void onClick(View v) {
			Log.i(TAG, "选择并读取文件OnClickListener");
			
			Intent intent = new Intent();
			intent.putExtra("explorer_title", getString(R.string.dialog_read_from_dir));
			intent.setDataAndType(Uri.fromFile(new File(sdcardDir)), "*/*");
			// 
			intent.setClass(SendMessageActivity.this, ExDialog.class);
			startActivityForResult(intent, REQUEST_EX);
		}
	};

	
	/**
	 * 发送短信
	 */
	private OnClickListener sendListener = new View.OnClickListener() {

		public void onClick(View v) {
			
			Log.i(TAG, "发送短信OnClickListener");
			
			SharedPreferences share = SendMessageActivity.this.getSharedPreferences("perference", MODE_PRIVATE);
			
			// 发送号码
			String telNo = etTelNo.getText().toString();
			// 短信中心号码
			String centerTelNo= share.getString("centerTelNo", "");
			// 信息前缀
			String prefix= share.getString("prefix", "");
			// 发送间隔
			String delay= share.getString("delay", "");
			// 发送内容
			String message = etMessage.getText().toString();

			// 移动运营商允许每次发送的字节数据有限，我们可以使用Android给我们提供 的短信工具。
			if (message != null) {
				SmsManager sms = SmsManager.getDefault();

				String [] contents = message.split("\n"); 
				// 以行为单位发送
				for (int i = 0; i < contents.length; i++) {
					// 如果短信没有超过限制长度，则返回一个长度的List。
					List<String> texts = sms.divideMessage(contents[i]);

					for (String text : texts) {
						// sms.sendTextMessage(destinationAddress, scAddress,
						// text, sentIntent, deliveryIntent)：
						// 1. destinationAddress:接收方的手机号码。
						// 2. scAddress:短信中心号码，测试时可以不填写。
						// 3. text:信息内容。
						// 4. sentIntent:发送是否成功的回执，以后会详细介绍。
						// 5. deliveryIntent:接收是否成功的回执。
						
						if (prefix != null && !"".equals(prefix)){
							
							sms.sendTextMessage(telNo, centerTelNo, prefix+text, null, null);
							Log.i(TAG, (i+1) + " 发送短信 电话号码：" + telNo + " 短信中心号码：" + centerTelNo + " 前缀：" + prefix + " 短信内容：" + (prefix+text));
							
						} else {
							sms.sendTextMessage(telNo, centerTelNo, text, null, null);
							Log.i(TAG, (i+1) + " 发送短信 电话号码：" + telNo + " 短信中心号码：" + centerTelNo + " 短信内容：" + text);
						}
						
						// 间隔发送
						if (delay != null && !"".equals(delay)){
							try {
								Thread.sleep(Integer.valueOf(delay) * 1000);
							} catch (NumberFormatException e) {
								e.printStackTrace();
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					}
				}
			}
		}
	};
	
	/**
	 * 跳转到设置画面
	 */
	private OnClickListener settingListener = new View.OnClickListener() {
		public void onClick(View v) {
			Log.i(TAG, "跳转到设置画面OnClickListener");
			
			Intent intent = new Intent();  
			intent.setClass(SendMessageActivity.this, SettingActivity.class);
			startActivity(intent);
		}
	};
	
	
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		String path = null;
		if (resultCode == RESULT_OK) {
			if (requestCode == REQUEST_EX) {
				Uri uri = intent.getData();
				
				String fullPath = uri.toString();
				String filename = fullPath.substring(fullPath.indexOf(":")+1, fullPath.length());
				File file = new File(filename);
				
				etMessage.setText(FileUtil.readFileByLines(file));
				
				//Toast.makeText(SendMessageActivity.this, "文件读取成功！",Toast.LENGTH_LONG).show();
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.send_message_layout, menu);
		return true;
	}
	
}
