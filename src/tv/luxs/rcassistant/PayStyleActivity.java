package tv.luxs.rcassistant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tv.luxs.adapter.PayStyleAdapter;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 支付方式
 * 
 * @author lyl
 * 
 */
public class PayStyleActivity extends Activity {

	// 标题
	private TextView titleTextView;
	// 数据显示
	private ListView paysListView;
	// 支付方式数据
	private List<Map<String, Object>> paysList = new ArrayList<Map<String, Object>>();
	// 支付方式容器
	private PayStyleAdapter payStyleAdapter;

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
		titleTextView.setText("支付方式");

		paysListView = (ListView) findViewById(R.id.my_list_v);
		payStyleAdapter = new PayStyleAdapter(this, paysList);
		paysListView.setAdapter(payStyleAdapter);

		new Thread(loadDataRunnable).start();

	}

	private Runnable loadDataRunnable = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("paystyle", "支付宝");
			map.put("payimg", "2014-01-01");
			map.put("paycode", "卡号：000125366");
			paysList.add(map);

			Map<String, Object> map0 = new HashMap<String, Object>();
			map0.put("paystyle", "网银");
			map0.put("payimg", "2014-01-01");
			map0.put("paycode", "账号：000125366");
			paysList.add(map0);

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
				payStyleAdapter.notifyDataSetChanged();
				break;

			default:
				break;
			}

		};
	};

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
