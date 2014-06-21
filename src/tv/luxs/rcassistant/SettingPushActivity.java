package tv.luxs.rcassistant;

import tv.luxs.rcassistant.utils.Utils;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class SettingPushActivity extends Activity {
	Context context;
	Activity activity;
	
	ListView listView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting_push);
		initUI();
	}
	
	/**
	 * 初始化UI
	 */
	public void initUI(){
		context = this;
		activity = this;
		listView = (ListView)findViewById(R.id.listView);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context, R.array.setting_push, R.layout.simple_list_item);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new ListOnItemClickListener());
	}
	
	/**
	 * 点击设置列表监听器
	 */
	class ListOnItemClickListener implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
				long arg3) {
			switch (position) {
			case 0:
				Utils.toActivity(activity, SettingPushCommonCountActivity.class);
				break;
			default:
				break;
			}
			
		}
		
	}
	
	/**
	 * 点击事件
	 */
	public void doClick(View view){
		switch (view.getId()) {
		case R.id.btnBack:
			finish();
			break;
		}
	}
}
