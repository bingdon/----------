package tv.luxs.rcassistant;

import java.util.ArrayList;
import java.util.List;



import com.viewpagerindicator.TabPageIndicator;

import tv.luxs.adapter.BusinessFraAdapter;
import tv.luxs.rcassistant.frament.CaidanFra;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;

/**
 * 菜单
 * 
 * @author lyl
 * 
 */
public class CaiDanActivity extends FragmentActivity {

	private ViewPager pager;
	private BusinessFraAdapter adapter;
	private TabPageIndicator indicator;
	private Button orderfood;
	private List<Fragment> caidanFragments = new ArrayList<Fragment>();
	private CaidanFra caidanFra;
	private CaidanFra caidanFra2;
	private CaidanFra caidanFra3;
	private String[] caiString=new String[] { "川菜", "粤菜", "西洋" };

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_caidan);
		initView();
	}

	private void initView() {
		
		caidanFra=new CaidanFra();
		caidanFra2=new CaidanFra();
		caidanFra3=new CaidanFra();
		caidanFragments.add(caidanFra);
		caidanFragments.add(caidanFra2);
		caidanFragments.add(caidanFra3);
		
		pager = (ViewPager) findViewById(R.id.pager);
		adapter = new BusinessFraAdapter(getSupportFragmentManager(),
				caidanFragments,caiString);
		pager.setAdapter(adapter);
		indicator = (TabPageIndicator) findViewById(R.id.indicator);
		indicator.setViewPager(pager);
		
		orderfood=(Button)findViewById(R.id.caidan_btn);
		orderfood.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent();
				intent.setClass(CaiDanActivity.this, OrderFood.class);
				startActivity(intent);
			}
		});
	}

}
