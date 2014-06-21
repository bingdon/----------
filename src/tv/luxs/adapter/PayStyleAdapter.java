package tv.luxs.adapter;

import java.util.List;
import java.util.Map;

import tv.luxs.rcassistant.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 支付方式容器
 * 
 * @author lyl
 * 
 */
public class PayStyleAdapter extends BaseAdapter {
	// 数据表
	private List<Map<String, Object>> list;
	// 上下文
	private Context context;
	// 获取布局用
	private LayoutInflater inflater;

	public PayStyleAdapter(Context context, List<Map<String, Object>> list) {
		this.list = list;
		inflater = LayoutInflater.from(context);
		this.context = context;
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
		PayViewHolder holder = null;
		if (convertView == null) {
			holder = new PayViewHolder();

			convertView = inflater.inflate(R.layout.adapter_pay_list, null);
			holder.payCodeTextView = (TextView) convertView
					.findViewById(R.id.pay_style_zhanghao_txt);
			holder.payStyleTextView = (TextView) convertView
					.findViewById(R.id.pay_style_txt);
			holder.payImageView = (ImageView) convertView
					.findViewById(R.id.pay_style_img);
			convertView.setTag(holder);
		} else {
			holder = (PayViewHolder) convertView.getTag();
		}
		
		holder.payStyleTextView.setText(list.get(position).get("paystyle").toString());
		holder.payCodeTextView.setText(list.get(position).get("paycode").toString());
		
		return convertView;
	}

	public class PayViewHolder {
		public ImageView payImageView;
		public TextView payStyleTextView;
		public TextView payCodeTextView;
	}

}
