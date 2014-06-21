package tv.luxs.rcassistant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tv.luxs.adapter.CaidanAdapter;
import tv.luxs.config.MyCaiDan;
import tv.luxs.luxinterface.CaidanListener;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

/**
 * 已点菜单
 * 
 * @author lyl
 * 
 */
public class OrderFood extends Activity implements CaidanListener,
		OnClickListener {

	private List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
	private ListView foodListView;
	private CaidanAdapter yiCaidanAdapter;
	private View footView;
	private Button caidanButton;
	private Spinner paystyleSpinner;
	private ArrayAdapter<String> sadapter;
	 private static final String[] m={"支付宝","微信","网银"};  

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_order_food);
		initView();
	}

	private void initView() {

		LayoutInflater inflater = getLayoutInflater();
		footView = inflater.inflate(R.layout.order_foot_view, null);
		foodListView = (ListView) findViewById(R.id.order_food_list_v);
		foodListView.addFooterView(footView);
		yiCaidanAdapter = new CaidanAdapter(list, OrderFood.this);
		foodListView.setAdapter(yiCaidanAdapter);
		yiCaidanAdapter.setListener(this);
		caidanButton = (Button) findViewById(R.id.diancai_btn);
		caidanButton.setOnClickListener(this);

		paystyleSpinner=(Spinner)footView.findViewById(R.id.pay_style_spinner);
		sadapter=new ArrayAdapter<String>(OrderFood.this, android.R.layout.simple_spinner_item,m);
		sadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		paystyleSpinner.setAdapter(sadapter);
		paystyleSpinner.setVisibility(View.VISIBLE);
		new Thread(loadDataRunnable).start();
	}

	private void getMyDate() {
		for (int i = 0; i < MyCaiDan.caidanList.size(); i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("right", "删除");
			list.add(map);
		}

	}

	private Runnable loadDataRunnable = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			if (list.size() == 0) {
				getMyDate();
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
				yiCaidanAdapter.notifyDataSetChanged();
				break;

			default:
				break;
			}

		};
	};

	@Override
	public void selectbing(int position) {
		// TODO Auto-generated method stub
		MyCaiDan.caidanList.remove(position);
		list.remove(position);
		yiCaidanAdapter.notifyDataSetChanged();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int id = v.getId();
		switch (id) {
		case R.id.diancai_btn:
			finish();
			break;

		default:
			break;
		}
	}

}
