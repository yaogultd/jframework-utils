package j.util;

/**
 *
 * @author 肖炯
 * @date 2023-06-07
 *
 */
public class JUtilBytes{
	/**
	 * 将byte转为16进制
	 * 
	 * @param bytes
	 * @return
	 */
	public static String byte2Hex(byte[] bytes){
		StringBuffer buffer=new StringBuffer();
		String temp=null;
		for(int i=0;i<bytes.length;i++){
			temp=Integer.toHexString(bytes[i]&0xFF);
			if(temp.length()==1){
				//得到一位的进行补0操作
				buffer.append("0");
			}
			buffer.append(temp);
		}
		return buffer.toString();
	}

	/**
	 *
	 * @param one
	 * @param other
	 * @return
	 */
	public static boolean equals(byte[] one, byte[] other){
		if(one==null && other==null) return true;

		if(one==null || other==null) return false;

		if(one.length != other.length) return false;

		for(int i=0; i<one.length; i++){
			if(one[i] != other[i]) return false;
		}

		return true;
	}

	/**
	 *
	 * @param one
	 * @param other
	 * @return
	 */
	public static boolean startsWith(byte[] one, byte[] other){
		if(one==null && other==null) return false;

		if(one==null || other==null) return false;

		if(one.length < other.length) return false;

		for(int i=0; i<other.length; i++){
			if(one[i] != other[i]) return false;
		}

		return true;
	}
}
