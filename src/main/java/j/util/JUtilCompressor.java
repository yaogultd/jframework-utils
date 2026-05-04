package j.util;

import java.io.*;
import java.util.zip.*;

/**
 *
 * @author 肖炯
 * @date 2023-06-07
 * 压缩解压
 */
public class JUtilCompressor {
	/**
	 * 
	 * 
	 */
	public JUtilCompressor() {
	}

	/**
	 * 
	 * @param zipFilePath
	 * @param outPath
	 * @param password
	 */
	public static void extractZipFile(String zipFilePath, String outPath,String password) {
		//TODO
	}

	/**
	 * 将dirPath及其子目录和文件压缩为destFile
	 *
	 * @param dirPath
	 * @param destFile
	 * @throws Exception
	 */
	public static void createZipFile(String dirPath, String destFile) throws Exception {
		//TODO
	}

	/**
	 * 解压gzip流为byte[]
	 * 
	 * @param in
	 * @return
	 * @throws Exception
	 */
	public static byte[] readGZipStream2Bytes(InputStream in) throws Exception {
		GZIPInputStream gzin = new GZIPInputStream(in);
		return JUtilInputStream.bytes(gzin);
	}

	/**
	 * 解压gzip流为String
	 * 
	 * @param in
	 * @return
	 * @throws Exception
	 */
	public static String readGZipStream2String(InputStream in) throws Exception {
		return new String(readGZipStream2Bytes(in));
	}

	/**
	 * 解压gzip流为指定编码的String
	 * 
	 * @param in
	 * @return
	 * @throws Exception
	 */
	public static String readGZipStream2String(InputStream in, String encoding)
			throws Exception {
		return new String(readGZipStream2Bytes(in),encoding);
	}

	/**
	 * 解压gzip流并写入文件
	 * 
	 * @param in
	 * @param file
	 * @throws Exception
	 */
	public static void gzipIs2File(InputStream in, String file) throws Exception {
		FileOutputStream fo = new FileOutputStream(file);
		GZIPOutputStream os = new GZIPOutputStream(fo);
		byte[] buffer = new byte[1024];
		int readed = in.read(buffer);
		while (readed > -1) {
			os.write(buffer, 0, readed);
			readed = in.read(buffer);
		}
		os.flush();
		os.close();
		fo.close();
		in.close();
	}

	/**
	 * 
	 * @param source
	 * @return
	 * @throws Exception
	 */
	public static String gzipString(String source,String encoding) throws Exception {
		if(source == null || source.length() == 0) {   
			return source;   
		}   
		
		ByteArrayOutputStream out = new ByteArrayOutputStream();   
		GZIPOutputStream gzip = new GZIPOutputStream(out);   
		gzip.write(source.getBytes(encoding));   
		gzip.close(); 
		out.close();
		
		return out.toString("ISO-8859-1");
	}

	/**
	 *
	 * @param source
	 * @return
	 */
	public static String unGzipString(String source) throws Exception {
		return unGzipString(source, null);
	}

	/**
	 *
	 * @param source
	 * @return
	 */
	public static String unGzipString(String source, String encoding) throws Exception {
		try {
			if (JUtilString.isBlank(source)) {
				return source;
			}
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			ByteArrayInputStream in = new ByteArrayInputStream(source.getBytes());

			GZIPInputStream ungzip = new GZIPInputStream(in);
			byte[] buffer = new byte[256];
			int n;
			while ((n = ungzip.read(buffer)) >= 0) {
				out.write(buffer, 0, n);
			}

			if (JUtilString.isBlank(encoding)) return new String(out.toByteArray());
			return new String(out.toByteArray(), encoding);
		}catch (Exception e){
			return source;
		}
	}

	/**
	 *
	 * @param bytes
	 * @return
	 */
	public static String unGzipString(byte[] bytes) throws Exception {
		return unGzipString(bytes, null);
	}

