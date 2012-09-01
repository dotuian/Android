package com.ian.messagecharge;

import java.util.List;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class SendMessageActivity extends Activity {
	
	private Button btnSend ;
	private Button btnSetting;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.send_message_layout);
		
		// 发送按钮
		btnSend = (Button) this.findViewById(R.id.btnSend);
		btnSend.setOnClickListener(sendListener);
		// 设置按钮
		btnSetting = (Button) this.findViewById(R.id.btnSetting);
		btnSetting.setOnClickListener(settingListener);
	}
	
	/**
	 * 发送短信
	 */
	private OnClickListener sendListener = new View.OnClickListener() {

		public void onClick(View v) {

			// 根据ID获取手机号码编辑框
			EditText mobileText = (EditText) findViewById(R.id.etTelNo);

			// 获取手机号码
			String mobile = mobileText.getText().toString();

			// 根据ID获取信息内容编辑框
			EditText messageText = (EditText) findViewById(R.id.etMessage);

			// 获取信息内容
			String message = messageText.getText().toString();

			// 移动运营商允许每次发送的字节数据有限，我们可以使用Android给我们提供 的短信工具。
			if (message != null) {
				SmsManager sms = SmsManager.getDefault();

				// 如果短信没有超过限制长度，则返回一个长度的List。
				List<String> texts = sms.divideMessage(message);

				for (String text : texts) {
					// sms.sendTextMessage(destinationAddress, scAddress,
					// text, sentIntent, deliveryIntent)：
					// 1. destinationAddress:接收方的手机号码。
					// 2. scAddress:短信中心号码，测试时可以不填写。
					// 3. text:信息内容。
					// 4. sentIntent:发送是否成功的回执，以后会详细介绍。
					// 5. deliveryIntent:接收是否成功的回执。
					sms.sendTextMessage(mobile, null, text, null, null);
					Log.i("sms", "send a message");
				}
			}
		}
	};
	
	/**
	 * 跳转到设置画面
	 */
	private OnClickListener settingListener = new View.OnClickListener() {
		public void onClick(View v) {
			Intent intent = new Intent();  
			intent.setClass(SendMessageActivity.this, SettingActivity.class);
			startActivity(intent);
		}
	};
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.send_message_layout, menu);
		return true;
	}
}
