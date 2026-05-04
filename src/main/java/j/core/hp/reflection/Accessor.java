package j.core.hp.reflection;

import com.esotericsoftware.reflectasm.ConstructorAccess;
import com.esotericsoftware.reflectasm.MethodAccess;
import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

@Setter
@Getter
public class Accessor {
    private ConstructorAccess constructorAccess;

    private MethodAccess methodAccess;
    private Map<String, Integer> methodIndexes = new HashMap<>();

    public Accessor(Class clazz){
        this.constructorAccess = ConstructorAccess.get(clazz);
        this.methodAccess = MethodAccess.get(clazz);

        java.lang.reflect.Method[] methods = clazz.getDeclaredMethods();
        for(int i=0; methods!=null && i<methods.length; i++){
            if(Modifier.isPrivate(methods[i].getModifiers())) continue;
        	String methodName = methods[i].getName();
        	
            this.methodIndexes.put(methodName, Integer.valueOf(this.methodAccess.getIndex(methodName)));
        	
            Class[] paramTypes = methods[i].getParameterTypes();
            if(paramTypes!=null && paramTypes.length>0) {
            	String key = methodName;
                for (int j = 0; j < paramTypes.length; j++) {
                    key += "." + paramTypes[j].getCanonicalName();
                }
                this.methodIndexes.put(key, Integer.valueOf(this.methodAccess.getIndex(methodName, paramTypes)));
            }
        }

    }

    public int getMethodIndex(String methodName){
    	return this.methodIndexes.get(methodName);
    }

    public int getMethodIndex(String methodName, Class... paramTypes){
        String key = methodName;
        for (int i = 0; paramTypes != null && i < paramTypes.length; i++) {
            key += "." + paramTypes[i].getCanonicalName();
        }
        return this.methodIndexes.get(key);
    }

    public Object newObject(){
        return this.constructorAccess.newInstance();
    }

    public Object invokeMethod(Object object, String methodName, Object... params){
        return this.methodAccess.invoke(object, this.getMethodIndex(methodName), params);
    }

    public Object invokeMethod(Object object, String methodName, Class[] paramTypes, Object[] params){
        return this.methodAccess.invoke(object, this.getMethodIndex(methodName, paramTypes), paramTypes, params);
    }
}
