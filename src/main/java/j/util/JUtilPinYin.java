package j.util;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;

/**
 *
 * @author 肖炯
 * @date 2023-06-07
 * 中文拼音
 */
public class JUtilPinYin {
	public static HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
    
	static{
		defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);//小写
	    defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);//不标注声调
	}

	/**
	 *
	 * @param src
	 * @param spliter
	 * @param uppercaseFirstChar
	 * @return
	 */
	public static String toPinYin(String src,String spliter,boolean uppercaseFirstChar) {
		if(JUtilString.isBlank(src)) return src;
		char[] temp=src.toCharArray();
		StringBuilder sb=new StringBuilder();
		for(int i=0;i<temp.length;i++){
			if(i>0) sb.append(spliter);
			try{
				String[] pys=PinyinHelper.toHanyuPinyinStringArray(temp[i],defaultFormat);

				if(pys!=null&&pys.length>0){
					String py=pys[0];
					if(uppercaseFirstChar) py=JUtilString.upperFirstChar(py);
					
					sb.append(py);
				}else{
					sb.append(temp[i]);
				}
			}catch(Exception e){
				e.printStackTrace();
				sb.append(temp[i]);
			}
		}
		return sb.toString();
	}

	/**
	 *
	 * @param src
	 * @param format
	 * @param spliter
	 * @return
	 */
	public static String toPinYin(String src, HanyuPinyinOutputFormat format, String spliter, boolean retain) {
		try{
			return PinyinHelper.toHanYuPinyinString(src, format==null?defaultFormat:format, spliter, retain);
		}catch (Exception e){
			e.printStackTrace();
			return src;
		}
	}

	public static void main(String[] args) throws Exception{
		System.out.println(JUtilPinYin.toPinYin("寻行数墨", null," ", true));
		System.out.println(JUtilPinYin.toPinYin("人行道", null, " ", true));
	}
}