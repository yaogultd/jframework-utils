package j.core.type;

import j.util.JUtilString;

import java.util.List;

/**
 *
 * @author 肖炯
 * @date 2023-06-07
 * 数组的一些操作
 */
public final class JArray {
	
	/**
	 * 截取数组的一部分
	 * @param objects
	 * @param sub
	 * @param from
	 * @param to
	 * @return
	 */
	public static Object[] subArray(Object[] objects,Object[] sub,int from,int to) {
		if(objects==null) return null;
		if(from<0||from>to) return null;
		for(int i=from;i<to&&i<objects.length;i++) {
			sub[i-from]=objects[i];
		}
		return sub;
	}
	
	/**
	 * 
	 * @param all
	 * @param from
	 * @param to
	 * @return
	 */
	public static byte[] sub(byte[] all,int from,int to) {
		if(all==null) return null;
		if(from<0||from>to) return null;
		if(from==to) return new byte[0];
		if(to>all.length) to=all.length;

		byte[] sub=new byte[to-from];
		for(int i=from;i<to&&i<all.length;i++) {
			sub[i-from]=all[i];
		}
		return sub;
	}

	/**
	 *
	 * @param all
	 * @param prefix
	 * @return
	 */
	public static boolean startsWith(byte[] all, byte[] prefix) {
		if(all==null || prefix==null) return false;
		if(prefix.length > all.length) return false;

		for(int i=0; i<prefix.length; i++){
			if(prefix[i] != all[i]) return false;
		}

		return true;
	}

	/**
	 *
	 * @param all
	 * @param append
	 * @return
	 */
	public static byte[] append(byte[] all, byte[] append) {
		if(all==null || append==null) return all;

		byte[] bytes = new byte[all.length + append.length];
		for(int i=0; i<all.length; i++) bytes[i]=all[i];
		for(int i=0; i<append.length; i++) bytes[all.length + i]=append[i];
		return bytes;
	}

	/**
	 *
	 * @param blocks
	 * @return
	 */
	public static byte[] append(List<byte[]> blocks){
		int size=0;
		for(int i=0; i<blocks.size(); i++){
			size+=blocks.get(i).length;
		}

		byte[] bytes=new byte[size];

		int index=0;
		for(int i=0; i<blocks.size(); i++){
			byte[] block=blocks.get(i);
			for(int j=0; j<block.length; j++){
				bytes[index++]=block[j];
			}
		}

		return  bytes;
	}
	
	/**
	 * 
	 * @param all
	 * @param from
	 * @param to
	 * @return
	 */
	public static int[] sub(int[] all,int from,int to) {
		if(all==null) return null;
		if(from<0||from>to) return null;
		if(from==to) return new int[0];
		if(to>all.length) to=all.length;
		
		int[] sub=new int[to-from];
		for(int i=from;i<to&&i<all.length;i++) {
			sub[i-from]=all[i];
		}
		return sub;
	}
	
	/**
	 * 
	 * @param all
	 * @param from
	 * @param to
	 * @return
	 */
	public static long[] sub(long[] all,int from,int to) {
		if(all==null) return null;
		if(from<0||from>to) return null;
		if(from==to) return new long[0];
		if(to>all.length) to=all.length;
		
		long[] sub=new long[to-from];
		for(int i=from;i<to&&i<all.length;i++) {
			sub[i-from]=all[i];
		}
		return sub;
	}
	
	/**
	 * 
	 * @param all
	 * @param from
	 * @param to
	 * @return
	 */
	public static double[] sub(double[] all,int from,int to) {
		if(all==null) return null;
		if(from<0||from>to) return null;
		if(from==to) return new double[0];
		if(to>all.length) to=all.length;
		
		double[] sub=new double[to-from];
		for(int i=from;i<to&&i<all.length;i++) {
			sub[i-from]=all[i];
		}
		return sub;
	}
	
