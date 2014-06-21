package tv.luxs.rcassistant;

import tv.luxs.rcassistant.utils.ShareUtlis;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.PopupMenu.OnMenuItemClickListener;

/**
 * 乐分享
 * 
 * @author lyl
 * 
 */
public class LeSahre extends Activity {

	private TextView titleTextView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_share);
		initView();
	}

	private void initView() {
		findViewById(R.id.m_back_img_btn).setVisibility(View.INVISIBLE);
		titleTextView = (TextView) findViewById(R.id.title_txt);
		titleTextView.setText(R.string.share);
		findViewById(R.id.main_share_btn).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						ShareUtlis.share2Fre(LeSahre.this);
					}
				});

		setMenu();
	}

	private void setMenu() {
		ImageView menu = (ImageView) findViewById(R.id.menu_img);
		MainActivity.popupMenus[1] = new PopupMenu(LeSahre.this, menu);
		MainActivity.popupMenus[1].inflate(R.menu.le_more);
		MainActivity.popupMenus[1]
				.setOnMenuItemClickListener(new OnMenuItemClickListener() {

					@Override
					public boolean onMenuItemClick(MenuItem item) {
						// TODO Auto-generated method stub
						Intent intent = new Intent();
						switch (item.getItemId()) {
						case R.id.le_more:
							intent.setClass(LeSahre.this, MoreActivity.class);
							startActivity(intent);
							break;

						case R.id.isao:
							intent.setClass(LeSahre.this,
									SettingConnectActivity.class);
							startActivity(intent);
							break;

						case R.id.le_vip_m:

							intent.setClass(LeSahre.this, LeVipActivity.class);
							startActivity(intent);
							break;

						case R.id.leshare:

							ShareUtlis.share2Fre(LeSahre.this);

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
				MainActivity.popupMenus[1].show();
			}
		});

	}

}
