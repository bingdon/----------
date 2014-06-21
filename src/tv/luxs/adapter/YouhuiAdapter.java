package tv.luxs.adapter;

import java.util.List;
import java.util.Map;

import tv.luxs.imgutils.ImageFetcher;
import tv.luxs.rcassistant.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 适配器
 * 
 * @author lyl
 * 
 */
public class YouhuiAdapter extends BaseAdapter {

	private List<Map<String, Object>> list;
	private LayoutInflater inflater;
	private Context context;
	private ImageFetcher mImageFetcher;

	public YouhuiAdapter(Context context, List<Map<String, Object>> list) {

		this.list = list;
		inflater = LayoutInflater.from(context);
		this.context = context;
		mImageFetcher=new ImageFetcher(context, 80);
		mImageFetcher.setLoadingImage(R.drawable.empty_photo);

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
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.vip_list, null);
			holder.leftImageV = (ImageView) convertView
					.findViewById(R.id.left_img);
			holder.upTextView = (TextView) convertView
					.findViewById(R.id.up_txt);
			holder.dnTextView = (TextView) convertView
					.findViewById(R.id.down_txt);
			holder.ceTextView = (TextView) convertView
					.findViewById(R.id.center_txt);
			holder.cashTextView = (TextView) convertView
					.findViewById(R.id.rmb_txt);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		if (list.get(position).containsKey("downtxt")) {
			holder.dnTextView.setVisibility(View.VISIBLE);
		} else {
			holder.dnTextView.setVisibility(View.GONE);
		}

		if (!list.get(position).containsKey("center")) {
			holder.ceTextView.setVisibility(View.GONE);
		}

		if (list.get(position).containsKey("downtxt")) {
			holder.dnTextView.setText(list.get(position).get("downtxt")
					.toString());
		}

		if (!list.get(position).containsKey("rmb")) {
			holder.cashTextView.setVisibility(View.INVISIBLE);
		}else {
			holder.cashTextView.setText(list.get(position).get("rmb").toString());
		}
		
		
		if (list.get(position).containsKey("imgurl")) {
			mImageFetcher.loadImage(list.get(position).get("imgurl"), holder.leftImageV);
		}
		
		holder.upTextView.setText(list.get(position).get("uptxt").toString());

		return convertView;
	}

	public class ViewHolder {
		public ImageView leftImageV;
		public TextView upTextView;
		public TextView dnTextView;
		public TextView ceTextView;
		public TextView cashTextView;
	}

}
