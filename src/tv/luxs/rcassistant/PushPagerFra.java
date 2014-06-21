package tv.luxs.rcassistant;

import net.zhaidian.file.FileBean;
import tv.luxs.config.G;
import tv.luxs.imgutils.ImageFetcher;
import tv.luxs.rcassistant.utils.PicUtils;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore.Video.Thumbnails;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class PushPagerFra extends Fragment {

	private String path = "";
	private static final String TAG = "PushPagerFra";

	private ImageFetcher imageFetcher;
	private String title = "";
	private FileBean filebean;
	private  Bitmap mBitmap;
	private ImageView imageView;
	private String action="";

	public PushPagerFra(FileBean filebean) {
		this.filebean = filebean;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View rootView = inflater.inflate(R.layout.pager_frame, null);
		imageView = (ImageView) rootView.findViewById(R.id.pager_push_img);
		TextView textView = (TextView) rootView
				.findViewById(R.id.push_title_txt);
		textView.setText(filebean.getFileName());
		initFiler();
		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);

		Log.i(TAG, "======onActivityCreated========" + path);
		
		// Uri uri=Uri.parse(path);
		// imageView.setImageURI(uri);

	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (null==mBitmap) {
			getpictag();
		}else {
			imageView.setImageBitmap(mBitmap);
		}
		
		Log.i(TAG, "======onResume====bitmap===="+mBitmap);
	}


	
	
	@Override
	public void onLowMemory() {
		// TODO Auto-generated method stub
		super.onLowMemory();
		Log.i(TAG, "======onLowMemory======");
	}

	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		getActivity().unregisterReceiver(mReceiver);
	}
	private void getpictag() {
		switch (filebean.getFileType()) {
		case G.IMAGE:
//			mBitmap = PicUtils.getSmallBitmap(filebean.getFilePath())/*BitmapFactory.decodeFile(filebean.getFilePath())*/;
//			imageView.setImageBitmap(mBitmap);
//			new GetBitmapTask().execute(filebean.getFilePath());
			new Thread(mRunnable).start();
			break;

		case G.VIDEO:
			mBitmap=getVideoThumbnail(filebean.getFilePath(), 100, 100, Thumbnails.MICRO_KIND);
			imageView.setImageBitmap(mBitmap);
//			imageView.setImageResource(R.drawable.icon_file_video);
//			Log.i(TAG, "缩略图:"+filebean.getFileThumb());
			break;

		case G.MUSIC:
			imageView.setImageResource(R.drawable.cd);
			break;

		default:
			break;
		}
		
	}

	
	  private Bitmap getVideoThumbnail(String videoPath, int width, int height,  
	            int kind) {  
	        Bitmap bitmap = null;  
	        // 获取视频的缩略图  
	        bitmap = ThumbnailUtils.createVideoThumbnail(videoPath, kind);  
	        Log.i(TAG, "缩略图:"+bitmap);
	        
	        if(bitmap != null){
		        System.out.println("w"+bitmap.getWidth());  
		        System.out.println("h"+bitmap.getHeight());  
		        bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,  
		                ThumbnailUtils.OPTIONS_RECYCLE_INPUT);  
	        }
	        return bitmap;  
	    }  
	
	  
	  private class GetBitmapTask extends AsyncTask<String, Integer, Bitmap>{

		@Override
		protected Bitmap doInBackground(String... params) {
			// TODO Auto-generated method stub
			
			Log.i(TAG, "======onLowMemory======");
			return mBitmap;
		}
		
		@Override
		protected void onPostExecute(Bitmap result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			imageView.setImageBitmap(result);
		}
		
		  
	  }
	  
	  
	  
	  private Runnable mRunnable=new Runnable() {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			mBitmap = PicUtils.getSmallBitmap(filebean.getFilePath());
			Log.i(TAG, "======线程======");
			if (mBitmap!=null) {
				Intent intent=new Intent();
				intent.setAction(action);
				getActivity().sendBroadcast(intent);
			}
//			
			
			
			
		}
	};
	  
	
	private BroadcastReceiver mReceiver=new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			if (mBitmap!=null) {
				imageView.setImageBitmap(mBitmap);
			}
		}
	};
	
	private void initFiler(){
		action="liubing"+Math.random()*10;
		IntentFilter filter=new IntentFilter();
		filter.addAction(action);
		getActivity().registerReceiver(mReceiver, filter);
	}
	  
}
