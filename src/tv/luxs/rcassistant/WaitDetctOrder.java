package tv.luxs.rcassistant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tv.luxs.adapter.YouhuiAdapter;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 待处理账单
 * 
 * @author lyl
 * 
 */
public class WaitDetctOrder extends Activity implements OnItemClickListener {

	// 标题
	private TextView titleTextView;
	// 待处理账单容器
	private YouhuiAdapter wOrderAdapter;
	// 待处理账单数据
	private List<Map<String, Object>> wOrderlist = new ArrayList<Map<String, Object>>();
	// 待处理账单listview显示
	private ListView wOrderlistView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_le_center_temp);
		initView();
	}

	private void initView() {
		titleTextView = (TextView) findViewById(R.id.ac_title_txt);
		titleTextView.setText("待处理账单");

		wOrderlistView = (ListView) findViewById(R.id.my_list_v);
		wOrderAdapter = new YouhuiAdapter(this, wOrderlist);
		wOrderlistView.setAdapter(wOrderAdapter);
		wOrderlistView.setOnItemClickListener(this);

		LayoutInflater inflater = getLayoutInflater();
		// loadingView = inflater.inflate(R.layout.list_loading, null);
		// wOrderlistView.addHeaderView(loadingView);
		new Thread(loadDataRunnable).start();

	}

	private Runnable loadDataRunnable = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			for (int i = 0; i < 10; i++) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("uptxt", "酒店" + i);
				map.put("downtxt", "2014-01-01");
				map.put("rmb", "" + i + "000RMB");
				wOrderlist.add(map);
			}

			Message message = new Message();
			message.what = 0;
			loadDataHandler.sendMessage(message);

		}
	};

	private Handler loadDataHandler = new Handler() {
		public void handleMessage(Message msg) {
			int what = msg.what;
			switch (what) {
			case 0:
				// wOrderlistView.removeHeaderView(loadingView);
				wOrderAdapter.notifyDataSetChanged();
				break;

			default:
				break;
			}

		};
	};

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub

		Intent intent = new Intent();
		intent.setClass(WaitDetctOrder.this, OrderInfo.class);
		startActivity(intent);

	}

	
	/**
  	 * 按钮点击事件
  	 * @param view
  	 */
	public void doClick(View view) {
		switch (view.getId()) {
		case R.id.order_back_img_btn:
			finish();
			break;
		default:
			break;
		}
	}
	
}
