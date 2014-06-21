package tv.luxs.rcassistant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tv.luxs.adapter.YouhuiAdapter;
import tv.luxs.imgutils.ImageFetcher;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 详情
 * 
 * @author lyl
 * 
 */
public class BusinessInfoActivity extends Activity implements
		OnItemClickListener {

	// 列表
	private ListView businessListView;
	// 标题
	private TextView titleTextView;
	// 内容容器
	private YouhuiAdapter infoAdapter;
	// 信息数据
	private List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
	// 标志
	private LayoutInflater inflater;
	// listview头
	private View headView;
	// 加载图片
	private ImageFetcher imageFetcher;

	private String flagsr = "";

	private String headurl = "";

	private TextView addressTextView;

	private TextView phoneTextView;

	private String addressString = "";

	private String phonenumString = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_business_info);
		initView();
	}

	/**
	 * 初始化
	 */
	private void initView() {

		flagsr = getIntent().getStringExtra("style");
		headurl = getIntent().getStringExtra("head");
		addressString = getIntent().getStringExtra("address");
		phonenumString = getIntent().getStringExtra("phonenum");

		imageFetcher = new ImageFetcher(this, 100);
		imageFetcher.setLoadingImage(R.drawable.empty_photo);
		inflater = getLayoutInflater();
		headView = inflater.inflate(R.layout.activity_le_business_head_info,
				null);

		addressTextView = (TextView) headView
				.findViewById(R.id.business_head_address_txt);
		phoneTextView = (TextView) headView
				.findViewById(R.id.business_head_phone_txt);

		if (null != addressString) {
			addressTextView.setText(addressString);
		}

		if (null != phonenumString) {
			phoneTextView.setText(phonenumString);
		}

		businessListView = (ListView) findViewById(R.id.business_info_list_v);
		businessListView.addHeaderView(headView);
		imageFetcher.loadImage(headurl, (ImageView) headView
				.findViewById(R.id.business_head_info_hotel_img));
		titleTextView = (TextView) findViewById(R.id.business_info_title_txt);

		Intent intent = getIntent();
		String name = intent.getStringExtra("name");

		titleTextView.setText("" + name);

		infoAdapter = new YouhuiAdapter(BusinessInfoActivity.this, list);
		businessListView.setAdapter(infoAdapter);
		new Thread(loadDataRunnable).start();

		businessListView.setOnItemClickListener(this);

	}

	private Runnable loadDataRunnable = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			if (list.size() == 0) {
				loadData();
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
				infoAdapter.notifyDataSetChanged();
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

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		intent.putExtra("name", list.get(position - 1).get("uptxt").toString());
		intent.putExtra("imgurl", list.get(position - 1).get("imgurl")
				.toString());
		intent.putExtra(
				"summary",
				"		权金城•休闲家园是权品品牌管"
				+ "理公司旗下沐浴产业最为资深的品牌，"
				+ "是韩式风格的时尚洗浴连锁品牌。现代沐浴概念"
				+ "，融合了传统的韩式元素，温馨典雅的设计、韩"
				+ "国进口的洗浴设备、时尚舒适的空间布局，集餐饮"
				+ "、住宿、保健、棋牌、健身、专业韩式按摩等多功"
				+ "能一体的富于时尚气息的健康休闲方式，广受顾客"
				+ "的青睐。权品休闲家园正成为中高档消费群体，家之"
				+ "外的第一休闲选择。");
		intent.setClass(BusinessInfoActivity.this, BusinessInfo2Activity.class);
		startActivity(intent);
	}

	private void loadData() {
		if (flagsr.equals("")) {
			for (int i = 0; i < 10; i++) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("uptxt", "包厢" + i);
				map.put("downtxt", "晚餐可预订");
				map.put("rmb", "100$");
				map.put("imgurl",
						"http://www.thevalleyresort.com/upload/picfiles/20130228175238900.jpg");
				list.add(map);
			}
		} else if (flagsr.equals("推荐")) {

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("uptxt", "庭院标间");
			map.put("downtxt", "晚餐可预订");
			map.put("rmb", "RMB 1800++");
			map.put("imgurl",
					"http://www.thevalleyresort.com/upload/picfiles/20130228175238900.jpg");
			list.add(map);

			Map<String, Object> map1 = new HashMap<String, Object>();
			map1.put("uptxt", "观景标间");
			map1.put("downtxt", "晚餐可预订");
			map1.put("rmb", "RMB 1800++");
			map1.put("imgurl",
					"http://www.thevalleyresort.com/upload/picfiles/20130228175353911.jpg");
			list.add(map1);

			Map<String, Object> map2 = new HashMap<String, Object>();
			map2.put("uptxt", "空中套房");
			map2.put("downtxt", "晚餐可预订");
			map2.put("rmb", "RMB 2200++");
			map2.put("imgurl",
					"http://www.thevalleyresort.com/upload/picfiles/20130228175523569.jpg");
			list.add(map2);

			Map<String, Object> map3 = new HashMap<String, Object>();
			map3.put("uptxt", "行政套房");
			map3.put("downtxt", "晚餐可预订");
			map3.put("rmb", "RMB 3300++");
			map3.put("imgurl",
					"http://www.thevalleyresort.com/upload/picfiles/20130228174705893.jpg");
			list.add(map3);

		} else if (flagsr.equals("常用")) {
			Map<String, Object> map4 = new HashMap<String, Object>();
			map4.put("uptxt", "权品庄园");
			map4.put("downtxt", "晚餐可预订");
			map4.put("rmb", "RMB 1800++");
			map4.put(
					"imgurl",
					"http://www.championbrand.com/imgdw/xiuixan/%E6%9D%83%E5%93%81%E5%BA%84%E5%9B%AD.jpg");
			list.add(map4);

			Map<String, Object> map5 = new HashMap<String, Object>();
			map5.put("uptxt", "庄园");
			map5.put("downtxt", "晚餐可预订");
			map5.put("rmb", "RMB 1800++");
			map5.put("imgurl",
					"http://www.thevalleyresort.com/upload/picfiles/20130228175353911.jpg");
			list.add(map5);
		} else if (flagsr.equals("洗浴")) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("uptxt", "客房");
			map.put("downtxt", "晚餐可预订");
			map.put("rmb", "500$");
			map.put("imgurl", "http://s.luxs.tv:8070/luxs/QPF_05.png");
			list.add(map);
			Map<String, Object> map1 = new HashMap<String, Object>();
			map1.put("uptxt", "总统套房");
			map1.put("downtxt", "晚餐可预订");
			map1.put("rmb", "500$");
			map1.put("imgurl", "http://s.luxs.tv:8070/luxs/QPG_01.png");

			list.add(map1);
			Map<String, Object> map2 = new HashMap<String, Object>();
			map2.put("uptxt", "套房");
			map2.put("downtxt", "晚餐可预订");
			map2.put("rmb", "500$");
			map2.put("imgurl", "http://s.luxs.tv:8070/luxs/QPF_01.png");

			list.add(map2);
		}
		// switch (flagsr) {
		// case "":
		// for (int i = 0; i < 10; i++) {
		// Map<String, Object> map = new HashMap<String, Object>();
		// map.put("uptxt", "包厢" + i);
		// map.put("downtxt", "晚餐可预订");
		// map.put("rmb", "100$");
		// map.put("imgurl",
		// "http://www.thevalleyresort.com/upload/picfiles/20130228175238900.jpg");
		// list.add(map);
		// }
		// break;
		//
		// case "推荐":
		// Map<String, Object> map = new HashMap<String, Object>();
		// map.put("uptxt", "庭院标间");
		// map.put("downtxt", "晚餐可预订");
		// map.put("rmb", "RMB 1800++");
		// map.put("imgurl",
		// "http://www.thevalleyresort.com/upload/picfiles/20130228175238900.jpg");
		// list.add(map);
		//
		// Map<String, Object> map1 = new HashMap<String, Object>();
		// map1.put("uptxt", "观景标间");
		// map1.put("downtxt", "晚餐可预订");
		// map1.put("rmb", "RMB 1800++");
		// map1.put("imgurl",
		// "http://www.thevalleyresort.com/upload/picfiles/20130228175353911.jpg");
		// list.add(map1);
		//
		// Map<String, Object> map2 = new HashMap<String, Object>();
		// map2.put("uptxt", "空中套房");
		// map2.put("downtxt", "晚餐可预订");
		// map2.put("rmb", "RMB 2200++");
		// map2.put("imgurl",
		// "http://www.thevalleyresort.com/upload/picfiles/20130228175523569.jpg");
		// list.add(map2);
		//
		// Map<String, Object> map3 = new HashMap<String, Object>();
		// map3.put("uptxt", "行政套房");
		// map3.put("downtxt", "晚餐可预订");
		// map3.put("rmb", "RMB 3300++");
		// map3.put("imgurl",
		// "http://www.thevalleyresort.com/upload/picfiles/20130228174705893.jpg");
		// list.add(map3);
		//
		// break;
		//
		// case "常用":
		//
		// Map<String, Object> map4 = new HashMap<String, Object>();
		// map4.put("uptxt", "权品庄园");
		// map4.put("downtxt", "晚餐可预订");
		// map4.put("rmb", "RMB 1800++");
		// map4.put(
		// "imgurl",
		// "http://www.championbrand.com/imgdw/xiuixan/%E6%9D%83%E5%93%81%E5%BA%84%E5%9B%AD.jpg");
		// list.add(map4);
		//
		// Map<String, Object> map5 = new HashMap<String, Object>();
		// map5.put("uptxt", "庄园");
		// map5.put("downtxt", "晚餐可预订");
		// map5.put("rmb", "RMB 1800++");
		// map5.put("imgurl",
		// "http://www.thevalleyresort.com/upload/picfiles/20130228175353911.jpg");
		// list.add(map5);
		//
		//
		// break;
		//
		// default:
		// break;
		// }

	}

}
