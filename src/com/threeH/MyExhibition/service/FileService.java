package com.threeH.MyExhibition.service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import android.content.Context;
import android.os.Environment;

public class FileService {
	private Context context;
	
	public FileService(Context context) {
		this.context = context;
	}

	public void saveToSDCard(String filename, String content)throws Exception {
		File file = new File(Environment.getExternalStorageDirectory(), filename);
		FileOutputStream outStream = new FileOutputStream(file);
		outStream.write(content.getBytes());
		outStream.close();
	}
    public void saveToSDCard(String filename, byte[] data)throws Exception {
        File file = new File(Environment.getExternalStorageDirectory(), filename);
        FileOutputStream outStream = new FileOutputStream(file);
        outStream.write(data);
        outStream.close();
    }
	/**
	 * �����ļ�
	 * @param filename �ļ����
	 * @param content �ļ�����
	 */
	public void save(String filename, String content) throws Exception {
		//˽�в���ģʽ�������������ļ�ֻ�ܱ���Ӧ�÷��ʣ�����Ӧ���޷����ʸ��ļ����������˽�в���ģʽ�������ļ���д���ļ��е����ݻḲ��ԭ�ļ�������
		FileOutputStream outStream = context.openFileOutput(filename, Context.MODE_PRIVATE);
		outStream.write(content.getBytes());
		outStream.close();
	}
	
	/**
	 * �����ļ�
	 * @param filename �ļ����
	 * @param content �ļ�����
	 */
	public void saveAppend(String filename, String content) throws Exception {//ctrl+shift+y / x
		//˽�в���ģʽ�������������ļ�ֻ�ܱ���Ӧ�÷��ʣ�����Ӧ���޷����ʸ��ļ����������˽�в���ģʽ�������ļ���д���ļ��е����ݻḲ��ԭ�ļ�������
		FileOutputStream outStream = context.openFileOutput(filename, Context.MODE_APPEND);
		outStream.write(content.getBytes());
		outStream.close();
	}
	
	/**
	 * �����ļ�
	 * @param filename �ļ����
	 * @param content �ļ�����
	 */
	public void saveReadable(String filename, String content) throws Exception {
		//˽�в���ģʽ�������������ļ�ֻ�ܱ���Ӧ�÷��ʣ�����Ӧ���޷����ʸ��ļ����������˽�в���ģʽ�������ļ���д���ļ��е����ݻḲ��ԭ�ļ�������
		FileOutputStream outStream = context.openFileOutput(filename, Context.MODE_WORLD_READABLE);
		outStream.write(content.getBytes());
		outStream.close();
	}
	
	/**
	 * �����ļ�
	 * @param filename �ļ����
	 * @param content �ļ�����
	 */
	public void saveWriteable(String filename, String content) throws Exception {
		//˽�в���ģʽ�������������ļ�ֻ�ܱ���Ӧ�÷��ʣ�����Ӧ���޷����ʸ��ļ����������˽�в���ģʽ�������ļ���д���ļ��е����ݻḲ��ԭ�ļ�������
		FileOutputStream outStream = context.openFileOutput(filename, Context.MODE_WORLD_WRITEABLE);
		outStream.write(content.getBytes());
		outStream.close();
	}
	
	/**
	 * �����ļ�
	 * @param filename �ļ����
	 * @param content �ļ�����
	 */
	public void saveRW(String filename, String content) throws Exception {
		//˽�в���ģʽ�������������ļ�ֻ�ܱ���Ӧ�÷��ʣ�����Ӧ���޷����ʸ��ļ����������˽�в���ģʽ�������ļ���д���ļ��е����ݻḲ��ԭ�ļ�������
		FileOutputStream outStream = context.openFileOutput(filename, Context.MODE_WORLD_WRITEABLE + Context.MODE_WORLD_READABLE);
		outStream.write(content.getBytes());
		outStream.close();
	}
	
	/**
	 * ��ȡ�ļ�����
	 * @param filename �ļ����
	 * @return �ļ�����
	 * @throws Exception
	 */
	public byte[] read(String filename) throws Exception {
		FileInputStream inStream = new FileInputStream(filename);//context.openFileInput(filename);
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		while((len = inStream.read(buffer)) != -1){
			outStream.write(buffer, 0, len);
		}
		byte[] data = outStream.toByteArray();
		return data;
	}


}
