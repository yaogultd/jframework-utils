package j.util;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class JUtilProperties {
    private String filePath;
    private ConcurrentMap<String, JUtilKeyValue> properties= new ConcurrentMap<>();
    private ConcurrentMap<String, JUtilKeyValue[]> groups= new ConcurrentMap<>();

    /**
     *
     * @param filePath 文件路径
     * @throws Exception 文件不存在
     */
    public JUtilProperties(String filePath) throws Exception{
        this.filePath=filePath;
        this.load();
    }

    /**
     *
     * @throws Exception 文件不存在
     */
    private void load() throws Exception{
        java.util.Properties ps=new java.util.Properties();
        ps.load(new InputStreamReader(new FileInputStream(this.filePath), StandardCharsets.UTF_8));

        for(Iterator it = ps.keySet().iterator(); it.hasNext();){
            String key=(String)it.next();
            String value=ps.getProperty(key);

            int no=0;
            if(key.startsWith("<")){
                int noEnd=key.indexOf(">");
                if(noEnd>1){
                    String noString=key.substring(1,noEnd);
                    if(JUtilMath.isInt(noString)) no=Integer.parseInt(noString);
                    key=key.substring(noEnd+1);
                }
            }
            properties.put(key,new JUtilKeyValue(key,value,no));
            System.out.println(this.filePath + " load property -> "+key+" = "+value);
        }
    }

    /**
     *
     * @param propertyName 属性名
     * @return 属性值
     */
    public String getProperty(String propertyName){
        JUtilKeyValue p=properties.get(propertyName);
        return p==null?null:p.getValue().toString();
    }

    /**
     *
     * @param groupName 分组
     * @param propertyName 属性名
     * @return 属性值
     */
    public String getProperty(String groupName, String propertyName){
        JUtilKeyValue p=properties.get("["+groupName+"]"+propertyName);
        return p==null?null:p.getValue().toString();
    }

    /**
     * 获得某个分组的key-values
     * @param group 分组
     * @return 属于该分组的key-values
     */
    public JUtilKeyValue[] getProperties(String group){
        if(!groups.containsKey(group)){
            List<JUtilKeyValue> props=new ArrayList<>();
            Iterator<String> it=properties.keySet().iterator();
            while(it.hasNext()){
                String key=it.next();
                if(key.startsWith("["+group+"]")){
                    JUtilKeyValue kv=(JUtilKeyValue)properties.get(key);
                    key=key.substring(group.length()+2);
                    props.add(new JUtilKeyValue(key,kv.getValue(),kv.getNo()));
                }
            }

            JUtilPropertiesSorter sorter=new JUtilPropertiesSorter();
            props=sorter.bubble(props, JUtilSorter.ASC);
            groups.put(group, props.toArray(new JUtilKeyValue[0]));
        }
        return groups.get(group);
    }
}