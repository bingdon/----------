package tv.luxs.rcassistant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tv.luxs.adapter.YouhuiAdapter;
import tv.luxs.config.G;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.GestureDetector.OnGestureListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.ViewFlipper;

public class LeVipActivity extends Activity implements OnClickListener {

	private Context context;
	private Activity activity;

	// 滑动变量
	private GestureDetector detector;
	// 滑动View
	private ViewFlipper flipper;
	// 滑动tab
	private ImageView tabLineImgV;
	// 优惠标签
	private Button tabyouhui;
	// 会员中心标签
	private Button tabcenter;
	// 支付标签
	private Button tabpay;
	// 优惠界面
	private View youhuiView;
	// 中心界面
	private View vipcenterView;
	// 支付界面
	private View payView;
	// 当前位置
	private float currentX = 0;
	// 优惠位置
	private float youthuiX = 0;
	// 会员中心位置
	private float centerX = 0;
	// 支付位置
	private float payX = 0;
	// 详细信息
	private RelativeLayout infoLayout;
	// 我的预定
	private RelativeLayout userBookLayout;
	// 消费记录
	private RelativeLayout pastLayout;
	// 代金券
	private RelativeLayout daijinquanLayout;
	// 优惠容器
	private YouhuiAdapter youhuiAdapter;
	// 优惠列表
	private ListView youhuiListView;
	// 优惠数据
	private List<Map<String, Object>> youhuiList = new ArrayList<Map<String, Object>>();
	// 支付容器
	private YouhuiAdapter payAdapter;
	// 支付列表
	private ListView payListView;
	// 支付数据
	private List<Map<String, Object>> payhuiList = new ArrayList<Map<String, Object>>();
	// 加载试图
	private View loadingView;

