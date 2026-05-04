package j.core;

import j.core.type.JArray;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 *
 */
public final class OS {
    /**
     *
     * @return
     */
    public static String getOsType(){
        String os=System.getProperty("os.name");
        if(os!=null&&os.toLowerCase().indexOf("windows")>-1) os="windows";
        else os="linux";
        return os;
    }

    /**
     *
     * @param args
     * @return
     * @throws Exception
     */
    public static String executeCommand(List<String> args) throws Exception{
        return executeCommand(args, 30000);
    }

    /**
     *
     * @param args
     * @param timeout
     * @return
     * @throws Exception
     */
    public static String executeCommand(List<String> args, long timeout) throws Exception{
        return executeCommand(args, timeout, true);
    }


    /**
     *
     * @param args 带空格的参数会自动在两边加上双引号
     * @param timeout 等待超时时间(ms)， 小于等于0表示立即返回
     * @param needOutputs
     * @return
     * @throws Exception
     */
    public static String executeCommand(List<String> args, long timeout, boolean needOutputs) throws Exception{
        try{
            ProcessBuilder builder = new ProcessBuilder(args);
            Process process = builder.start();

            int returnCode = waitForProcess(process, timeout);
            if (returnCode > 0) {
                List<String> errors=getErrors(process.getErrorStream());
                destroy(process);
                if (errors.size() > 0) {
                    throw new Exception(JArray.toString(errors, "\r\n"));
                } else {
                    throw new Exception("errors("+returnCode+") while executing "+JArray.toString(args, " "));
                }
            }

            List<String> outputs=new ArrayList<>();
            if(needOutputs) outputs=getOutput(process.getInputStream());
            destroy(process);
            return JArray.toString(outputs, "\r\n");
        }catch(Exception ex){
            throw  ex;
        }
    }

    /**
     *
     * @param command
     * @return
     * @throws Exception
     */
    public static String executeCommand(String command) throws Exception{
        return executeCommand(command, 60000);
    }

    /**
     *
     * @param command
     * @param timeout
     * @return
     * @throws Exception
     */
    public static String executeCommand(String command, long timeout) throws Exception{
        return executeCommand(command, timeout, true);
    }

    /**
     *
     * @param command
     * @param timeout
     * @param needOutputs
     * @return
     * @throws Exception
     */
    public static String executeCommand(String command, long timeout, boolean needOutputs) throws Exception{
        try{
            ProcessBuilder builder = new ProcessBuilder(command);
            Process process = builder.start();

            int returnCode = waitForProcess(process, timeout);
            if (returnCode > 0) {
                List<String> errors=getErrors(process.getErrorStream());
                destroy(process);
                if (errors.size() > 0) {
                    throw new Exception(JArray.toString(errors, "\r\n"));
                } else {
                    throw new Exception("errors("+returnCode+") while executing "+command);
                }
            }

            List<String> outputs=new ArrayList<>();
            if(needOutputs) outputs=getOutput(process.getInputStream());
            destroy(process);
            return JArray.toString(outputs, "\r\n");
        }catch(Exception ex){
            throw  ex;
        }
    }

    /**
     *
     * @param is
     * @return
     * @throws IOException
     */
    private static List<String> getOutput(InputStream is) throws IOException {
        InputStreamReader isReader = new InputStreamReader(is);
        BufferedReader reader = new BufferedReader(isReader);
        List<String> outputs= new ArrayList<>();
        String line;
        while ((line=reader.readLine()) != null) {
            System.out.println("xxx");
            outputs.add(line);
        }
        reader.close();
        isReader.close();

        return outputs;
    }

    /**
     *
     * @param is
     * @return
     * @throws IOException
     */
    private static List<String> getErrors(InputStream is) throws IOException {
        InputStreamReader isReader = new InputStreamReader(is);
        BufferedReader reader = new BufferedReader(isReader);
        List<String> errors= new ArrayList<>();
        String line;
        while ((line=reader.readLine()) != null) {
            errors.add(line);
        }
        reader.close();
        isReader.close();
        return errors;
    }

    /**
     *
     * @param process
     * @param timeout
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    private static int waitForProcess(final Process process, long timeout) throws IOException, InterruptedException {
        int rc=-1;

        if(timeout > 0){
            process.waitFor(timeout, TimeUnit.MILLISECONDS);

            try{
                rc=process.exitValue();
            }catch (Exception e){
                //e.printStackTrace();
            }
        }

        return rc;
    }

    /**
     *
     * @param process
     * @throws IOException
     * @throws InterruptedException
     */
    private static void destroy(final Process process) throws IOException, InterruptedException {
        try {
            process.getInputStream().close();
            process.getOutputStream().close();
            process.getErrorStream().close();
        } catch (Exception e) {}
    }
}
