package net.zhaidian.more;

import java.util.List;

import net.zhaidian.file.FileBean;
import net.zhaidian.file.Ilister;

import org.apache.http.NameValuePair;

public class ShakeLister implements Ilister {

	@Override
	public List<?> getList(FileBean fileBean) {
 		return null;
	}

	@Override
	public List<?> getList(FileBean fileBean, List<NameValuePair> postParameters) {
		//获取云端数据
		return null;
	}

	@Override
	public int getCount(FileBean fileBean) {
		return 0;
	}

	@Override
	public int getRemoteCount(FileBean fileBean) {
		return 0;
	}

}
