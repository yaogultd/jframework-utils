package j.util;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Base64;

/**
 *
 * @author 肖炯
 * @date 2023-06-07
 * Base64常用功能
 */
public final class JUtilBase64{
	/**
	 * 
	 * @param binaryData
	 * @return
	 */
	public static String encode(byte[] binaryData){
		return encode(binaryData, false);
	}

	/**
	 *
	 * @param data
	 * @return
	 */
	public static String encode(String data){
		if(data==null) return null;
		return encode(data, true);
	}

	/**
	 * 
	 * @param encoded
	 * @return
	 */
	public static byte[] decode(String encoded){
		return decode(encoded, false);
	}
	
	/**
	 * 
	 * @param binaryData
	 * @param useMimeEncoder use Base64.getMimeEncoder() if true, or else use Base64.getEncoder()
	 * @return
	 */
	public static String encode(byte[] binaryData, boolean useMimeEncoder){
		try {
			if(useMimeEncoder) return Base64.getMimeEncoder().encodeToString(binaryData);
			else return Base64.getEncoder().encodeToString(binaryData);
		}catch(Exception e) {
			return new String(binaryData);
		}
	}

	/**
	 *
	 * @param data
	 * @param useMimeEncoder use Base64.getMimeEncoder() if true, or else use Base64.getEncoder()
	 * @return
	 */
	public static String encode(String data, boolean useMimeEncoder){
		if(data==null) return null;
		try {
			if(useMimeEncoder) return Base64.getMimeEncoder().encodeToString(data.getBytes());
			else return Base64.getEncoder().encodeToString(data.getBytes());
		}catch(Exception e) {
			return data;
		}
	}

	/**
	 * 
	 * @param encoded
	 * @param useMimeDecoder use Base64.getMimeEncoder() if true, or else use Base64.getEncoder()
	 * @return
	 */
	public static byte[] decode(String encoded, boolean useMimeDecoder){
		try {
			if(useMimeDecoder) return Base64.getMimeDecoder().decode(encoded);
			else return Base64.getDecoder().decode(encoded);
		}catch(Exception e) {
			e.printStackTrace();
			return encoded.getBytes();
		}
	}
	
	/**
	 * 
	 * @param file
	 * @return
	 */
	public static String image2Base64(File file) {
		 try {
             InputStream is = new FileInputStream(file);
             
             //将内容读取内存中
             ByteArrayOutputStream data = new ByteArrayOutputStream();
             int len = -1;
             byte[] buffer= new byte[1024];
             while ((len = is.read(buffer)) != -1) {
                 data.write(buffer, 0, len);
             }
    
             //对字节数组Base64编码
             String encode=Base64.getMimeEncoder().encodeToString(data.toByteArray());
         
             // 关闭流
             is.close();
             
             return encode;
         } catch (Exception e) {
             e.printStackTrace();
             return null;
         }
	}

	/**
	 *
	 * @param is
	 * @return
	 */
	public static String image2Base64(InputStream is) {
		 try {
             //将内容读取内存中
             ByteArrayOutputStream data = new ByteArrayOutputStream();
             int len = -1;
             byte[] buffer= new byte[1024];
             while ((len = is.read(buffer)) != -1) {
                 data.write(buffer, 0, len);
             }
    
             //对字节数组Base64编码
             String encode=Base64.getMimeEncoder().encodeToString(data.toByteArray());
         
             // 关闭流
             is.close();
             
             return encode;
         } catch (Exception e) {
             e.printStackTrace();
             return null;
         }
	}
	
	/**
	 * 
	 * @param image
	 * @param imageType
	 * @return
	 */
	public static String image2Base64(BufferedImage image, String imageType) {
		 try {
			 InputStream is= JUtilImage.image2InputStream(image, imageType);
			
             //将内容读取内存中
             ByteArrayOutputStream data = new ByteArrayOutputStream();
             int len = -1;
             byte[] buffer= new byte[1024];
             while ((len = is.read(buffer)) != -1) {
                 data.write(buffer, 0, len);
             }
    
             //对字节数组Base64编码
             String encode=Base64.getMimeEncoder().encodeToString(data.toByteArray());
         
             // 关闭流
             is.close();
             
             return encode;
         } catch (Exception e) {
             e.printStackTrace();
             return null;
         }
	}

	public static void base64ToImage(String base64Str, String path) {
		byte[] imageBytes = Base64.getDecoder().decode(base64Str);
		try {
			ByteArrayInputStream bis = new ByteArrayInputStream(imageBytes);
			BufferedImage image = ImageIO.read(bis);
			bis.close();

			File file = new File(path);
			File dir = file.getParentFile();
			if(!dir.exists()) dir.mkdirs();

			ImageIO.write(image, "png", new File(path));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static BufferedImage base64ToImage(String base64Str) {
		byte[] imageBytes = Base64.getDecoder().decode(base64Str);
		try {
			ByteArrayInputStream bis = new ByteArrayInputStream(imageBytes);
			BufferedImage image = ImageIO.read(bis);
			bis.close();

			return image;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static void main(String[] args) throws Exception{
		String s="AYFx2HWz3JRwZEyysOcLbBDqa0hkvvAHH0AS70Pf9dSn3cQJNeAFbIjDMz2oywk20IR3Ef4KpWgeAJYj:EOxZtI_6cBXCfmBOKQyzHaUtCGJ1c6Tqj_geNbuhO4LEpxwrf5Zu_Qyx6L4DUObPe7lWObCsGCkSQhNt";
		System.out.println(JUtilBase64.encode(s, false));
	}
}