	/**
	 *
	 * @param bytes
	 * @return
	 */
	public static String unGzipString(byte[] bytes, String encoding) throws Exception {
		try {
			if (bytes == null || bytes.length == 0) {
				return null;
			}
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			ByteArrayInputStream in = new ByteArrayInputStream(bytes);

			GZIPInputStream ungzip = new GZIPInputStream(in);
			byte[] buffer = new byte[256];
			int n;
			while ((n = ungzip.read(buffer)) >= 0) {
				out.write(buffer, 0, n);
			}

			if (JUtilString.isBlank(encoding)) return new String(out.toByteArray());
			return new String(out.toByteArray(), encoding);
		}catch (Exception e){
			//e.printStackTrace();
			if (JUtilString.isBlank(encoding)) return new String(bytes);
			return new String(bytes, encoding);
		}
	}
	
	
	/**
	 * 
	 * @param source
	 * @param encoding
	 * @return
	 * @throws IOException
	 */
	public static String gunzipString(String source,String encoding) throws IOException {   
		if (source == null || source.length() == 0) {   
			return source;   
	    }   
		
		ByteArrayOutputStream out = new ByteArrayOutputStream();   
		ByteArrayInputStream in = new ByteArrayInputStream(source.getBytes("ISO-8859-1"));   
		GZIPInputStream gunzip = new GZIPInputStream(in);   
	    byte[] buffer = new byte[256];   
	    int n;   
	    while ((n = gunzip.read(buffer))>= 0) {   
	    	out.write(buffer, 0, n);   
	    }   
	    gunzip.close();
	    out.close();
	    
	    return out.toString(encoding);	    
	}

	/**
	 * zlib 压缩
	 * @param data
	 * @return
	 */
	public static byte[] zlibCompress(byte[] data) {
		byte[] output = new byte[0];
		Deflater compresser = new Deflater(Deflater.BEST_COMPRESSION, true);
		compresser.reset();
		compresser.setInput(data);
		compresser.finish();
		ByteArrayOutputStream bos = new ByteArrayOutputStream(data.length);
		try {
			byte[] buf = new byte[1024];
			while (!compresser.finished()) {
				int i = compresser.deflate(buf);
				bos.write(buf, 0, i);
			}
			output = bos.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
			output = data;
		} finally {
			try {
				bos.close();
			} catch (IOException e) {}
		}
		compresser.end();
		return output;
	}

	/**
	 * zlib 压缩
	 * @param data
	 * @param os
	 * @throws Exception
	 */
	public static void zlibCompress(byte[] data, OutputStream os) throws Exception{
		DeflaterOutputStream dos = new DeflaterOutputStream(os, new Deflater(Deflater.BEST_COMPRESSION, true));
		dos.write(data, 0, data.length);
		dos.finish();
		dos.flush();
	}

	/**
	 * zlib 解压缩
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public static byte[] zlibDecompress(byte[] data) throws Exception{
		byte[] output = new byte[0];
		Inflater inflater = new Inflater(true);
		inflater.setInput(data);
		ByteArrayOutputStream outputStream  = new ByteArrayOutputStream(data.length);
		try {
			byte[] result  = new byte[1024];
			while (!inflater.finished()) {
				int count  = inflater.inflate(result);
				outputStream .write(result , 0, count );
			}
			output = outputStream .toByteArray();
		} catch (Exception e) {
			throw e;
		} finally {
			try {
				outputStream .close();
				inflater.end();
			} catch (IOException e) {}
		}

		return output;
	}

	/**
	 * zlib 解压缩
	 * @param data
	 * @param encoding
	 * @return
	 * @throws Exception
	 */
	public static String zlibDecompressString(byte[] data, String encoding) throws Exception{
		byte[] output = new byte[0];
		Inflater inflater = new Inflater(true);
		inflater.setInput(data);
		ByteArrayOutputStream outputStream  = new ByteArrayOutputStream(data.length);
		try {
			byte[] buf = new byte[1024];
			while (!inflater.finished()) {
				int count  = inflater.inflate(buf);
				outputStream .write(buf, 0, count );
			}
			output = outputStream.toByteArray();
		} catch (Exception e) {
			throw e;
		} finally {
			try {
				outputStream .close();
				inflater.end();
			} catch (IOException e) {}
		}

		if(JUtilString.isBlank(encoding)) return new String(output);
		try{
			return new String(output, encoding);
		}catch (Exception e){
			return new String(output);
		}
	}

	/**
	 * zlib 解压缩
	 * @param is
	 * @return
	 */
	public static byte[] zlibDecompress(InputStream is) {
		InflaterInputStream iis = new InflaterInputStream(is, new Inflater(true), 1024);
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream(1024);
		try {
			int i = 1024;
			byte[] buf = new byte[i];
			while ((i = iis.read(buf, 0, i)) > 0) {
				outputStream.write(buf, 0, i);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return outputStream.toByteArray();
	}

	/**
	 * test
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		extractZipFile("F:\\tmp\\新建 文本文档2.zip","F:\\tmp\\",null);
	}
}
