package com.lt.quota.core.quota;

import com.lt.quota.core.quota.bean.QuotaBean;
import com.lt.quota.core.quota.clean.CleanInstance;
import com.lt.util.utils.DoubleUtils;
import com.lt.util.utils.StringTools;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * 对接第三方行情接口
 */
public class TaoBaoQuota {

	private static Socket socket = null;
	private static BufferedInputStream Recv = null;
	private static BufferedOutputStream Send = null;
	static boolean connected = false;

	static void print(String str) {
		System.out.println(str);
	}

	public static byte[] compress(String str, String encoding) {
		if (str == null || str.length() == 0) {
			return null;
		}
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		GZIPOutputStream gzip;
		try {
			gzip = new GZIPOutputStream(out);
			gzip.write(str.getBytes(encoding));
			gzip.close();
		} catch (IOException e) {
			print("gzip compress error." + e);
		}
		return out.toByteArray();
	}

	//压缩
	public static byte[] compress(byte[] bytes) {
		if (bytes == null || bytes.length == 0) {
			return null;
		}
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		GZIPOutputStream gzip;
		try {
			gzip = new GZIPOutputStream(out);
			gzip.write(bytes);
			gzip.close();
		} catch (IOException e) {
			print("gzip compress error." + e);
		}
		return out.toByteArray();
	}

	//解压
	public static byte[] uncompress(byte[] bytes) {
		if (bytes == null || bytes.length == 0) {
			return null;
		}
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ByteArrayInputStream in = new ByteArrayInputStream(bytes);
		try {
			GZIPInputStream ungzip = new GZIPInputStream(in);
			byte[] buffer = new byte[256];
			int n;
			while ((n = ungzip.read(buffer)) >= 0) {
				out.write(buffer, 0, n);
			}
		} catch (IOException e) {
			print("gzip uncompress error." + e);
		}
		return out.toByteArray();
	}

