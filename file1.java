package com.mycompany.myapp;

import android.os.*;
import android.util.*;
import java.io.*;
import org.apache.http.util.*;
public class file1
{
    static String AppPath,SdcPath,mPath;
	file1(){
		SdcPath=Environment.getExternalStorageDirectory().getPath();
		AppPath=SdcPath;
	}
	file1(String path){
		mPath=path;
		SdcPath=Environment.getExternalStorageDirectory().getPath();
		AppPath=SdcPath+"/"+path;
	}
	
	public static String path(){
		return SdcPath;
	}
	
	private static int bufferd = 1024;
	/*
	 * <!-- 在SDCard中创建与删除文件权限 --> <uses-permission
	 * android:name="android.permission.MOUNT_UNMOUNT_FILESYSTEMS"/> <!--
	 * 往SDCard写入数据权限 --> <uses-permission
	 * android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
	 */

	// =================获取SDCard信息===================
	public static boolean isSdcardAvailable() {
		String status = Environment.getExternalStorageState();
		if (status.equals(Environment.MEDIA_MOUNTED)) {
			return true;
		}
		return false;
	}

	public static long getSDAllSizeKB() {
		// 获取sdcard路径
		File path = Environment.getExternalStorageDirectory();
		StatFs sf = new StatFs(path.getPath());
		// get single block size(Byte)
		long blockSize = sf.getBlockSize();
		// 获取所有数据块数
		long allBlocks = sf.getBlockCount();
		// 返回SD卡大小
		return (allBlocks * blockSize) / 1024; // KB
	}

	/**
	 * free size for normal application
	 * 
	 * @return
	 */
	public static long getSDAvalibleSizeKB() {
		File path = Environment.getExternalStorageDirectory();
		StatFs sf = new StatFs(path.getPath());
		long blockSize = sf.getBlockSize();
		long avaliableSize = sf.getAvailableBlocks();
		return (avaliableSize * blockSize) / 1024;// KB
	}

	// =====================判断File存在==========================
	public static boolean isFileExist(String director) {
		File file = new File(Environment.getExternalStorageDirectory()
							 + File.separator + director);
		return file.exists();
	}

	/**创建文件*/
	public static boolean createFile(String director) {
		if (isFileExist(AppPath+"/"+director)) {
			return true;
		} else {
			File file = new File(AppPath+"/"+ director);
			try{file.createNewFile();}catch(Exception e){return false;}
			return true;
		}
	}
    //创建文件夹
	public static boolean createFolder(String director) {
		if (isFileExist(AppPath+"/"+director)) {
			return true;
		} else {
			File file = new File(AppPath+ "/" + director);
			if (!file.mkdir()) {return false;}
			return true;
		}
	}
//写文件
	public static File write(String filename,String content,boolean isAppend){
		return writeToSDCardFile(AppPath,filename,content,isAppend);
	}
	public static File writeToSDCardFile(String directory, String fileName,
										 String content, boolean isAppend) {
		return writeToSDCardFile(directory, fileName, content, "", isAppend);
	}
	//写文件
	public static File writeToSDCardFile(String directory, 
	                                     String fileName,
										 String content, 
										 String encoding,
										 boolean isAppend) {
		File file = null;
		OutputStream os = null;
		try {
			if (!createFolder(directory)) {
				return file;
			}
			file = new File(AppPath+ "/" + directory + "/" + fileName);
			os = new FileOutputStream(file, isAppend);
			if (encoding.equals("")) {
				os.write(content.getBytes());
			} else {
				os.write(content.getBytes(encoding));
			}
			os.flush();
		} catch (IOException e) {
			
			Log.e("FileUtil", "writeToSDCardFile:" + e.getMessage());
		} finally {
			try {
				if(os != null){
					os.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return file;
	}

	/**
	 * write data from inputstream to SDCard
	 */
	public File writeToSDCardFromInput(String directory, String fileName,
									   InputStream input) {
		File file = null;
		OutputStream os = null;
		try {
			if (createFile(directory)) {
				return file;
			}
			file = new File(Environment.getExternalStorageDirectory()
							+ File.separator + directory + fileName);
			os = new FileOutputStream(file);
			byte[] data = new byte[bufferd];
			int length = -1;
			while ((length = input.read(data)) != -1) {
				os.write(data, 0, length);
			}
			// clear cache
			os.flush();
		} catch (Exception e) {
			Log.e("FileUtil", "" + e.getMessage());
			e.printStackTrace();
		} finally {
			try {
				os.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return file;
	}

	/**
	 * this url point to image(jpg)
	 * 
	 * @param url
	 * @return image name
	 */
	public static String getUrlLastString(String url) {
		String[] str = url.split("/");
		int size = str.length;
		return str[size - 1];
	}
	//读文件

	public static String read(String name)
	{
		return read("",name);
	}
	public static String read(String path,String name){
		String s="";
		try{
			FileInputStream in = new FileInputStream(AppPath+"/"+path+"/"+name);
			int length = in.available();
			byte[] buf = new byte[length];
			in.read(buf);
			s = EncodingUtils.getString(buf,"UTF-8");
			in.close();
		}
		catch(Exception e){s="false";}
		return s;
	}
}
