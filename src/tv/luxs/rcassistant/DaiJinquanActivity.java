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
 * 代金券
 * 
 * @author lyl
 * 
 */
public class DaiJinquanActivity extends Activity {

	// 标题
	private TextView titleTextView;
	// 代金券容器
	private YouhuiAdapter daijinquanAdapter;
	// 代金券数据
	private List<Map<String, Object>> daijinquanlist = new ArrayList<Map<String, Object>>();
	// 代金券listview显示
	private ListView daijinquanlistView;
	// 加载试图
	private View loadingView;


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
		titleTextView.setText("代金券");

		daijinquanlistView = (ListView) findViewById(R.id.my_list_v);
		daijinquanAdapter = new YouhuiAdapter(this, daijinquanlist);
		daijinquanlistView.setAdapter(daijinquanAdapter);
		LayoutInflater inflater = getLayoutInflater();
		loadingView = inflater.inflate(R.layout.list_loading, null);
		// daijinquanlistView.addHeaderView(loadingView);
		new Thread(loadDataRunnable).start();

	}

	private Runnable loadDataRunnable = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			for (int i = 0; i < 10; i++) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("uptxt", "酒店" + i);
				map.put("downtxt", "三里屯");
				map.put("downtxt", "有效日期");
				map.put("center", "bing");
				daijinquanlist.add(map);
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
				// daijinquanlistView.removeHeaderView(loadingView);
				daijinquanAdapter.notifyDataSetChanged();
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
