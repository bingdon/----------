package net.zhaidian.img;

import net.zhaidian.file.FileBean;
import android.graphics.drawable.Drawable;

public class ImgBean extends FileBean {
	private Drawable drawable;

	public Drawable getDrawable() {
		return drawable;
	}

	public void setDrawable(Drawable drawable) {
		this.drawable = drawable;
	}
	
}
