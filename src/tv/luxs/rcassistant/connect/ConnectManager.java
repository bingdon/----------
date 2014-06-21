package tv.luxs.rcassistant.connect;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import android.util.Log;
import tv.luxs.config.G;
import tv.luxs.rcassistant.RCActivity;
import tv.luxs.rcassistant.SettingConnectActivity;
import tv.luxs.rcassistant.utils.CodeUtils;
import tv.luxs.rcassistant.utils.Utils;

/**
 * 机顶盒连接管理理器
 */
public class ConnectManager {
	private static final String TAG = "ConnectManager";
	static Socket socket = null;
	static DataOutputStream dos = null;
	static DataInputStream dis = null;
	static boolean isConected = false;
	public static String myhost = "";

	/**
	 * 执行链接
	 * 
	 * @param host
	 * @return
	 */
	public static boolean connectServer(String host) {
		try {
			close();
			if (!isConected) {
				Utils.log("connecting" + host);
				socket = new Socket(host, G.PORT);
				Utils.log("socket true");
				dos = new DataOutputStream(socket.getOutputStream());
				dis = new DataInputStream(socket.getInputStream());
				Utils.log("dos true");
				isConected = true;
				myhost = host;
				if (SettingConnectActivity.is2codcon) {
					SettingConnectActivity.is2codcon = false;
					dos.writeUTF("Random#");
					dos.flush();
					Log.i(TAG, "发送Random#成功");

				} else if (SettingConnectActivity.isconfirmcon) {
					SettingConnectActivity.isconfirmcon = false;
					dos.writeUTF("LuxShare#");
					dos.flush();
					Log.i(TAG, "发送LuxShare#成功");
				}
				new RecvThread().start();
			}
			return true;
		} catch (UnknownHostException e) {
			Utils.log("UnknownHostException");
			e.printStackTrace();
			close();
		} catch (IOException e) {
			Utils.log("IOException");
			e.printStackTrace();
			close();
		}
		return false;
	}

	/**
	 * 发送获取密码
	 * 
	 * @return
	 */
	public static boolean sendGetPassword() {
		if (dos != null) {
			try {
				dos.writeUTF("ErroCode#");
				dos.flush();
				return true;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return false;
	}

	/**
	 * 发送推送状态
	 * 
	 * @return
	 */
	public static boolean sendPlayStatue() {
		if (dos != null) {
			try {
				dos.writeUTF("Start#");
				dos.flush();
				return true;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return false;
	}

	/**
	 * 是否已连接
	 */
	public static boolean isConnected() {
		try {
			if (socket != null) {
				socket.sendUrgentData(0xFF);
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			G.CURRENT_VALUE="";
			close();
		}
		return false;

	}

	/**
	 * 发送指令
	 * 
	 * @param info
	 */
	public static boolean send(String info) {
		try {
			// Log.i("ConnectManager", "socket:" + socket);

			if (socket != null && dos != null) {
				Log.i("ConnectManager", "CURRENT_VALUE:" + G.CURRENT_VALUE);
				if (!"".equals(G.CURRENT_VALUE)) {
					dos.writeUTF((G.CURRENT_VALUE + "#" + info));
					Utils.log(G.CURRENT_VALUE + "#" + info);
				} else {
					dos.writeUTF((G.CURRENT_VALUE + "#" + info));
					// dos.writeUTF(("liubing" + "#" + info));
					// dos.writeInt(Integer.parseInt(info));
				}
				dos.flush();
				return true;
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
			Utils.log("UnknownHostException");
			close();
		} catch (IOException e) {
			e.printStackTrace();
			Utils.log("IOException");
			close();
		}
		return false;
	}

	/**
	 * 启动更新随机码线程
	 */
	public static class RecvThread extends Thread {

		public void run() {
			try {
				Utils.log("启动更新随机码线程" + "连接:" + isConected);
				while (isConected) {
					String str = dis.readUTF();
					Log.i("ConnectManager", "机顶盒端读入:" + str);
					Log.i("ConnectManager",
							"str.indexOf('#'):" + str.indexOf("#"));
					if (!"".equals(str) && str.length() > 5) {
						G.CURRENT_VALUE = str;
						Log.i("ConnectManager", "G.CURRENT_VALUE:"
								+ G.CURRENT_VALUE);
						if (RCActivity.activity != null) {
							CodeUtils.saveSuijiCode(RCActivity.activity,
									G.CURRENT_VALUE);
						}

						Utils.log(G.CURRENT_VALUE);
					}
					if ("0".equals(str)) {
						G.CURRENT_VALUE="";
						close();
					}
				}
			} catch (SocketException e) {
				Utils.log("SocketException 机顶盒端已退出！");
				e.printStackTrace();
				close();
			} catch (EOFException e) {
				Utils.log("EOFException 机顶盒端已退出！");
				close();
			} catch (IOException e) {
				e.printStackTrace();
				Utils.log("IOException 机顶盒退出");
				close();
			}

		}

	}

	/**
	 * 断开链接
	 */
	public static void close() {
		try {
			isConected = false;
			if (socket != null)
				socket.close();
			if (dos != null)
				dos.close();
			if (dis != null)
				dis.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
