package tv.luxs.rcassistant.frament;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tv.luxs.adapter.CaidanAdapter;
import tv.luxs.config.MyCaiDan;
import tv.luxs.luxinterface.CaidanListener;
import tv.luxs.rcassistant.R;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class CaidanFra extends Fragment implements OnItemClickListener, CaidanListener {

	private ListView listView;
	
	private CaidanAdapter caidanAdapter;
	
	private List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View rootView=inflater.inflate(R.layout.pager_shangwu_, null);
		
		listView = (ListView) rootView.findViewById(R.id.le_shangwu_list_v);
		caidanAdapter = new CaidanAdapter(list, getActivity());
		listView.setAdapter(caidanAdapter);
		listView.setCacheColorHint(Color.TRANSPARENT);
		listView.setOnItemClickListener(this);
		
		caidanAdapter.setListener(this);
	
		return rootView;
	}
	
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		new Thread(loadDataRunnable).start();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void selectbing(int position) {
		// TODO Auto-generated method stub
		MyCaiDan.caidanList.add(0);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("right", "已添加");
		list.set(position, map);
		caidanAdapter.notifyDataSetChanged();
	}
	
	private void getMyDate() {
			for (int i = 0; i < 10; i++) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("right", "添加");
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
				// daijinquanlistView.removeHeaderView(loadingView);
				caidanAdapter.notifyDataSetChanged();
				break;

			default:
				break;
			}

		};
	};
	
	
}
