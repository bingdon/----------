package net.zhaidian.file;


public interface IDownloadable{
	int downloadToSDCard(FileBean fileBean);
	String downloadStr(FileBean fileBean);
}
