package j.core.hp.reflection;

import j.util.ConcurrentMap;

public final class Accessors {
    private static ConcurrentMap<String, Accessor> accessors = new ConcurrentMap<>();

    synchronized public static Accessor getAccessor(Class clazz){
        String className = clazz.getCanonicalName();
        if(accessors.containsKey(className)) return accessors.get(className);

        Accessor accessor = new Accessor(clazz);
        accessors.put(className, accessor);
        return accessor;
    }
}
