package tv.luxs.rcassistant.frament;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tv.luxs.adapter.YouhuiAdapter;
import tv.luxs.rcassistant.BusinessInfoActivity;
import tv.luxs.rcassistant.LeActivity;
import tv.luxs.rcassistant.R;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

/**
 * 乐商务列表
 * 
 * @author lyl
 * 
 */
public class BusinessFra extends Fragment implements OnItemClickListener {

	private ListView listView;

	private YouhuiAdapter BusinessAdapter;

	private String BUSINESS_FLAG = "";

	private List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

	public BusinessFra(String BUSINESS_FLAG) {
		this.BUSINESS_FLAG = BUSINESS_FLAG;
//		Log.i("BusinessFra", "=======onActivityCreated======="+BUSINESS_FLAG);
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View rootView = inflater.inflate(R.layout.pager_shangwu_, null);
		listView = (ListView) rootView.findViewById(R.id.le_shangwu_list_v);
		BusinessAdapter = new YouhuiAdapter(getActivity(), list);
		listView.setAdapter(BusinessAdapter);
		listView.setCacheColorHint(Color.TRANSPARENT);
		listView.setOnItemClickListener(this);

		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);

		Log.i("BusinessFra", "=======onActivityCreated======="+BUSINESS_FLAG);
		if (list.size() == 0) {
			new Thread(loadDataRunnable).start();
		}

	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		
	}

	private void getMyDate() {
		if (BUSINESS_FLAG.equals("")) {
			for (int i = 0; i < 10; i++) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("uptxt", "酒店" + i);
				map.put("downtxt", "三里屯");
				map.put("rmb", "点评");
				map.put("imgurl",
						"http://www.championbrand.com/common/images/theme/default/kfrx207.gif");
				list.add(map);
			}
		} else if (BUSINESS_FLAG.equals("推荐")) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("uptxt", "权品机构");
			map.put("downtxt", "常春藤");
			map.put("rmb", "点评");
			map.put("imgurl",
					"http://www.championbrand.com/gyqp/common/upload/2013/03/26/1756216b.gif");
			list.add(map);
		} else if (BUSINESS_FLAG.equals("常用")) {
			Map<String, Object> map1 = new HashMap<String, Object>();
			map1.put("uptxt", "权品公馆");
			map1.put("downtxt", "海淀区");
			map1.put("rmb", "点评");
			map1.put("imgurl",
					"http://www.championbrand.com/imgdw/qxpp_img/qpgg.gif");
			list.add(map1);
		}else if (BUSINESS_FLAG.equals(LeActivity.CONTENT[2])) {
			
		}else if (BUSINESS_FLAG.equals("洗浴")) {
			Map<String, Object> map=new HashMap<String, Object>();
			map.put("uptxt", "权金城休闲家园");
			map.put("downtxt", "昆明湖南路9号");
			map.put("rmb", "点评");
			map.put("imgurl",
					"http://s.luxs.tv:8070/luxs/QPF_03.png");
			list.add(map);
		}else if (BUSINESS_FLAG.equals(LeActivity.CONTENT[4])) {
			
		}else if (BUSINESS_FLAG.equals(LeActivity.CONTENT[5])) {
			
		}
		// switch (BUSINESS_FLAG) {
		// case "":
		// for (int i = 0; i < 10; i++) {
		// Map<String, Object> map = new HashMap<String, Object>();
		// map.put("uptxt", "酒店" + i);
		// map.put("downtxt", "三里屯");
		// map.put("rmb", "点评");
		// map.put("imgurl",
		// "http://www.championbrand.com/common/images/theme/default/kfrx207.gif");
		// list.add(map);
		// }
		// break;
		//
		// case "推荐":
		//
		// Map<String, Object> map = new HashMap<String, Object>();
		// map.put("uptxt", "权品机构");
		// map.put("downtxt", "常春藤");
		// map.put("rmb", "点评");
		// map.put("imgurl",
		// "http://www.championbrand.com/gyqp/common/upload/2013/03/26/1756216b.gif");
		// list.add(map);
		//
		// break;
		//
		// case "常用":
		//
		// Map<String, Object> map1 = new HashMap<String, Object>();
		// map1.put("uptxt", "权品公馆");
		// map1.put("downtxt", "海淀区");
		// map1.put("rmb", "点评");
		// map1.put("imgurl",
		// "http://www.championbrand.com/imgdw/qxpp_img/qpgg.gif");
		// list.add(map1);
		//
		// break;
		//
		//
		// default:
		// break;
		// }
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
				// daijinquanlistView.removeHeaderView(loadingView);
				BusinessAdapter.notifyDataSetChanged();
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
		intent.putExtra("name", list.get(position).get("uptxt").toString());
		intent.putExtra("style", BUSINESS_FLAG);
		intent.putExtra("head", list.get(position).get("imgurl").toString());
		if (BUSINESS_FLAG.equals("洗浴")) {
			intent.putExtra("address", "地址：北京市海淀区昆明湖南路9号");
			intent.putExtra("phonenum", "电话：010-88473311");
		}
		intent.setClass(getActivity(), BusinessInfoActivity.class);
		startActivity(intent);
	}

}
