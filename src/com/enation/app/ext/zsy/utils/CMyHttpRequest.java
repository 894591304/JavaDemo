package com.enation.app.ext.zsy.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.URL;
import java.security.SecureRandom;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import org.apache.log4j.Logger;

import com.enation.app.ext.zsy.https.TrustAnyTrustManager;

public class CMyHttpRequest {
	private static Logger m_logger = Logger.getLogger(CMyHttpRequest.class);

	public static String getRequestURLContent(String requestUrl,
			String requestMethod, String outputStr, int nConnectTimeout,
			int nReadTimeOut, String sOutputEncode, String sInputEncode)
			throws Exception {
		sOutputEncode = sOutputEncode == null ? "UTF-8" : sOutputEncode;

		sInputEncode = sInputEncode == null ? "UTF-8" : sInputEncode;
		StringBuffer buffer = new StringBuffer();
		try {
			TrustManager[] tm = { new TrustAnyTrustManager() };
			SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
			sslContext.init(null, tm, new SecureRandom());

			SSLSocketFactory ssf = sslContext.getSocketFactory();

			URL url = new URL(requestUrl);
			HttpsURLConnection httpUrlConn = (HttpsURLConnection) url
					.openConnection();
			httpUrlConn.setSSLSocketFactory(ssf);

			httpUrlConn.setRequestProperty("User-agent", "Mozilla/4.0");
			httpUrlConn.setDoOutput(true);
			if (nReadTimeOut > 0)
				httpUrlConn.setReadTimeout(nReadTimeOut);
			if (nConnectTimeout > 0)
				httpUrlConn.setConnectTimeout(nConnectTimeout);
			httpUrlConn.setDoInput(true);
			httpUrlConn.setUseCaches(false);

			httpUrlConn.setRequestMethod(requestMethod);

			if ("GET".equalsIgnoreCase(requestMethod)) {
				httpUrlConn.connect();
			}

			if (outputStr != null) {
				OutputStream outputStream = httpUrlConn.getOutputStream();

				outputStream.write(outputStr.getBytes("UTF-8"));
				outputStream.close();
			}

			InputStream inputStream = httpUrlConn.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(
					inputStream, "utf-8");
			BufferedReader bufferedReader = new BufferedReader(
					inputStreamReader);

			String str = null;
			while ((str = bufferedReader.readLine()) != null) {
				buffer.append(str);
			}
			bufferedReader.close();
			inputStreamReader.close();

			inputStream.close();
			inputStream = null;
			httpUrlConn.disconnect();
		} catch (ConnectException ce) {
			m_logger.error("http connection timed out.");
			throw ce;
		}
		return buffer.toString();
	}

	public static String getRequestURLContent(String sRequestURL,
			int nConnectTimeout, int nReadTimeOut) throws Exception {
		return getRequestURLContent(sRequestURL, "GET", null, nConnectTimeout,
				nReadTimeOut, null, null);
	}

	public static String getRequestURLContent(String requestURL)
			throws Exception {
		return getRequestURLContent(requestURL, "GET", null, -1, -1, null, null);
	}

	public static String getRequestURLContent(String requestUrl,
			String requestMethod, String outputStr) throws Exception {
		return getRequestURLContent(requestUrl, requestMethod, outputStr, -1,
				-1, null, null);
	}
}
