package j.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author 肖炯
 * @date 2023-06-07
 */
public class JUtilInputStream {
	
	/**
	 * 将输入流读入字节数组变量(bytes)
	 * @param in
	 * @return
	 * @throws IOException
	 */
	public static byte[] bytes(InputStream in) throws IOException {
		ByteArrayOutputStream bos=new ByteArrayOutputStream();
		
		byte[] buffer=new byte[1024];
		int readed=in.read(buffer);
		while(readed>-1){
			bos.write(buffer,0,readed);
			readed=in.read(buffer);
		}
		bos.flush();
		byte[] bytes=bos.toByteArray();		

		try{
			in.close();
			in=null;
		}catch(Exception e){}
		
		try{
			bos.close();
			bos=null;
		}catch(Exception e){}
	
		return bytes;
	}
	
	/**
	 * 将输入流解析成字符串
	 * @param in
	 * @return
	 * @throws IOException
	 */
	public static String string(InputStream in)throws IOException{
		if(in==null) return null;
//		BufferedReader reader=new BufferedReader(new InputStreamReader(in));
//    	String txt="";
//    	String line=reader.readLine();
//    	while(line!=null){	        		
//    		txt+=line+Global.lineSeparator;
//    		line=reader.readLine();
//    	}
//    	
//		try{
//			reader.close();
//		}catch(Exception e){}
//		
//		try{
//			in.close();
//		}catch(Exception e){}  
//		
//		if(txt.endsWith(Global.lineSeparator)) txt=txt.substring(0,txt.length()-Global.lineSeparator.length());
//
//    	return JUtilString.reviseString(txt,"ISO-8859-1");
		
		return new String(bytes(in));
	}
	
	/**
	 * 将输入流解析成指定编码的字符串
	 * @param in
	 * @param encoding 字符串编码
	 * @return
	 * @throws IOException
	 */
	public static String string(InputStream in,String encoding)throws IOException{
//		BufferedReader reader=new BufferedReader(new InputStreamReader(in,encoding));
//    	String txt="";
//    	String line=reader.readLine();
//    	while(line!=null){	        		
//    		txt+=line+Global.lineSeparator;
//    		line=reader.readLine();
//    	}
//    	
//		try{
//			reader.close();
//		}catch(Exception e){}
//		
//		try{
//			in.close();
//		}catch(Exception e){} 
//		
//		if(txt.endsWith(Global.lineSeparator)) txt=txt.substring(0,txt.length()-Global.lineSeparator.length());
//
//		return JUtilString.reviseString(txt,encoding);
		
		return new String(bytes(in),encoding);
	}
}