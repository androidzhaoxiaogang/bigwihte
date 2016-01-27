package com.xst.bigwhite.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.security.MessageDigest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;


/**
 * 企业短信通ESMS的短信发送接口
 * @author gaobq
 *
 */
@Service
@EnableAutoConfiguration
public class SMSManager {

	@Autowired
    private SMSSettings smsSettings;
	
	@Autowired
	public SMSManager(SMSSettings smsSettings){
		this.smsSettings = smsSettings;
		this._uc = smsSettings.getUseraccount();
		this._pwd = smsSettings.getPassword();
	}
	
	String _uc, _pwd; // 帐号，密码
	String _host = "http://c.kf10000.com/sdk/SMS?";

	public String get_pwd() {
		return this.MD5Encode(_pwd);
	}

	/**
	 * 发送短信
	 * 
	 * @param mobiles
	 *            String 接收号码
	 * @param cont
	 *            String 短信内容
	 * @param msgid
	 *            String 短信ID
	 * @return String
	 */
	public String sendSMS(String mobiles, String cont, String msgid) {
		String re = "";
		try {
			cont = URLEncoder.encode(cont, "GBK"); // 短信内容需要编码
			String sendUrl = _host + "cmd=send&uid=" + _uc + "&psw="
					+ this.get_pwd() + "&mobiles=" + mobiles + "&msgid="
					+ msgid + "&msg=" + cont + " ";
			re = submit(sendUrl);
		} catch (Exception ex) {
			
		}
		return re;
	}

	/**
	 * 接收短信
	 * 
	 * @return String
	 */
	public String getMO() {
		String re = "";
		try {
			String moUrl = _host + "cmd=getmo&uid=" + _uc + "&psw="
					+ this.get_pwd() + "";
			re = submit(moUrl);
		} catch (Exception ex) {

		}
		return re;
	}

	/**
	 * 取发送状态
	 * 
	 * @return String
	 */
	public String getStatus() {
		String re = "";
		String getstatusUrl = _host + "cmd=getstatus&uid=" + _uc + "&psw="
				+ this.get_pwd() + "";
		re = submit(getstatusUrl);
		return re;
	}

	/**
	 * GET提交
	 * 
	 * @param url
	 *            String
	 * @return String
	 */
	private String submit(String url) {
		String re = "";
		int byteMax = 50;
		try {
			URL myurl = new URL(url);
			URLConnection urlcon = myurl.openConnection();
			urlcon.setUseCaches(false);
			urlcon.connect();
			InputStream inputstream = urlcon.getInputStream();
			byte[] buff = new byte[byteMax];
			ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream();
			int ins;
			while ((ins = inputstream.read(buff)) >= 0) {
				bytearrayoutputstream.write(buff, 0, ins);
			}
			re = new String(bytearrayoutputstream.toByteArray());
			re.trim();
		} catch (MalformedURLException ex) {
		} catch (IOException ex) {
			/** @todo Handle this exception */
		}

		return re;
	}

	/**
	 * MD5加密
	 * 
	 * @param origin
	 * @return
	 */
	private String MD5Encode(String origin) {
		String resultString = null;
		try {
			resultString = new String(origin);
			MessageDigest md = MessageDigest.getInstance("MD5");
			resultString = byteArrayToHexString(md.digest(resultString
					.getBytes()));
		} catch (Exception ex) {

		}
		return resultString;
	}

	private String byteToHexString(byte b) {
		int n = b;
		if (n < 0) {
			n = 256 + n;
		}
		int d1 = n / 16;
		int d2 = n % 16;
		return hexDigits[d1] + hexDigits[d2];
	}

	public String byteArrayToHexString(byte[] b) {
		StringBuffer resultSb = new StringBuffer();
		for (int i = 0; i < b.length; i++) {
			resultSb.append(byteToHexString(b[i]));
		}
		return resultSb.toString();
	}

	private final static String[] hexDigits = { "0", "1", "2", "3", "4", "5",
			"6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };

}
