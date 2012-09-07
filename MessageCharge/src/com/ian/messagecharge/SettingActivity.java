package com.ian.messagecharge;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SettingActivity extends Activity {

	/** Called when the activity is first created. */
	private Button btnSave;
	private Button btnClear;
	private Button btnBack;
	
	private EditText etCenterTelNo;
	private EditText etPrefix;
	private EditText etDelay;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting_layout);
		
		etCenterTelNo = (EditText) this.findViewById(R.id.etCenterTelNo);
		etPrefix = (EditText) this.findViewById(R.id.etPrefix);
		etDelay = (EditText) this.findViewById(R.id.etDelay);
		
		// 保存配置按钮
		btnSave = (Button) this.findViewById(R.id.btnSave);
		btnSave.setOnClickListener(saveListener);

		// 清除配置按钮
		btnClear = (Button) this.findViewById(R.id.btnClear);
		btnClear.setOnClickListener(clearListener);

		// 返回配置按钮
		btnBack = (Button) this.findViewById(R.id.btnBack);
		btnBack.setOnClickListener(jumpToSendMessageListener);
		
		// 读取配置文件的值
		SharedPreferences share = SettingActivity.this.getSharedPreferences("perference", MODE_PRIVATE);
		etCenterTelNo.setText(share.getString("centerTelNo", ""));
		etPrefix.setText(share.getString("prefix", ""));
		etDelay.setText(share.getString("delay", ""));

	}
	
	private OnClickListener saveListener = new View.OnClickListener() {
		public void onClick(View v) {
			
			// 短信中心号码
			String centerTelNo = etCenterTelNo.getText().toString();
			// 信息前缀
			String prefix = etPrefix.getText().toString();
			// 发送间隔
			String delay = etDelay.getText().toString();
			
			SharedPreferences share = SettingActivity.this.getSharedPreferences("perference", MODE_PRIVATE);
			// 得到配置参数的类 参数1 配置参数文件的名字,没有后缀名 参数2 文件访问模式 只能是生成这个文件的应用访问
			// 取得编辑器
			Editor editor = share.edit();
			// 存储配置 参数1 是key 参数2 是值
			editor.putString("centerTelNo", centerTelNo.trim());
			editor.putString("prefix", prefix.trim());
			editor.putString("delay", delay.trim());
			// 提交刷新数据
			editor.commit();
			
			// 保存成功提示消息
			Toast.makeText(SettingActivity.this, "设置成功",Toast.LENGTH_LONG).show();

		}
	};
	
	private OnClickListener clearListener = new View.OnClickListener() {
		public void onClick(View v) {
			
			etCenterTelNo.setText(null);
			etPrefix.setText(null);
			etDelay.setText(null);
			
			SharedPreferences share = SettingActivity.this.getSharedPreferences("perference", MODE_PRIVATE);

			Editor editor = share.edit();
			editor.putString("centerTelNo", "");
			editor.putString("prefix", "");
			editor.putString("delay", "");
			editor.commit();
			
			Toast.makeText(SettingActivity.this, "清除成功",Toast.LENGTH_LONG).show();
		}
	};

	/**
	 * 跳转到设置画面
	 */
	private OnClickListener jumpToSendMessageListener = new View.OnClickListener() {
		public void onClick(View v) {
			Intent intent = new Intent();  
			intent.setClass(SettingActivity.this, SendMessageActivity.class);
			startActivity(intent);
		}
	};
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.send_message_layout, menu);
		return true;
	}
}
