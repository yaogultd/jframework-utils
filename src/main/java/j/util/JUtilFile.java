package j.util;

import java.io.*;

/**
 * @author 肖炯
 * @date 2023/6/7
 */
public class JUtilFile {
    ///////////////一些实用的方法////////////////////////
    /**
     *
     * @param path
     * @param os
     * @return
     */
    public static String adjustFileSeperator(String path,String os){
        if(path==null||"".equals(path)) return path;

        if("linux".equalsIgnoreCase(os)){
            path=JUtilString.replaceAll(path,"\\","/");
            if(path.endsWith("//")) path=path.substring(0, path.length()-1);
        }else{
            path=JUtilString.replaceAll(path,"/","\\");
            if(path.endsWith("\\\\")) path=path.substring(0, path.length()-1);
        }

        return path;
    }

    /**
     * 将文件读取成字符串
     * @return
     */
    public static String read(File file){
        return read(file,null);
    }

    /**
     * 将文件读取成指定编码的字符串
     * @param encoding
     * @return
     */
    public static String read(File file,String encoding){
        try{
            if(file.exists()){
                if(!JUtilString.isBlank(encoding)) return JUtilInputStream.string(new FileInputStream(file),encoding);
                else return JUtilInputStream.string(new FileInputStream(file));
            }else{
                return null;
            }
        }catch(Exception e){
            return null;
        }
    }

    /**
     * 将输入流保持到文件
     * @param is
     * @param path 本地文件路径
     */
    public static void save(InputStream is, String path){
        save(is, path, true);
    }

    /**
     *
     * @param is
     * @param path
     * @param closeStreamOnEnd
     */
    public static void save(InputStream is, String path, boolean closeStreamOnEnd){
        try{
            File file=new File(path);
            if(file.exists()) file.delete();
            else file.getParentFile().mkdirs();

            OutputStream os=new FileOutputStream(file);

            byte[] buffer=new byte[1024];
            int readed=is.read(buffer);
            while(readed>-1){
                os.write(buffer,0, readed);
                readed=is.read(buffer);
            }
            os.flush();

            if(closeStreamOnEnd){
                try{
                    is.close();
                }catch(Exception e){}
            }

            try{
                os.close();
            }catch(Exception e){}
        }catch(Exception e){
            if(closeStreamOnEnd) {
                try {
                    is.close();
                } catch (Exception ex) {
                }
            }
        }
    }

    /**
     * 将字符串以指定编码保持到文件
     * @param path 本地文件路径
     * @param content
     * @param append
     * @param encoding
     */
    public static void save(String path, String content, boolean append, String encoding) throws Exception{
        File file=new File(path);
        file.getParentFile().mkdirs();

        Writer writer=null;
        if(!JUtilString.isBlank(encoding)) writer=new OutputStreamWriter(new FileOutputStream(file,append),encoding);
        else writer=new OutputStreamWriter(new FileOutputStream(file,append));
        writer.write(content);
        writer.flush();

        try{
            writer.close();
        }catch(Exception e){}
    }

    /**
     * 删除文件或目录，如果是非空目录将删除目录内所有内容
     * @param file
     */
    public static void delete(File file){
        if(!file.exists()) return;

        if(file.isFile()){
            file.delete();
            return;
        }

        File[] files=file.listFiles();
        if(files==null || files.length==0){
            file.delete();
            return;
        }

        for(int i=0; i<files.length; i++){
            delete(files[i]);
        }

        file.delete();
    }
    ///////////////一些实用的方法  end////////////////////////
}
