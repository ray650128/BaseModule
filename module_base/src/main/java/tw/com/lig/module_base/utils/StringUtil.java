package tw.com.lig.module_base.utils;

import android.content.Context;
import android.os.Build;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Formatter;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class StringUtil {

    public static Pattern numericPattern = Pattern.compile("^[0-9\\-]+$");
    public static final String PHONE_FORMAT = "^1[0-9]{10}$";
    public static final String EMAIL_FORMAT = "^[a-zA-Z0-9_.-]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*\\.[a-zA-Z0-9]{2,6}$";
//    public static final String EMAIL_FORMAT = "^[0-9a-zA-Z][_.0-9a-zA-Z-]{0,43}@([0-9a-zA-Z][0-9a-zA-Z-]{0," +
//            "30}[0-9a-zA-Z].){1,4}[a-zA-Z]{2,4}$";
    public static final String VERIFY_CODE_FORMAT = "^\\d{4}$";
    public static final String PASSWORD_LEGAL_CHARACTERS = "[a-zA-Z0-9]{6,20}";
    public static final String SHENFENZHENG = "^\\d{15}$|^\\d{17}[0-9Xx]$";

    private StringUtil() {
    }

    public static boolean isNullString(String str) {
        return (null == str || isBlank(str.trim()) || "null".equals(str.trim().toLowerCase())) ? true : false;
    }

    public static boolean isEmpty(CharSequence cs) {
        return cs == null || cs.length() == 0;
    }

    public static boolean isBlank(CharSequence cs) {
        int strLen;
        if (cs == null || (strLen = cs.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (Character.isWhitespace(cs.charAt(i)) == false) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断字串是否為空或空字元
     *
     * @param strSource 源字串
     * @return true表示為空，false表示不為空
     */
    public static boolean isPassWord(final String strSource) {
        return (strSource == null) || strSource.matches(PASSWORD_LEGAL_CHARACTERS);
    }

    /**
     * 判断是否
     *
     * @param strSource 源字串
     * @return true表示為空，false表示不為空
     */
    public static boolean isNull(final String strSource) {
        return strSource == null || "".equals(strSource.trim());
    }


    /**
     * 判断字串是否為空或空符串。
     *
     * @param str 要判断的字串。
     * @return String 返回判断的结果。如果指定的字串為空或空符串，则返回true；否则返回false。
     */
    public static boolean isNullOrEmpty(String str) {
        return (str == null) || (str.trim().length() == 0);
    }

    /**
     * 去掉字串兩端的空白字元。因為String類裡面的trim()方法不能出現null.trim()的情況，因此這裡重新寫一個工具方法。
     *
     * @param str 要去掉空白的字串。
     * @return String 返回去掉空白後的字串。如果字串為null，则返回null；否则返回str.trim()。 *
     */
    public static String trim(String str) {
        return str == null ? str : str.trim();
    }

    /**
     * 判断参數是否為數字
     *
     * @param strNum 待判断的數字参數
     */
    public static boolean isNum(final String strNum) {
        return strNum.matches("^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$");
    }

    /**
     * 判断参數是否為手機号.
     */
    public static boolean isPhoneNum(final String strPhoneNum) {
        return Pattern.matches(PHONE_FORMAT, strPhoneNum);
    }

    /**
     * 隐藏手機号.
     */
    public static String hidePhoneNum(final String strPhoneNum) {
        if (isNullOrEmpty(strPhoneNum)){
            return "****";
        }
        return strPhoneNum.replaceAll("(\\d{3})\\d{4}(\\d{4})","$1****$2");
    }


    /**
     * 判断参數是否為身份证号
     */
    public static boolean isShenFenNum(final String strPhoneNum) {
        return Pattern.matches(SHENFENZHENG, strPhoneNum);
    }

    /**
     * 隐藏身份证号
     */
    public static String hideShenFenNum(final String strPhoneNum) {
        if (isNullOrEmpty(strPhoneNum)){
            return "****";
        }
        return strPhoneNum.replaceAll("(\\d{4})\\d{10}(\\d{4})","$1****$2");
    }

    /**
     * 判断是否為正确的邮箱格式
     *
     * @param email
     * @return
     */
    public static boolean isEmail(String email) {
        String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))" +
                "([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);
        return m.matches();
    }

    /**
     * 判断是否是一個IP
     *
     * @param IP
     * @return boolean
     */
    public static boolean isIp(String IP) {
        boolean b = false;
        IP = trimSpaces(IP);
        if (IP.matches("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}")) {
            String s[] = IP.split("\\.");
            if (Integer.parseInt(s[0]) < 255)
                if (Integer.parseInt(s[1]) < 255)
                    if (Integer.parseInt(s[2]) < 255)
                        if (Integer.parseInt(s[3]) < 255)
                            b = true;
        }
        return b;
    }

    /**
     * 方法: checkPhone
     * 描述: 提取電話号碼
     *
     * @param content
     * @return ArrayList<String>    返回類型
     */
    public static ArrayList<String> checkPhone(String content) {
        ArrayList<String> list = new ArrayList<String>();
        if (isEmpty(content)) return list;
        Pattern p = Pattern.compile("1([\\d]{10})|((\\+[0-9]{2,4})?\\(?[0-9]+\\)?-?)?[0-9]{7,8}");
        Matcher m = p.matcher(content);
        while (m.find()) {
            list.add(m.group());
        }
        return list;
    }

    /**
     * 是否含有表情符
     * false 為含有表情符
     */
    public static boolean checkFace(String checkString) {
        String reg = "^([a-z]|[A-Z]|[0-9]|[\u0000-\u00FF]|[\u2000-\uFFFF]){1,}$";
        Pattern pattern = Pattern.compile(reg);
        Matcher matcher = pattern.matcher(checkString.replaceAll(" ", ""));
        if (!matcher.matches()) {
            return false;
        }
        return true;
    }

    /**
     * 格式化字串 如果為空，返回“”
     *
     * @param str
     * @return String
     */
    public static String formatString(String str) {
        if (isNullString(str)) {
            return "";
        } else {
            return str;
        }
    }

    /**
     * 獲得文件名称
     *
     * @param path
     * @return String
     */
    public static String getFileName(String path) {
        if (isNullString(path))
            return null;
        int bingindex = path.lastIndexOf("/");
        int endindex = path.lastIndexOf(".");
        return path.substring(bingindex + 1, endindex);
    }

    /**
     * 判断字串是否是數字
     *
     * @param src
     * @return boolean
     */
    public static boolean isNumeric(String src) {
        boolean return_value = false;
        if (src != null && src.length() > 0) {
            Matcher m = numericPattern.matcher(src);
            if (m.find()) {
                return_value = true;
            }
        }
        return return_value;
    }

    /**
     * 自动命名文件,命名文件格式如：IP地址+時間戳+三位随机數 .doc
     *
     * @param ip       ip地址
     * @param fileName 文件名
     * @return String
     */
    public static String getIPTimeRandName(String ip, String fileName) {
        StringBuffer buf = new StringBuffer();
        if (ip != null) {
            String str[] = ip.split("\\.");
            for (int i = 0; i < str.length; i++) {
                buf.append(addZero(str[i], 3));
            }
        }// 加上IP地址
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        buf.append(sdf.format(new Date()));// 加上日期
        Random random = new Random();
        for (int i = 0; i < 3; i++) {
            buf.append(random.nextInt(10));// 取三個随机數追加到StringBuffer
        }
        buf.append("." + getFileExt(fileName));// 加上扩展名
        return buf.toString();

    }

    /**
     * 自动命名文件,命名文件格式如：時間戳+三位随机數 .doc
     *
     * @param fileName
     * @return String
     */
    public static String getTmeRandName(String fileName) {
        return getIPTimeRandName(null, fileName);
    }

    /**
     * 字串补零
     * @param str
     * @param len 多少個零
     * @return
     */
    public static String addZero(String str, int len) {
        StringBuffer s = new StringBuffer();
        s.append(str);
        while (s.length() < len) {
            s.insert(0, "0");
        }
        return s.toString();
    }

    /**
     * 獲得文件扩展名
     *
     * @param filename
     * @return String
     */
    public static String getFileExt(String filename) {
        int i = filename.lastIndexOf(".");// 返回最後一個點的位置
        String extension = filename.substring(i + 1);// 取出扩展名
        return extension;
    }

    /**
     * 將url进行utf-8编碼
     *
     * @param url
     * @return String
     */
    public static final String encodeURL(String url) {
        try {
            return URLEncoder.encode(url, "utf-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 將url进行utf-8解碼
     *
     * @param url
     * @return String
     */
    public static final String decodeURL(String url) {
        try {
            return URLDecoder.decode(url, "utf-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 將字串集合 变為以 separator 分割的字串
     *
     * @param array     字串集合
     * @param separator 分隔符
     * @return String
     */
    public static String join(final ArrayList<String> array, String separator) {
        StringBuffer result = new StringBuffer();
        if (array != null && array.size() > 0) {
            for (String str : array) {
                result.append(str);
                result.append(separator);
            }
            result.delete(result.length() - 1, result.length());
        }
        return result.toString();
    }

    /**
     * 压缩字串
     *
     * @param str
     * @return String
     * @throws IOException
     */
    public static String compress(String str) throws IOException {
        if (str == null || str.length() == 0) {
            return str;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        GZIPOutputStream gzip = new GZIPOutputStream(out);
        gzip.write(str.getBytes());
        gzip.close();
        return out.toString("ISO-8859-1");
    }

    /**
     * 解压缩字串
     *
     * @param str
     * @return String
     * @throws IOException
     */
    public static String uncompress(String str) throws IOException {
        if (str == null || str.length() == 0) {
            return str;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayInputStream in = new ByteArrayInputStream(
                str.getBytes("ISO-8859-1"));
        GZIPInputStream gunzip = new GZIPInputStream(in);
        byte[] buffer = new byte[256];
        int n;
        while ((n = gunzip.read(buffer)) >= 0) {
            out.write(buffer, 0, n);
        }
        return out.toString("UTF-8");
    }

    /**
     * 去除特殊字元或將所有中文標号替換為英文標号
     *
     * @param input
     * @return String
     */
    public static String stringFilter(String input) {
        if (input == null)
            return null;
        input = input.replaceAll("【", "[").replaceAll("】", "]")
                .replaceAll("！", "!").replaceAll("：", ":");// 替換中文標号
        String regEx = "[『』]"; // 清除掉特殊字元
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(input);
        return m.replaceAll("").trim();
    }

    /**
     * 半角字元轉全角字元
     *
     * @param input
     * @return String
     */
    public static String toDBC(String input) {
        if (input == null)
            return null;
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 12288) {
                c[i] = (char) 32;
                continue;
            }
            if (c[i] > 65280 && c[i] < 65375)
                c[i] = (char) (c[i] - 65248);
        }
        return new String(c);
    }

    /**
     * 得到文件的後缀名(扩展名)
     *
     * @param name
     * @return String 後缀名
     */
    public static String getAfterPrefix(String name) throws Exception {
        return name.substring(name.lastIndexOf(".") + 1, name.length());
    }

    /**
     * 分割字串
     *
     * @param values 要分割的内容
     * @param limit  分隔符 例：以“,”分割
     * @return String[] 返回陣列，没有返回null
     */
    public static String[] splitMoreSelect(String values, String limit) {
        if (isNullOrEmpty(values)) {
            return null;
        }
        return values.trim().split(limit);
    }

    /**
     * 將字串陣列轉化為字串
     *
     * @param needvalue
     * @return String 返回字串，否则返回null
     */
    public static String arr2Str(String[] needvalue) {
        String str = "";
        if (needvalue != null) {
            int len = needvalue.length;
            for (int i = 0; i < len; i++) {
                if (i == len - 1) {
                    str += needvalue[i];
                } else {
                    str += needvalue[i] + ",";
                }
            }
            return str;
        } else {
            return null;
        }
    }

    /**
     * 更具配置的string.xml 里的id，得到内容
     *
     * @param context
     * @param id
     * @return String
     */
    public static String getValueById(Context context, int id) {
        return context.getResources().getString(id);
    }

    /**
     * 用于文中强制換行的处理
     *
     * @param oldstr
     * @return String
     */
    public static String replaceStr(String oldstr) {
        oldstr = oldstr.replaceAll("\n", "<br>");// 替換換行
        oldstr = oldstr.replaceAll("\r\n", "<br>");// 替換回车換行
        oldstr = oldstr.replaceAll(" ", "&nbsp;" + " ");// 替換空格
        return oldstr;
    }

    /**
     * 判断是否是數字
     *
     * @param c
     * @return boolean
     */
    public static boolean isNum(char c) {
        if (c >= 48 && c <= 57) {
            return true;
        }
        return false;
    }

    /**
     * 獲得題号 例如：2.本文选自哪篇文章？ 提取題号中的數字 2
     *
     * @param content
     * @return int
     */
    public static int getThemeNum(String content) {
        int tnum = -1;
        if (isNullOrEmpty(content))
            return tnum;
        int a = content.indexOf(".");
        if (a > 0) {
            String num = content.substring(0, a);
            try {
                tnum = Integer.parseInt(num);
            } catch (NumberFormatException e) {
                e.printStackTrace();
                return tnum;
            }
        }
        return tnum;
    }

    // 添加自己的字串操作

    public static String dealDigitalFlags(String str) {
        String result = "";
        if (str == null || str.length() < 0) {
            return null;
        } else {
            int len = str.length();
            for (int i = 0; i < len; i++) {
                String tmp = str.substring(i, i + 1);
                if (tmp.equals("+") || tmp.equals("*") || tmp.equals("=")) {
                    tmp = " " + tmp + " ";
                }
                result += tmp;
            }
        }
        return result;
    }

    public static  String toDoubleFormat(String content) {

        if(StringUtil.isNullOrEmpty(content)){
            return "0";
        } else{
            Double aaa;
            try {
                aaa= Double.valueOf(content);
            } catch (Exception e) {
                aaa=0.00d;
            }
            DecimalFormat df = new DecimalFormat("0.00%");
            String r = df.format(aaa);

            return  r;
        }


    }

    /**
     * 截取序号 例如：01026---->26
     *
     * @param oldnum
     * @return String
     */
    public static String detailNum(String oldnum) {
        if (isNullOrEmpty(oldnum))
            return oldnum;
        int newnum = Integer.parseInt(oldnum);
        return newnum + ".";
    }

    public static String[] getStoreArr(String[] arr) throws Exception {
        String temp;
        for (int i = 0; i < arr.length; i++) {
            for (int j = arr.length - 1; j > i; j--) {
                int a = Integer.parseInt(arr[i]);
                int b = Integer.parseInt(arr[j]);
                if (a > b) {
                    temp = arr[i];
                    arr[i] = arr[j];
                    arr[j] = temp;
                }
            }
        }
        return arr;
    }

    /**
     * 给數字字串排序 如：3，1，2 --->1，2，3
     *
     * @param str
     * @return String
     * @throws Exception
     */
    public static String resetStoreNum(String str) {
        String value = "";
        try {
            if (str == null || str.length() < 1)
                return value;
            String[] results = str.split(",");
            String[] newarr = getStoreArr(results);
            for (int i = 0; i < newarr.length; i++) {
                value += newarr[i] + ",";
            }
            value = value.substring(0, value.length() - 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }

    /**
     * 判断陣列中是否包含某個值
     *
     * @param srcValue
     * @param values
     * @return boolean
     */
    public static boolean arrIsValue(String srcValue, String[] values) {
        if (values == null) {
            return false;
        }
        for (String value : values) {
            if (value.equals(srcValue)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 獲得"."之後的所有内容
     *
     * @param content 原字串
     * @return String
     */
    public static String DeleteOriNumber(String content) {
        if (content.trim().length() > 1) {
            int index = content.indexOf(".");
            String AfterStr = content.substring(index + 1, content.length());
            return AfterStr;
        } else {
            return content;
        }
    }

    /**
     * GBK编碼
     *
     * @param content
     * @return String
     */
    public static String convertToGBK(String content) {
        if (!isEmpty(content)) {
            try {
                content = new String(content.getBytes(), "GBK");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return content;
    }

    private static String trimSpaces(String IP) {// 去掉IP字串前後所有的空格
        while (IP.startsWith(" ")) {
            IP = IP.substring(1, IP.length()).trim();
        }
        while (IP.endsWith(" ")) {
            IP = IP.substring(0, IP.length() - 1).trim();
        }
        return IP;
    }

    /**
     * 方法: distanceSize
     * 描述: 計算距離
     *
     * @param distance 距離數 單位千米
     * @return String  轉換後的距離
     */
    public static String distanceSize(double distance) {
        if (distance < 1.0) return (int) (distance * 1000) + "m";
        String dd = "0";
        try {
            DecimalFormat fnum = new DecimalFormat("##0.00");
            dd = fnum.format(distance);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dd + "km";
    }


    /**
     * 方法: replaceResult
     * 描述: 替換结果字串
     *
     * @param content
     * @return String    返回類型
     */
    public static String replaceResult(String content) {
        if (!isEmpty(content))
            return content = content.replace("\\", "").replace("\"{", "{").replace("}\"", "}");
        return content;
    }

    /**
     * <p>描述:保留一位小數</p>
     *
     * @param value
     * @return 设定文件
     */
    public static String parseStr(String value) {
        if (StringUtil.isNullString(value)) return "0.0";
        DecimalFormat df = new DecimalFormat("######0.0");
        double mvalue = Double.parseDouble(value);
        return df.format(mvalue);
    }

    public static String parseStr(double value) {
        if (value == 0) return "0.0";
        DecimalFormat df = new DecimalFormat("######0.0");
        return df.format(Double.parseDouble(String.valueOf(value)));
    }

    /**
     * 处理自动換行問題
     *
     * @param input 字串
     * @return 设定文件
     */
    public static String toWrap(String input) {
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 12288) {
                c[i] = (char) 32;
                continue;
            }
            if (c[i] > 65280 && c[i] < 65375)
                c[i] = (char) (c[i] - 65248);
        }
        return new String(c);
    }

    /**
     * 位元組陣列轉換成Mac地址
     */
    public static String byteToMac(byte[] resBytes) {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < resBytes.length; i++) {
            String hex = Integer.toHexString(resBytes[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            buffer.append(hex.toUpperCase());
        }
        return buffer.toString();
    }

    /**
     * 位元組資料轉換成十六进制字串
     *
     * @param b
     * @return
     */
    public static String toHexString(byte[] b) {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < b.length; ++i) {
            String s = Integer.toHexString(b[i] & 0xFF);
            if (s.length() == 1) {
                s = "0" + s;
            }
            buffer.append(s + " ");
        }
        return buffer.toString();
    }

    /**
     * 位元組陣列轉為16进制字串
     *
     * @param bytes 位元組陣列
     * @return 16进制字串
     */
    public static String byteArrayToHexString(byte[] bytes) {
        @SuppressWarnings("resource")
        Formatter fmt = new Formatter(new StringBuilder(bytes.length * 2));
        for (byte b : bytes) {
            fmt.format("%02x", b);
        }
        return fmt.toString();
    }

    /**
     * 對象轉整數
     *
     * @param obj
     * @return 轉換異常返回 0
     */
    public static int toInt(Object obj) {
        if (obj == null)
            return 0;
        return toInt(obj.toString(), 0);
    }

    /**
     * 字串轉整數
     *
     * @param str
     * @param defValue
     * @return
     */
    public static int toInt(String str, int defValue) {
        try {
            return Integer.parseInt(str);
        } catch (Exception e) {
        }
        return defValue;
    }

    /**
     * 判断给定字元是Ascill字元还是其它字元(如汉，日，韩文字元)
     */
    public static boolean isLetter(final char c) {
        int k = 0xFF;
        if (c / k == 0) {
            return true;
        }
        return false;
    }

    /**
     * 計算字元的长度  Ascii字元算一個长度 非Ascii字元算兩個长度
     */
    public static int getCharLength(final char c) {
        if (StringUtil.isLetter(c)) {
            return 1;
        }
        return 2;
    }

    /**
     * 獲取字串的长度,
     */
    public static int getStringLength(final String strSource) {
        int iSrcLen = 0;
        char[] arrChars = strSource.toCharArray();
        for (char arrChar : arrChars) {
            iSrcLen += StringUtil.getCharLength(arrChar);
        }
        return iSrcLen;
    }


    /***
     * 獲取url 指定name的value;
     * @param url
     * @param name
     * @return
     */
    public static String getValueByName(String url, String name) {
        String result = "";
        int index = url.indexOf("?");
        String temp = url.substring(index + 1);
        String[] keyValue = temp.split("&");
        for (String str : keyValue) {
            if (str.contains(name)) {
                result = str.replace(name + "=", "");
                break;
            }
        }
        return result;
    }

    /**
     * 截取字串，若参數strSuffix不為null，则加上該参數作為後缀
     *
     * @param strSource 原始字串
     * @param iSubLen   截取的长度
     * @param strSuffix 後缀字串，null表示不需要後缀
     * @return 截取後的字串
     */
    public static String sub(final String strSource, final int iSubLen,
                             final String strSuffix) {
        if (StringUtil.isNull(strSource)) {
            return strSource;
        }
        String strFilter = strSource.trim(); // 过滤首尾空字元
        int iLength = StringUtil.getStringLength(strFilter); // 字元的长度
        if (iLength <= iSubLen) {
            return strFilter; // 字元长度小于待截取的长度
        }
        int iNum = iSubLen; // 可截取字元的數量
        int iSubIndex = 0; // 截取位置的游標
        char[] arrChars = strFilter.toCharArray();
        int iArrLength = arrChars.length;
        char c = arrChars[iSubIndex];
        StringBuffer sbContent = new StringBuffer();
        iNum -= StringUtil.getCharLength(c);
        while (iNum > -1 && iSubIndex < iArrLength) {
            ++iSubIndex;
            sbContent.append(c);
            if (iSubIndex < iArrLength) {
                c = arrChars[iSubIndex];
                iNum -= StringUtil.getCharLength(c);
            }
        }
        strFilter = sbContent.toString();
        if (!StringUtil.isNull(strSuffix)) {
            strFilter += strSuffix;
        }
        return strFilter;
    }

    /**
     * 截取字串，长度超出的部分用省略号替代
     *
     * @param strSource 原始字串
     * @param iSubLen   截取的长度
     * @return 截取後的字串
     */
    public static String subWithDots(final String strSource, final int iSubLen) {
        return StringUtil.sub(strSource, iSubLen, "...");
    }

    public static String object2Str(Object obj) {
        String result = null;
        if (obj != null) {
            result = (String) obj;
        }

        return result;
    }

    public static byte[] getBytes(String src, Charset charSet) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.GINGERBREAD) {
            try {
                return src.getBytes(charSet.name());
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return null;
        } else {
            return src.getBytes(charSet);
        }
    }

    /**
     * 時間显示轉換
     *
     * @param duration   時間區間 0-59
     * @param isShowZero 小于10是否显示0 如：09
     * @return
     */
    public static String durationShow(int duration, boolean isShowZero) {
        String showStr = "";
        if (isShowZero) {
            if (duration < 10) {
                showStr = "0" + String.valueOf(duration);
            } else {
                showStr = String.valueOf(duration);
            }
        } else {
            showStr = String.valueOf(duration);
        }
        return showStr;
    }

    public static long fromTimeString(String s) {
        if (s.lastIndexOf(".") != -1) {
            s = s.substring(0, s.lastIndexOf("."));
        }
        String[] split = s.split(":");
        if (split.length == 3) {
            return Long.parseLong(split[0]) * 3600L + Long.parseLong(split[1]) * 60L + Long.parseLong(split[2]);
        } else if (split.length == 2) {
            return Long.parseLong(split[0]) * 60L + Long.parseLong(split[0]);
        } else {
            throw new IllegalArgumentException("Can\'t parse time string: " + s);
        }
    }

    public static String toTimeString(long seconds) {
        seconds = seconds / 1000;
        long hours = seconds / 3600L;
        long remainder = seconds % 3600L;
        long minutes = remainder / 60L;
        long secs = remainder % 60L;
        if (hours == 0) {
            return (minutes < 10L ? "0" : "") + minutes + ":" + (secs < 10L ? "0" : "") + secs;
        }
        return (hours < 10L ? "0" : "") + hours + ":" + (minutes < 10L ? "0" : "") + minutes + ":" + (secs < 10L ?
                "0" : "") + secs;
    }

    /**
     * 格式化數字返回保留2位的百分數
     * @param percent
     * @return
     */
    public static String getTwoPointPercent(Double percent){
        DecimalFormat df=(DecimalFormat)NumberFormat.getInstance();
        df.applyPattern("##.##%");
        return df.format(percent);
    }

    /**
     * 格式化數字返回保留4位小數
     * @param value
     * @return
     */
    public static String getValidateFraction(Double value,int fractions){
        NumberFormat nf = NumberFormat.getNumberInstance();
        nf.setMaximumFractionDigits(fractions);
        return nf.format(value);
    }


}
