package tv.luxs.adapter;

import java.util.List;
import java.util.Map;

import tv.luxs.luxinterface.CaidanListener;
import tv.luxs.rcassistant.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 菜单内容适配器
 * 
 * @author lyl
 * 
 */
public class CaidanAdapter extends BaseAdapter {

	private List<Map<String, Object>> list;
	private LayoutInflater inflater;
	private Context context;
	private CaidanListener listener;
	private String add="添加";
	private String added="已添加";
	
	
	public CaidanAdapter(List<Map<String, Object>> list, Context context) {
		this.list = list;
		this.context = context;
		inflater = LayoutInflater.from(context);
	}

	public void setListener(CaidanListener listener) {
		this.listener = listener;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	
	
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;

		final int myposition = position;

		if (convertView == null) {

			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.adapter_caidan_list, null);
			holder.caidanImageView = (ImageView) convertView
					.findViewById(R.id.caidan_img);
			holder.caidantitle = (TextView) convertView
					.findViewById(R.id.caidan_title_txt);
			holder.caidanchoiceLayout = (RelativeLayout) convertView
					.findViewById(R.id.caidan_choice_layout);
			holder.preiceinfoTextView = (TextView) convertView
					.findViewById(R.id.caidan_price_txt);
			holder.caidanchoice = (TextView) convertView
					.findViewById(R.id.caidan_choice_txt);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		
		if ( list.get(position).containsKey("right")) {
			holder.caidanchoice.setText(list.get(position).get("right").toString());
		}
		
		
		holder.caidanchoiceLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				listener.selectbing(myposition);
			}
		});

		return convertView;
	}

	private class ViewHolder {
		public ImageView caidanImageView;
		public TextView caidantitle;
		public RelativeLayout caidanchoiceLayout;
		public TextView preiceinfoTextView;
		public TextView caidanchoice;
	}

}