	/**
	 * 
	 * @param array
	 * @return
	 */
	public static boolean duplicatedElements(Object[] array) {
		if(array==null||array.length==0) return false;
		
		for(int i=0; i<array.length; i++) {
			for(int j=0; j<array.length; j++) {
				if(i==j) continue;
				
				if(array[i]==null && array[j]==null) return true;
				else if(array[i]!=null && array[j]!=null && array[i].equals(array[j])) return true;
			}
		}
		
		return false;
	}

	/**
	 *
	 * @param array
	 * @return
	 */
	public static boolean duplicatedElements(List array) {
		if(array==null||array.isEmpty()) return false;

		for(int i=0; i<array.size(); i++) {
			for(int j=0; j<array.size(); j++) {
				if(i==j) continue;

				if(array.get(i)==null && array.get(j)==null) return true;
				else if(array.get(i)!=null && array.get(j)!=null && array.get(i).equals(array.get(j))) return true;
			}
		}

		return false;
	}
	
	/**
	 * 
	 * @param objects
	 * @return
	 */
	public static String toString(List objects) {
		return toString(objects, null);
	}
	
	/**
	 * 
	 * @param objects
	 * @param splitter
	 * @return
	 */
	public static String toString(List objects, String splitter) {
		return toString(objects, splitter, false);
	}
	
	/**
	 * 
	 * @param objects
	 * @param splitter
	 * @param quote
	 * @return
	 */
	public static String toString(List objects, String splitter, boolean quote) {
		if(objects==null) return null;
		if(objects.isEmpty()) return "";
		
		StringBuffer sb=new StringBuffer();
		
		for(int i=0; i<objects.size(); i++) {
			if(splitter!=null&&i>0) sb.append(splitter);
			if(!quote) sb.append(objects.get(i)==null?"null":objects.get(i).toString());
			else sb.append(objects.get(i)==null?"null":("\""+objects.get(i).toString()+"\""));
		}
		
		return sb.toString();
	}
	
	/**
	 * 
	 * @param objects
	 * @return
	 */
	public static String toString(Object[] objects) {
		return toString(objects, null);
	}
	
	/**
	 * 
	 * @param objects
	 * @param splitter
	 * @return
	 */
	public static String toString(Object[] objects, String splitter) {
		return toString(objects, splitter, false);
	}
	
	/**
	 * 
	 * @param objects
	 * @param splitter
	 * @param quote
	 * @return
	 */
	public static String toString(Object[] objects, String splitter, boolean quote) {
		if(objects==null) return null;
		if(objects.length==0) return "";
		
		StringBuffer sb=new StringBuffer();
		
		for(int i=0; i<objects.length; i++) {
			if(splitter!=null&&i>0) sb.append(splitter);
			if(!quote) sb.append(objects[i]==null?"null":objects[i].toString());
			else sb.append(objects[i]==null?"null":("\""+objects[i].toString()+"\""));
		}
		
		return sb.toString();
	}

	/**
	 *
	 * @param objects
	 * @param splitter
	 * @param quote
	 * @return
	 */
	public static String toString(Object[] objects, String splitter, String quote) {
		if(objects==null) return null;
		if(objects.length==0) return "";

		StringBuffer sb=new StringBuffer();

		for(int i=0; i<objects.length; i++) {
			if(splitter!=null&&i>0) sb.append(splitter);
			if(JUtilString.isBlank(quote)) sb.append(objects[i]==null?"null":objects[i].toString());
			else sb.append(objects[i]==null?"null":(quote+objects[i].toString()+quote));
		}

		return sb.toString();
	}

	/**
	 *
	 * @param objects
	 * @param splitter
	 * @param quote
	 * @return
	 */
	public static String toString(List objects, String splitter, String quote) {
		if(objects==null) return null;
		if(objects.size()==0) return "";

		StringBuffer sb=new StringBuffer();

		for(int i=0; i<objects.size(); i++) {
			if(splitter!=null&&i>0) sb.append(splitter);
			if(JUtilString.isBlank(quote)) sb.append(objects.get(i)==null?"null":objects.get(i).toString());
			else sb.append(objects.get(i)==null?"null":(quote+objects.get(i).toString()+quote));
		}

		return sb.toString();
	}
}
