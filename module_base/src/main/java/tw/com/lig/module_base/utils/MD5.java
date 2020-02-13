package tw.com.lig.module_base.utils;

import java.security.MessageDigest;

public class MD5 {

	public final static String md5Encode(String s) {
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
		try {
			byte[] strTemp = s.getBytes();
			// 使用MD5创建MessageDigest對象
			MessageDigest mdTemp = MessageDigest.getInstance("MD5");
			mdTemp.update(strTemp);
			byte[] md = mdTemp.digest();
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte b = md[i];
				// System.out.println((int)b);
				// 將没個數(int)b进行双位元組加密
				str[k++] = hexDigits[b >> 4 & 0xf];
				str[k++] = hexDigits[b & 0xf];
			}
			return new String(str);
		}
		catch (Exception e) {
			return null;
		}
	}

	/**
	 * main函數.
	 * 
	 * @param args
	 *            启动参數
	 * @throws Exception
	 *             Exception
	 */
	public static void main(String... args) throws Exception {
		// System.out.println(md5Encode("123456"));
		System.out.println(1200 % 100);
	}

}