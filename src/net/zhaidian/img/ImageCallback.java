package net.zhaidian.img;

import android.graphics.drawable.Drawable;

public interface ImageCallback {
	public void imageLoaded();
	public void imageLoaded(Drawable drawable);

}
