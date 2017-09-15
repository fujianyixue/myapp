package com.mycompany.myapp;


import android.content.*;
import android.os.*;
import android.widget.*;
import com.mycompany.myapp.*;
import java.io.*;
import org.apache.http.util.*;

public class fso
 {
	 public String p="";
	private Context mContext;
	final String sd0=Environment.getExternalStorageDirectory().getPath();//"/storage/sdcard0/";
	final String sd1="/storage/sdcard1/";
	private String myapp=sd0 + "/MyApp001/";
	
	private static int bufferd = 1024;
	
	public fso(Context context){
		this.mContext = context;
	}
	
	public void toast(String toast) {
		Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
	}
	
	public Boolean file(String s){
			File file=new File(myapp + s);
		if (!file.mkdir()) {return false;}
		return true;
	}

	public Boolean setpath(String s){
	myapp =s;
		return true;
	}
	public String getpath(){
		
		return myapp;
	}
	public boolean isFileExist(String director) {
		File file = new File(myapp + director);
		return file.exists();
	}

	/**创建文件*/
	public boolean createFile(String director) {
		if (isFileExist(director)) {
			return true;
		} else {
			File file = new File(myapp+ director);
			try{file.createNewFile();}catch(Exception e){return false;}
			return true;
		}
	}
    //创建文件夹
	public boolean createFolder(String director) {
		if (isFileExist(director+File.separator)) {
			return true;
		} else {
			File file = new File(myapp + director);
			if (!file.mkdir()) {
				return false;}
			return true;
		}
	}
//写文件
	public boolean w(
		String filename,
		String content,
		String isAppend)
	{
		 write("",filename,content,new Boolean(isAppend));
		 return true;
	}
	public File write(
						String filename,
						String content,
						boolean isAppend)
	{
		return write("",filename,content,isAppend);
	}
	public File write(String directory, String fileName,
										 String content, boolean isAppend) {
		return write(directory, fileName, content, "", isAppend);
	}
	public File write(
						String directory,
						String fileName,
						String content,
						String encoding,
						boolean isAppend) 
	{
		File file = null;
		OutputStream os = null;
		try {
			if (!createFolder(directory)) {
				return file;
			}
			file = new File(myapp+ directory + "/" + fileName);
			os = new FileOutputStream(file, isAppend);
			if (encoding.equals("")) {
				os.write(content.getBytes());
			} else {
				os.write(content.getBytes(encoding));
			}
			os.flush();
		} catch (IOException e) {
			
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
	public File write(	String directory, 
						String fileName,
						InputStream input)
	{
		File file = null;
		OutputStream os = null;
		try {
			if (createFile(myapp+directory)) {
				return file;
			}
			file = new File(myapp + directory + fileName);
			os = new FileOutputStream(file);
			byte[] data = new byte[bufferd];
			int length = -1;
			while ((length = input.read(data)) != -1) {
				os.write(data, 0, length);
			}
			// clear cache
			os.flush();
		} catch (Exception e) {
		} finally {
			try {
				os.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return file;
	}
	//读文件

	public String read(String name)
	{
		return read("",name);
	}
	public String read(String path,String name){
		String s="";
		try{
			FileInputStream in = new FileInputStream(myapp+path+"/"+name);
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
