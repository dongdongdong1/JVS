package com.app.kingvtalking.util;

import android.content.Context;
import android.text.Html;
import android.text.Spanned;
import android.util.Base64;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Locale;

public class StringUtil {

	public static LinkedHashMap<String, String> lastStr = new LinkedHashMap<>();
	private final static SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
	private final static SimpleDateFormat dateFormater2 = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
	private final static SimpleDateFormat dateFormater4 = new SimpleDateFormat("MM-dd HH:mm", Locale.CHINA);
	private final static SimpleDateFormat dateFormater3 = new SimpleDateFormat("HH:mm", Locale.CHINA);

	/**
	 * 把输入流转为字符串
	 * @param is
	 * @return
	 * @throws IOException
	 */
	public static String InputStream2String(InputStream is) throws IOException {

		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		StringBuffer sb = new StringBuffer();
		String line = null;
		while ( (line = br.readLine()) != null) {
			sb.append(line);
			sb.append("\r\n");
		}
		return sb.toString();
	}
	
	/**
	 * 检查字符串是否符合email规则
	 * @param str
	 * @return 字符串为Email，返回 true
	 */
	public boolean isEmail(String str) {
		
		if (str == null) {
			return false;
		}
		str = str.trim();
		/* 假设最短的email为 a@b.c，它的长度为5，不可能再小 */
		if (str.length() < 5) {
			return false;
		}
		String matcher = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";
		if (str.matches(matcher)) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * 密码明文需要经过这个方法进行简单的加密再传到服务端
	 * @param password
	 * @return
	 */
	public final static String simpleEncode(String password) {
		String md5Str1 = calcMD5(password);
		String tail = md5Str1.substring(20);
		return calcMD5(md5Str1 + tail);
	}
	
	/**
	 * 对数据进行简单的加密，防止本地数据被第三方获取
	 * @param str
	 * @return
	 */
	public final static String localEncryption(String str) {
		if (str == null) {
			return null;
		}
		byte[] strByteArray = str.getBytes(Charset.defaultCharset());
		byte[] mba = new byte[strByteArray.length];
		int mbaIndex = 0;
		for (int i = 0 ; i < strByteArray.length; i += 2, mbaIndex ++) {
			mba[mbaIndex] = (byte) (strByteArray[i] - 64);
		}
		for (int j = 1; j < strByteArray.length; j += 2, mbaIndex ++) {
			mba[mbaIndex] = (byte) (strByteArray[j] - 24);
		}
		return Base64.encodeToString(mba, Base64.DEFAULT);
	}
	
	/**
	 * 对经过localEncryption进行过简单加密的数据进行解密
	 * @param str
	 * @return
	 */
	public final static String localDecryption(String str) {
		if (str == null) {
			return null;
		}
		byte[]  mba = Base64.decode(str, Base64.DEFAULT);
		byte[] strByteArray = new byte[mba.length];
		int mbaIndex = 0;
		for (int i = 0; i < strByteArray.length; i += 2, mbaIndex ++) {
			strByteArray[i] = (byte) (mba[mbaIndex] + 64);
		}
		for (int j = 1; j < strByteArray.length; j += 2, mbaIndex ++) {
			strByteArray[j] = (byte) (mba[mbaIndex] + 24);
		}
		return new String(strByteArray, Charset.defaultCharset());
	}
	
	/**
	 * 计算字符串的 md5 值
	 * @param aStr
	 * @return
	 */
	public final static String calcMD5(String aStr)
	{
		MessageDigest md5 = null;
		try {
			md5 = MessageDigest.getInstance("MD5");
			md5.reset(); 
			md5.update(aStr.getBytes("UTF-8"));// 计算md5
		} catch (NoSuchAlgorithmException e) {
			System.out.println("NoSuchAlgorithmException caught!"); 
		}  catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}  
		
        byte[] m = md5.digest();// 获取结果

        StringBuffer md5StrBuff = new StringBuffer(); 

        for (int i = 0; i < m.length; i++)  {  
        	if (Integer.toHexString(0xFF & m[i]).length() == 1)  
        		md5StrBuff.append("0").append(Integer.toHexString(0xFF & m[i]));  
        	else  
        		md5StrBuff.append(Integer.toHexString(0xFF & m[i]));  
        }  

		return md5StrBuff.toString().toUpperCase(Locale.getDefault());
	}
	
