
package j.util;

import java.io.File;
import java.sql.Timestamp;

/**
 * 
 * @author 肖炯
 *
 */
public final class JUtilUUID {
	private static Object lock = new Object();

	private static int hostUnique = (new Object()).hashCode();
	private static long lastTime = System.currentTimeMillis();
	private static long DELAY = 1L;

	/**
	 * 生成UUID
	 * @return
	 */
	public static String genUUID() {
		synchronized (lock) {
			String uuid=String.join("", Integer.toString(hostUnique, 16), Long.toString(lastTime, 16));
			lastTime+=DELAY;
			return uuid;
		}
	}

	/**
	 * 生成UUID
	 * @return
	 */
	public static String genUUIDShort() {
		synchronized (lock) {
			String uuid=Long.toString(lastTime + DELAY);
			lastTime+=DELAY;
			return uuid;
		}
	}

	public static void main(String[] args) throws Exception{
		File dir = new File("D:\\instraw\\物料\\型号标签\\长裤条码和标签\\TEMP");
		File dest = new File("D:\\instraw\\物料\\型号标签\\长裤条码和标签");
		File[] files = dir.listFiles();
		for(File file : files){
			if(file.isDirectory()) continue;
			if(!file.getName().endsWith("_L.ai")) continue;

			try {
				String newName = file.getName().replaceAll("_L.ai", "_S.ai");
				file.renameTo(new File(JUtilString.appendPath(dest.getAbsolutePath(), newName)));
			}catch (Exception e){
				e.printStackTrace();
			}
		}

		for(File file : files){
			if(file.isDirectory()) continue;
			if(!file.getName().endsWith("_XL.ai")) continue;

			try {
				String newName = file.getName().replaceAll("_XL.ai", "_M.ai");
				file.renameTo(new File(JUtilString.appendPath(dest.getAbsolutePath(), newName)));
			}catch (Exception e){
				e.printStackTrace();
			}
		}

		for(File file : files){
			if(file.isDirectory()) continue;
			if(!file.getName().endsWith("_XXL.ai")) continue;

			try {
				String newName = file.getName().replaceAll("_XXL.ai", "_L.ai");
				file.renameTo(new File(JUtilString.appendPath(dest.getAbsolutePath(), newName)));
			}catch (Exception e){
				e.printStackTrace();
			}
		}

		for(File file : files){
			if(file.isDirectory()) continue;
			if(!file.getName().endsWith("_XXXL.ai")) continue;

			try {
				String newName = file.getName().replaceAll("_XXXL.ai", "_XL.ai");
				file.renameTo(new File(JUtilString.appendPath(dest.getAbsolutePath(), newName)));
			}catch (Exception e){
				e.printStackTrace();
			}
		}

		System.out.println(System.currentTimeMillis());
	}
}