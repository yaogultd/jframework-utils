package j.util;

import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.model.enums.AesKeyStrength;
import net.lingala.zip4j.model.enums.CompressionLevel;
import net.lingala.zip4j.model.enums.CompressionMethod;
import net.lingala.zip4j.model.enums.EncryptionMethod;

import java.io.*;
import java.nio.charset.Charset;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;


/**
 * 
 * @author 肖炯
 * 
 */
public class JUtilZip {
	/**
	 *
	 * @param code
	 * @return
	 */
	private static CompressionLevel compressionLevelFromCode(int code){
		if(code == CompressionLevel.NO_COMPRESSION.getLevel()) return CompressionLevel.NO_COMPRESSION;
		if(code == CompressionLevel.FASTEST.getLevel()) return CompressionLevel.FASTEST;
		if(code == CompressionLevel.FAST.getLevel()) return CompressionLevel.FAST;
		if(code == CompressionLevel.MEDIUM_FAST.getLevel()) return CompressionLevel.MEDIUM_FAST;
		if(code == CompressionLevel.NORMAL.getLevel()) return CompressionLevel.NORMAL;
		if(code == CompressionLevel.HIGHER.getLevel()) return CompressionLevel.HIGHER;
		if(code == CompressionLevel.MAXIMUM.getLevel()) return CompressionLevel.MAXIMUM;
		if(code == CompressionLevel.PRE_ULTRA.getLevel()) return CompressionLevel.PRE_ULTRA;
		if(code == CompressionLevel.ULTRA.getLevel()) return CompressionLevel.ULTRA;
		return CompressionLevel.NO_COMPRESSION;
	}

	/**
	 *
	 * @param code
	 * @return
	 */
	private static CompressionMethod compressionMethodFromCode(int code){
		if(code == CompressionMethod.STORE.getCode()) return CompressionMethod.STORE;
		if(code == CompressionMethod.DEFLATE.getCode()) return CompressionMethod.DEFLATE;
		if(code == CompressionMethod.AES_INTERNAL_ONLY.getCode()) return CompressionMethod.AES_INTERNAL_ONLY;
		return CompressionMethod.STORE;
	}

	/**
	 *
	 * @param length
	 * @return
	 */
	private static AesKeyStrength aesKeyStrengthFromLength(int length){
		if(length==128) return AesKeyStrength.KEY_STRENGTH_128;
		if(length==192) return AesKeyStrength.KEY_STRENGTH_192;
		if(length==256) return AesKeyStrength.KEY_STRENGTH_256;
		return AesKeyStrength.KEY_STRENGTH_128;
	}

	/**
	 *
	 *
	 */
	public JUtilZip() {
	}

	/**
	 * 将zip文件zipFilePath解压到目录outPath中
	 * @param zipFilePath
	 * @param outPath
	 * @param fileNameCharSet
	 * @param password
	 * @throws Exception
	 */
	public static void extractZipFile(String zipFilePath, String outPath,String fileNameCharSet,String password)throws Exception{
		ZipFile zipFile = new ZipFile(zipFilePath);
		if(fileNameCharSet!=null&&!"".equals(fileNameCharSet)) zipFile.setCharset(Charset.forName(fileNameCharSet));
		if(password!=null&&!"".equals(password)) zipFile.setPassword(password.toCharArray());
		zipFile.extractAll(outPath);
	}

	/**
	 * 将dirPath及其子目录和文件压缩为destFile
	 * @param dirPath
	 * @param destFile
	 * @param fileNameCharSet
	 * @param compressMethod
	 * @param compressLevel
	 * @throws Exception
	 */
	public static void createZipFile(String dirPath, String destFile,String fileNameCharSet,int compressMethod,int compressLevel) throws Exception {
		//创建目录
		File destDir = (new File(destFile)).getParentFile();
		if(!destDir.exists()) destDir.mkdirs();

		ZipFile zipFile = new ZipFile(destFile);
		if(fileNameCharSet!=null&&!"".equals(fileNameCharSet)) zipFile.setCharset(Charset.forName(fileNameCharSet));

		ZipParameters parameters = new ZipParameters();

		//设置压缩方法
		parameters.setCompressionMethod(compressionMethodFromCode(compressMethod));

		//设置压缩率
		parameters.setCompressionLevel(compressionLevelFromCode(compressLevel));

		//read hidden files
		parameters.setReadHiddenFiles(true);

		zipFile.createSplitZipFileFromFolder(new File(dirPath),parameters,false,0);
	}

