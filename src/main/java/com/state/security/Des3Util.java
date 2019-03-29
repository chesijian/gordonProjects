package com.state.security;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
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
public class Des3Util {
	
	public static byte[] _key = null;
	public static byte[] _keyiv = { 1, 2, 3, 4, 5, 6, 7, 8 };
	public static BASE64Decoder base64Decode = new BASE64Decoder();
	public static BASE64Encoder base64Encode = new BASE64Encoder();
	public static DESedeKeySpec _spec_e = null;
	public static DESedeKeySpec _spec_d = null;
	public static Cipher _cipher_e = null;
	public static Cipher _cipher_d = null;
	private static final Des3Util single = new Des3Util();
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
		public static Des3Util getInstance() {
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
			byte[] data = str.getBytes("UTF-8");
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
			return new String(str4, "UTF-8");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	@SuppressWarnings("restriction")
	public static String encode(String enStr){
		try {
			byte[] str3 = des3EncodeECB(_key,enStr.getBytes("UTF-8") );
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
			return new String(str, "UTF-8");
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
	public static void main(String[] args) throws Exception {
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

