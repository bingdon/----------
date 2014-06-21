package cat.projects.mediaplayer_ui;

import java.util.ArrayList;
import java.util.List;

import javax.security.auth.callback.Callback;

import net.zhaidian.file.FileBean;
import net.zhaidian.file.FolderLister;
import tv.luxs.adapter.BusinessFraAdapter;
import tv.luxs.adapter.PageBingAdapter;
import tv.luxs.config.G;
import tv.luxs.rcassistant.PushPagerFra;
import tv.luxs.rcassistant.R;
import tv.luxs.rcassistant.R.drawable;
import tv.luxs.rcassistant.utils.Utils;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import cat.projects.constant.DMCConstants;
import cat.projects.dmc_service.DMCMediaService;

@SuppressLint("SdCardPath")
public class DMCPlayerActivity extends FragmentActivity implements
		OnPageChangeListener {
	private static final int MENU_EXIT = 0xCC882201;

	private static final java.lang.String TAG = "DMCPlayerActivity";

	// http://192.168.2.126:8080/

	private boolean FLAG = false;
	boolean inServer = false;
	boolean inStreaming = false;

	@SuppressLint("SdCardPath")
	final String resourceDirectory = "/sdcard/";

	private Messenger mServiceMessenger = null;
	private Messenger mMessenger;
	private boolean mBound;

	private static boolean isPlaying = false;

	private ImageButton mControlButton;
	private SeekBar mPlayerSeekBar;
	private TextView mTotalTimeText, mCurrentTimeText;
	private ImageButton mSearchButton, mQuitButton;
	private ImageView mImageView;
	private ImageButton mCotrolStartButton;
	private ImageButton mControlFastStepButton;

	private String mPlayUri = "";
	private int mCurrentTime, mTotalTime = 0;
	private boolean mUserTouchFlag = false;

	private FolderLister mCommonLister = null;

	private FileBean fileBean;
	private List<FileBean> mCurrentLists;
	private int mCurrentListPosition = 0;
	// 展示图片
	private Bitmap showBitmap;
	// 自动播放
	private AutoPlayer autoPlayer;

	private boolean bing = false;

	private ViewPager pager;

	private List<View> views = new ArrayList<View>();
	private View pagerView;

	private ImageView pagerImageView;

	private PageBingAdapter pageBingAdapter;

	private Bitmap puBitmap;

	private List<Fragment> pushFragments = new ArrayList<Fragment>();

	private BusinessFraAdapter pushAdapter;

	private String[] title;

	@SuppressWarnings("unchecked")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mCurrentLists = (List<FileBean>) getIntent().getSerializableExtra(
				"dataList");
		mCurrentListPosition = getIntent().getIntExtra("position", 0);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		Window win = getWindow();
		win.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		win.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.dmcplayer_layout);
		getPlayUri();
		setupView();

		// new Thread(new loadDataThread()).start();

		bindService(new Intent(this, DMCMediaService.class), mConnection,
				Context.BIND_AUTO_CREATE);
		Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub

				sendEventToService(DMCConstants.MEDIA_DMC_CTL_MSG_PLAY,
						mPlayUri);
				
				if (DMCPlayerActivity.this.getIntent().getIntExtra("type", 0) != G.IMAGE) {
					sendEventToService(
							DMCConstants.MEDIA_DMC_CTL_MSG_TOTALTIME, "");
				}

				mControlButton.setImageDrawable(getResources().getDrawable(
						R.drawable.c_play));
			}
		}, 1000);

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	/**
	 * 加载数据线程
	 */
	private class loadDataThread extends Thread {

		@Override
		public void run() {

			for (int i = 0; i < mCurrentLists.size(); i++) {
				PushPagerFra pagerFra = new PushPagerFra(mCurrentLists.get(i));
				pushFragments.add(pagerFra);
			}

			// PushPagerFra pagerFra=new PushPagerFra(mCurrentLists.get(
			// mCurrentListPosition).getFilePath());
			// pushFragments.add(pagerFra);

			// LayoutInflater inflater=getLayoutInflater();
			// pagerView=inflater.inflate(R.layout.pager_frame, null);
			//
			//
			// for (int i = 0; i < mCurrentLists.size(); i++) {
			// View view=inflater.inflate(R.layout.pager_frame, null);
			// views.add(view);
			// }
			// pagerImageView=(ImageView)views.get(mCurrentListPosition).findViewById(R.id.pager_push_img);
			// puBitmap= BitmapFactory
			// .decodeFile(mCurrentLists.get(
			// mCurrentListPosition).getFilePath());
			Message message = new Message();
			bingHandler.sendMessage(message);

		}
	}

	private void getPlayUri() {
		// TODO Auto-generated method stub
		mPlayUri = this.getIntent().getStringExtra("path");
		Log.d(TAG, "Got the paly uri, is" + mPlayUri);

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		sendEventToService(DMCConstants.MEDIA_DMC_CTL_STOP, "stop");
		FLAG = true;
		Utils.send_back(DMCPlayerActivity.this);
		mImageView.setImageResource(R.drawable.icon);
		unbindService(mConnection);
	}

	public void sendEventToService(int messageType, String param) {
		if (!mBound)
			return;
		// ��Service����һ��Message
		Message msg = Message.obtain(null, messageType);
		msg.obj = param;
		try {
			msg.replyTo = mMessenger;
			mServiceMessenger.send(msg);
			Log.d(TAG, "sendEvent to Service is sending");
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	private final Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case DMCConstants.MEDIA_DMC_CTL_MSG_GETLIST:
				Log.d(TAG, "Get Render List");
				Toast.makeText(getApplicationContext(),
						getString(R.string.no_dmr), Toast.LENGTH_LONG).show();
				break;

			case DMCConstants.MEDIA_DMC_CTL_MSG_PLAY:
				// Play button status changed.
				Log.d(TAG, "Got Message from Service MSG_PLAY");
				isPlaying = true;

				/*
				 * switch (msg.arg1) { case
				 * BluetoothChatService.STATE_CONNECTED: //
				 * mTitle.setText(R.string.title_connected_to); //
				 * mTitle.append(mConnectedDeviceName); //
				 * mConversationArrayAdapter.clear(); break;}
				 */

				break;
			case DMCConstants.MEDIA_DMC_CTL_MSG_PAUSE:
				// Play button status changed.
				Log.d(TAG, "Got Message from Service MSG_PAUSE");
				isPlaying = false;
				/*
				 * switch (msg.arg1) { case
				 * BluetoothChatService.STATE_CONNECTED: //
				 * mTitle.setText(R.string.title_connected_to); //
				 * mTitle.append(mConnectedDeviceName); //
				 * mConversationArrayAdapter.clear(); break;}
				 */

				break;
			case DMCConstants.MEDIA_DMC_CTL_MSG_TOTALTIME:
				mTotalTime = (Integer) (msg.obj);
				mTotalTimeText.setText(Utils.convertToTime(mTotalTime));
				mPlayerSeekBar.setMax(mTotalTime);
				break;
			case DMCConstants.MEDIA_DMC_CTL_MSG_CURRENTTIME:
				mCurrentTime = (Integer) (msg.obj);
				mCurrentTimeText.setText(Utils.convertToTime(mCurrentTime));
				if (!mUserTouchFlag) { // user clicked
					mPlayerSeekBar.setProgress(mCurrentTime);
				}

				Log.i(TAG, "当前:" + mCurrentTime);
				Log.i(TAG, "总进程:" + mTotalTime);
				if (mCurrentTime == mTotalTime && !bing && mTotalTime > 0) {
					send2next();
				}

				mUserTouchFlag = false;
				break;
			case DMCConstants.MEDIA_DMC_CTL_MSG_DMRLOST:
				Log.d(TAG, "dmr lost was received.");
				finish();
				break;
			default:
				break;

			}
		}
	};

	private ServiceConnection mConnection = new ServiceConnection() {
		public void onServiceConnected(ComponentName className, IBinder service) {

			mServiceMessenger = new Messenger(service);
			mMessenger = new Messenger(mHandler);
			mBound = true;
		}

		public void onServiceDisconnected(ComponentName className) {
			mServiceMessenger = null;
			mBound = false;
		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu m) {
		m.add(0, MENU_EXIT, 0, "Exit");
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem i) {
		switch (i.getItemId()) {
		case MENU_EXIT:
			finish();
			return true;
		default:
			return false;
		}
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	private void setupView() {

		pager = (ViewPager) findViewById(R.id.push_pager);
		pageBingAdapter = new PageBingAdapter(views);
		getMtitle();
		pushAdapter = new BusinessFraAdapter(getSupportFragmentManager(),
				pushFragments, title);

		// pager.setAdapter(pageBingAdapter);
		pager.setAdapter(pushAdapter);
		pager.setOnPageChangeListener(this);
		new loadDataThread().start();

		mControlButton = (ImageButton) findViewById(R.id.dmcplayer_control_button);
		mControlButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
//				Toast.makeText(DMCPlayerActivity.this, "点击", Toast.LENGTH_LONG).show();
				if (mCurrentLists.get(mCurrentListPosition).getFileType() == G.IMAGE) {
					if (!FLAG) {
						mControlButton.setImageResource(R.drawable.c_pause);
						FLAG=true;
					}else {
						FLAG=false;
						mControlButton.setImageResource(R.drawable.c_play);
						new AutoPlayer().execute(G.IMAGE);
					}
					return;
				}else {
					if (!isPlaying) {
						
						mControlButton.setImageDrawable(getResources().getDrawable(
								R.drawable.c_play));
						sendEventToService(DMCConstants.MEDIA_DMC_CTL_MSG_PLAY,
								mPlayUri);
						sendEventToService(
								DMCConstants.MEDIA_DMC_CTL_MSG_TOTALTIME, "");
					} else {
						mControlButton.setImageDrawable(getResources().getDrawable(
								R.drawable.c_pause));
						sendEventToService(DMCConstants.MEDIA_DMC_CTL_MSG_PAUSE,
								"pause");
					}
				}
				

			}
		});

		mPlayerSeekBar = (SeekBar) findViewById(R.id.dmcplayer_controlbar_seekbar);
		mPlayerSeekBar
				.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

					@Override
					public void onProgressChanged(SeekBar arg0, int arg1,
							boolean arg2) {
						// TODO Auto-generated method stub
						Log.d(TAG, "dmcPlayerSeekBar progress changed" + arg1);
						if (arg2) {
							sendEventToService(
									DMCConstants.MEDIA_DMC_CTL_MSG_SEEK,
									Integer.toString(arg1));
						}
					}

					@Override
					public void onStartTrackingTouch(SeekBar arg0) {
						// TODO Auto-generated method stub
						mUserTouchFlag = true;
					}

					@Override
					public void onStopTrackingTouch(SeekBar arg0) {
						// TODO Auto-generated method stub
						mUserTouchFlag = false;

					}

				});

		mSearchButton = (ImageButton) findViewById(R.id.dmcplayer_search);
		mSearchButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				sendEventToService(DMCConstants.MEDIA_DMC_CTL_MSG_PLAY,
						mPlayUri);
			}
		});

		mQuitButton = (ImageButton) findViewById(R.id.dmcplayer_quit);
		mQuitButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		mCotrolStartButton = (ImageButton) findViewById(R.id.dmcplayer_pre_one);
		mCotrolStartButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (mCurrentLists.size() == 0) {
					Log.d(TAG, "no common list");
					Toast.makeText(getApplicationContext(),
							getString(R.string.no_commonlist),
							Toast.LENGTH_LONG).show();
					return;
				}
				if (mCurrentListPosition >= 1) {
					mCurrentListPosition--;
					while (mCurrentLists.get(mCurrentListPosition)
							.getFileType() == G.FOLDER) {
						mCurrentListPosition--;
						if (mCurrentListPosition < 0) {
							mCurrentListPosition = 0;
							return;
						}
					}
				}
				Log.d(TAG, "mCurrentListPosition is " + mCurrentListPosition);

				// TODO Auto-generated method stub
				if (mCurrentListPosition >= 0
						&& mCurrentListPosition <= mCurrentLists.size() - 1) {
					mCurrentLists.get(mCurrentListPosition);
					if (mCurrentLists.get(mCurrentListPosition).getFilePath()
							.endsWith(".jpg")
							|| mCurrentLists.get(mCurrentListPosition)
									.getFilePath().endsWith(".png")) {
						Bitmap myBitmap = BitmapFactory
								.decodeFile(mCurrentLists.get(
										mCurrentListPosition).getFilePath());
						mImageView.setImageBitmap(myBitmap);
						mTotalTimeText.setVisibility(View.GONE);
						mCurrentTimeText.setVisibility(View.GONE);
						mPlayerSeekBar.setVisibility(View.GONE);
					} else {
						mImageView.setImageResource(R.drawable.icon);
						mTotalTimeText.setVisibility(View.VISIBLE);
						mCurrentTimeText.setVisibility(View.VISIBLE);
						mPlayerSeekBar.setVisibility(View.VISIBLE);
					}
					if (mCurrentLists.get(mCurrentListPosition).getFileType() != G.FOLDER) {
						sendEventToService(DMCConstants.MEDIA_DMC_CTL_MSG_PLAY,
								mCurrentLists.get(mCurrentListPosition)
										.getFilePath());
						Log.d(TAG, "mCurrentListPosition send is "
								+ mCurrentListPosition);
					}

					/******* 推送 ********/
					pager.setCurrentItem(mCurrentListPosition);
					// sendEventToService(DMCConstants.MEDIA_DMC_CTL_MSG_PLAY,
					// mCurrentLists.get(mCurrentListPosition)
					// .getFilePath());
					// sendEventToService(
					// DMCConstants.MEDIA_DMC_CTL_MSG_TOTALTIME, "");

				}
			}
		});

		mControlFastStepButton = (ImageButton) findViewById(R.id.dmcplayer_next_one);
		mControlFastStepButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (mCurrentLists.size() == 0) {
					Log.d(TAG, "no common list");
					Toast.makeText(getApplicationContext(),
							getString(R.string.no_commonlist),
							Toast.LENGTH_LONG).show();
					return;
				}

				if (mCurrentListPosition <= mCurrentLists.size() - 2) {
					mCurrentListPosition++;
					while (mCurrentLists.get(mCurrentListPosition)
							.getFileType() == G.FOLDER) {
						mCurrentListPosition++;
						if (mCurrentListPosition > mCurrentLists.size() - 1) {
							mCurrentListPosition = mCurrentLists.size() - 1;
							return;
						}
					}
				}
				Log.d(TAG, "mCurrentListPosition is " + mCurrentListPosition);

				if (mCurrentListPosition > mCurrentLists.size() - 1) {
					mCurrentListPosition = mCurrentLists.size() - 1;
				}
				if (mCurrentListPosition <= mCurrentLists.size() - 1
						&& mCurrentListPosition >= 0) {
					mCurrentLists.get(mCurrentListPosition);
					if (mCurrentLists.get(mCurrentListPosition).getFilePath()
							.endsWith(".jpg")
							|| mCurrentLists.get(mCurrentListPosition)
									.getFilePath().endsWith(".png")) {
						Bitmap myBitmap = BitmapFactory
								.decodeFile(mCurrentLists.get(
										mCurrentListPosition).getFilePath());
						mImageView.setImageBitmap(myBitmap);
						mTotalTimeText.setVisibility(View.GONE);
						mCurrentTimeText.setVisibility(View.GONE);
						mPlayerSeekBar.setVisibility(View.GONE);
					} else {
						mImageView.setImageResource(R.drawable.icon);
						mTotalTimeText.setVisibility(View.VISIBLE);
						mCurrentTimeText.setVisibility(View.VISIBLE);
						mPlayerSeekBar.setVisibility(View.VISIBLE);
					}
					if (mCurrentLists.get(mCurrentListPosition).getFileType() != G.FOLDER) {
						sendEventToService(DMCConstants.MEDIA_DMC_CTL_MSG_PLAY,
								mCurrentLists.get(mCurrentListPosition)
										.getFilePath());
						Log.d(TAG, "mCurrentListPosition send is "
								+ mCurrentListPosition);
					}

					/******* 推送 ********/
					pager.setCurrentItem(mCurrentListPosition);
					// sendEventToService(DMCConstants.MEDIA_DMC_CTL_MSG_PLAY,
					// mCurrentLists.get(mCurrentListPosition)
					// .getFilePath());
					// sendEventToService(
					// DMCConstants.MEDIA_DMC_CTL_MSG_TOTALTIME, "");

				}
			}

		});
		mTotalTimeText = (TextView) findViewById(R.id.TotalDurationLabel);
		mCurrentTimeText = (TextView) findViewById(R.id.CurrentDurationLabel);

		mImageView = (ImageView) findViewById(R.id.dmcplayer_show);
		if (this.getIntent().getIntExtra("type", 0) == G.IMAGE) {
			Bitmap myBitmap = BitmapFactory.decodeFile(mPlayUri);
			mImageView.setImageBitmap(myBitmap);
			mTotalTimeText.setVisibility(View.GONE);
			mCurrentTimeText.setVisibility(View.GONE);
			mPlayerSeekBar.setVisibility(View.GONE);
			Log.i(TAG, "计时卡死hi:");
			autoPlayer = new AutoPlayer();
			autoPlayer.execute(G.IMAGE);

		}

	}

	private class AutoPlayer extends AsyncTask<Integer, Integer, Integer> {

		@Override
		protected Integer doInBackground(Integer... params) {
			// TODO Auto-generated method stub
			Log.i(TAG, "计时卡死hi:");
			int count = 0;
			bing = true;

			while (!FLAG) {
				if (mCurrentLists.get(mCurrentListPosition).getFileType() != G.IMAGE) {
					continue;
				}
				if (mCurrentListPosition < mCurrentLists.size() - 1) {
					if (count != 0) {

						mCurrentListPosition++;
					}

				} else {
					mCurrentListPosition = 0;
				}
				count++;
				Log.i(TAG, "计时:" + count);

				// sendEventToService(DMCConstants.MEDIA_DMC_CTL_MSG_PLAY,
				// mCurrentLists.get(mCurrentListPosition).getFilePath());
				publishProgress(mCurrentListPosition);
				try {

					Thread.sleep(10000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				

			}

			return mCurrentListPosition;
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			// TODO Auto-generated method stub
			super.onProgressUpdate(values);
			pager.setCurrentItem(values[0]);
			// setBitmap(mCurrentLists.get(values[0]).getFilePath());

		}
	}

	/**
	 * 设置背景
	 * 
	 * @param path
	 *            图片路径
	 */
	private void setBitmap(String path) {
		showBitmap = BitmapFactory.decodeFile(path);
		mImageView.setImageBitmap(showBitmap);
		mTotalTimeText.setVisibility(View.GONE);
		mCurrentTimeText.setVisibility(View.GONE);
		mPlayerSeekBar.setVisibility(View.GONE);
	}

	private long time = 0;

	/**
	 * 推送下一个
	 */
	private void send2next() {

		if (System.currentTimeMillis() - time < 10000) {
			time = System.currentTimeMillis();
			return;
		}
		time = System.currentTimeMillis();
		Log.i(TAG, "当前位置:" + mCurrentListPosition);
		if (mCurrentListPosition < mCurrentLists.size() - 1) {
			mCurrentListPosition++;
		} else {
			mCurrentListPosition = 0;
		}
		pager.setCurrentItem(mCurrentListPosition);
//		sendEventToService(DMCConstants.MEDIA_DMC_CTL_MSG_PLAY, mCurrentLists
//				.get(mCurrentListPosition).getFilePath());
//		sendEventToService(DMCConstants.MEDIA_DMC_CTL_MSG_TOTALTIME, "");
		
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onPageSelected(int arg0) {
		// TODO Auto-generated method stub
		Log.i(TAG, "onPageSelected==ID：" + arg0);
		mCurrentListPosition = arg0;
		sendEventToService(DMCConstants.MEDIA_DMC_CTL_MSG_PLAY, mCurrentLists
				.get(mCurrentListPosition).getFilePath());

	}

	private Handler bingHandler = new Handler(new Handler.Callback() {

		@Override
		public boolean handleMessage(Message msg) {
			// TODO Auto-generated method stub

			// pageBingAdapter.notifyDataSetChanged();
			// pager.setCurrentItem(mCurrentListPosition);
			// pagerImageView.setImageBitmap(puBitmap);
			pager.setCurrentItem(mCurrentListPosition);
			pushAdapter.notifyDataSetChanged();

			return false;
		}
	});

	private void sendnext() {
		if (mCurrentListPosition < mCurrentLists.size() - 1) {
			mCurrentListPosition++;

		} else {
			mCurrentListPosition = 0;
		}

		sendEventToService(DMCConstants.MEDIA_DMC_CTL_MSG_PLAY, mCurrentLists
				.get(mCurrentListPosition).getFilePath());
	}

	private void sendpre() {
		if (mCurrentListPosition > 0) {
			mCurrentListPosition--;

		} else {
			mCurrentListPosition = mCurrentLists.size() - 1;
		}

		sendEventToService(DMCConstants.MEDIA_DMC_CTL_MSG_PLAY, mCurrentLists
				.get(mCurrentListPosition).getFilePath());
	}

	private void getMtitle() {
		int length = mCurrentLists.size();
		title = new String[length];
		for (int i = 0; i < length; i++) {
			title[i] = mCurrentLists.get(i).getFileName();
		}
	}

}
