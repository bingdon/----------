package net.zhaidian.file;


public interface IDownloader {
	int downloadToSDCard(FileBean fileBean);
	String downloadStr(FileBean fileBean);
}
