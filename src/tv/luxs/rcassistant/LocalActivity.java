package tv.luxs.rcassistant;

import java.util.ArrayList;
import java.util.List;

import com.viewpagerindicator.CirclePageIndicator;
import com.viewpagerindicator.PageIndicator;

import tv.luxs.adapter.BusinessFraAdapter;
import tv.luxs.rcassistant.utils.FixedSpeedScroller;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.TextView;

import java.lang.reflect.Field;

public class LocalActivity extends FragmentActivity {

	private String[] CONTENT = new String[] { "推荐", "常用", "周边", "洗浴", "足疗",
			"酒店" };
	private List<View> picList = new ArrayList<View>();
	private ViewGroup group;
	private BusinessFraAdapter adapter;
	private ViewPager pager;
	private List<Fragment> picFragments = new ArrayList<Fragment>();
	PageIndicator mIndicator;
	private TextView titleView;
	FixedSpeedScroller mScroller;
	private boolean taskflag=false;
	private int [] resID={R.drawable.jsa,R.drawable.jsb,R.drawable.jsc,R.drawable.jsd,R.drawable.jse};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_local);
		initView();
	}

	private void initView() {
		titleView=(TextView)findViewById(R.id.local_title_txt);
		titleView.setText("权品金城");
		
		for (int i = 0; i < resID.length; i++) {
			picFragments.add(new PicFra(resID[i]));
		}
		pager = (ViewPager) findViewById(R.id.guanggao_pager);
		adapter = new BusinessFraAdapter(getSupportFragmentManager(),
				picFragments, CONTENT);
		pager.setAdapter(adapter);
		mIndicator = (CirclePageIndicator) findViewById(R.id.indicator);
		mIndicator.setViewPager(pager);
		taskflag=true;
		setSpeed();
		AutopicTask mAutopicTask=new AutopicTask();
		mAutopicTask.execute(0);

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		taskflag=false;
	}
	
	public void setSpeed(){
		try {  
            Field mField = ViewPager.class.getDeclaredField("mScroller");  
            mField.setAccessible(true);  
            mScroller = new FixedSpeedScroller(pager.getContext(),  
                    new AccelerateInterpolator());  
            mField.set(pager, mScroller);  
        } catch (Exception e) {  
            e.printStackTrace();  
        } 
	}
	
	
	/**
  	 * 按钮点击事件
  	 * @param view
  	 */
	public void doClick(View view) {
		switch (view.getId()) {
		case R.id.local_back_img_btn:
			finish();
			break;
		default:
			break;
		}
	}
	
	
	class AutopicTask extends AsyncTask<Integer, Integer, Integer>{

		@Override
		protected Integer doInBackground(Integer... params) {
			// TODO Auto-generated method stub
			int id=0;
			
			while (taskflag) {
				
				publishProgress(id);
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				id=params[0]+id+1;
				if (id>pager.getAdapter().getCount()-1) {
					id=0;
					mScroller.setmDuration(0);
					
				}else {
					if (mScroller.getDuration()==0) {
						mScroller.setmDuration(1000);
					}
				}
				
			}
			
			return id;
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			// TODO Auto-generated method stub
			super.onProgressUpdate(values);
			
			pager.setCurrentItem(values[0]);
			
		}
		
	}
	
}
