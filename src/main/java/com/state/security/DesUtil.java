package com.state.security;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;
/**
 * 可以和C#加密解密交互
 * @author 大雄
 * @Description TODO
 * @Date 2014-6-5
 */
@SuppressWarnings("restriction")
public class DesUtil {
	
	public static byte[] _key = null;
	public static byte[] _keyiv = { 1, 2, 3, 4, 5, 6, 7, 8 };
	public static BASE64Decoder base64Decode = new BASE64Decoder();
	public static BASE64Encoder base64Encode = new BASE64Encoder();
	public static DESedeKeySpec _spec_e = null;
	public static DESedeKeySpec _spec_d = null;
	public static Cipher _cipher_e = null;
	public static Cipher _cipher_d = null;
	private static final DesUtil single = new DesUtil();
	static{
		try {
			_key = base64Decode.decodeBuffer("YWJjZGVmZ2hpamtsbW5vcHFyc3R1dnd4");
			_spec_e = new DESedeKeySpec(_key);
			Key deskey = null;
	        SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");
	        deskey = keyfactory.generateSecret(_spec_e);
	        _cipher_e = Cipher.getInstance("desede" + "/ECB/PKCS5Padding");
	        _cipher_e.init(Cipher.DECRYPT_MODE, deskey);
	        
	        _spec_d = new DESedeKeySpec(_key);
	        Key deskey_d = null;
	        SecretKeyFactory keyfactory_d = SecretKeyFactory.getInstance("desede");
	        deskey_d = keyfactory_d.generateSecret(_spec_d);
	        _cipher_d = Cipher.getInstance("desede" + "/ECB/PKCS5Padding");
	        _cipher_d.init(Cipher.DECRYPT_MODE, deskey_d);
	        
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	// 静态工厂方法
		public static DesUtil getInstance() {
			return single;
		}
	
	/**
	 * 自定义加密
	 * @author 大雄
	 * @Title encodeECB
	 * @Date 2014-6-5
	 * @Description TODO
	 * @Params @param str
	 * @Params @return
	 * @Return String
	 */
	public static String encodeECB(String str){
		try {
			byte[] data = str.getBytes("gbk");
			byte[] str3 = des3EncodeECB(_key,data );
			return base64Encode.encode(str3);
		}  catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static String decodeECB(String str){
		try {
			byte[] str3 = base64Decode.decodeBuffer(str);
			byte[] str4 = ees3DecodeECB(_key, str3);
			return new String(str4);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	@SuppressWarnings("restriction")
	public static String encode(String enStr){
		try {
			byte[] str3 = des3EncodeECB(_key,enStr.getBytes("gbk") );
			return base64Encode.encode(str3);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return enStr;
	}
	
	@SuppressWarnings("restriction")
	public static String decode(String enStr){
		try {
			byte[] str = ees3DecodeECB(_key,base64Decode.decodeBuffer(enStr) );
			return new String(str, "utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return enStr;
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
					resultString.append(myString);//.append("\n");
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
					//os.write(URLDecoder.decode(content, "utf-8").getBytes("utf-8"));
					os.write(content.getBytes("gbk"));
					// fw.write(URLDecoder.decode(attachment.getContent(), "utf-8"));
					os.flush();
					os.close();
					/*
					OutputStreamWriter osw = new OutputStreamWriter(new
							 FileOutputStream(filePath, true),"gbk");
							 osw.write(content.toString()); osw.close();
					*/
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			file.setWritable(false);
		}
		
    @SuppressWarnings("restriction")
	public static void main(String[] args) throws Exception {
    	
    	//2 D:\\temp\\guodiao\\cbpm\\data\\CBPM_国调_20161205.dat 
    	//D:\\temp\\guodiao\\cbpm\\data\\CBPM_国调_20161205.dattt
    	
    	//String type = args[0];
    	//String srcFilePath = args[1];
    	//String desFilePath = args[2];
    	
    	String type = "1";
    	String srcFilePath = "D:\\temp\\guodiao\\cbpm\\data\\CBPM_国调_20161205.dattt";
    	String desFilePath = "D:\\temp\\guodiao\\cbpm\\data\\CBPM_国调_20161205.datt";
    	System.out.println("srcFilePath---"+srcFilePath); 	
    	File srcFile = new File(srcFilePath);
    	if(type == null){
    		throw new Exception("请选择加密或解密");
    	}
    	String str = "加密";
    	if(type.equals("1")){
    		str = "解密";
    	}
    	if(!srcFile.exists()){
    		throw new Exception(str+"的文件不存在");
    	}
    	
    	String content = readFile(srcFilePath);
    	System.out.println("content--"+content);
    	if(content == null || content.equals("")){
    		throw new Exception(str+"文件内容为空");
    	}
    	//解密
    	String desContent = null;
    		try{
    			if(type.equals("1")){
    				desContent = decode(content);
    			}else{
    				desContent = encodeECB(content);
    			}
    			
    		}catch(Exception e){
    			throw new Exception(str+"失败");
    		}
    		System.out.println("desContent--"+desContent);
    	writeToFile(desFilePath, desContent, true);
    	/*
    	String s = encode("22");
    	System.out.println(s);
    	System.out.println(decode(s));
    	
        //byte[] key=base64Decode.decodeBuffer("YWJjZGVmZ2hpamtsbW5vcHFyc3R1dnd4");
        byte[] keyiv = { 1, 2, 3, 4, 5, 6, 7, 8 };
        byte[] data="admin".getBytes("UTF-8");
        
        System.out.println("ECB加密解密");
        byte[] str3 = des3EncodeECB(_key,data );
        byte[] str4 = ees3DecodeECB(_key, str3);
        //new BASE64Decoder().decodeBuffer("");
        System.out.println(base64Encode.encode(str3));
        System.out.println(new String(str4, "UTF-8"));
        System.out.println();
        System.out.println("CBC加密解密");
        byte[] str5 = des3EncodeCBC(_key, keyiv, data);
        byte[] str6 = des3DecodeCBC(_key, keyiv, str5);
        System.out.println(base64Encode.encode(str5));
        System.out.println(new String(str6, "UTF-8"));
        
        
        System.out.println(encodeECB("0"));
        */
    }
    /**
     * ECB加密,不要IV
     * @param key 密钥
     * @param data 明文
     * @return Base64编码的密文
     * @throws Exception
     */
    public static byte[] des3EncodeECB(byte[] key, byte[] data)
            throws Exception {
        Key deskey = null;
        DESedeKeySpec spec = new DESedeKeySpec(key);
        SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");
        deskey = keyfactory.generateSecret(spec);
        Cipher cipher = Cipher.getInstance("desede" + "/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, deskey);
        byte[] bOut = cipher.doFinal(data);
    	//byte[] bOut = cipher.doFinal(data);
        return bOut;
    }
    /**
     * ECB解密,不要IV
     * @param key 密钥
     * @param data Base64编码的密文
     * @return 明文
     * @throws Exception
     */
    public static byte[] ees3DecodeECB(byte[] key, byte[] data)
            throws Exception {
        
//    	Key deskey = null;
//        DESedeKeySpec spec = new DESedeKeySpec(key);
//        SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");
//        deskey = keyfactory.generateSecret(spec);
//        Cipher cipher = Cipher.getInstance("desede" + "/ECB/PKCS5Padding");
        //cipher.init(Cipher.DECRYPT_MODE, deskey);
        byte[] bOut = _cipher_d.doFinal(data);
        return bOut;
    }
    /**
     * CBC加密
     * @param key 密钥
     * @param keyiv IV
     * @param data 明文
     * @return Base64编码的密文
     * @throws Exception
     */
    public static byte[] des3EncodeCBC(byte[] key, byte[] keyiv, byte[] data)
            throws Exception {
        Key deskey = null;
        SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");
        deskey = keyfactory.generateSecret(_spec_e);
        Cipher cipher = Cipher.getInstance("desede" + "/CBC/PKCS5Padding");
        IvParameterSpec ips = new IvParameterSpec(keyiv);
        cipher.init(Cipher.ENCRYPT_MODE , deskey, ips);
       byte[] bOut = cipher.doFinal(data);
        return bOut;
    }
    /**
     * CBC解密
     * @param key 密钥
     * @param keyiv IV
     * @param data Base64编码的密文
     * @return 明文
     * @throws Exception
     */
    public static byte[] des3DecodeCBC(byte[] key, byte[] keyiv, byte[] data)
            throws Exception {
        Key deskey = null;
        SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");
        deskey = keyfactory.generateSecret(_spec_d);
        Cipher cipher = Cipher.getInstance("desede" + "/CBC/PKCS5Padding");
        IvParameterSpec ips = new IvParameterSpec(keyiv);
        cipher.init(Cipher.DECRYPT_MODE, deskey, ips);
        byte[] bOut = cipher.doFinal(data);
        return bOut;
    }
}

