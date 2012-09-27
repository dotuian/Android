package com.ian.messagecharge;

import java.util.List;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

public class SendMessageService extends Service {
	
	private static final String TAG = "SendMessageService";
	
	private SmsManager sms = SmsManager.getDefault();
	
	private Thread thread = null;
	
	private boolean flag = true;
	// 发送号码
	private String telNo ;
	// 短信中心号码
	private String centerTelNo;
	// 信息前缀
	private String prefix;
	// 发送间隔
	private String delay;
	// 发送内容
	private String message;
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		Toast.makeText(this, "My Service Created", Toast.LENGTH_LONG).show();
		Log.d(TAG, "onCreate");
	}

	@Override
	public void onStart(Intent intent, int startid) {
		Toast.makeText(this, "My Service Started", Toast.LENGTH_LONG).show();
		Log.d(TAG, "onStart");

		// 接受来自Activity的数据
		Bundle bundle = intent.getExtras();
		telNo = bundle.getString("TELNO");
		centerTelNo = bundle.getString("CENTERTELNO");
		prefix  = bundle.getString("PREFIX");
		delay  = bundle.getString("DELAY");
		message  = bundle.getString("MESSAGE");
		
		if (thread == null) {
			
			thread = new Thread(){
				public void run() {
					if (message != null) {
						String [] contents = message.split("\n"); 
						// 以行为单位发送
						for (int i = 0; i < contents.length && flag; i++) {
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
										Thread.sleep((long)(Float.valueOf(delay) * 1000));
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
							}
						}
					}
					
				}
			};
			
			Log.i(TAG, "线程创建成功！");
			
			thread.start();
		}
	}

	@Override
	public void onDestroy() {
		Toast.makeText(this, "My Service Stopped", Toast.LENGTH_LONG).show();
		Log.d(TAG, "onDestroy");

		flag = false;
		thread = null;
	}

}