	public static byte[] StringToUtf8(String str) {
		byte[] bb = null;
		try {
			bb = str.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {

			e.printStackTrace();
		}
		return bb;
	}

	public static String AscToUtf8(byte[] bb) {
		String str = "";
		try {
			str = new String(bb, "UTF-8");
			str = str.replace('\0', ' ').trim();
		} catch (UnsupportedEncodingException e) {

			e.printStackTrace();
		}
		return str;
	}

	//生成登录结构体
	public static byte[] getlogin(String username, String userpass) {
		ByteBuffer bb = ByteBuffer.allocate(56);
		bb.order(ByteOrder.LITTLE_ENDIAN);
		byte[] b1 = StringToUtf8(username);
		bb.put(b1);
		byte b0 = 0;
		for (int t = 0; t < (20 - b1.length); t++)
			bb.put(b0);
		byte[] b2 = StringToUtf8(userpass);
		bb.put(b2);
		for (int t = 0; t < (32 - b2.length); t++)
			bb.put(b0);
		bb.putInt(3);
		return bb.array();
	}

	//打包
	public static byte[] packdata(int cmd, byte[] send) {
		byte[] dc = compress(send);
		ByteBuffer bb = ByteBuffer.allocate(10 + dc.length);
		bb.order(ByteOrder.LITTLE_ENDIAN);
		bb.put((byte) '[');
		bb.putInt(dc.length);
		bb.putInt(cmd);
		bb.put(dc);
		bb.put((byte) ']');
		return bb.array();
	}

	//发送数据
	public static void WriteByte(byte[] buf) {
		if (buf == null)
			return;
		try {
			Send.write(buf);
			Send.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	//关闭连接
	private static void closeSocket() {
		if (socket != null) {
			connected = false;
			try {
				socket.shutdownInput();
				socket.shutdownOutput();
				Recv.close();
				Send.close();
				socket.close();
			} catch (Exception e) {
			}
			socket = null;
			Recv = null;
			Send = null;
		}
	}

	public static void main(String [] args){
		startQuota();
	}

	public static void startQuota(){

		String HOST = "112.74.81.175";
		int PORT = 9909;
		String username = "mk007";
		String userpass = "123456";
		byte[] logindata = packdata(40, getlogin(username, userpass));
		final byte[] keepline = packdata(1000, new byte[1]);
		try {
			SocketAddress socketaddress = new InetSocketAddress(HOST, PORT);// address,Port);
			socket = new Socket();// HOST, PORT);
			socket.setKeepAlive(true);
			socket.connect(socketaddress, 2000);
			Recv = new BufferedInputStream(socket.getInputStream());
			Send = new BufferedOutputStream(socket.getOutputStream());
			connected = true;
		} catch (ConnectException e) {
			connected = false;
			System.out.println("#无法连接服务器，请检查端口是否填写正确！"); // sendUI("#无法连接服务器，请检查端口是否填写正确！");
		} catch (UnknownHostException e) {
			connected = false;
			System.out.println("#无法连接服务器，请检查地址是否填写正确！"); // sendUI("#无法连接服务器，请检查地址是否填写正确！");
		} catch (IOException e) {
			connected = false;
			e.printStackTrace();
		}
		if (!connected) {
			return;
		}
		WriteByte(logindata);
		byte[] recv = new byte[14];
		long times = System.currentTimeMillis();

		int checktimes = 30000;//30秒
		Timer timer = new Timer();
		TimerTask task = new TimerTask() {
			public void run() {
				print("发心跳包");
				WriteByte(keepline);
			}
		};
		timer.schedule(task, checktimes, checktimes);

		byte[] ContractNoId = new byte[20];
		byte[] DateTimeStamp = new byte[24];
		double QPreClosingPrice;
		double QOpeningPrice;
		double QLastPrice;
		double[] QBidPrice = new double[5];
		long[] QBidQty = new long[5];
		double[] QAskPrice = new double[5];
		long[] QAskQty = new long[5];
		double QHighPrice;//最高
		double QLowPrice;//最低
		long QTotalQty;//现量
		double QChangeRate;//涨幅
		double QChangeValue;//涨跌

		int head = 0;
		try {
			while (connected) {
				int rb = Recv.available();
				if (rb == 0) {
					long stimes = System.currentTimeMillis();
					if ((stimes - times) > checktimes + 5000) {
						//60秒发一次心跳,表明客户端正常
						Date d1 = new Date(times);
						Date d2 = new Date(stimes);
						print("超时断开1:" + d1.toLocaleString() + "  ==>" + d2.toLocaleString());
						break;
					}
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					continue;
				}
				times = System.currentTimeMillis();
				int len = Recv.read(recv, head, 9 - head);
				if (len > 0)
					head += len;
				if (len == 0)
					continue;
				if (len < 0) {
					print("接收为：" + len);
					break;
				}
				if (head == 9) {
					head = 0;
					ByteBuffer bb = ByteBuffer.wrap(recv);
					bb.order(ByteOrder.LITTLE_ENDIAN);
					byte i1 = bb.get();// [
					int Count = bb.getInt();
					int Command = bb.getInt();
					if (Count < 0) {
						print("recv 数据包长度: Count=" + Count);
						break;
					}
					byte Buffer[] = new byte[Count];
					int RecvCount = 0;
					while ((RecvCount < Count) && (connected)) {
						int c1 = Recv.available();
						if (c1 > 0) {
							times = System.currentTimeMillis();
							int clen = Math.min(Count - RecvCount, c1);
							int RecvLen = Recv.read(Buffer, RecvCount, clen);
							if (RecvLen > 0)
								RecvCount += RecvLen;
						} else {
							long stimes = System.currentTimeMillis();
							if ((stimes - times) > checktimes + 5000) {
								print("超时断开2");
								break;
							}
							try {
								Thread.sleep(10);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					}
					int i2 = Recv.read();// Buffer[Count - 1];
					if (i2 != ']') {
						print(" i2：" + i2 + " 错误");
						break;
					}
					if ((i1 == '[') && (i2 == ']') && (RecvCount == Count)) {
						byte debuffer[] = uncompress(Buffer);
						if (debuffer == null) {
							print("recv error:" + Count);
							break;
						}
						//print("recv Cmd=" + Command + " date len=" + debuffer.length);
						if (Command == 21)//返回字符串信息
						{
							String str = AscToUtf8(debuffer);
							print("Msg :" + str);
						}
						if (Command == 40)//返回登录信息
						{
							byte r1 = debuffer[0];
							String str = AscToUtf8(debuffer);
							String msg = "";
							if (r1 == '1')
								msg = "登录成功!";
							if (r1 == '2')
								msg = "账号已过期!";
							if (r1 == '3')
								msg = "禁止登录!";
							if (r1 == '4')
								msg = "用户或者密码错误!";
							if (r1 == '5')
								msg = "账号重复登录,连接断开!";
							if (r1 == '6')
								msg = "连接保持!";
							print("Login :[" + str + "] 返回:" + msg);
							if ((r1 > 1) && (r1 < 6))
								break;
						}
						if (Command == 2000) {
							/*
							   public class NewTapAPIQuoteWhole
							    {
							        [MarshalAs(UnmanagedType.ByValTStr, SizeConst = 20)]
							        public string ContractNoId;//合约名称
							        [MarshalAs(UnmanagedType.ByValTStr, SizeConst = 24)]
							        public string DateTimeStamp;//时间
							        public double QPreClosingPrice;//昨收
							        public double QOpeningPrice;//开盘
							        public double QLastPrice;//最新
							        [MarshalAs(UnmanagedType.ByValArray, SizeConst = 5)]
							        public double[] QBidPrice;//买价1-5档
							        [MarshalAs(UnmanagedType.ByValArray, SizeConst = 5)]
							        public ulong[] QBidQty;//买量1-5档
							        [MarshalAs(UnmanagedType.ByValArray, SizeConst = 5)]
							        public double[] QAskPrice;//卖价1-5档
							        [MarshalAs(UnmanagedType.ByValArray, SizeConst = 5)]
							        public ulong[] QAskQty;//卖量1-5档
							        public double QHighPrice;//最高
							        public double QLowPrice;//最低
							        public ulong QTotalQty;//现量
							        public double QChangeRate;//涨幅
							        public double QChangeValue;//涨跌
							    }
							 */
							int count = debuffer.length / 268;
							if (count == 0)
								continue;

							ByteBuffer p1 = ByteBuffer.wrap(debuffer);
							p1.order(ByteOrder.LITTLE_ENDIAN);
							for (int i = 0; i < count; i++) {
								p1.get(ContractNoId);
								String code = AscToUtf8(ContractNoId);
								p1.get(DateTimeStamp);
								String timestr = AscToUtf8(DateTimeStamp);
								QPreClosingPrice = p1.getDouble();     //昨收价
								QOpeningPrice = p1.getDouble();       //开盘价
								QLastPrice = p1.getDouble();         //最新价
								for (int t = 0; t < 5; t++)
									QBidPrice[t] = p1.getDouble();
								for (int t = 0; t < 5; t++)
									QBidQty[t] = p1.getLong();
								for (int t = 0; t < 5; t++)
									QAskPrice[t] = p1.getDouble();
								for (int t = 0; t < 5; t++)
									QAskQty[t] = p1.getLong();
								QHighPrice = p1.getDouble();//最高
								QLowPrice = p1.getDouble();//最低
								QTotalQty = p1.getLong();//现量
								QChangeRate = p1.getDouble();//涨幅
								QChangeValue = p1.getDouble();//涨跌

								QuotaBean quotaBean  = new QuotaBean();

								if(code .contains("HSI") || code.contains("MHI")){
									//恒指  最小价格是1
									quotaBean.setAskPrice1(String.valueOf(DoubleUtils.Doublefloor(QBidPrice[0],true)));
									quotaBean.setAskQty1(String.valueOf(QBidQty[0]));
									quotaBean.setBidPrice1(String.valueOf(DoubleUtils.Doublefloor(QAskPrice[0],true)));
									quotaBean.setBidQty1(String.valueOf(QAskQty[0]));
									quotaBean.setChangeRate(String.valueOf(DoubleUtils.scaleFormat(QChangeRate*100,2)));
									quotaBean.setChangeValue(String.valueOf(DoubleUtils.scaleFormat(QChangeValue,2)));
									quotaBean.setHighPrice(String.valueOf(DoubleUtils.Doublefloor(QHighPrice,true)));
									quotaBean.setLastPrice(String.valueOf(DoubleUtils.Doublefloor(QLastPrice,true)));
									quotaBean.setLowPrice(String.valueOf(DoubleUtils.Doublefloor(QLowPrice,true)));
									quotaBean.setOpenPrice(String.valueOf(DoubleUtils.Doublefloor(QOpeningPrice,true)));
									//quotaBean.setPositionQty(String.valueOf(DoubleUtils.Doublefloor(QOpeningPrice,true)));
									quotaBean.setPreClosePrice(String.valueOf(DoubleUtils.Doublefloor(QPreClosingPrice,true)));
									//quotaBean.set(String.valueOf(DoubleUtils.Doublefloor(QPreClosingPrice,true)));
									quotaBean.setTimeStamp(timestr);
									String tempCode = code.replace("[","");
									tempCode = tempCode.replace("]","");
									String codes [] =tempCode.split(" ");
									if(codes != null && codes.length==3){
										quotaBean.setProductName(codes[1]+codes[2]);
									}
									//print(quotaBean.toString());
								}else if(code.contains("CL") || code.contains("GC")){
									//美原油 美黄金 0.01
									quotaBean.setAskPrice1(String.valueOf(DoubleUtils.scaleFormatEnd(QBidPrice[0],2)));     //卖一价格
									quotaBean.setAskQty1(String.valueOf(QBidQty[0]));  //卖一量
									quotaBean.setBidPrice1(String.valueOf(DoubleUtils.scaleFormatEnd(QAskPrice[0],2))); //买一价格
									quotaBean.setBidQty1(String.valueOf(QAskQty[0])); //买一量
									quotaBean.setChangeRate(String.valueOf(DoubleUtils.scaleFormat(QChangeRate*100,2)));  //涨跌幅
									quotaBean.setChangeValue(String.valueOf(DoubleUtils.scaleFormat(QChangeValue,2)));   //涨跌值
									quotaBean.setHighPrice(String.valueOf(DoubleUtils.scaleFormatEnd(QHighPrice,2)));      //最高价
									quotaBean.setLastPrice(String.valueOf(DoubleUtils.scaleFormatEnd(QLastPrice,2))); //最新价
									quotaBean.setLowPrice(String.valueOf(DoubleUtils.scaleFormatEnd(QLowPrice,2)));  //最低价
									quotaBean.setOpenPrice(String.valueOf(DoubleUtils.scaleFormatEnd(QOpeningPrice,2))); //开盘价
									//quotaBean.setPositionQty(String.valueOf(DoubleUtils.Doublefloor(QOpeningPrice,true)));
									quotaBean.setPreClosePrice(String.valueOf(DoubleUtils.scaleFormatEnd(QPreClosingPrice,2))); //昨收价
									//quotaBean.set(String.valueOf(DoubleUtils.Doublefloor(QPreClosingPrice,true)));
									quotaBean.setTimeStamp(timestr);
									String tempCode = code.replace("[","");
									tempCode = tempCode.replace("]","");
									String codes [] =tempCode.split(" ");
									if(codes != null && codes.length==3){
										quotaBean.setProductName(codes[1]+codes[2]);
									}
								//	print(quotaBean.toString());

								}else if(code.equals("SI")){
									//美白银 0.005
									quotaBean.setAskPrice1(String.valueOf(DoubleUtils.scaleFormatEnd(QBidPrice[0],3)));     //卖一价格
									quotaBean.setAskQty1(String.valueOf(QBidQty[0]));  //卖一量
									quotaBean.setBidPrice1(String.valueOf(DoubleUtils.scaleFormatEnd(QAskPrice[0],3))); //买一价格
									quotaBean.setBidQty1(String.valueOf(QAskQty[0])); //买一量
									quotaBean.setChangeRate(String.valueOf(DoubleUtils.scaleFormat(QChangeRate*100,2)));  //涨跌幅
									quotaBean.setChangeValue(String.valueOf(DoubleUtils.scaleFormat(QChangeValue,2)));   //涨跌值
									quotaBean.setHighPrice(String.valueOf(DoubleUtils.scaleFormatEnd(QHighPrice,3)));      //最高价
									quotaBean.setLastPrice(String.valueOf(DoubleUtils.scaleFormatEnd(QLastPrice,3))); //最新价
									quotaBean.setLowPrice(String.valueOf(DoubleUtils.scaleFormatEnd(QLowPrice,3)));  //最低价
									quotaBean.setOpenPrice(String.valueOf(DoubleUtils.scaleFormatEnd(QOpeningPrice,3))); //开盘价
									//quotaBean.setPositionQty(String.valueOf(DoubleUtils.Doublefloor(QOpeningPrice,true)));
									quotaBean.setPreClosePrice(String.valueOf(DoubleUtils.scaleFormatEnd(QPreClosingPrice,3))); //昨收价
									//quotaBean.set(String.valueOf(DoubleUtils.Doublefloor(QPreClosingPrice,true)));
									quotaBean.setTimeStamp(timestr);
									String tempCode = code.replace("[","");
									tempCode = tempCode.replace("]","");
									String codes [] =tempCode.split(" ");
									if(codes != null && codes.length==3){
										quotaBean.setProductName(codes[1]+codes[2]);
									}
								//	print(quotaBean.toString());
								}else if(code.equals("HG")){
									//美铜 0.0005
									quotaBean.setAskPrice1(String.valueOf(DoubleUtils.scaleFormatEnd(QBidPrice[0],4)));     //卖一价格
									quotaBean.setAskQty1(String.valueOf(QBidQty[0]));  //卖一量
									quotaBean.setBidPrice1(String.valueOf(DoubleUtils.scaleFormatEnd(QAskPrice[0],4))); //买一价格
									quotaBean.setBidQty1(String.valueOf(QAskQty[0])); //买一量
									quotaBean.setChangeRate(String.valueOf(DoubleUtils.scaleFormat(QChangeRate*100,2)));  //涨跌幅
									quotaBean.setChangeValue(String.valueOf(DoubleUtils.scaleFormat(QChangeValue,2)));   //涨跌值
									quotaBean.setHighPrice(String.valueOf(DoubleUtils.scaleFormatEnd(QHighPrice,4)));      //最高价
									quotaBean.setLastPrice(String.valueOf(DoubleUtils.scaleFormatEnd(QLastPrice,4))); //最新价
									quotaBean.setLowPrice(String.valueOf(DoubleUtils.scaleFormatEnd(QLowPrice,4)));  //最低价
									quotaBean.setOpenPrice(String.valueOf(DoubleUtils.scaleFormatEnd(QOpeningPrice,4))); //开盘价
									//quotaBean.setPositionQty(String.valueOf(DoubleUtils.Doublefloor(QOpeningPrice,true)));
									quotaBean.setPreClosePrice(String.valueOf(DoubleUtils.scaleFormatEnd(QPreClosingPrice,4))); //昨收价
									//quotaBean.set(String.valueOf(DoubleUtils.Doublefloor(QPreClosingPrice,true)));
									quotaBean.setTimeStamp(timestr);
									String tempCode = code.replace("[","");
									tempCode = tempCode.replace("]","");
									String codes [] =tempCode.split(" ");
									if(codes != null && codes.length==3){
										quotaBean.setProductName(codes[1]+codes[2]);
									}
								//	print(quotaBean.toString());
								}else if(code.equals("SC")){
									//原油
									quotaBean.setAskPrice1(String.valueOf(DoubleUtils.scaleFormatEnd(QBidPrice[0],1)));     //卖一价格
									quotaBean.setAskQty1(String.valueOf(QBidQty[0]));  //卖一量
									quotaBean.setBidPrice1(String.valueOf(DoubleUtils.scaleFormatEnd(QAskPrice[0],1))); //买一价格
									quotaBean.setBidQty1(String.valueOf(QAskQty[0])); //买一量
									quotaBean.setChangeRate(String.valueOf(DoubleUtils.scaleFormat(QChangeRate*100,2)));  //涨跌幅
									quotaBean.setChangeValue(String.valueOf(DoubleUtils.scaleFormat(QChangeValue,2)));   //涨跌值
									quotaBean.setHighPrice(String.valueOf(DoubleUtils.scaleFormatEnd(QHighPrice,1)));      //最高价
									quotaBean.setLastPrice(String.valueOf(DoubleUtils.scaleFormatEnd(QLastPrice,1))); //最新价
									quotaBean.setLowPrice(String.valueOf(DoubleUtils.scaleFormatEnd(QLowPrice,1)));  //最低价
									quotaBean.setOpenPrice(String.valueOf(DoubleUtils.scaleFormatEnd(QOpeningPrice,1))); //开盘价
									//quotaBean.setPositionQty(String.valueOf(DoubleUtils.Doublefloor(QOpeningPrice,true)));
									quotaBean.setPreClosePrice(String.valueOf(DoubleUtils.scaleFormatEnd(QPreClosingPrice,1))); //昨收价
									//quotaBean.set(String.valueOf(DoubleUtils.Doublefloor(QPreClosingPrice,true)));
									quotaBean.setTimeStamp(timestr);
									String tempCode = code.replace("[","");
									tempCode = tempCode.replace("]","");
									String codes [] =tempCode.split(" ");
									if(codes != null && codes.length==3){
										quotaBean.setProductName(codes[1]+"1809");
									}
								//	print(quotaBean.toString());
								}

								print(quotaBean.toString());
								if(StringTools.isNotEmpty(quotaBean.getProductName())){
									//CleanInstance.getInstance().setMarketDataQueue(quotaBean);
									print(quotaBean.toString());
								}


								print("code:[" + code + "] time:" + timestr + " 卖一价:" + QBidPrice[0] + " 卖一量:" + QBidQty[0] + " 买一价:" + String.valueOf(QAskPrice[0]) + " 买一量:" + QAskQty[0]+" 涨跌幅:"+QChangeRate+" 涨跌值:"+QChangeValue);
							}

						}

					} else {
						print("recv 数据包长度: i1=" + i1 + " i2=" + i2 + " Count=" + Count + " RecvCount=" + RecvCount);
						break;
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		print("退出");
		closeSocket();
		if (timer != null)
			timer.cancel();
	}

}
