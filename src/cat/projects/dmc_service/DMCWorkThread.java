package cat.projects.dmc_service;

import android.content.Context;
import android.util.Log;

public class DMCWorkThread extends Thread implements IBaseEngine{


	
	private static final int CHECK_INTERVAL = 30 * 1000;

	private static final String TAG = "DMCWorkThread"; 
	
	private Context mContext = null;
	private boolean mStartSuccess = false;
	private boolean mExitFlag = false;
	
	private String mFriendName = "";
	private String mUUID = "";	
	
	public DMCWorkThread(Context context){
		mContext = context;
	}
	
	public void  setFlag(boolean flag){
		mStartSuccess = flag;
	}
	
	public void setParam(String friendName, String uuid){
		mFriendName = friendName;
		mUUID = uuid;
	}
	
	public void awakeThread(){
		synchronized (this) {
			notifyAll();
		}
	}
	
	public void exit(){
		mExitFlag = true;
		awakeThread();
	}

	@Override
	public void run() {

		Log.d(TAG, "DMCWorkThread run...");
		
		while(true)
		{
			if (mExitFlag){
				stopEngine();
				break;
			}
			refreshNotify();
			synchronized(this)
			{				
				try
				{
					wait(CHECK_INTERVAL);
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}								
			}
			if (mExitFlag){
				stopEngine();
				break;
			}
		}
		
		Log.e(TAG, "DMCWorkThread over...");
		
	}
	
	public void refreshNotify(){
	/*	if (!CommonUtil.checkNetworkState(mContext)){
			return ;
		} */
		
		if (!mStartSuccess){
			stopEngine();
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			boolean ret = startEngine();
			if (ret){
				mStartSuccess = true;
			}
		}

	}
	
	@Override
	public boolean startEngine() {
		
		if (mFriendName.length() == 0){
			return false;
		}
		//start engine
//		return result;
		return true;
	}

	@Override
	public boolean stopEngine() {
		//stop engine
		return true;
	}

	@Override
	public boolean restartEngine() {
		setFlag(false);
		awakeThread();
		return true;
	}

}
