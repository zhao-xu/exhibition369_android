package com.threeH.MyExhibition.service;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import com.threeH.MyExhibition.tools.StreamTool;

public class ImageService {
	/**
	 * ��ȡ����ͼƬ�����
	 * @param path ����ͼƬ·��
	 * @return
	 */
	public static byte[] getImage(String path) throws Exception{
		URL url = new URL(path);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();//����HTTPЭ�����Ӷ���
		conn.setConnectTimeout(5000);
		conn.setRequestMethod("GET");
		if(conn.getResponseCode() == 200){
			InputStream inStream = conn.getInputStream();
			return StreamTool.read(inStream);
		}
		return null;
	}

}
