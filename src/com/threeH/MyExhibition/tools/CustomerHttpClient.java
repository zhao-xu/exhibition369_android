package com.threeH.MyExhibition.tools;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.util.Log;

public class CustomerHttpClient {
    private static final String CHARSET = HTTP.UTF_8;
    private static HttpClient customerHttpClient;
    private static final String TAG = "CustomerHttpClient";

    private CustomerHttpClient() {

    }

    public static synchronized HttpClient getHttpClient() {
        if (customerHttpClient == null) {
            HttpParams params = new BasicHttpParams();
            // 设置基本参数
            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(params, CHARSET);
            HttpProtocolParams.setUseExpectContinue(params, true);
            HttpProtocolParams
                    .setUserAgent(
                            params,
                            "Mozilla/5.0(Linux;U;Android 2.2.1;en-us;Nexus One Build.FRG83)"
                                    + "AppleWebKit/553.1(KHTML,like Gecko)Version/4.0 Mobile Safari/533.1");
            // 超时设置
            /* 从连接池中取连接的超时时间 */
            ConnManagerParams.setTimeout(params, 1000);
			/* 连接超时 */
            HttpConnectionParams.setConnectionTimeout(params, 2000);
			/* 请求超时 */
            HttpConnectionParams.setSoTimeout(params, 4000);

            // 设置HttpClient支持HTTP和HTTPS两种模式
            SchemeRegistry schReg = new SchemeRegistry();
            schReg.register(new Scheme("http", PlainSocketFactory
                    .getSocketFactory(), 80));
            schReg.register(new Scheme("https", PlainSocketFactory
                    .getSocketFactory(), 443));

            // 使用线程安全的连接管理来创建HttpClient
            ClientConnectionManager conMgr = new ThreadSafeClientConnManager(
                    params, schReg);
            customerHttpClient = new DefaultHttpClient(conMgr, params);
        }
        return customerHttpClient;
    }

    public static String post(String url, NameValuePair... params) {

        try {
            // 编码参数
            List<NameValuePair> formparams = new ArrayList<NameValuePair>();
            // 请求参数
            for (NameValuePair p : params) {
                formparams.add(p);
            }
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams,
                    CHARSET);
            // 创建POST请求
            HttpPost request = new HttpPost(url);
            request.setEntity(entity);
            request.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
            // 发送请求
            HttpClient client = getHttpClient();
            HttpResponse response = client.execute(request);
            HttpEntity resEntity = response.getEntity();
            return (resEntity == null) ? null : EntityUtils.toString(resEntity,
                    CHARSET);
        } catch (UnsupportedEncodingException e) {
            return null;
        } catch (ClientProtocolException e) {
            e.getMessage();
            return null;
        } catch (IOException e) {
            System.out.println("链接失败");
            throw new RuntimeException("连接失败", e);
        }
    }

    public static String postJson(String url, String jSonData) {
        HttpClient client = getHttpClient();
        HttpResponse response;

        try {
            HttpPost post = new HttpPost(url);
            StringEntity se = new StringEntity(jSonData);
            se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
            //se.setContentEncoding("UTF-8");
            post.setEntity(se);
            response = client.execute(post);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String get(String url, Map<String, String> params) {
        String result = "";
        StringBuilder urlBuilder = null;
        BufferedReader in = null;
        try {

            urlBuilder = new StringBuilder();
            urlBuilder.append(url);

            if (null != params) {

                urlBuilder.append("?");

                Iterator<Map.Entry<String, String>> iterator = params.entrySet()
                        .iterator();

                while (iterator.hasNext()) {
                    Map.Entry<String, String> param = iterator.next();
                    urlBuilder
                            .append(URLEncoder.encode(param.getKey(), "UTF-8"))
                            .append('=')
                            .append(URLEncoder.encode(param.getValue(), "UTF-8"));
                    if (iterator.hasNext()) {
                        urlBuilder.append('&');
                    }
                }
            }

            URL realUrl = new URL(urlBuilder.toString());
            // 打开和URL之间的连接
            HttpURLConnection conn = (HttpURLConnection) realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
            conn.setConnectTimeout(5000);
            conn.setRequestMethod("GET");
            // 建立实际的连接
            conn.connect();
            // 获取所有响应头字段
            Map<String, List<String>> map = conn.getHeaderFields();
            // 遍历所有的响应头字段
            // for (String key : map.keySet()) {
            // System.out.println(key + "--->" + map.get(key));
            // }
            // 定义BufferedReader输入流来读取URL的响应
            if (conn.getResponseCode() == 200) {
                in = new BufferedReader(
                        new InputStreamReader(conn.getInputStream()));
                String line;
                while ((line = in.readLine()) != null) {
                    result += "\n" + line;
                }
            }
        } catch (Exception e) {
            System.out.println("发送GET请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;

    }

//    public static String put(String url,String data) {
//        String response = "";
//        try {
//            URL mUrl = new URL(url);
//            HttpURLConnection connection = (HttpURLConnection) mUrl.openConnection();
//            connection.setDoOutput(true);
//            connection.setRequestMethod("PUT");
//
//            connection.setHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
//            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
//
//            writer.write(data);
//
//            BufferedReader in = new BufferedReader(
//                    new InputStreamReader(connection.getInputStream()));
//            String line;
//            while ((line = in.readLine()) != null) {
//                response += "\n" + line;
//            }
//            System.out.println(response);
//            writer.close();
//            return response;
//
//        } catch (UnsupportedEncodingException e) {
//            return null;
//        } catch (ClientProtocolException e) {
//            e.getMessage();
//            return null;
//        } catch (IOException e) {
//            System.out.println("链接失败");
//            throw new RuntimeException("连接失败", e);
//        }
//
//    }

    /**
     * get请求
     *
     * @param urlString
     * @param params
     * @return
     */
    public static String getRequest(String urlString, Map<String, String> params) {

        try {
            StringBuilder urlBuilder = new StringBuilder();
            urlBuilder.append(urlString);

            if (null != params) {

                urlBuilder.append("?");

                Iterator<Map.Entry<String, String>> iterator = params.entrySet()
                        .iterator();

                while (iterator.hasNext()) {
                    Map.Entry<String, String> param = iterator.next();
                    urlBuilder
                            .append(URLEncoder.encode(param.getKey(), "UTF-8"))
                            .append('=')
                            .append(URLEncoder.encode(param.getValue(), "UTF-8"));
                    if (iterator.hasNext()) {
                        urlBuilder.append('&');
                    }
                }
            }
Log.i("data",urlBuilder.toString());
            // 创建HttpClient对象
            HttpClient client = new DefaultHttpClient();
            // 发送get请求创建HttpGet对象
            HttpGet getMethod = new HttpGet(urlBuilder.toString());
            HttpResponse response = client.execute(getMethod);
            // 获取状态码
            int res = response.getStatusLine().getStatusCode();
            System.out.println("res======" + res);
            if (res >= 200 && res < 400) {

                StringBuilder builder = new StringBuilder();
                // 获取响应内容
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(response.getEntity().getContent()));

                for (String s = reader.readLine(); s != null; s = reader
                        .readLine()) {
                    builder.append(s);
                }
                return builder.toString();
            }
        } catch (Exception e) {
            e.getMessage();
            System.out.println("211313=======" + e.getMessage());
        }

        return null;
    }

}
