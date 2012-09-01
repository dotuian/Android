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
	
	private EditText etDelay;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting_layout);
		
		// 保存配置按钮
		btnSave = (Button) this.findViewById(R.id.btnSave);
		btnSave.setOnClickListener(saveListener);

		// 清除配置按钮
		btnClear = (Button) this.findViewById(R.id.btnClear);
		btnClear.setOnClickListener(clearListener);

		// 返回配置按钮
		btnBack = (Button) this.findViewById(R.id.btnBack);
		btnBack.setOnClickListener(jumpToSendMessageListener);
	}
	
	private OnClickListener saveListener = new View.OnClickListener() {
		public void onClick(View v) {
		
			SharedPreferences share = SettingActivity.this.getSharedPreferences("perference", MODE_PRIVATE);

			etDelay = (EditText) SettingActivity.this.findViewById(R.id.delay);
			String name = etDelay.getText().toString();
			
			// 得到配置参数的类 参数1 配置参数文件的名字,没有后缀名 参数2 文件访问模式 只能是生成这个文件的应用访问
			// 取得编辑器
			Editor editor = share.edit();
			// 存储配置 参数1 是key 参数2 是值
			editor.putString("delay", name);
			// 提交刷新数据
			editor.commit();
			
			// 保存成功提示消息
			Toast.makeText(SettingActivity.this, "设置成功！", 1).show();

		}
	};
	
	private OnClickListener clearListener = new View.OnClickListener() {
		public void onClick(View v) {
			
			SharedPreferences share = SettingActivity.this.getSharedPreferences("perference", MODE_PRIVATE);
			etDelay = (EditText) SettingActivity.this.findViewById(R.id.delay);
			etDelay.setText(null);
			
			Editor editor = share.edit();
			editor.putString("delay", "");
			editor.commit();
			
//			 根据key寻找值 
//			 1. 参数1 key ，
//			 2. 参数2，如果没有value显示的内容
//			String name = share.getString("delay", "");
//			String age = share.getString("age", "");
//			result = (TextView) SettingActivity.this.findViewById(R.id.resulttext);
//			result.setText("姓名:" + name + "   年龄:" + age);
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
