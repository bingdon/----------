package tv.luxs.rcassistant;

import java.util.ArrayList;
import java.util.List;

import tv.luxs.adapter.BusinessFraAdapter;
import tv.luxs.rcassistant.frament.BusinessFra;
import tv.luxs.rcassistant.utils.ShareUtlis;

import com.viewpagerindicator.TabPageIndicator;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;

/**
 * 乐商务
 * 
 * @author lyl
 * 
 */
public class LeActivity extends FragmentActivity {

	public static final String[] CONTENT = new String[] { "推荐", "常用", "周边",
			"洗浴", "足疗", "酒店" };

	private List<Fragment> shenagwFragments = new ArrayList<Fragment>();
	private BusinessFra businessFra0;
	private BusinessFra businessFra1;
	private BusinessFra businessFra2;
	private BusinessFra businessFra3;
	private BusinessFra businessFra4;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_le);

		businessFra0 = new BusinessFra(CONTENT[0]);
		businessFra1 = new BusinessFra(CONTENT[1]);
		businessFra2 = new BusinessFra(CONTENT[2]);
		businessFra3 = new BusinessFra(CONTENT[3]);
		businessFra4 = new BusinessFra(CONTENT[4]);

		shenagwFragments.add(businessFra0);
		shenagwFragments.add(businessFra1);
		shenagwFragments.add(businessFra2);
		shenagwFragments.add(businessFra3);
		shenagwFragments.add(businessFra4);
		shenagwFragments.add(new BusinessFra(CONTENT[5]));

		// FragmentPagerAdapter adapter = new
		// GoogleMusicAdapter(getSupportFragmentManager(),shenagwFragments);
		BusinessFraAdapter adapter2 = new BusinessFraAdapter(
				getSupportFragmentManager(), shenagwFragments, CONTENT);

		ViewPager pager = (ViewPager) findViewById(R.id.pager);
		pager.setAdapter(adapter2);

		TabPageIndicator indicator = (TabPageIndicator) findViewById(R.id.indicator);
		indicator.setViewPager(pager);

		if (!MainActivityt.maint) {
			setMenu();
		}
		
	}

	class GoogleMusicAdapter extends FragmentPagerAdapter {
		private List<BusinessFra> shenagwFragments;

		public GoogleMusicAdapter(FragmentManager fm,
				List<BusinessFra> shenagwFragments) {
			super(fm);
			this.shenagwFragments = shenagwFragments;
		}

		@Override
		public Fragment getItem(int position) {

			return shenagwFragments.get(position);
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return CONTENT[position % CONTENT.length].toUpperCase();
		}

		@Override
		public int getCount() {
			return shenagwFragments.size();
		}
	}

	/**
	 * 按钮点击事件
	 * 
	 * @param view
	 */
	public void doClick(View view) {
		switch (view.getId()) {
		case R.id.le_business_search_img:
			// Toast.makeText(LeActivity.this, "搜索", Toast.LENGTH_LONG).show();
			onSearchRequested();
			// Utils.toActivity(LeActivity.this, MySearchActivity.class);
			break;
		default:
			break;
		}
	}

	@Override
	public boolean onSearchRequested() {
		// TODO Auto-generated method stub
		startSearch(null, false, null, false);
		return true;
	}

	private void setMenu() {
		ImageView menu = (ImageView)findViewById(R.id.menu_img);
		MainActivity.popupMenus[3] = new PopupMenu(LeActivity.this, menu);
		MainActivity.popupMenus[3].inflate(R.menu.le_more);
		MainActivity.popupMenus[3].setOnMenuItemClickListener(new OnMenuItemClickListener() {
			
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				// TODO Auto-generated method stub
				Intent intent=new Intent();
				switch (item.getItemId()) {
				case R.id.le_more:
					intent.setClass(LeActivity.this, MoreActivity.class);
					startActivity(intent);
					break;
					
				case R.id.le_rc:
					intent.setClass(LeActivity.this, MainActivityt.class);
					intent.putExtra("main", 0);
					startActivity(intent);
					break;

				case R.id.le_push:

					intent.setClass(LeActivity.this,
							MainActivityt.class);
					intent.putExtra("main", 1);
					startActivity(intent);
					break;
					
				case R.id.leshare:
					
					ShareUtlis.share2Fre(LeActivity.this);
					
					break;	
					
				default:
					break;
				}
				return false;
			}
		});
		
		
		menu.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MainActivity.popupMenus[3].show();
			}
		});
		
	}

}
