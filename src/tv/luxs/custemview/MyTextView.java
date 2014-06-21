package tv.luxs.custemview;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class MyTextView extends TextView {

	private List<String> list;
	private Paint mPaint,mPathPaint;
	
	public MyTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		init();
	}
	
	
	private void init() {
		setFocusable(true);
		//这里主要处理如果没有传入内容显示的默认值
		if(list==null){
			list=new ArrayList<String>();
			String str="c罗";
			list.add(0, str);
		}		
		//普通文字的字号，以及画笔颜色的设置
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setTextSize(16);
		mPaint.setColor(Color.BLACK);
		mPaint.setTypeface(Typeface.SERIF);		
		//高亮文字的字号，以及画笔颜色的设置
		mPathPaint = new Paint();
		mPathPaint.setAntiAlias(true);
		mPathPaint.setColor(Color.RED);
		mPathPaint.setTextSize(16);
		mPathPaint.setTypeface(Typeface.SANS_SERIF);
	}
	
	

}