	/**
	 * 将dirPath及其子目录和文件压缩为destFile并设置密码（使用标准加密）
	 * @param dirPath
	 * @param destFile
	 * @param fileNameCharSet
	 * @param password
	 * @throws Exception
	 */
	public static void createZipFileEncryptStandard(String dirPath, String destFile,String fileNameCharSet,int compressMethod,int compressLevel,String password) throws Exception {
		if(password==null||"".equals(password)) throw new Exception("password is empty");

		//创建目录
		File destDir = (new File(destFile)).getParentFile();
		if(!destDir.exists()) destDir.mkdirs();

		ZipFile zipFile = new ZipFile(destFile);
		if(fileNameCharSet!=null&&!"".equals(fileNameCharSet)) zipFile.setCharset(Charset.forName(fileNameCharSet));

		ZipParameters parameters = new ZipParameters();

		//设置压缩方法
		parameters.setCompressionMethod(compressionMethodFromCode(compressMethod));

		//设置压缩率
		parameters.setCompressionLevel(compressionLevelFromCode(compressLevel));

		parameters.setEncryptFiles(true);
		parameters.setEncryptionMethod(EncryptionMethod.ZIP_STANDARD);
		if(!JUtilString.isBlank(password)) zipFile.setPassword(password.toCharArray());

		//read hidden files
		parameters.setReadHiddenFiles(true);

		zipFile.createSplitZipFileFromFolder(new File(dirPath), parameters,false,0);
	}

	/**
	 * 将dirPath及其子目录和文件压缩为destFile并设置密码（使用AES加密）
	 * @param dirPath
	 * @param destFile
	 * @param fileNameCharSet
	 * @param compressMethod
	 * @param compressLevel
	 * @param password
	 * @param aesStrengthLength
	 * @throws Exception
	 */
	public static void createZipFileEncryptAES(String dirPath, String destFile,String fileNameCharSet,int compressMethod,int compressLevel,String password,int aesStrengthLength) throws Exception {
		if(password==null||"".equals(password)) throw new Exception("password is empty");

		//创建目录
		File destDir = (new File(destFile)).getParentFile();
		if(!destDir.exists()) destDir.mkdirs();

		ZipFile zipFile = new ZipFile(destFile);
		if(fileNameCharSet!=null&&!"".equals(fileNameCharSet)) zipFile.setCharset(Charset.forName(fileNameCharSet));

		ZipParameters parameters = new ZipParameters();

		//设置压缩方法
		parameters.setCompressionMethod(compressionMethodFromCode(compressMethod));

		//设置压缩率
		parameters.setCompressionLevel(compressionLevelFromCode(compressLevel));

		parameters.setEncryptFiles(true);
		parameters.setEncryptionMethod(EncryptionMethod.AES);
		parameters.setAesKeyStrength(aesKeyStrengthFromLength(aesStrengthLength));
		if(!JUtilString.isBlank(password)) zipFile.setPassword(password.toCharArray());

		//read hidden files
		parameters.setReadHiddenFiles(true);

		zipFile.createSplitZipFileFromFolder(new File(dirPath), parameters, false, 0);
	}


	/**
	 * 解压gzip流并写入文件
	 *
	 * @param in
	 * @param file
	 * @throws Exception
	 */
	public static void gzipIs2File(InputStream in, String file)
			throws Exception {
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
	 * 压缩字符串
	 *
	 * @param str
	 * @return
	 * @throws Exception
	 */
	public static String gzipString(String str,String encoding) throws Exception {
		if(str == null || str.length() == 0) return str;

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		GZIPOutputStream gzip = new GZIPOutputStream(out);
		if(encoding==null) gzip.write(str.getBytes());
		else gzip.write(str.getBytes(encoding));
		gzip.close();
		return out.toString("ISO-8859-1");
	}

	public static String gzipBytes(byte[] bytes) throws Exception {
		if (bytes==null) return null;

		try{
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			GZIPOutputStream gzip = new GZIPOutputStream(out);
			gzip.write(bytes);
			gzip.close();
			return out.toString("ISO-8859-1");
		}catch(Exception e){
			return new String("ISO-8859-1");
		}
	}

	/**
	 * 解压字符串
	 *
	 * @param str
	 * @return
	 * @throws Exception
	 */
	public static String readGzipString(String str,String encoding) throws Exception {
		if (str == null || str.length() == 0) return str;

		try{
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			ByteArrayInputStream in = new ByteArrayInputStream(str.getBytes("ISO-8859-1"));
			GZIPInputStream gunzip = new GZIPInputStream(in);
			byte[] buffer = new byte[256];
			int n;
			while ((n = gunzip.read(buffer)) >= 0) {
				out.write(buffer, 0, n);
			}
			gunzip.close();
			if(encoding==null) return out.toString();
			else  return out.toString(encoding);
		}catch(Exception e){
			return str;
		}
	}

	public static byte[] gzip(byte[] bytes) throws Exception {
		if(bytes==null || bytes.length==0) return bytes;

		try{
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			GZIPOutputStream gzip = new GZIPOutputStream(out);
			gzip.write(bytes);
			gzip.close();
			return out.toByteArray();
		}catch(Exception e){
			return bytes;
		}
	}

	public static byte[] unGzip(byte[] bytes) throws Exception {
		if(bytes==null || bytes.length==0) return bytes;

		try{
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			ByteArrayInputStream in = new ByteArrayInputStream(bytes);
			GZIPInputStream gunzip = new GZIPInputStream(in);
			byte[] buffer = new byte[256];
			int n;
			while ((n = gunzip.read(buffer)) >= 0) {
				out.write(buffer, 0, n);
			}
			gunzip.close();
			return out.toByteArray();
		}catch(Exception e){
			return bytes;
		}
	}
}
