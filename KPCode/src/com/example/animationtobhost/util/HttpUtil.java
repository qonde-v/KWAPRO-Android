package com.example.animationtobhost.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Map;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.util.Log;

public class HttpUtil {
	public static final String TAG_GET = "Get方式";
	public static final String TAG_POST = "Post方式";
	public static final int HTTP_200 = 200;

	public static String post(String url, Map<String, String> params,
			Map<String, File> files) throws IOException {
		String BOUNDARY = java.util.UUID.randomUUID().toString();
		String PREFIX = "--", LINEND = "\r\n";
		String MULTIPART_FROM_DATA = "multipart/form-data";
		String CHARSET = "UTF-8";

		URL uri = new URL(url);
		HttpURLConnection conn = (HttpURLConnection) uri.openConnection();
		conn.setReadTimeout(10 * 1000); // 缓存的最长时间
		conn.setDoInput(true);// 允许输入
		conn.setDoOutput(true);// 允许输出
		conn.setUseCaches(false); // 不允许使用缓存
		conn.setRequestMethod("POST");
		conn.setRequestProperty("connection", "keep-alive");
		conn.setRequestProperty("Charsert", "UTF-8");
		conn.setRequestProperty("Content-Type", MULTIPART_FROM_DATA
				+ ";boundary=" + BOUNDARY);

		// 首先组拼文本类型的参数
		StringBuilder sb = new StringBuilder();
		for (Map.Entry<String, String> entry : params.entrySet()) {
			sb.append(PREFIX);
			sb.append(BOUNDARY);
			sb.append(LINEND);
			sb.append("Content-Disposition: form-data; name=\""
					+ entry.getKey() + "\"" + LINEND);
			sb.append("Content-Type: text/plain; charset=" + CHARSET + LINEND);
			sb.append("Content-Transfer-Encoding: 8bit" + LINEND);
			sb.append(LINEND);
			sb.append(entry.getValue());
			sb.append(LINEND);
		}

		DataOutputStream outStream = new DataOutputStream(
				conn.getOutputStream());
		outStream.write(sb.toString().getBytes());
		// 发送文件数据
		if (files != null)
			for (Map.Entry<String, File> file : files.entrySet()) {
				StringBuilder sb1 = new StringBuilder();
				sb1.append(PREFIX);
				sb1.append(BOUNDARY);
				sb1.append(LINEND);
				sb1.append("Content-Disposition: form-data; name=\""
						+ file.getKey() + "\"; filename=\""
						+ file.getValue().getName() + "\"" + LINEND);
				sb1.append("Content-Type: application/octet-stream; charset="
						+ CHARSET + LINEND);
				sb1.append(LINEND);
				outStream.write(sb1.toString().getBytes());

				InputStream is = new FileInputStream(file.getValue());
				byte[] buffer = new byte[3024];
				int len = 0;
				while ((len = is.read(buffer)) != -1) {
					outStream.write(buffer, 0, len);
				}
				is.close();
				outStream.write(LINEND.getBytes());
			}

		// 请求结束标志
		byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINEND).getBytes();
		outStream.write(end_data);
		outStream.flush();
		// 得到响应码
		int res = conn.getResponseCode();
		InputStream in = conn.getInputStream();
		StringBuilder sb2 = new StringBuilder();
		if (res == 200) {
			int ch;
			while ((ch = in.read()) != -1) {
				sb2.append((char) ch);
			}
		}
		outStream.close();
		conn.disconnect();
		return sb2.toString();
	}

	/**
	 * 带参数 post方式
	 */
	public static String requestByPost(String u, Map<String, String> maps,
			int timeout) throws Exception {
		String result = null;
		URL url = null;
		HttpURLConnection connection = null;
		InputStreamReader in = null;
		String strs = "";

		url = new URL(u);
		connection = (HttpURLConnection) url.openConnection();
		// 设置连接超时时间
		connection.setConnectTimeout(timeout * 1000);
		connection.setReadTimeout(timeout * 1000);
		connection.setDoInput(true);
		connection.setDoOutput(true);
		connection.setRequestMethod("POST");
		connection.setRequestProperty("Content-Type",
				"application/x-www-form-urlencoded");
		connection.setRequestProperty("Charset", "utf-8");
		try {
			DataOutputStream dop = new DataOutputStream(
					connection.getOutputStream());

			for (Map.Entry<String, String> param : maps.entrySet()) {
				strs += param.getKey() + "="
						+ URLEncoder.encode(param.getValue(), "utf-8") + "&";
			}
			strs = strs.substring(0, (strs.length() - 1));
			// dop.writeBytes("t="+URLEncoder.encode("哈哈", "utf-8"));
			dop.writeBytes(strs);
			dop.flush();
			dop.close();

			in = new InputStreamReader(connection.getInputStream());
			BufferedReader bufferedReader = new BufferedReader(in);
			StringBuffer strBuffer = new StringBuffer();
			String line = null;
			while ((line = bufferedReader.readLine()) != null) {
				strBuffer.append(line);
			}
			result = strBuffer.toString();

		} catch (SocketTimeoutException e) {
			result = null;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}
		// 去空字符串处理
		// result = result.replaceAll("\\D+","").replaceAll("\r",
		// "").replaceAll("\n", "").trim();
		// result = result.replaceAll("\r", "").replaceAll("\n", "").trim();
		// System.out.println("result2=>" + result);
		return result;
	}

	/**
	 * 带参数 get请求方式
	 */
	public static String RequstHostWithParams(String hostStr,
			Map<String, String> maps, int timeout) throws Exception {

		for (Map.Entry<String, String> param : maps.entrySet()) {
			hostStr += "&"+param.getKey() + "="
					+ URLEncoder.encode(param.getValue(), "utf-8") + "&";
		}
		hostStr = hostStr.substring(0, (hostStr.length() - 1));
//		if (MyConfig.DEBUGE_FLAG) {
//			Log.i("hnyer", "get 方式访问的地址  url =" + hostStr);
//		}
		// 新建一个URL对象
		URL url = new URL(hostStr);
		// 打开一个HttpURLConnection连接
		HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
		// 设置连接超时时间
		urlConn.setConnectTimeout(timeout * 1000);
		urlConn.setReadTimeout(timeout * 1000);
		// 开始连接
		urlConn.connect();
		// 判断请求是否成功
		if (urlConn.getResponseCode() == HTTP_200) {
			// 获取返回的数据
			byte[] data = readStream(urlConn.getInputStream());
			Log.i(TAG_GET, "Get方式请求成功，返回数据如下：");
			Log.i(TAG_GET, new String(data, "UTF-8"));
			// 关闭连接
			urlConn.disconnect();
			return new String(data, "UTF-8");
		} else {
			Log.i(TAG_GET, "Get方式请求失败");
			// 关闭连接
			urlConn.disconnect();
			return "";
		}

	}

	/**
	 * 不带参数 get方式
	 */
	public static String requestByGet(String u, int timeout) throws Exception {
		// String path =
		// "https://reg.163.com/logins.jsp?id=helloworld&pwd=android";
		String path = u;

		// 新建一个URL对象
		URL url = new URL(path);
		// 打开一个HttpURLConnection连接
		HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
		// 设置连接超时时间
		urlConn.setConnectTimeout(timeout * 1000);
		urlConn.setReadTimeout(timeout * 1000);
		// 开始连接
		urlConn.connect();
		// 判断请求是否成功
		if (urlConn.getResponseCode() == HTTP_200) {
			// 获取返回的数据
			byte[] data = readStream(urlConn.getInputStream());
			Log.i(TAG_GET, "Get方式请求成功，返回数据如下：");
			Log.i(TAG_GET, new String(data, "UTF-8"));
			// 关闭连接
			urlConn.disconnect();
			return new String(data, "UTF-8");
		} else {
			Log.i(TAG_GET, "Get方式请求失败");
			// 关闭连接
			urlConn.disconnect();
			return "";
		}

	}

	// 获取连接返回的数据
	private static byte[] readStream(InputStream inputStream) throws Exception {
		byte[] buffer = new byte[1024];
		int len = -1;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		while ((len = inputStream.read(buffer)) != -1) {
			baos.write(buffer, 0, len);
		}
		byte[] data = baos.toByteArray();
		inputStream.close();
		baos.close();
		return data;
	}

	public static Bitmap loadImageFromUrl(String urlStr) {
		Bitmap bitmap = null;
		try {
			URL url = new URL(urlStr);
			URLConnection conn = url.openConnection();
			conn.connect();
			InputStream in = conn.getInputStream();
			BufferedInputStream bis = new BufferedInputStream(in);

			BitmapFactory.Options options = new Options();
			options.inDither = false; /* 不进行图片抖动处理 */
			options.inPreferredConfig = null; /* 设置让解码器以最佳方式解码 */
			options.inSampleSize = 5; /* 图片长宽方向缩小倍数 */

			bitmap = BitmapFactory.decodeStream(bis, null, options);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return bitmap;
	}

}