	private String[] zhifuStrings = { "待处理账单", "历史记录账单", "支付方式设置" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_le_vip);
		initView();
		setMenu();
	}

	private void initView() {
		context = this;
		detector = new GestureDetector(context, vipGestureListener);

		flipper = (ViewFlipper) findViewById(R.id.le_vip_flipper);
		tabLineImgV = (ImageView) findViewById(R.id.le_vip_tabCurrent);
		tabyouhui = (Button) findViewById(R.id.le_vip_tabyouhui);
		tabcenter = (Button) findViewById(R.id.le_vip_tabvip_center);
		tabpay = (Button) findViewById(R.id.le_vip_table_pay);

		Display mDisplay = getWindowManager().getDefaultDisplay();
		int width = mDisplay.getWidth();
		LayoutParams params = (LayoutParams) tabLineImgV.getLayoutParams();
		float distance = width / 3;
		params.width = (int) (distance);
		tabLineImgV.setLayoutParams(params);

		centerX = distance * 1;
		payX = distance * 2;

		LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		youhuiView = inflater.inflate(R.layout.flipper_youhui, null);
		vipcenterView = inflater.inflate(R.layout.flipper_vip_center, null);
		payView = inflater.inflate(R.layout.flipper_youhui, null);
		loadingView = inflater.inflate(R.layout.list_loading, null);

		youhuiView.setId(G.YOUHUI_TAG);
		vipcenterView.setId(G.VIP_CENTER_TAG);
		payView.setId(G.LE_PAY_TAG);

		flipper.addView(youhuiView);
		flipper.addView(vipcenterView);
		flipper.addView(payView);

		// flipper.setDisplayedChild(G.VIP_CENTER_TAG);

		tabyouhui.setOnClickListener(this);
		tabcenter.setOnClickListener(this);
		tabpay.setOnClickListener(this);

		infoLayout = (RelativeLayout) vipcenterView
				.findViewById(R.id.vip_info_layout);
		userBookLayout = (RelativeLayout) vipcenterView
				.findViewById(R.id.user_book_layout);
		pastLayout = (RelativeLayout) vipcenterView
				.findViewById(R.id.vip_past_layout);
		daijinquanLayout = (RelativeLayout) vipcenterView
				.findViewById(R.id.vip_daijinquan_layout);

		infoLayout.setOnClickListener(this);
		userBookLayout.setOnClickListener(this);
		pastLayout.setOnClickListener(this);
		daijinquanLayout.setOnClickListener(this);

		youhuiListView = (ListView) youhuiView
				.findViewById(R.id.le_huiyuan_list);
		youhuiListView.setCacheColorHint(Color.TRANSPARENT);
		youhuiAdapter = new YouhuiAdapter(context, youhuiList);
		youhuiListView.setAdapter(youhuiAdapter);

		payListView = (ListView) payView.findViewById(R.id.le_huiyuan_list);
		payListView.setCacheColorHint(Color.TRANSPARENT);
		payAdapter = new YouhuiAdapter(context, payhuiList);
		payListView.setAdapter(payAdapter);

		payListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				enterActivity(position);
			}
		});

		new Thread(loadDataRunnable).start();

	}

	private Handler loadDataHandler = new Handler() {
		public void handleMessage(Message msg) {
			int what = msg.what;
			switch (what) {
			case G.YOUHUI_TAG:
				youhuiListView.removeHeaderView(loadingView);
				youhuiAdapter.notifyDataSetChanged();
				break;

			case G.LE_PAY_TAG:
				youhuiListView.removeHeaderView(loadingView);
				payAdapter.notifyDataSetChanged();
				break;

			default:
				break;
			}

		};
	};

	private OnGestureListener vipGestureListener = new OnGestureListener() {

		@Override
		public boolean onSingleTapUp(MotionEvent e) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void onShowPress(MotionEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2,
				float distanceX, float distanceY) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void onLongPress(MotionEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			// TODO Auto-generated method stub

			Log.i("bing", "当前标签:" + flipper.getCurrentView().getId());
			Log.i("bing", "当距离:" + (e1.getX() - e2.getX()));

			if (e1.getX() > (e2.getX() + 100)) {
				flipper.setInAnimation(AnimationUtils.loadAnimation(
						LeVipActivity.this, R.anim.left_in));
				flipper.setOutAnimation(AnimationUtils.loadAnimation(
						LeVipActivity.this, R.anim.left_out));
				flipper.showNext();
				View view = flipper.getCurrentView();

				switch (view.getId()) {
				case G.YOUHUI_TAG:

					if (youhuiList.size() == 0) {
						youhuiListView.addFooterView(loadingView);
						new Thread(loadDataRunnable).start();
					}

					anim(tabLineImgV, currentX, youthuiX, 0f, 0f);

					break;
				case G.VIP_CENTER_TAG:
					anim(tabLineImgV, currentX, centerX, 0f, 0f);

					break;
				case G.LE_PAY_TAG:

					if (payhuiList.size() < 1) {
						payListView.addFooterView(loadingView);
						new Thread(loadDataRunnable).start();
					}

					anim(tabLineImgV, currentX, payX, 0f, 0f);

					break;
				default:
					break;
				}

				return true;
			} else if (e1.getX() < (e2.getX() - 100)) {
				flipper.setInAnimation(AnimationUtils.loadAnimation(
						LeVipActivity.this, R.anim.right_in));
				flipper.setOutAnimation(AnimationUtils.loadAnimation(
						LeVipActivity.this, R.anim.right_out));
				flipper.showPrevious();
				View view = flipper.getCurrentView();
				switch (view.getId()) {
				case G.YOUHUI_TAG:
					anim(tabLineImgV, currentX, youthuiX, 0f, 0f);

					break;
				case G.VIP_CENTER_TAG:
					anim(tabLineImgV, currentX, centerX, 0f, 0f);

					break;
				case G.LE_PAY_TAG:
					anim(tabLineImgV, currentX, payX, 0f, 0f);

					break;
				default:
					break;
				}

				return true;
			}

			return false;
		}

		@Override
		public boolean onDown(MotionEvent e) {
			// TODO Auto-generated method stub
			return false;
		}

	};

	/**
	 * 
	 * @param view
	 *            变化的View
	 * @param x1
	 *            开始x坐标
	 * @param x2
	 *            结束x坐标
	 * @param y1
	 *            开始y坐标
	 * @param y2
	 *            结束y坐标
	 */
	private void anim(View view, float x1, float x2, float y1, float y2) {
		Animation translateAnimation = new TranslateAnimation(x1, x2, y1, y2);
		translateAnimation.setDuration(500);
		translateAnimation.setFillAfter(true);
		view.startAnimation(translateAnimation);
		currentX = x2;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		super.onTouchEvent(event);
		return detector.onTouchEvent(event);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		detector.onTouchEvent(ev);
		super.dispatchTouchEvent(ev);
		return true;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int id = v.getId();
		switch (id) {
		case R.id.le_vip_tabyouhui:
			anim(tabLineImgV, currentX, youthuiX, 0f, 0f);
			flipper.setDisplayedChild(G.YOUHUI_TAG);
			break;

		case R.id.le_vip_tabvip_center:
			anim(tabLineImgV, currentX, centerX, 0f, 0f);
			flipper.setDisplayedChild(G.VIP_CENTER_TAG);
			break;

		case R.id.le_vip_table_pay:
			anim(tabLineImgV, currentX, payX, 0f, 0f);
			flipper.setDisplayedChild(G.LE_PAY_TAG);

			if (payhuiList.size() < 1) {
				payListView.addFooterView(loadingView);
				new Thread(loadDataRunnable).start();
			}

			break;

		case R.id.vip_info_layout:

			enterActivity(R.id.vip_info_layout);

			break;

		case R.id.user_book_layout:

			enterActivity(R.id.user_book_layout);

			break;

		case R.id.vip_past_layout:
			enterActivity(R.id.vip_past_layout);
			break;

		case R.id.vip_daijinquan_layout:
			enterActivity(R.id.vip_daijinquan_layout);
			break;

		default:
			break;
		}

	}

	private Runnable loadDataRunnable = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			int id = flipper.getCurrentView().getId();
			switch (id) {
			case G.YOUHUI_TAG:
				for (int i = 0; i < 10; i++) {
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("uptxt", "酒店" + i);
					map.put("downtxt", i);
					youhuiList.add(map);
				}

				Message message = new Message();
				message.what = G.YOUHUI_TAG;
				loadDataHandler.sendMessage(message);
				break;

			case G.LE_PAY_TAG:

				for (int i = 0; i < zhifuStrings.length; i++) {
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("uptxt", zhifuStrings[i]);
					payhuiList.add(map);
				}

				Message messagepay = new Message();
				messagepay.what = G.LE_PAY_TAG;
				loadDataHandler.sendMessage(messagepay);

				break;

			default:
				break;
			}

		}
	};

	/**
	 * Activity跳转
	 * 
	 * @param id
	 *            Activity id
	 */
	private void enterActivity(int id) {

		Intent intent = new Intent();

		switch (id) {

		case R.id.vip_info_layout:
			intent.setClass(LeVipActivity.this, UserInfoActivity.class);
			break;

		case R.id.user_book_layout:
			intent.setClass(LeVipActivity.this, UserBookActivity.class);
			break;

		case R.id.vip_past_layout:
			intent.setClass(LeVipActivity.this, UserPastRecored.class);
			break;

		case R.id.vip_daijinquan_layout:
			intent.setClass(LeVipActivity.this, DaiJinquanActivity.class);
			break;

		case 0:
			intent.setClass(LeVipActivity.this, WaitDetctOrder.class);
			break;

		case 1:
			intent.setClass(LeVipActivity.this, HistoryOrderActivity.class);
			break;

		case 2:
			intent.setClass(LeVipActivity.this, PayStyleActivity.class);
			break;

		default:
			break;
		}

		startActivity(intent);

	}

	@SuppressLint("NewApi")
	private void setMenu() {
		ImageView menu = (ImageView) findViewById(R.id.menu_img);
		MainActivity.popupMenus[0] = new PopupMenu(LeVipActivity.this, menu);
		MainActivity.popupMenus[0].inflate(R.menu.le_more);
		MainActivity.popupMenus[0]
				.setOnMenuItemClickListener(new OnMenuItemClickListener() {

					@Override
					public boolean onMenuItemClick(MenuItem item) {
						// TODO Auto-generated method stub
						Intent intent = new Intent();
						switch (item.getItemId()) {
						case R.id.le_more:
							intent.setClass(LeVipActivity.this,
									MoreActivity.class);
							startActivity(intent);
							break;

						case R.id.le_rc:
							intent.setClass(LeVipActivity.this, MainActivityt.class);
							intent.putExtra("main", 0);
							startActivity(intent);
							break;

						case R.id.le_push:

							intent.setClass(LeVipActivity.this,
									MainActivityt.class);
							intent.putExtra("main", 1);
							startActivity(intent);
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
				MainActivity.popupMenus[0].show();
			}
		});

	}

}
