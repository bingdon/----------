package tv.luxs.rcassistant;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class PicFra extends Fragment {

	private int resId=0;
	
	public PicFra(int resid){
		this.resId=resid;
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View rootView=inflater.inflate(R.layout.pager_frame, null);
		ImageView imageView = (ImageView) rootView.findViewById(R.id.pager_push_img);
		if (resId!=0) {
			imageView.setImageResource(resId);
		}else {
			imageView.setImageResource(R.drawable.hotel);
		}
		
		return rootView;
	}
	
	
	
}
