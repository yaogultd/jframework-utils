package j.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JUtilMap {
    /**
     *
     * @param map
     * @return
     */
    public static List listKeys(Map map){
        List keys = new ArrayList();
        if(map!=null && !map.isEmpty()) keys.addAll(map.keySet());
        return keys;
    }

    /**
     *
     * @param map
     * @return
     */
    public static List listValues(Map map){
        List values = new ArrayList();
        if(map!=null && !map.isEmpty()) values.addAll(map.values());
        return values;
    }
}
