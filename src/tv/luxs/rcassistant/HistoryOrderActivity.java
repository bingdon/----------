package tv.luxs.rcassistant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tv.luxs.adapter.YouhuiAdapter;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 历史账单
 * 
 * @author lyl
 * 
 */
public class HistoryOrderActivity extends Activity {

	// 标题
	private TextView titleTextView;
	// 历史账单容器
	private YouhuiAdapter historyOdAdapter;
	// 预定数据
	private List<Map<String, Object>> historyOdlist = new ArrayList<Map<String, Object>>();
	// 预定listview显示
	private ListView historyOdlistView;

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
		titleTextView.setText("历史账单");

		historyOdlistView = (ListView) findViewById(R.id.my_list_v);
		historyOdAdapter = new YouhuiAdapter(this, historyOdlist);
		historyOdlistView.setAdapter(historyOdAdapter);
		LayoutInflater inflater = getLayoutInflater();
		// loadingView = inflater.inflate(R.layout.list_loading, null);
		// historyOdlistView.addHeaderView(loadingView);
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
				historyOdlist.add(map);
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
				// historyOdlistView.removeHeaderView(loadingView);
				historyOdAdapter.notifyDataSetChanged();
				break;

			default:
				break;
			}

		};
	};

	/**
	 * 按钮点击事件
	 * 
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
