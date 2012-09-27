package com.ian.messagecharge;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.ian.utils.FileUtil;

public class SendMessageActivity extends Activity {
	
	public static String TAG = "SendMessageActivity";
	public static final String TEL_NUMBER = "TEL_NUMBER";
	public static final String MESSAGE = "MESSAGE";
	
	public static final int REQUEST_EXDIALOG = 1;
	public static final int REQUEST_SETTING = 2;
	
	private static final String sdcardDir = Environment.getExternalStorageDirectory().getPath();
	
	private TextView tvTitle;
	private EditText etTelNo;
	private EditText etMessage; 
	private Button btnStop ;
	private Button btnSend ;
	private ImageButton btnSelect ;
	private ImageButton btnSetting;
	private int messageCount = 0 ;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.send_message_layout);

		// 标题
		tvTitle = (TextView) findViewById(R.id.tvTitle);
		// 发送号码
		etTelNo = (EditText) findViewById(R.id.etTelNo);
		// 发送信息
		etMessage = (EditText) findViewById(R.id.etMessage);
		etMessage.setMovementMethod(ScrollingMovementMethod.getInstance());
		
		etMessage.setOnKeyListener(onkeylistener);
		
		// 停止按钮
		btnStop = (Button) this.findViewById(R.id.btnStop);
		btnStop.setOnClickListener(stopListener);
		// 发送按钮
		btnSend = (Button) this.findViewById(R.id.btnSend);
		btnSend.setOnClickListener(sendListener);
		// 选择按钮
		btnSelect = (ImageButton) this.findViewById(R.id.btnSelect);
		btnSelect.setOnClickListener(selectListener);
		// 设置按钮
		btnSetting = (ImageButton) this.findViewById(R.id.btnSetting);
		btnSetting.setOnClickListener(settingListener);
	}
	
	private OnKeyListener onkeylistener = new EditText.OnKeyListener() {
		public boolean onKey(View v, int keyCode, KeyEvent event) {
			
			String message = etMessage.getText().toString();
			messageCount = FileUtil.countLines(message);
			tvTitle.setText(getString(R.string.sendpage) +  "(" + messageCount + ")");
			
			return false;
		}
	};
	
	
	/**
	 * 停止按钮
	 */
	private OnClickListener stopListener = new View.OnClickListener() {
		public void onClick(View v) {
			Log.i(TAG, "停止OnClickListener");
			
			Intent intent = new Intent();
			intent.setClass(SendMessageActivity.this, SendMessageService.class);
			stopService(intent);
		}
	};
	
	/**
	 * 发送短信
	 */
	private OnClickListener sendListener = new View.OnClickListener() {

		public void onClick(View v) {
			
			Log.i(TAG, "发送短信OnClickListener");
			
			if(!validateForSendMessage()){
				Log.i(TAG, "验证不通过，返回！");
				return ;
			}
			Log.i(TAG, "验证通过，开始发送短信！");
			
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

			// 通过Bundle对象存储需要传递的数据
			Bundle bundle = new Bundle();
			bundle.putString("TELNO", telNo);
			bundle.putString("CENTERTELNO", centerTelNo);
			bundle.putString("PREFIX", prefix);
			bundle.putString("DELAY", delay);
			bundle.putString("MESSAGE", message);

			Intent intent = new Intent();
			intent.putExtras(bundle);
			intent.setClass(SendMessageActivity.this, SendMessageService.class);
			
			Toast.makeText(SendMessageActivity.this, "My Service Started", Toast.LENGTH_LONG).show();
			
			startService(intent);
			
//			messageCount = FileUtil.countLines(message);
//			tvTitle.setText(getString(R.string.sendpage) +  "(" + messageCount + ")");
//			// 移动运营商允许每次发送的字节数据有限，我们可以使用Android给我们提供 的短信工具。
//			if (message != null) {
//				SmsManager sms = SmsManager.getDefault();
//
//				String [] contents = message.split("\n"); 
//				// 以行为单位发送
//				for (int i = 0; i < contents.length; i++) {
//					// 如果短信没有超过限制长度，则返回一个长度的List。
//					
//					List<String> texts = sms.divideMessage(contents[i]);
//
//					for (String text : texts) {
//						// sms.sendTextMessage(destinationAddress, scAddress,
//						// text, sentIntent, deliveryIntent)：
//						// 1. destinationAddress:接收方的手机号码。
//						// 2. scAddress:短信中心号码，测试时可以不填写。
//						// 3. text:信息内容。
//						// 4. sentIntent:发送是否成功的回执，以后会详细介绍。
//						// 5. deliveryIntent:接收是否成功的回执。
//						
//						if (prefix != null && !"".equals(prefix)){
//							
//							sms.sendTextMessage(telNo, centerTelNo, prefix+text, null, null);
//							Log.i(TAG, (i+1) + " 发送短信 电话号码：" + telNo + " 短信中心号码：" + centerTelNo + " 前缀：" + prefix + " 短信内容：" + (prefix+text));
//							
//						} else {
//							sms.sendTextMessage(telNo, centerTelNo, text, null, null);
//							Log.i(TAG, (i+1) + " 发送短信 电话号码：" + telNo + " 短信中心号码：" + centerTelNo + " 短信内容：" + text);
//						}
//						
//						// 间隔发送
//						if (delay != null && !"".equals(delay)){
//							try {
//								Thread.sleep((long)(Float.valueOf(delay) * 1000));
//							} catch (NumberFormatException e) {
//								e.printStackTrace();
//							} catch (InterruptedException e) {
//								e.printStackTrace();
//							}
//						}
//					}
//					
//					
//					Log.i(TAG, "messageCount = " + messageCount);
//					
//					messageCount--;
//					if (messageCount == 0){
//						tvTitle.setText("短信发送");
//					} else {
//						tvTitle.setText("短信发送(" + messageCount + ")");
//					}
//				}
//			}
//			
//			// 发送完毕
//			etMessage.setText(null);
		}
	};
	
	private boolean validateForSendMessage(){
		
		if(etTelNo.getText().toString() == null || "".equals(etTelNo.getText().toString()) ){
			Log.i(TAG, "电话号码为空！");
			Toast.makeText(SendMessageActivity.this, getString(R.string.tip_telNo),Toast.LENGTH_LONG).show();
			return false;
		}
		
		if(etMessage.getText().toString() == null || "".equals(etMessage.getText().toString()) ){
			Log.i(TAG, "消息内容为空！");
			Toast.makeText(SendMessageActivity.this, getString(R.string.tip_content),Toast.LENGTH_LONG).show();
			return false;
		}
		
		return true;
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
			startActivityForResult(intent, REQUEST_EXDIALOG);
		}
	};
	
	/**
	 * 跳转到设置画面
	 */
	private OnClickListener settingListener = new View.OnClickListener() {
		public void onClick(View v) {
			Log.i(TAG, "跳转到设置画面OnClickListener");
			
			Intent intent = new Intent();  
			
			// 通过Bundle对象存储需要传递的数据
			Bundle bundle = new Bundle();
			// 字符、字符串、布尔、字节数组、浮点数等等，都可以传
			bundle.putString(TEL_NUMBER, etTelNo.getText().toString());
			bundle.putString(MESSAGE, etMessage.getText().toString());
			// 把bundle对象assign给Intent
			intent.putExtras(bundle);
			
			intent.setClass(SendMessageActivity.this, SettingActivity.class);
			
			startActivityForResult(intent, REQUEST_SETTING);
		}
	};
	
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		
		Log.i(TAG, "resultCode = " + resultCode);
		Log.i(TAG, "requestCode = " + requestCode);
		
		if (resultCode == RESULT_OK) {
			
			Log.i(TAG, "onActivityResult  "  + requestCode) ;
			
			switch(requestCode){  
				case REQUEST_EXDIALOG: 
					// 保留从文件选择画面回来的值
					Log.i(TAG, "保留从文件选择画面回来的值");
					
					Uri uri = intent.getData();
					String fullPath = uri.toString();
					String filename = fullPath.substring(fullPath.indexOf(":")+1, fullPath.length());
					File file = new File(filename);
					
					etMessage.setText(FileUtil.readFileByLines(file));
					messageCount = FileUtil.countFileLines(file);
					tvTitle.setText(getString(R.string.sendpage) +  "(" + messageCount + ")");
					
					//Toast.makeText(SendMessageActivity.this, "文件读取成功！",Toast.LENGTH_LONG).show();
					break;
					
				case REQUEST_SETTING:
					// 保留从设置画面回来的值
					Log.i(TAG, "保留从设置画面回来的值");
					
					Bundle bundle = intent.getExtras();
					etTelNo.setText(bundle.getString(TEL_NUMBER));
					etMessage.setText(bundle.getString(MESSAGE));
					break;
					
				default:
					break;
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.send_message_layout, menu);
		return true;
	}
	
}
