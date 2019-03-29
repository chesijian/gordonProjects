package com.state.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;
import org.springframework.util.FileCopyUtils;

import com.state.util.sys.SystemConstUtil;

public class FileManager {

	private static final InputStream Response = null;

	/**
	 * @param args
	 */
	// 新建文件夹即目录
	public static void newMulu() {
		String filePath = new StringBuffer("D:\\haha\\123\\").toString();
		filePath = filePath.toString();// 中文转换
		File myFilePath = new File(filePath);
		if (!myFilePath.exists()) {
			myFilePath.mkdir();
		}
	}

	// 新建文件
	public static void newFile() throws IOException {
		String filePath = new StringBuffer("D:/haha/你好.doc").toString();
		filePath = filePath.toString();
		File myFilePath = new File(filePath);
		if (!myFilePath.exists()) {
			myFilePath.createNewFile();
			FileWriter resultFile = new FileWriter(myFilePath);
			PrintWriter myFile = new PrintWriter(resultFile);

			String strContent = "中文测试".toString();
			myFile.println(strContent);
			resultFile.close();
		}

	}

	/**
	 * 如果flag == true，清空内容重新写，否则追加到末尾
	 * 
	 * @param filePath
	 * @param content
	 * @param flag
	 */
	public static void writeToFile(String filePath, String content, boolean flag) {
		File file = new File(filePath);
		// System.out.println(file.exists());
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			file.delete();
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		// System.out.println("222"+file.getAbsolutePath());

		if (flag == false) {
			try {
				// 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
				FileWriter writer = new FileWriter(file, true);
				writer.write(content);
				writer.close();

			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {

			// System.out.println("---"+content);
			try {
				FileOutputStream os = new FileOutputStream(file);
				os.write(URLDecoder.decode(content, "utf-8").getBytes("utf-8"));
				// fw.write(URLDecoder.decode(attachment.getContent(),
				// "utf-8"));
				os.flush();
				os.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		file.setWritable(false);
	}

	public static synchronized String readRandomAccess(String filePath) {
		try {

			// sleep(5000);

			File file = new File(filePath);

			// 给该文件加锁
			RandomAccessFile fis = new RandomAccessFile(file, "rw");
			FileChannel fcin = fis.getChannel();
			FileLock flin = null;
			while (true) {
				try {
					flin = fcin.tryLock();
					break;
				} catch (Exception e) {
					System.out.println("有其他线程正在操作该文件，当前线程休眠1000毫秒");
					Thread.sleep(1000);
				}

			}

			byte[] buf = new byte[1024];
			StringBuffer sb = new StringBuffer();
			while ((fis.read(buf)) != -1) {
				//System.out.println("**************"+new String(buf, "utf-8"));
				sb.append(new String(buf, "utf-8"));
				buf = new byte[1024];
			}

			// System.out.println("督导的内容------------------------"+sb.toString());

			flin.release();
			fcin.close();
			fis.close();
			fis = null;
			// Calendar calend=Calendar.getInstance();
			// System.out.println("读文件共花了"+(calend.getTimeInMillis()-calstart.getTimeInMillis())+"秒");
			return sb.toString();
			// }
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static synchronized void writeRandomAccess(String filePath, String content) {

		// System.out.println("222"+file.getAbsolutePath());
		File file = new File(filePath);
		// System.out.println("-------------"+filePath);
		try {
			if (!file.exists()) {
				file.createNewFile();
			}
			// 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
			// FileWriter writer = new FileWriter(file, true);
			// writer.write(content);
			// writer.close();
			// 对该文件加锁
			RandomAccessFile out = new RandomAccessFile(file, "rw");
			FileChannel fcout = out.getChannel();
			FileLock flout = null;
			while (true) {
				try {
					flout = fcout.tryLock();
					break;
				} catch (Exception e) {
					System.out.println("有其他线程正在操作该文件，当前线程休眠1000毫秒");
					try {
						Thread.sleep(500);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}

			}
			long fileLength = out.length();
			// 将写文件指针移到文件尾。
			out.seek(fileLength);
			out.write(new String(content.getBytes(), "utf-8").getBytes());
			flout.release();
			fcout.close();
			out.close();
			out = null;
		} catch (IOException e) {
			e.printStackTrace();
		}

		// file.setWritable(false);
	}

	// 删除文件
	public static void delFile() {
		String filePath = new StringBuffer("D:/test/111.txt").toString();
		filePath = filePath.toString();
		File myDelFile = new File(filePath);
		myDelFile.delete();
	}

	// 文件拷贝
	public static void copyFile(String file1, String file2) throws IOException {
		int bytesum = 0;
		int byteread = 0;
		InputStream inStream = new FileInputStream(file1);
		FileOutputStream fs = new FileOutputStream(file2);
		byte[] buffer = new byte[1000];
		int length;
		while ((byteread = inStream.read(buffer)) != -1)// read:从输入流中读取一定数量的字节，并将其存储在缓冲区数组
														// b 中。以整数形式返回实际读取的字节数
		{
			// System.out.println("byteread-->"+byteread);
			bytesum += byteread;
			// System.out.println(bytesum);
			// 将指定 byte 数组中从偏移量 off 开始的 len 个字节写入此文件输出流。
			fs.write(buffer, 0, byteread);
		}
		inStream.close();
		fs.close();
	}

	public static void copyFile(File file1, File file2) throws IOException {
		int bytesum = 0;
		int byteread = 0;
		InputStream inStream = new FileInputStream(file1);
		FileOutputStream fs = new FileOutputStream(file2);
		byte[] buffer = new byte[1000];
		int length;
		while ((byteread = inStream.read(buffer)) != -1)// read:从输入流中读取一定数量的字节，并将其存储在缓冲区数组
														// b 中。以整数形式返回实际读取的字节数
		{
			// System.out.println("byteread-->"+byteread);
			bytesum += byteread;
			// System.out.println(bytesum);
			// 将指定 byte 数组中从偏移量 off 开始的 len 个字节写入此文件输出流。
			fs.write(buffer, 0, byteread);
		}
		inStream.close();
		fs.close();
	}

	// 文件拷贝
	public static void copyFile(InputStream in, OutputStream out) throws IOException {
		// 方法一：
		// IOUtils.copy(in, fos);
		// 方法二：
		/*
		 * int temp = -1; while((temp = in.read()) != -1){ fos.write(temp); }
		 * fos.flush(); fos.close(); in.close();
		 */
		// 方法三
		FileCopyUtils.copy(in, out);
	}

	// 整个文件夹拷贝
	public static void copyMulu() throws IOException {
		String url1 = new StringBuffer("D:/haha/").toString();
		String url2 = new StringBuffer("D:/ahah/").toString();
		File in = new File(url1);
		File out = new File(url2);
		// System.out.println("in-->"+in.getAbsolutePath());
		// System.out.println("out-->"+out.getAbsolutePath());

		(new File(url2)).mkdirs();
		File[] file = (new File(url1)).listFiles();
		for (int i = 0; i < file.length; i++) {
			if (file[i].isFile()) {
				file[i].toString();
				FileInputStream input = new FileInputStream(file[i]);
				FileOutputStream output = new FileOutputStream(url2 + "/" + (file[i].getName().toString()));
				byte[] b = new byte[1024 * 5];
				int len;
				while ((len = input.read(b)) != -1) {
					output.write(b, 0, len);
				}
				output.flush();
				output.close();
				input.close();
			}
		}
	}

	// 文件下载
	public static void downFile() throws IOException {
		String fileName = "你好.txt".toString();
		// 读到流程
		InputStream inStream = new FileInputStream("D:/haha/你好.txt");
		// 设置输出格式
		// response.reset();
		// response.addHeader("Content-Disposition","attachment;filename=\""+fileName+"\"");
		// //循环取出流中的数据
		byte[] b = new byte[100];
		@SuppressWarnings("unused")
		int len;
		while ((len = inStream.read(b)) > 0) {
			// response.getOutputStream().write(b,0,len);
		}
		inStream.close();
	}

	// 数据库字段中文件的下载
	public static void downFile2() {
		// int bytesum=0;
		// int byteread=0;
		// //打开数据库
		// ResultSet result=null;
		// String Sql=null;
		// PreparedStatement prestmt=null;
		// //取得数据库中的数据
		// Sql="select * from table";
	}

	// 把网页保存成文件
	public static void saveWebFile() throws MalformedURLException {
		URL stdURL = null;
		BufferedReader stdIn = null;
		PrintWriter stdOut = null;
		try {

			stdURL = new URL("http://www.qq.com");

		} catch (Exception e) {
			e.printStackTrace();
		}

		try {

			stdIn = new BufferedReader(new InputStreamReader(stdURL.openStream()));
			stdOut = new PrintWriter(new BufferedWriter(new FileWriter("D:/haha/qq.html")));

		} catch (IOException e) {
			e.printStackTrace();
		}
		/** 把URL制定的页面以流的形式读出，写出制定的文件 **/
		try {

			String strHtml = "";
			while ((strHtml = stdIn.readLine()) != null) {
				stdOut.println("strHtml-->" + strHtml);
			}

		} catch (IOException e) {
			try {
				throw e;
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} finally {
			try {

				if (stdIn != null) {
					stdIn.close();
				}
				if (stdOut != null) {
					stdOut.close();
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	// 直接下载网上文件
	public static void downloadFile() throws IOException {
		int bytesum = 0;
		int byteread = 0;
		URL url = new URL("http://58.248.245.233:82/down/acdsee.zip");
		// URL url=new URL("http://192.168.1.103/y/spket-1.6.22.zip");
		URLConnection conn = url.openConnection();
		InputStream inStream = conn.getInputStream();
		FileOutputStream fs = new FileOutputStream("D:/test/acdsee.zip");
		// fs.
		byte[] buffer = new byte[1444];
		int length;
		while ((byteread = inStream.read(buffer)) != -1) {
			// out.println("<DT><B>"+byteread+"</B></DT>");
			System.out.println("正在下载-->" + new Date());
			bytesum += byteread;
			System.out.println("bytesum-->" + bytesum);
			fs.write(buffer, 0, byteread);
		}
		System.out.println("下载完毕");
	}

	// 按行读文件
	public static String readFile(String fileDir) {
		StringBuilder resultString = new StringBuilder();
		try {
			// FileReader myFileReader=new FileReader("D:/haha/你好.doc");
			FileReader myFileReader = new FileReader(fileDir);
			BufferedReader myBufferedReader = new BufferedReader(myFileReader);
			String myString = null;

			while ((myString = myBufferedReader.readLine()) != null) {
				resultString.append(myString);// .append("\n");
			}
			myFileReader.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resultString.toString();
	}

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub

		// FileManager.newMulu();//创建文件夹
		// FileManager.newFile();//创建文件
		// FileManager.delFile();// 删除文件
		// FileManager.copyFile();//拷贝文件
		// FileManager.copyMulu();//拷贝文件夹
		// FileManager.saveWebFile();//保存网页
		// FileManager.downloadFile();//下载网上文件
		// FileManager.readFile();
		/*
		 * File file = new File("c:/index.jsp"); byte[] aBuffer =
		 * FileManager.getBytes(file); BASE64Encoder encode = new
		 * BASE64Encoder(); String content = encode.encode(aBuffer);
		 * System.out.println(content);
		 */
		File file = new File("c:/test");
		FileManager.delete(file);

	}

	/**
	 * 递归删除文件
	 * 
	 * @author 大雄
	 * @Title delete
	 * @Date 2016-6-30
	 * @Description TODO
	 * @param file
	 * @Return void
	 */
	public static void delete(File file) {
		if (file.exists()) {
			if (file.isDirectory()) {
				File[] files = file.listFiles();
				if (files != null) {
					for (File c : files) {
						delete(c);
					}
				}
			}
			file.delete();
		}
	}

	/**
	 * 删除文件
	 * 
	 * @description
	 * @author 大雄
	 * @date 2016年9月26日下午4:04:10
	 * @param filePath
	 */
	public static void delete(String filePath) {
		delete(new File(filePath));
	}

	/**
	 * 获取文件大小
	 * 
	 * @param fileS
	 * @return
	 */
	public static String FormetFileSize(long fileS) {// 转换文件大小
		DecimalFormat df = new DecimalFormat("#.00");
		String fileSizeString = "";
		if (fileS < 1024) {
			fileSizeString = df.format((double) fileS) + "B";
		} else if (fileS < 1048576) {
			fileSizeString = df.format((double) fileS / 1024) + "K";
		} else if (fileS < 1073741824) {
			fileSizeString = df.format((double) fileS / 1048576) + "M";
		} else {
			fileSizeString = df.format((double) fileS / 1073741824) + "G";
		}
		return fileSizeString;
	}

	/**
	 * 文件转换为数组
	 * 
	 * @author 大雄
	 * @Title getBytes
	 * @Date 2016-6-30
	 * @Description TODO
	 * @param file
	 * @return
	 * @Return byte[]
	 */
	public static byte[] getBytes(File file) {
		byte[] buffer = null;
		try {
			FileInputStream fis = new FileInputStream(file);
			ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
			byte[] b = new byte[1000];
			int n;
			while ((n = fis.read(b)) != -1) {
				bos.write(b, 0, n);
			}
			fis.close();
			bos.close();
			buffer = bos.toByteArray();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return buffer;
	}

	/**
	 * 压缩文件
	 * 
	 * @description
	 * @author 大雄
	 * @date 2016年9月26日下午3:55:08
	 * @param strs
	 * @param zipname
	 * @param temppath
	 * @throws IOException
	 */
	public static void writeZip(String[] strs, String zipname, String temppath) throws IOException {
		String[] files = strs;
		OutputStream os = new BufferedOutputStream(new FileOutputStream(temppath + "//" + zipname));
		ZipOutputStream zos = new ZipOutputStream(os);
		byte[] buf = new byte[8192];
		int len;
		for (int i = 0; i < files.length; i++) {
			if (files[i] != null) {
				File file = new File(files[i]);
				if (!file.isFile())
					continue;
				ZipEntry ze = new ZipEntry(file.getName());
				zos.putNextEntry(ze);
				BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
				while ((len = bis.read(buf)) > 0) {
					zos.write(buf, 0, len);
				}
				zos.closeEntry();
				bis.close();
			}

		}
		for (int i = 0; i < files.length; i++) {
			if (files[i] != null) {
				File file = new File(files[i]);
				file.delete();
			}
		}
		zos.setEncoding("GBK");
		zos.closeEntry();
		zos.close();
		os.close();
	}

}
