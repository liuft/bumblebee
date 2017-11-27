package com.jx.jebe.bumble.httphand;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;


import org.apache.http.Header;
import org.apache.http.HttpEntity;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class HttpTest {

	public static void main(String[] args) throws Exception {
		String yunsuo_session_verify = "a88dc6ebf24b49e75be4150318c5234";
		String SESSION = "826f9416-ccb7-4763-bfeb-b43b5db2bd9c";
		String code = "m9cc";
		String body = "login_name=robbin1995&user_pwd=XWLZ2017&verify_code=" + code;
		
		Map<String, String> headers = new HashMap<String,String>();
		headers.put("Connection", "keep-alive");
		headers.put("Pragma", "no-cache");
		headers.put("Cache-Control", "no-cache");
		headers.put("Accept", "*/*");
		headers.put("Origin", "http,//wsdj.baic.gov.cn");
		headers.put("X-Requested-With", "XMLHttpRequest");
		headers.put("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.94 Safari/537.36");
		headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		headers.put("Referer", "http,//wsdj.baic.gov.cn/");
		headers.put("Accept-Encoding", "gzip, deflate");
		headers.put("Accept-Language", "zh-CN,zh;q=0.9,en;q=0.8");
		headers.put("Cookie", "yunsuo_session_verify=" + yunsuo_session_verify + "; SESSION=" + SESSION);
		
		HttpRequest request = new HttpRequest();
		request.setUrl("http://wsdj.baic.gov.cn/system/login.do");
		request.setHeaders(headers);
		request.setContent(body);
		
		String res = post(request);
		System.out.println(res);
	}
	
	
	public static String post(HttpRequest request) throws Exception {
		BasicCookieStore cookieStore = new BasicCookieStore();
		CloseableHttpClient httpClient = null;
		CloseableHttpResponse response = null;
		HttpEntity responseEntity = null;

		try {
			httpClient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
			HttpPost post = new HttpPost(request.getUrl());

			Map<String, String> headers = request.getHeaders();
			if (headers == null || headers.size() == 0) { // 默认ContentType.URLEncoded编码
				headers = new HashMap<String, String>();
				headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
			}

			if (headers.size() > 0) {
				Iterator<Entry<String, String>> itr = headers.entrySet().iterator();
				while (itr.hasNext()) {
					Entry<String, String> ent = itr.next();
					post.setHeader(ent.getKey(), ent.getValue());
				}
			}

			ByteArrayEntity byteEntity = new ByteArrayEntity(request.getData());
			post.setEntity(byteEntity);

			response = httpClient.execute(post);
			responseEntity = response.getEntity();

			Header[] resHeaders = response.getAllHeaders();
			for (Header h : resHeaders) {
				System.out.println("header k:" + h.getName() + " v:" + h.getValue());
			}
			
			System.out.println("getStatusCode:" + response.getStatusLine().getStatusCode());
			String ret = new String(EntityUtils.toByteArray(responseEntity));
			return ret;
		} catch (Exception e) {
			throw e;
		} finally {
			if (responseEntity != null) {
				EntityUtils.consume(responseEntity);
			}

			if (response != null) {
				response.close();
			}

			if (httpClient != null) {
				httpClient.close();
			}
		}
	}
	
}
