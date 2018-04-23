package com.lt.quota.core.http;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;
import java.util.zip.GZIPInputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MitakeSocket implements Runnable
{
	private static final boolean DEBUG = false;
	
	private Socket socket;
	private ISocketListener socketListener;
	private String api;
	private InputStream is;
	private OutputStream os;

	public long idleTime;
	public String ip;
	public String port;
	public String desc;
	
	private long responseTime;
	private Hashtable<String, HttpData> dataQueue;
	private ConcurrentLinkedQueue<HttpData> writeQueue;
	private boolean running;
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	public MitakeSocket(String[] host, ISocketListener socketListener)
	{
		int point = 0;

		if(host[1].indexOf("http://") > -1)
		{
			point = 7;
		}
		else if(host[1].indexOf("https://") > -1)
		{
			point = 8;
		}

		this.ip = host[1].substring(point);
		this.port = host[2];
		this.desc = host[3];
		this.socketListener = socketListener;

		if(DEBUG)
		{
			logger.info("Socket初始化: ip==" + ip + ",port==" + port);
		}
	}

	public void start()
	{
		dataQueue = new Hashtable<String, HttpData>();
		writeQueue = new ConcurrentLinkedQueue<HttpData>();
		running = true;

		Thread writeThread = new Thread(new WritePackage());
		writeThread.start();
		
		Thread t = new Thread(this);
		t.start();
	}

	private byte[] readHeader() throws Exception
	{
		int ch = 0;
		boolean flag = false;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		while((ch = is.read()) > -1)
		{
			if(responseTime == -1)
			{
				responseTime = System.nanoTime();
			}
			
			if(flag && ch == (byte)0x0d)
			{
				/**
				 * 把下一个0x10读出来
				 */
				is.read();
				break;
			}
			else
			{
				flag = false;
			}

			if(ch == (byte)0x0a)
			{
				flag = true;
			}

			baos.write(ch);
		}

		if(ch == -1)
		{
			throw new Exception("InputStream is close!");
		}

		baos.flush();
		byte[] b = baos.toByteArray();
		baos.close();

		return b;
	}

	private byte[] readChunked() throws Exception
	{
		int ch = 0;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ByteArrayOutputStream body = new ByteArrayOutputStream();

		while((ch = is.read()) > -1)
		{
			if(ch == (byte)0x0d)
			{
				/**
				 * 把下一个0x10读出来
				 */
				is.read();
				/**
				 * 找出长度
				 */
				baos.flush();
				byte[] lenByte = baos.toByteArray();
				baos.close();
				baos = null;

				baos = new ByteArrayOutputStream();

				body.write(read(getChunkedLength(lenByte)));
				/**
				 * 将此区块的/r/n读出来
				 */
				is.read();
				is.read();
				/**
				 * 读下一个字元来判断是否已经结束了
				 */
				ch = is.read();

				if(ch == (byte)0x30)
				{
					/**
					 * 把最后的/r/n/r/n读出来
					 */
					is.read();
					is.read();
					is.read();
					is.read();
					break;
				}
			}

			baos.write(ch);
		}

		if(ch == -1)
		{
			throw new Exception("InputStream is close!");
		}

		body.flush();
		byte[] b = body.toByteArray();
		body.close();

		return b;
	}

	private byte[] read(int total) throws Exception
	{
		byte[] b = new byte[total];
		int readBytesEnd = 0;

		while(readBytesEnd != total)
		{
			int result = is.read(b, readBytesEnd, total - readBytesEnd);

			if(result == -1)
			{
				throw new Exception("InputStream is close!");
			}
			else
			{
				readBytesEnd += result;
			}
		}

		return b;
	}

	public boolean write(MitakeHttpParams params)
	{
		if(DEBUG)
		{
			logger.info("送出的Http write: " + isConnected() + "==" + params.api);
		}

		if(isConnected())
		{
			HttpData httpData = new HttpData();
			httpData.name = desc;
			httpData.host = ip + ":" + port;
			
			dataQueue.put(params.packageNo, httpData);
			
			idleTime = System.currentTimeMillis();
			httpData.stockID = params.stockID;
			httpData.S2CDataType = params.S2CDataType;

			int totalLength = 0;
			byte[] body = null;
			/**
			 * Http Header
			 */
			StringBuilder content = new StringBuilder();
			content.append(params.method).append(" ").append(params.api).append(" ").append("HTTP/1.1\r\n");
			content.append("Accept-Encoding: ");
			
			if(params.acceptEncoding != null)
			{
				content.append(params.acceptEncoding).append("\r\n");
			}
			else
			{
				content.append("gzip,deflate\r\n");
			}
			
			content.append("User-Agent: ").append(params.userAgent).append("\r\n");

			if(params.method.equals("POST"))
			{
				content.append("Content-type: ");
				
				if(params.contentType != null)
				{
					content.append(params.contentType).append("\r\n");
				}
				else
				{
					content.append("multipart/form-data\r\n");
				}
			}

			for(int i = 0 ; i < params.headers.length ; i++)
			{
				content.append(params.headers[i][0]).append(": ").append(params.headers[i][1]).append("\r\n");
			}

			content.append("Host: ").append(ip).append(":").append(port).append("\r\n");
			content.append("K: ").append(params.packageNo).append("\r\n");

			if(params.keepAlive)
			{
				content.append("Connection: Keep-Alive\r\n");
			}
			else
			{
				content.append("Connection: Close\r\n");
			}
			/**
			 * Http Body
			 */
			if(params.method.equals("POST"))
			{
				if(MitakeHttpParams.C_S_DATA_TYPE_STRING == params.C2SDataType)
				{
					if(params.kv != null && params.kv.size() > 0)
					{
						StringBuilder values = new StringBuilder();

						for(Enumeration<String> e = params.kv.keys(); e.hasMoreElements(); )
						{
							String key = e.nextElement();
							String value = params.kv.get(key);

							values.append(key).append("=").append(value).append("&");
						}

						values.setLength(values.length() - 1);

						try
						{
							body = values.toString().getBytes("utf-8");
							totalLength = body.length;
						}
						catch(UnsupportedEncodingException e)
						{
							e.printStackTrace();
						}
					}
				}
				else if(MitakeHttpParams.C_S_DATA_TYPE_BYTES == params.C2SDataType)
				{
					body = params.b;
					totalLength = body.length;
				}
			}

			if(body != null)
			{
				content.append("Content-Length: ").append(String.valueOf(body.length)).append("\r\n");
			}
			else
			{
				content.append("Content-Length: 0\r\n");
			}

			content.append("\r\n");

			if(DEBUG)
			{
				logger.info("送出的Http Header: " + content.toString().replaceAll("\r\n", "<换行>"));

				if(body != null)
				{
					logger.info("送出的Http Body: " + new String(body));
				}
			}

			try
			{
				byte[] h = content.toString().getBytes("utf-8");
				totalLength += h.length;

				byte[] total = new byte[totalLength];

				System.arraycopy(h, 0, total, 0, h.length);

				if(body != null)
				{
					System.arraycopy(body, 0, total, h.length, body.length);
				}

				httpData.sendData = total;
				writeQueue.offer(httpData);
				return true;
			}
			catch(Exception e)
			{
				e.printStackTrace();
				disconnect(params.packageNo, true);

				return false;
			}
		}

		return false;
	}

	public boolean isConnected()
	{
		if(socket != null)
		{
			return socket.isConnected();
		}

		return false;
	}

	public void run()
	{
		InetSocketAddress serverAddress = new InetSocketAddress(ip, Integer.parseInt(port));

		socket = new Socket();

		try
		{
			/**
			 * 建立連線
			 */
			socket.connect(serverAddress, 5000);
			/**
  			 * 接通串流
  			 */
  			is = socket.getInputStream();
			os = socket.getOutputStream();
  			/**
			 * 通知網路連線成功
			 */
			socketListener.onNetworkStatusChanged(this, "OK", "00");
  			/**
  			 * 開始接收封包
  			 */
			String headerValues = null;
			String[] headerCols = null;
			byte[] body = null;

  			while(socket != null && is != null && os != null) 
  			{
				idleTime = System.currentTimeMillis();
				/**
				 * 先取Http Header
				 */
				responseTime = -1;
				byte[] httpHeader = readHeader();

				idleTime = System.currentTimeMillis();

				headerValues = new String(httpHeader, "utf-8");
				String[] headerRows = headerValues.trim().split("\r\n");
				HashMap<String, String> header = new HashMap<String, String>();
				/**
				 * HTTP/1.1 200 OK
				 */
				String[] first = headerRows[0].split(String.valueOf((char)0x20));

				if(DEBUG)
				{
					logger.info("收进来的Http Header--------------------------------------------------------");
					logger.info(headerRows[0]);
				}

				for(int i = 1 ; i < headerRows.length ; i++)
				{
					headerCols = headerRows[i].split(":");

					header.put(headerCols[0].trim().toLowerCase(), headerCols[1].trim().toLowerCase());

					if(DEBUG)
					{
						logger.info(headerCols[0].trim() + ":" + headerCols[1].trim());
					}
				}

				//HttpData httpData = dataQueue.get(header.get("k"));
				HttpData httpData = new HttpData();
				httpData.responseTime = responseTime;
				
				//dataQueue.remove(header.get("k"));
				
				httpData.code = Integer.parseInt(first[1]);
				httpData.message = first[2];
				httpData.headers = new HashMap<String, String>();
				httpData.headers.putAll(header);
				
				if(DEBUG)
				{
					logger.info("httpData.code: " + httpData.code);
				}
				/**
				 * 正确回应
				 */
				if(httpData.code == 200)
				{
					idleTime = System.currentTimeMillis();
					/**
					 * 读取Http Body
					 */
					if(header.get("transfer-encoding") != null && header.get("transfer-encoding").equals("chunked")) // Transfer-Encoding: chunked
					{
						body = readChunked();
					}
					else if(header.get("content-length") != null)
					{
						int len = Integer.parseInt(header.get("content-length"));

						if(len > 0)
						{
							body = read(len);
						}
					}

					idleTime = System.currentTimeMillis();
					/**
					 * 内容为压缩格式要解压缩
					 */
					if(header.get("content-encoding") != null && header.get("content-encoding").equals("gzip"))
					{
						ByteArrayInputStream bais = new ByteArrayInputStream(body);
						ByteArrayOutputStream rowdata = new ByteArrayOutputStream();

						GZIPInputStream gzip = new GZIPInputStream(bais, 512);

						byte[] data = new byte[512];
						int c;

						while((c = gzip.read(data)) != -1)
						{
							rowdata.write(data, 0, c);
						}

						rowdata.flush();
						body = rowdata.toByteArray();

						gzip.close();
						rowdata.close();
						bais.close();
					}

					if(MitakeHttpParams.S_C_DATA_TYPE_STRING == httpData.S2CDataType)
					{
						httpData.data = body == null ? "" : new String(body, "UTF-8");

						if(DEBUG)
						{
							logger.info(httpData.code + " " + api + ":回傳資料: " + httpData.data);
						}
					}
					else if(MitakeHttpParams.S_C_DATA_TYPE_BYTES == httpData.S2CDataType)
					{
						httpData.b = body;
					}

					socketListener.onContent(httpData);
				}
				else
				{
					if(httpData.code == 404)
					{
						/**
						 * 20151209[李欣骏]
						 * 修改同ios做法,当伺服器回传404,就当作正常。
						 */
						httpData.message = "请求失败";

					}
					else if(httpData.code == 307)
					{
						/**
						 * 20160411[James]
						 * Ap Server连线数限制
						 */
						httpData.message = "超过最大连线数";
					}
					else if(httpData.code == 401)
					{
						httpData.message = "session失效";
					}
					else if(httpData.code == 500)
					{
						httpData.message = "业务异常";
					}
					else if(httpData.code == 502)
					{
						httpData.message = "网关错误";
					}
					else
					{
						httpData.message = "未知错误";
					}

					socketListener.onContent(httpData);
				}
				/**
				 * 依据Http Header Connection决定要不要断线
				 */
				if(header.get("connection") != null && header.get("connection").equals("close"))
				{
					disconnect(header.get("k"), false);
				}
				else if(httpData.code == 307)
				{
					disconnect(header.get("k"), true);
				}
  			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			/**
			 * 無法連結或timeout
			 */
			disconnect("NO", true);
		}
	}

  	public void disconnect(String packageNo, boolean exception)
  	{
  		running = false;
  		
  		dataQueue.clear();
  		dataQueue = null;
  		
		writeQueue.clear();
		writeQueue = null;
		
		socketListener.onNetworkStatusChanged(this, packageNo, exception ? "02" : "01");

  		try
  		{
  			if(is != null)
  			{
  				socket.shutdownInput();
  			}
  		} 
  		catch(Exception ex) 
  		{
			if(DEBUG)
			{
				logger.info(ip + " InputStream 連線已停止!");
			}
  		} 
  		finally 
  		{
  			is = null;
  		}
    
  		try 
  		{
  			if(os != null) 
  			{
  				socket.shutdownOutput();
  			}
  		} 
  		catch(Exception ex) 
  		{
			if(DEBUG)
			{
				logger.info(ip + " OutputStream 連線已停止!");
			}
		}
		finally
  		{
  			os = null;
  		}
    
  		try 
  		{
  			if(socket != null) 
  			{
  				socket.close();
  			}
  		} 
  		catch(Exception ex) 
  		{
			if(DEBUG)
			{
				logger.info(ip + " Socket 連線已停止!");
			}
  		} 
  		finally 
  		{
  			socket = null;
  		}
  	}

	private int getChunkedLength(byte[] b)
	{
		int len = b.length;
		int num = 0;
		int total = 0;

		for(int i = 0 ; i < len ; i++)
		{
			num = (int)b[i];

			if(num > 96)
			{
				total += (num - 87) * Math.pow(16 , len - i - 1);
			}
			else if(num > 64)
			{
				total += (num - 55) * Math.pow(16 , len - i - 1);
			}
			else if(num > 47)
			{
				total += (num - 48) * Math.pow(16 , len - i - 1);
			}
		}

		return total;
	}
	
	private class WritePackage implements Runnable
	{
		@Override
		public void run()
		{
			while(running)
			{
				try
				{
					HttpData httpData = writeQueue.poll();

					if(httpData == null)
					{
						try
						{
							TimeUnit.MILLISECONDS.sleep(10);
						}
						catch(Exception e)
						{
							e.printStackTrace();
						}
					}
					else
					{
						httpData.sendTime = System.nanoTime();
						os.write(httpData.sendData);
						os.flush();
					}
				}
				catch(Exception e)
				{
					e.printStackTrace();
					disconnect("", true);
				}
			}
		}
	}	
}