	/**
	 * 返回url中的文件名
	 * @param url
	 * @return
	 */
	public final static String getFileName(String url) {
		String[] splitUrl = url.split("/");
		String fileName = splitUrl[splitUrl.length - 1]; //url.substring(url.lastIndexOf('/') + 1);
		String[] split = fileName.split("\\?");
		if (split.length > 1 && fileName.contains("time")) {
			int timeIndex = fileName.indexOf("time=");
			if (timeIndex > -1) {
				int andIndex = fileName.indexOf('&', timeIndex);
				fileName = fileName.subSequence(timeIndex + 5, andIndex > 0 ? andIndex : fileName.length()) + "-" + split[0];
			}
		}
		if (splitUrl.length > 2)
			return splitUrl[splitUrl.length - 2] + '-' + fileName;
		else 
			return fileName;
	}
	
	/**
	 * 判断一个字符串是否为空(对象为空或字符串去掉空格后长度为0即为空)
	 * @param str
	 * @return true 表示字符串为空字符串
	 */
	public final static boolean isEmptyString(String str) {
		return str == null || str.trim().length() == 0;
	}
	
	public final static boolean isEqualsString(String str1, String str2) {
		if (!StringUtil.isEmptyString(str1) && !StringUtil.isEmptyString(str2)) {
			return str1.equals(str2);
		} else if (StringUtil.isEmptyString(str1) && StringUtil.isEmptyString(str2)) {	// 两个均为空
			return true;
		} else {	// 仅有一个为空
			return false;
		}
	}
	

	
	/**
	 * 把 error 加工成错误信息
	 * @param error
	 * @return
	 */
	public final static Spanned getError(String error) {
		String format = "<font color=#E10979>%s</font>";
		return Html.fromHtml(String.format(format, error));
	}
	


	
	/**
	 * 分：时格式化，假如被格式成：xx"x'
	 * @param format
	 * @param millisecond
	 * @return
	 */
	public static final String msFormat(String format, long milliseconds) {
		int seconds = (int) (milliseconds / 1000);
		if (seconds < 60) {
			return String.format(format, "00", get2bit(seconds));
		} else {
			return String.format(format, get2bit(seconds / 60), get2bit(seconds % 60));
		}
	}
	
	/**
	 * 数字不足2位就在前面加0
	 * @param num
	 * @return
	 */
	private static final String get2bit(int num) {
		if (num < 10) {
			return "0" + num;
		} else {
			return "" + num;
		}
	}


	/**
	 * 以友好的方式显示时间。先按照时常判断 刚刚(2分钟内) n分钟前(1小时内),超出一小时按照日历判断 昨天 今天 月-日
	 *
	 * @param sdate
	 * @return
	 */
	public static String friendly_time(String sdate) {
		Date time = toDate(sdate);// 传入时间
		if (time == null) {
			return "Unknown";
		}
		String ftime = "";
		Calendar cal = Calendar.getInstance();// 当前时间
		Calendar yescal = Calendar.getInstance();// 昨天时间
		yescal.add(Calendar.DAY_OF_MONTH, -1);

		// 判断是否是同一天
		String curDate = dateFormater2.format(cal.getTime());// 当前时间日期
		String yescurDate = dateFormater2.format(yescal.getTime());// 昨天日期
		String paramDate = dateFormater2.format(time);// 传入时间的日期
		int hour = (int) ((cal.getTimeInMillis() - time.getTime()) / 3600000);// 和当前相差小时数
		int minuts = (int) ((cal.getTimeInMillis() - time.getTime()) / 60000);// 和当前相差分钟数

		if (minuts < 1) {// 1分钟之内为“刚刚”
			ftime = "刚刚";
			return ftime;
		} else if (hour == 0) {// 2分钟~1小时为“..分钟前”
			ftime = Math.max(minuts, 2) + "分钟前";
			return ftime;
		}

		if (curDate.equals(paramDate)) {// 如果同一天
			ftime = "今天 "+dateFormater3.format(time);
		} else if (yescurDate.equals(paramDate)) {// 如果是当前时间的昨天等于传入时间
			ftime = "昨天 "+dateFormater3.format(time);
		} else {
			ftime = dateFormater4.format(time);
		}
		return ftime;
	}


	/**
	 * 将字符串转位日期类型
	 *
	 * @param sdate
	 * @return
	 */
	public static Date toDate(String sdate) {
		try {
			return dateFormater.parse(sdate);
		} catch (ParseException e) {
			return null;
		}
	}


	public static int Dp2Px(Context context, float dp) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dp * scale + 0.5f);
	}
}
