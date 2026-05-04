package j.util;

import j.core.hp.reflection.Accessor;
import j.core.hp.reflection.Accessors;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.Setter;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;

/**
 *
 * @author 肖炯
 * @date 2023-06-07
 *
 */
@Setter
@Getter
public class JUtilBeanImpl {
	private static ConcurrentMap<String, Method> methodCache=new ConcurrentMap<>();
	private static ConcurrentMap<String, Field> fieldCache=new ConcurrentMap<>();
	private static ConcurrentMap<String, String> columnFieldNameMapping=new ConcurrentMap<>();
	private static ConcurrentMap<String, String> fieldColumnNameMapping=new ConcurrentMap<>();

	private Map<String, String> keyMappings;
	private Map<String, String> valueMappings;

	/**
	 * 获取所有变量（含所有父类的）
	 * @param clazz
	 * @return
	 */
	public static Field[] getDeclaredFieldsWithAllParents(Class clazz){
		List<Field> allFields = new ArrayList();

		//递归获取所有父类的字段
		for(Class<?> currentClass = clazz; currentClass != null; currentClass = currentClass.getSuperclass()) {
			Field[] declaredFields = currentClass.getDeclaredFields();
			Collections.addAll(allFields, declaredFields);
		}

		return allFields.toArray(new Field[0]);
	}

	/**
	 * 获取所有变量（含直接父类）
	 * @param clazz
	 * @return
	 */
	public static Field[] getDeclaredFieldsWithDirectlyParent(Class clazz){
		List<Field> allFields = new ArrayList();
		Collections.addAll(allFields, clazz.getDeclaredFields());

		//直接父类
		clazz = clazz.getSuperclass();
		if(clazz != null) Collections.addAll(allFields, clazz.getDeclaredFields());

		return allFields.toArray(new Field[0]);
	}

	/**
	 * 得到http参数
	 * @param request
	 * @param name
	 * @return
	 */
	private String getHttpParameter(HttpServletRequest request,String name){
		return getHttpParameter(request, name, "UTF-8");
	}

	/**
	 * 得到http参数
	 * @param request
	 * @param name
	 * @param encoding
	 * @return
	 */
	private String getHttpParameter(HttpServletRequest request,String name, String encoding){
		if(request==null||name==null||name.trim().equals("")){
			return null;
		}
		String value=request.getParameter(name);
		if(value==null){
			return null;
		}

		if("application/x-www-form-urlencoded".equalsIgnoreCase(request.getContentType())
				&&JUtilString.isBlank(request.getHeader("Request_by_ajax"))){
			try{
				value=new String(value.getBytes("ISO-8859-1"), encoding);
			}catch(Exception e){}
		}
		if(value!=null&&value.startsWith("jis:")){
			value=JUtilString.intSequence2String(value);
		}

		if(value!=null) value=value.trim();

		return value;
	}

	/**
	 *
	 * @param cls
	 * @param fieldName
	 * @return
	 * @throws Exception
	 */
	public Field getField(Class cls, String fieldName)throws Exception {
		if(cls==null) return null;

		String cacheKey=cls.getCanonicalName()+"."+fieldName;
		Field field = fieldCache.get(cacheKey);
		if(field!=null) return field;
		try{
			field=cls.getDeclaredField(fieldName);
		}catch (Exception e){
			field=null;
		}
		if(field!=null) fieldCache.put(cacheKey, field);

		return field;
	}

	public Field getField(Class cls, String clsName, String fieldName)throws Exception {
		if(cls==null) return null;

		String cacheKey=clsName+"."+fieldName;
		Field field = fieldCache.get(cacheKey);
		if(field!=null) return field;
		try{
			field=cls.getDeclaredField(fieldName);
		}catch (Exception e){
			field=null;
		}
		if(field!=null) fieldCache.put(cacheKey, field);

		return field;
	}

	/**
	 *
	 * @param propertyName
	 * @return
	 */
	public String getSetterName(String propertyName) {
		return "set" + upperFirstChar(propertyName);
	}

	/**
	 * 根据字段名和参数，得到setter方法
	 *
	 * @param cls
	 * @param propertyName
	 * @param paras
	 * @return Method
	 * @throws Exception
	 */
	public Method getSetter(Class cls, String propertyName, Class[] paras)throws Exception {
		if(cls==null) return null;

		String methodName = "set" + upperFirstChar(propertyName);
		String cacheKey=cls.getCanonicalName()+"."+methodName;
		Method method = methodCache.get(cacheKey);
		if(method!=null) return method;

		try{
			method = cls.getDeclaredMethod(methodName, paras);
		}catch(Exception e){
			method = cls.getMethod(methodName, paras);
		}
		if(method!=null) {
			method.setAccessible(true);
			methodCache.put(cacheKey, method);
		}

		return method;
	}

	/**
	 * 根据字段名和参数，得到setter方法，不抛异常
	 * @param cls
	 * @param propertyName
	 * @param paras
	 * @return
	 */
	public Method getSetterIgnoreException(Class cls, String propertyName, Class[] paras){
		try {
			if(cls==null) return null;

			String methodName = "set" + upperFirstChar(propertyName);
			String cacheKey=cls.getCanonicalName()+"."+methodName;
			Method method = methodCache.get(cacheKey);
			if(method!=null) return method;

			try{
				method = cls.getDeclaredMethod(methodName, paras);
			}catch(Exception e){
				method = cls.getMethod(methodName, paras);
			}
			if(method!=null) {
				method.setAccessible(true);
				methodCache.put(cacheKey, method);
			}

			return method;
		}catch(Exception e) {
			return null;
		}
	}


	/**
	 *
	 * @param propertyName
	 * @return
	 */
	public String getGetterName(String propertyName) {
		return "get" + upperFirstChar(propertyName);
	}

	/**
	 * 根据字段名和参数，得到getter方法
	 *
	 * @param cls
	 * @param propertyName
	 * @param paras
	 * @return Method
	 * @throws Exception
	 */
	public Method getGetter(Class cls, String propertyName, Class[] paras)throws Exception {
		if(cls==null) return null;

		String methodName = "get" + upperFirstChar(propertyName);
		String cacheKey=cls.getCanonicalName()+"."+methodName;
		Method method = methodCache.get(cacheKey);
		if(method!=null) return method;

		try{
			method = cls.getDeclaredMethod(methodName, paras);
		}catch(Exception e){
			try{
				method = cls.getMethod(methodName, paras);
			}catch (Exception ex){}
		}

		if(method==null){
			methodName = "is" + upperFirstChar(propertyName);
			try{
				method = cls.getDeclaredMethod(methodName, paras);
			}catch(Exception e){
				method = cls.getMethod(methodName, paras);
			}
		}

		if(method!=null) {
			method.setAccessible(true);
			methodCache.put(cacheKey, method);
		}

		return method;
	}

	/**
	 *
	 * @param cls
	 * @param propertyName
	 * @param paras
	 * @return
	 */
	public Method getGetterIgnoreException(Class cls, String propertyName, Class[] paras){
		try {
			if(cls==null) return null;

			String methodName = "get" + upperFirstChar(propertyName);
			String cacheKey=cls.getCanonicalName()+"."+methodName;
			Method method = methodCache.get(cacheKey);
			if(method!=null) return method;

			try{
				method = cls.getDeclaredMethod(methodName, paras);
			}catch(Exception e){
				try{
					method = cls.getMethod(methodName, paras);
				}catch (Exception ex){}
			}

			if(method==null){
				methodName = "is" + upperFirstChar(propertyName);
				try{
					method = cls.getDeclaredMethod(methodName, paras);
				}catch(Exception e){
					method = cls.getMethod(methodName, paras);
				}
			}

			if(method!=null) {
				method.setAccessible(true);
				methodCache.put(cacheKey, method);
			}

			return method;
		}catch(Exception e) {
			return null;
		}
	}

	/**
	 * 得到obj对象，propertyName变量的值，int等基本类型会返回为Integer等对应的类类型
	 *
	 * @param obj
	 * @param propertyName
	 * @return
	 */
	public Object getPropertyValue(Object obj, String propertyName){
		if(obj==null) return null;

		try {
			Method getter = this.getGetter(obj.getClass(), propertyName, null);
			if(getter==null) return null;

			return getter.invoke(obj, (Object[]) null);
		}catch (Exception e){
			return null;
		}
	}

	/**
	 * 设置obj对象，propertyName变量的值为value，并指定value的类类型
	 *
	 * @param obj
	 * @param propertyName
	 * @param value
	 * @param valueType
	 * @return 当setter方法返回值为void时，返回null
	 * @throws Exception
	 */
	public Object setPropertyValue(Object obj, String propertyName,Object[] value, Class[] valueType) throws Exception {
		if(obj==null) return null;

		Method setter = this.getSetter(obj.getClass(), propertyName, valueType);
		if(setter==null) return null;
		return setter.invoke(obj, value);
	}

	/**
	 * 是否全部大写
	 *
	 * @param src
	 * @return
	 */
	private boolean isAllUpperCase(String src){
		for (int i = 0; i < src.length(); i++){
			if (src.charAt(i) < 'A' || src.charAt(i) > 'Z'){
				return false;
			}
		}
		return true;
	}

	/**
	 * 是否全部大写
	 *
	 * @param src
	 * @return
	 */
	private boolean isAllUpperCaseExceptUnderline(String src){
		for (int i = 0; i < src.length(); i++){
			if ((src.charAt(i) < 'A' || src.charAt(i) > 'Z')
					&&(src.charAt(i) < '0' || src.charAt(i) > '9')
					&&src.charAt(i)!='_'){
				return false;
			}
		}
		return true;
	}


	/**
	 * 将第一个字符小写
	 * @param str
	 * @return
	 */
	public String lowerFirstChar(String str){
		StringBuffer sb = new StringBuffer(str);
		if (((int) 'A' <= (int) sb.charAt(0))
				&& ((int) sb.charAt(0) <= (int) 'Z')){
			sb.setCharAt(0, (char) ((int) sb.charAt(0) + 32));
		}

		return sb.toString();
	}

	/**
	 * 将第一个字符大写
	 * @param str
	 * @return
	 */
	public String upperFirstChar(String str){
		StringBuffer sb = new StringBuffer(str);
		if (((int) 'a' <= (int) sb.charAt(0))
				&& ((int) sb.charAt(0) <= (int) 'z')){
			sb.setCharAt(0, (char) ((int) sb.charAt(0) - 32));
		}

		return sb.toString();
	}

	/**
	 * 数据库列名转换为变量名
	 * @param s
	 * @return
	 */
	public String colNameToVariableName(String s){
		if(columnFieldNameMapping.containsKey(s)) return columnFieldNameMapping.get(s);
		String variableName = lowerFirstChar(dbNameToVariableName(s));
		columnFieldNameMapping.put(s, variableName);
		return variableName;
	}

	/**
	 * 全部转小写 -> 下划线后第一个字母大写(如果是字母) -> 去掉下划线
	 * 数据库列名需遵循规则：1，仅使用大写字母+数字+下划线；2，数字前不要加下划线
	 *
	 * @param s
	 * @return
	 */
	public String dbNameToVariableName(String s){
		//找出下划线位置
		List<Integer> underlines=new ArrayList<>();
		for (int i = 0; i < s.length(); i++){
			if(s.charAt(i)=='_'){
				underlines.add(Integer.valueOf(i));
			}
		}

		//全部转为小写
		if(isAllUpperCaseExceptUnderline(s)) s=s.toLowerCase();

		StringBuffer variable=new StringBuffer();
		for (int i = 0; i < s.length(); i++){
			char c=s.charAt(i);
			if(c=='_') continue;

			if(c>='a' && c<='z'){
				if(i==0 || (underlines.contains(Integer.valueOf(i-1)) && i>2)){
					variable.append((char)(c-32));
				}else{
					variable.append(c);
				}
			}else{
				variable.append(c);
			}
		}

		return variable.toString();
	}

	/**
	 * 数据库列名转换为变量名
	 * @param s
	 * @return
	 */
	public String colNameToVariableNameOld(String s){
		return lowerFirstChar(dbNameToVariableNameOld(s));
	}

	/**
	 * 全部转小写 -> 下划线后第一个字母大写(如果是字母) -> 去掉下划线
	 * 数据库列名需遵循规则：1，仅使用大写字母+数字+下划线；2，数字前不要加下划线
	 *
	 * @param s
	 * @return
	 */
	public String dbNameToVariableNameOld(String s){
		if ("".equals(s)){
			return s;
		}
		if (s.indexOf("_") < 0 && !isAllUpperCase(s)){
			return s;
		}
		StringBuffer result = new StringBuffer();
		boolean capitalize = true;
		boolean lastCapital = false;
		boolean lastDecapitalized = false;
		String p = null;
		for (int i = 0; i < s.length(); i++){
			String c = s.substring(i, i + 1);
			if ("_".equals(c) || " ".equals(c)){
				capitalize = true;
			}else{
				if (c.toUpperCase().equals(c)){
					if (lastDecapitalized && !lastCapital){
						capitalize = true;
					}
					lastCapital = true;
				} else {
					lastCapital = false;
				}
				if (capitalize){
					if (p != null && i == 2 && !p.equals("_")){
						result.append(c.toLowerCase());
						capitalize = false;
						p = c;
					} else {
						result.append(c.toUpperCase());
						capitalize = false;
						p = c;
					}
				} else {
					result.append(c.toLowerCase());
					lastDecapitalized = true;
					p = c;
				}
			}
		}
		return result.toString();
	}

	/**
	 * 变量名转换成选小写的带下划线的格式（大写字母前加下划线）
	 * @param src
	 * @return
	 */
	public String variableNameToDbColumn(String src){
		if(fieldColumnNameMapping.containsKey(src)) return fieldColumnNameMapping.get(src);

		if(isAllUpperCase(src)){
			String s = src.toLowerCase();
			fieldColumnNameMapping.put(src, s);
			return s;
		}

		StringBuffer result=new StringBuffer();
		for(int i=0; i<src.length(); i++){
			if(src.charAt(i) < 'A' || src.charAt(i) > 'Z'){//如果不是大写字母
				result.append(src.charAt(i));
			}else{//如果是大写并且不是首字母，在前面加下划线，并转换成小写
				if(i>0) result.append('_');
				result.append((char)(src.charAt(i)+32));
			}
		}

		fieldColumnNameMapping.put(src, result.toString());
		return result.toString();
	}

	/**
	 * 通过命名规则对应，将客户端提交的数据转换为beanClass的对象
	 * @param beanClass
	 * @param request
	 * @return
	 */
	public Object form2Bean(Class beanClass, HttpServletRequest request) throws Exception{
		if (request == null||beanClass == null) return null;

		Accessor accessor = Accessors.getAccessor(beanClass);
		return form2Bean(accessor.newObject(), request);
	}

	/**
	 * 通过命名规则对应，将客户端提交的数据赋值给bean的相应变量
	 * @param bean
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public Object form2Bean(Object bean, HttpServletRequest request) throws Exception{
		return form2Bean(bean, request, false);
	}


	/**
	 * 通过命名规则对应，将客户端提交的数据转换为beanClass的对象
	 * @param beanClass
	 * @param request
	 * @return
	 */
	public Object form2BeanTrim(Class beanClass, HttpServletRequest request) throws Exception{
		if (request == null||beanClass == null) return null;
		Accessor accessor = Accessors.getAccessor(beanClass);
		return form2BeanTrim(accessor.newObject(), request);
	}

	/**
	 * 通过命名规则对应，将客户端提交的数据赋值给bean的相应变量
	 * @param bean
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public Object form2BeanTrim(Object bean, HttpServletRequest request) throws Exception{
		return form2Bean(bean, request, true);
	}


	/**
	 *
	 * @param bean
	 * @param request
	 * @param trim
	 * @return
	 * @throws Exception
	 */
	public Object form2Bean(Object bean, HttpServletRequest request, boolean trim) throws Exception{
		if (request == null||bean == null){
			return null;
		}

		Accessor accessor = Accessors.getAccessor(bean.getClass());

		Class beanClass = bean.getClass();
		String className = beanClass.getCanonicalName();
		Enumeration paraNames = request.getParameterNames();
		while (paraNames.hasMoreElements()){
			String name = (String) paraNames.nextElement();
			if(JUtilString.isBlank(name)) continue;

			Field field = this.getField(beanClass, className, this.colNameToVariableName(name));
			if (field == null) continue;

			String value = getHttpParameter(request,name);
			if(trim && value!=null) value=value.trim();

			String type = field.getType().getName();

			if (value == null|| (value.equals("") && !type.equals("java.lang.String"))){
				continue;
			}

			String fieldName = field.getName();
			String setterName = getSetterName(fieldName);
			if (type.equals("java.lang.String")){
				accessor.invokeMethod(bean, setterName, value);
			} else if (type.equals("java.lang.Long")){
				if(JUtilTimestamp.isTimestamp(value)){
					accessor.invokeMethod(bean, setterName, Long.valueOf(Timestamp.valueOf(value).getTime()));
				}else if(JUtilTimestamp.isTimestamp(value+" 00:00:00")){
					accessor.invokeMethod(bean, setterName, Long.valueOf(Timestamp.valueOf(value+" 00:00:00").getTime()));
				}else{
					accessor.invokeMethod(bean, setterName, JUtilMath.toLong(value));
				}
			}else if(type.equals("long")){
				if(JUtilTimestamp.isTimestamp(value)){
					accessor.invokeMethod(bean, setterName, Timestamp.valueOf(value).getTime());
				}else if(JUtilTimestamp.isTimestamp(value+" 00:00:00")){
					accessor.invokeMethod(bean, setterName, Timestamp.valueOf(value+" 00:00:00").getTime());
				}else{
					accessor.invokeMethod(bean, setterName, JUtilMath.parseLong(value));
				}
			} else if (type.equals("java.lang.Integer")){
				accessor.invokeMethod(bean, setterName, JUtilMath.toInteger(value));
			}else if (type.equals("int")){
				accessor.invokeMethod(bean, setterName, JUtilMath.parseInt(value));
			} else if (type.equals("java.lang.Short")){
				accessor.invokeMethod(bean, setterName, JUtilMath.toShort(value));
			}else if (type.equals("short")){
				accessor.invokeMethod(bean, setterName, JUtilMath.parseShort(value));
			}  else if (type.equals("java.lang.Float")){
				accessor.invokeMethod(bean, setterName, JUtilMath.toFloat(value));
			}else if (type.equals("float")){
				accessor.invokeMethod(bean, setterName, JUtilMath.parseFloat(value));
			} else if (type.equals("java.lang.Double")){
				accessor.invokeMethod(bean, setterName, JUtilMath.toDouble(value));
			}else if (type.equals("double")){
				accessor.invokeMethod(bean, setterName, JUtilMath.parseDouble(value));
			} else if (type.equals("java.sql.Timestamp")){
				value = value.trim();
				if (value.matches("^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}$")){
					value += ":00";
				} else if (value.matches("^\\d{4}-\\d{2}-\\d{2} \\d{2}$")){
					value += ":00:00";
				} else if (JUtilString.isDate(value)){
					value += " 00:00:00";
				}
				if(JUtilTimestamp.isTimestamp(value)){
					accessor.invokeMethod(bean, setterName, Timestamp.valueOf(value));
				}
			}
		}

		return bean;
	}

	/**
	 * 通过命名规则对应(map的key对应字段名)，将map中的数据转换为beanClass的对象
	 * @param beanClass
	 * @param m
	 * @return
	 * @throws Exception
	 */
	public Object map2Bean(Class beanClass, Map m) throws Exception{
		if (m == null||beanClass == null) return null;
		Accessor accessor = Accessors.getAccessor(beanClass);
		return map2Bean(accessor.newObject(), m);
	}

	/**
	 * 通过命名规则对应(map的key对应字段名)，将map中的数据赋值给bean的相应变量
	 * @param bean
	 * @param m
	 * @return
	 * @throws Exception
	 */
	public Object map2Bean(Object bean, Map m) throws Exception{
		if (bean == null) return null;
		if (m == null) return bean;

		Accessor accessor = Accessors.getAccessor(bean.getClass());
		Class beanClass = bean.getClass();
		String className = beanClass.getCanonicalName();

		for (Iterator it = m.keySet().iterator(); it.hasNext();){
			String name = (String) it.next();
			Field field = this.getField(beanClass, className, this.colNameToVariableName(name));
			if (field == null) continue;

			String value = (String) m.get(name);

			String type = field.getType().getName();
			String fieldName = field.getName();

			if (value == null|| (value.equals("") && !type.equals("java.lang.String"))){
				continue;
			}

			String setterName = getSetterName(fieldName);
			if (type.equals("java.lang.String")){
				accessor.invokeMethod(bean, setterName, value);
			} else if (type.equals("java.lang.Long")){
				if(JUtilTimestamp.isTimestamp(value)){
					accessor.invokeMethod(bean, setterName, Long.valueOf(Timestamp.valueOf(value).getTime()));
				}else if(JUtilTimestamp.isTimestamp(value+" 00:00:00")){
					accessor.invokeMethod(bean, setterName, Long.valueOf(Timestamp.valueOf(value+" 00:00:00").getTime()));
				}else{
					accessor.invokeMethod(bean, setterName, JUtilMath.toLong(value));
				}
			}else if(type.equals("long")){
				if(JUtilTimestamp.isTimestamp(value)){
					accessor.invokeMethod(bean, setterName, Timestamp.valueOf(value).getTime());
				}else if(JUtilTimestamp.isTimestamp(value+" 00:00:00")){
					accessor.invokeMethod(bean, setterName, Timestamp.valueOf(value+" 00:00:00").getTime());
				}else{
					accessor.invokeMethod(bean, setterName, JUtilMath.parseLong(value));
				}
			} else if (type.equals("java.lang.Integer")){
				accessor.invokeMethod(bean, setterName, JUtilMath.toInteger(value));
			}else if (type.equals("int")){
				accessor.invokeMethod(bean, setterName, JUtilMath.parseInt(value));
			} else if (type.equals("java.lang.Short")){
				accessor.invokeMethod(bean, setterName, JUtilMath.toShort(value));
			}else if (type.equals("short")){
				accessor.invokeMethod(bean, setterName, JUtilMath.parseShort(value));
			}  else if (type.equals("java.lang.Float")){
				accessor.invokeMethod(bean, setterName, JUtilMath.toFloat(value));
			}else if (type.equals("float")){
				accessor.invokeMethod(bean, setterName, JUtilMath.parseFloat(value));
			} else if (type.equals("java.lang.Double")){
				accessor.invokeMethod(bean, setterName, JUtilMath.toDouble(value));
			}else if (type.equals("double")){
				accessor.invokeMethod(bean, setterName, JUtilMath.parseDouble(value));
			} else if (type.equals("java.sql.Timestamp")){
				value = value.trim();
				if (value.matches("^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}$")){
					value += ":00";
				} else if (value.matches("^\\d{4}-\\d{2}-\\d{2} \\d{2}$")){
					value += ":00:00";
				} else if (JUtilString.isDate(value)){
					value += " 00:00:00";
				}
				if(JUtilTimestamp.isTimestamp(value)){
					accessor.invokeMethod(bean, setterName, Timestamp.valueOf(value));
				}
			}
		}

		return bean;
	}

	/**
	 *
	 * @param bean
	 * @return
	 * @throws Exception
	 */
	public Map bean2Map(Object bean) throws Exception{
		Map map=new LinkedHashMap();
		if(bean!=null){
			Class beanClass = bean.getClass();
			Field[] fields=getDeclaredFieldsWithDirectlyParent(beanClass);
			for(int i=0;i<fields.length;i++){
				String name=fields[i].getName();
				Object o= this.getPropertyValue(bean,name);
				if(o!=null) map.put(name,o);
			}
		}
		return map;
	}

	/**
	 *
	 * @param bean
	 * @return
	 */
	public String bean2Json(Object bean){
		return bean2Json(bean, false);
	}

	/**
	 *
	 * @param bean
	 * @param encode
	 * @return
	 */
	public String bean2Json(Object bean, boolean encode){
		return bean2Json(bean, encode, null);
	}

	/**
	 *
	 * @param bean
	 * @param encode
	 * @param extraKeyValues
	 * @return
	 */
	public String bean2Json(Object bean, boolean encode, Map extraKeyValues){
		return bean2Json(bean, encode, null, null);
	}

	/**
	 *
	 * @param bean
	 * @param encode
	 * @param extraKeyValues
	 * @param excludes
	 * @return
	 */
	public String bean2Json(Object bean, boolean encode, Map extraKeyValues, List<String> excludes){
		return bean2Json(bean, encode, extraKeyValues, excludes, null, null);
	}

	/**
	 *
	 * @param bean
	 * @param encode
	 * @param extraKeyValues
	 * @param excludes
	 * @param converter
	 * @param converterParams
	 * @return
	 */
	public String bean2Json(Object bean, boolean encode, Map extraKeyValues, List<String> excludes, JUtilDataConverter converter, Object converterParams){
		return bean2Json(bean, encode, extraKeyValues, excludes, converter, converterParams, true);
	}

	/**
	 *
	 * @param bean
	 * @param encode
	 * @param extraKeyValues
	 * @param excludes
	 * @param converter
	 * @param converterParams
	 * @param ignoreNulls
	 * @return
	 */
	public String bean2Json(Object bean, boolean encode, Map extraKeyValues, List<String> excludes, JUtilDataConverter converter, Object converterParams, boolean ignoreNulls){
		if(bean==null) return "{}";

		StringBuffer s=new StringBuffer();
		s.append("{");

		Class beanClass = bean.getClass();
		Field[] fields=getDeclaredFieldsWithDirectlyParent(beanClass);

		int index=0;
		for(int i=0;i<fields.length;i++){
			String name=fields[i].getName();
			try{
				if(excludes!=null && excludes.contains(name)) continue;

				Object o = this.getPropertyValue(bean, name);
				if(ignoreNulls && o==null) continue;

				if(index>0) s.append(",");
				index++;

				if(converter != null) o=converter.convert(name, o, converterParams);

				//别名
				if(this.keyMappings!=null && this.keyMappings.containsKey(name)) name=this.keyMappings.get(name);
				s.append("\"").append(name).append("\":");

				if(o==null){
					s.append("null");
				}else if(o instanceof String){
					if(JUtilJSON.isJson(o.toString()) != null) {//本身是json串
						s.append(o);
					}else {
						if(this.valueMappings!=null && this.valueMappings.containsKey(o)) o=this.valueMappings.get(o);
						s.append("\"").append(JUtilJSON.convertChars(o.toString())).append("\"");
					}
				}else if((o instanceof Integer)
						||(o instanceof Long)
						||(o instanceof Short)
						||(o instanceof Boolean)){
					s.append(o);
				}else if((o instanceof Double)
						||(o instanceof Float)){
					s.append(JUtilMath.formatPrintPrecisionNoChange((Double) o, (Double) o, 0));
				}else if(o instanceof BigDecimal){
					s.append(o);
				}else if((o instanceof Timestamp)){
					s.append("\"").append(o.toString().substring(0, 19)).append("\"");
				}else if((o instanceof List)){
					s.append(this.beans2Json((List)o, excludes, converter, converterParams, ignoreNulls));
				}else if((o instanceof Map)){
					s.append(this.map2Json((Map)o));
				}else if((o instanceof JSONObject)){
					s.append(o);
				}else if((o instanceof Object[])){//数组
					Object[] array = (Object[])o;
					List list = new ArrayList();
					for(int j=0; j<array.length; j++) list.add(array[j]);
					s.append(this.beans2Json(list));
				}else {
					if(o.getClass().isPrimitive()){
						s.append(o);
					}else if(JUtilJSON.isJson(o.toString()) != null) {//本身是json串
						s.append(o);
					}else {
						s.append(this.bean2Json(o, encode, extraKeyValues, excludes, converter, converterParams, ignoreNulls));
					}
				}
			} catch (Exception e){
				s.append("{}");
			}
		}

		if(extraKeyValues!=null && !extraKeyValues.isEmpty()) {
			for(Iterator it=extraKeyValues.keySet().iterator(); it.hasNext();) {
				try {
					if(index>0) s.append(",");
					index++;

					Object key=it.next();
					String name=key.toString();
					Object o=extraKeyValues.get(key);
					s.append("\"").append(name).append("\":");
					if(o==null){
						s.append("null");
					}else if((o instanceof String)){
						if(JUtilJSON.isJson(o.toString()) != null) {//本身是json串
							s.append(o);
						}else {
							s.append("\"").append(JUtilJSON.convertChars(o.toString())).append("\"");
						}
					}else if((o instanceof Integer)
							||(o instanceof Long)
							||(o instanceof Short)
							||(o instanceof Boolean)){
						s.append(o);
					}else if((o instanceof Double)
							||(o instanceof Float)){
						s.append(JUtilMath.formatPrintPrecisionNoChange((Double)o, (Double)o, 0));
					}else if((o instanceof Timestamp)){
						s.append("\"").append(o.toString().substring(0, 19)).append("\"");
					}else if((o instanceof List)){
						s.append(this.beans2Json((List)o));
					}else if((o instanceof Map)){
						s.append(this.map2Json((Map)o));
					}else if((o instanceof JSONObject)){
						s.append(o);
					}else {
						if(o.getClass().isPrimitive()){
							s.append(o);
						}else if(JUtilJSON.isJson(o.toString()) != null) {//本身是json串
							s.append(o);
						}else {
							s.append(this.bean2Json(o));
						}
					}
				} catch (Exception e){
				}
			}
		}

		s.append("}");

		return s.toString();
	}

	/**
	 *
	 * @param beans
	 * @return
	 */
	public String beans2Json(List beans){
		return beans2Json(beans, null);
	}

	/**
	 *
	 * @param beans
	 * @param excludes List中的对象转json时，排除哪些字段
	 * @return
	 */
	public String beans2Json(List beans, List<String> excludes){
		return  beans2Json(beans, excludes, null, null);
	}

	/**
	 *
	 * @param beans
	 * @param excludes List中的对象转json时，排除哪些字段
	 * @param converter
	 * @param converterParams
	 * @return
	 */
	public String beans2Json(List beans, List<String> excludes, JUtilDataConverter converter, Object converterParams){
		return beans2Json(beans, excludes, converter, converterParams, true);
	}

	/**
	 *
	 * @param beans
	 * @param excludes
	 * @param converter
	 * @param converterParams
	 * @param ignoreNulls
	 * @return
	 */
	public String beans2Json(List beans, List<String> excludes, JUtilDataConverter converter, Object converterParams, boolean ignoreNulls){
		if(beans==null||beans.isEmpty()) return "[]";

		StringBuffer s=new StringBuffer();
		s.append("[");

		int index=0;
		for(int b=0;b<beans.size();b++){
			Object o=beans.get(b);
			if(o==null && ignoreNulls) continue;

			if(index>0) s.append(",");
			index++;

			if(o==null){
				s.append("null");
			}else if((o instanceof String)){
				if(JUtilJSON.isJson(o.toString()) != null) {//本身是json串
					s.append(o);
				}else {
					s.append("\"").append(JUtilJSON.convertChars(o.toString())).append("\"");
				}
			}else if((o instanceof Integer)
					||(o instanceof Long)
					||(o instanceof Short)
					||(o instanceof Boolean)){
				s.append(o);
			}else if((o instanceof Double)
					||(o instanceof Float)){
				s.append(JUtilMath.formatPrintPrecisionNoChange((Double) o, (Double) o, 0));
			}else if(o instanceof BigDecimal){
				s.append(o);
			}else if((o instanceof Timestamp)){
				s.append("\"").append(o.toString().substring(0, 19)).append("\"");
			}else if((o instanceof List)){
				s.append(this.beans2Json((List)o));
			}else if((o instanceof Map)){
				s.append(this.map2Json((Map)o));
			}else if((o instanceof JSONObject)){
				s.append(o);
			}else {
				if(JUtilJSON.isJson(o.toString()) != null) {//本身是json串
					s.append(o);
				}else {
					s.append(this.bean2Json(o, false, null, excludes, converter, converterParams));
				}
			}
		}

		s.append("]");

		return s.toString();
	}

	/**
	 * 通过命名规则对应，将json（不考虑子对象）转换为beanClass的对象
	 * @param beanClass
	 * @param json
	 * @return
	 * @throws Exception
	 */
	public Object json2Bean(Class beanClass, JSONObject json) throws Exception{
		if (json == null||beanClass == null) return null;
		Accessor accessor = Accessors.getAccessor(beanClass);
		return json2Bean(accessor.newObject(), json);
	}

	/**
	 * 通过命名规则对应，将json（不考虑子对象）的变量赋值给bean的相应变量
	 * @param bean
	 * @param json
	 * @return
	 * @throws Exception
	 */
	public Object json2Bean(Object bean, JSONObject json) throws Exception{
		if (json == null) return bean;
		if(bean==null) return null;

		Accessor accessor = Accessors.getAccessor(bean.getClass());

		Class beanClass = bean.getClass();
		String className = beanClass.getCanonicalName();

		for(Iterator<String> keys = json.keys(); keys.hasNext();){
			String name = keys.next();
			Field field = this.getField(beanClass, className, this.colNameToVariableName(name));
			if (field == null) continue;
			String type = field.getType().getName();
			String fieldName = field.getName();

			//列表
			if(type.equals("java.util.List")){
				Class cType = JUtilBean.getActualType(field.getGenericType());
				if(cType!=null) {
					JSONArray array = JUtilJSON.array(json, name);
					List<Object> objects = JUtilJSON.json2Beans(cType, array);
					this.getSetter(beanClass, fieldName, new Class[]{List.class}).invoke(bean, new Object[]{objects});
				}
				continue;
			}

			String value = JUtilJSON.string(json, name);
			if (value == null|| (value.equals("") && !type.equals("java.lang.String"))){
				continue;
			}

			String setterName = getSetterName(fieldName);
			if (type.equals("java.lang.String")){
				accessor.invokeMethod(bean, setterName, value);
			} else if (type.equals("java.lang.Long")){
				if(JUtilTimestamp.isTimestamp(value)){
					accessor.invokeMethod(bean, setterName, Long.valueOf(Timestamp.valueOf(value).getTime()));
				}else if(JUtilTimestamp.isTimestamp(value+" 00:00:00")){
					accessor.invokeMethod(bean, setterName, Long.valueOf(Timestamp.valueOf(value+" 00:00:00").getTime()));
				}else{
					accessor.invokeMethod(bean, setterName, JUtilMath.toLong(value));
				}
			}else if(type.equals("long")){
				if(JUtilTimestamp.isTimestamp(value)){
					accessor.invokeMethod(bean, setterName, Timestamp.valueOf(value).getTime());
				}else if(JUtilTimestamp.isTimestamp(value+" 00:00:00")){
					accessor.invokeMethod(bean, setterName, Timestamp.valueOf(value+" 00:00:00").getTime());
				}else{
					accessor.invokeMethod(bean, setterName, JUtilMath.parseLong(value));
				}
			} else if (type.equals("java.lang.Integer")){
				accessor.invokeMethod(bean, setterName, JUtilMath.toInteger(value));
			}else if (type.equals("int")){
				accessor.invokeMethod(bean, setterName, JUtilMath.parseInt(value));
			} else if (type.equals("java.lang.Short")){
				accessor.invokeMethod(bean, setterName, JUtilMath.toShort(value));
			}else if (type.equals("short")){
				accessor.invokeMethod(bean, setterName, JUtilMath.parseShort(value));
			}  else if (type.equals("java.lang.Float")){
				accessor.invokeMethod(bean, setterName, JUtilMath.toFloat(value));
			}else if (type.equals("float")){
				accessor.invokeMethod(bean, setterName, JUtilMath.parseFloat(value));
			} else if (type.equals("java.lang.Double")){
				accessor.invokeMethod(bean, setterName, JUtilMath.toDouble(value));
			}else if (type.equals("double")){
				accessor.invokeMethod(bean, setterName, JUtilMath.parseDouble(value));
			} else if (type.equals("java.lang.Boolean")){
				Boolean _value = JUtilJSON.getBoolean(json, name);
				accessor.invokeMethod(bean, setterName, _value);
			}else if (type.equals("boolean")){
				Boolean _value = JUtilJSON.getBoolean(json, name);
				accessor.invokeMethod(bean, setterName, _value==null?false:_value);
			}  else if (type.equals("java.sql.Timestamp")){
				value = value.trim();
				if (value.matches("^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}$")){
					value += ":00";
				} else if (value.matches("^\\d{4}-\\d{2}-\\d{2} \\d{2}$")){
					value += ":00:00";
				} else if (JUtilString.isDate(value)){
					value += " 00:00:00";
				}
				if(JUtilTimestamp.isTimestamp(value)){
					accessor.invokeMethod(bean, setterName, Timestamp.valueOf(value));
				}
			}else if(Map.class.isAssignableFrom(field.getType())){//是否Map的子类
				JSONObject valueJson = JUtilJSON.object(json, fieldName);
				Map _value= this.jsonPlain2Map(valueJson);
				Method setter=this.getSetter(beanClass,fieldName,new Class[] { field.getType() });
				if(setter!=null) setter.invoke(bean,new Object[] { _value });
			}else if(List.class.isAssignableFrom(field.getType())){//是否List的子类
				JSONArray valueJson = JUtilJSON.array(json, fieldName);
				if(valueJson!=null && valueJson.length()>0){
					List _value = (List)field.getType().getConstructor().newInstance();
					Class eType = JUtilBean.getActualType(field.getType());
					if(eType==null) eType=Object.class;
					for(int e=0; e<valueJson.length(); e++){
						Object _e = this.json2Bean(eType, JUtilJSON.get(valueJson, e));
						_value.add(_e);
					}
					Method setter=this.getSetter(beanClass,fieldName,new Class[] { field.getType() });
					if(setter!=null) setter.invoke(bean,new Object[] { _value });
				}
			}else if(!field.getType().isPrimitive()){
				JSONObject valueJson = JUtilJSON.object(json, fieldName);
				if(valueJson!=null){
					Object _value= this.json2Bean(field.getType(), valueJson);
					Method setter=this.getSetter(beanClass,fieldName,new Class[] { field.getType() });
					if(setter!=null) setter.invoke(bean,new Object[] { _value });
				}
			}
		}

		return bean;
	}

	/**
	 *
	 * @param json
	 * @return
	 */
	public Map<String, String> jsonPlain2Map(JSONObject json){
		Map<String, String> keyValues=new LinkedHashMap();

		for(Iterator<String> keys=json.keys(); keys.hasNext();){
			String key=keys.next();
			String val=JUtilJSON.string(json, key);

			keyValues.put(key, val);
		}

		return keyValues;
	}

	/**
	 *
	 * @param datas
	 * @return
	 */
	public String map2Json(Map datas){
		StringBuffer s=new StringBuffer();
		s.append("{");

		List<Object> keys = JUtilMap.listKeys(datas);
		keys = JUtilString.getInstance().bubble(keys, JUtilSorter.ASC);
		for(Object key : keys){
			Object o=datas.get(key);

			//别名
			if(this.keyMappings!=null && this.keyMappings.containsKey(key)) key=this.keyMappings.get(key);

			s.append("\""+key+"\":");
			if(o instanceof Map){
				s.append(map2Json((Map)o));
			}else if(o instanceof List){
				s.append(this.beans2Json((List)o));
			}else if(o instanceof String){
				if(JUtilJSON.isJson(o.toString()) != null) {//本身是json串
					s.append(o);
				}else {
					if(this.valueMappings!=null && this.valueMappings.containsKey(o)) o=this.valueMappings.get(o);
					s.append("\"").append(JUtilJSON.convert(o.toString())).append("\"");
				}
			}else if((o instanceof Integer)
					||(o instanceof Long)
					||(o instanceof Short)
					||(o instanceof Boolean)){
				s.append(o);
			}else if((o instanceof Double)
					||(o instanceof Float)){
				s.append(JUtilMath.formatPrintPrecisionNoChange((Double)o, (Double)o, 0));
			}else if(o instanceof BigDecimal){
				s.append(o);
			}else if(o instanceof Timestamp){
				s.append("\"").append(o).append("\"");
			}else{
				s.append(this.bean2Json(o));
			}
			s.append(",");
		}
		if(s.charAt(s.length()-1)==',') s=s.deleteCharAt(s.length()-1);
		s.append("}");
		return s.toString();
	}

	/**
	 *
	 * @param bean
	 * @return
	 * @throws Exception
	 */
	public Map bean2MapString(Object bean) throws Exception{
		Map map=new LinkedHashMap();
		if(bean!=null){
			Class beanClass = bean.getClass();
			Field[] fields=getDeclaredFieldsWithDirectlyParent(beanClass);
			for(int i=0;i<fields.length;i++){
				String name=fields[i].getName();
				Object o= this.getPropertyValue(bean,name);
				if(o!=null) map.put(name,o.toString());
			}
		}
		return map;
	}

	/**
	 *
	 * @param beans
	 * @param encoding
	 * @return
	 * @throws Exception
	 */
	public String beans2Xml(List beans,String encoding) throws Exception{
		return beans2Xml(beans,encoding,null);
	}

	/**
	 *
	 * @param beans
	 * @param encoding
	 * @param beanClass
	 * @return
	 * @throws Exception
	 */
	public String beans2Xml(List beans,String encoding,Class beanClass) throws Exception{
		if(beans==null) return "";

		if(encoding==null||"".equals(encoding)) encoding="UTF-8";

		Document doc=DocumentHelper.createDocument();
		doc.setXMLEncoding(encoding);

		Element root=doc.addElement("root");

		for(int i=0;i<beans.size();i++){
			Object bean=beans.get(i);
			if(bean==null) continue;

			Class _beanClass=beanClass==null?bean.getClass():beanClass;

			Element b=root.addElement("bean");
			b.addAttribute("clz",_beanClass.getName());

			Field[] fields=getDeclaredFieldsWithDirectlyParent(_beanClass);
			for(int f=0;f<fields.length;f++){
				String name=fields[f].getName();
				Object o= this.getPropertyValue(bean,name);
				if(o==null) continue;

				String type=fields[f].getType().getName();
				if(type.equals("java.lang.String")) type="s";
				else if(type.equals("java.lang.Integer")) type="I";
				else if(type.equals("java.lang.Double")) type="D";
				else if(type.equals("java.lang.Long")) type="L";
				else if(type.equals("java.lang.Short")) type="S";
				else if(type.equals("java.lang.Float")) type="F";
				else if(type.equals("java.sql.Timestamp")) type="T";

				Element ele=b.addElement("field");
				ele.addAttribute("name",name);
				ele.addAttribute("type",type);

				ele.setText(o.toString());
			}
		}

		return JUtilDom4j.toString(doc,encoding);
	}

	/**
	 *
	 * @param xml
	 * @param encoding
	 * @return
	 */
	public List xml2Beans(String xml,String encoding){
		return xml2Beans(xml,encoding,null);
	}

	/**
	 *
	 * @param xml
	 * @param encoding
	 * @param beanClass
	 * @return
	 */
	public List xml2Beans(String xml,String encoding,Class beanClass){
		return xml2Beans(null,xml,encoding,beanClass);
	}

	/**
	 *
	 * @param xml
	 * @param encoding
	 * @return
	 */
	public List xml2Beans(Map cache, String xml,String encoding){
		return xml2Beans(cache, xml,encoding,null);
	}

	/**
	 *
	 * @param cache
	 * @param xml
	 * @param encoding
	 * @param beanClass
	 * @return
	 */
	public List xml2Beans(Map cache, String xml,String encoding,Class beanClass){
		String key=null;
		if(cache!=null) {
			key=JUtilMD5.MD5EncodeToHex(xml);
			if(cache.containsKey(key)) return (List)cache.get(key);
		}

		List beans=new LinkedList();

		if(xml==null||"".equals(xml)) return beans;

		if(encoding==null||"".equals(encoding)) encoding="UTF-8";

		try{
			Document doc=JUtilDom4j.parseString(xml,encoding);
			Element root=doc.getRootElement();
			List bs=root.elements("bean");

			Map<String, Class> classCache=new HashMap();
			for(int b=0;b<bs.size();b++){
				Element be=(Element)bs.get(b);
				String clz=be.attributeValue("clz");
				if(clz!=null && !"".equals(clz)) {
					Class _clz=classCache.get(clz);
					if(_clz==null) {
						try {
							classCache.put(clz, Class.forName(clz));
						}catch(Exception e) {
							classCache.put(clz, Object.class);
						}
					}
				}
			}

			for(int b=0;b<bs.size();b++){
				Element be=(Element)bs.get(b);

				Class _beanClass=beanClass==null?classCache.get(be.attributeValue("clz")):beanClass;
				if(_beanClass==null) continue;

				Accessor accessor = Accessors.getAccessor(_beanClass);
				Object bean=accessor.newObject();

				List fields=be.elements("field");
				for(int f=0;f<fields.size();f++){
					Element fe=(Element)fields.get(f);
					String fieldName=fe.attributeValue("name");
					String type=fe.attributeValue("type");

					if(type.equals("s")) type="java.lang.String";
					else if(type.equals("I")) type="java.lang.Integer";
					else if(type.equals("D")) type="java.lang.Double";
					else if(type.equals("L")) type="java.lang.Long";
					else if(type.equals("S")) type="java.lang.Short";
					else if(type.equals("F")) type="java.lang.Float";
					else if(type.equals("T")) type="java.sql.Timestamp";

					String value=fe.getText();

					Field field= this.getField(_beanClass, fieldName);
					if(field==null) continue;

					if(value==null
							||value.equals("_N_U_L_L_")
							||(value.equals("")&&!type.equals("java.lang.String"))){
						continue;
					}

					if(type.equals("java.lang.String")){
						this.getSetter(_beanClass,fieldName,new Class[]{String.class}).invoke(bean,new Object[]{value});
					}else if(type.equals("java.lang.Long")){
						if(JUtilMath.isLong(value)){
							this.getSetter(_beanClass,fieldName,new Class[]{Long.class}).invoke(bean,new Object[]{Long.valueOf(value)});
						}else if(JUtilTimestamp.isTimestamp(value)){
							this.getSetter(beanClass, fieldName,new Class[] { Long.class }).invoke(bean,new Object[] { Long.valueOf(Timestamp.valueOf(value).getTime()) });
						}else if(JUtilTimestamp.isTimestamp(value+" 00:00:00")){
							this.getSetter(beanClass, fieldName,new Class[] { Long.class }).invoke(bean,new Object[] { Long.valueOf(Timestamp.valueOf(value+" 00:00:00").getTime()) });
						}
					}else if(type.equals("java.lang.Integer")){
						if(JUtilMath.isInt(value)){
							Method method= this.getSetterIgnoreException(_beanClass,fieldName,new Class[]{Integer.class});
							if(method==null) {
								//没找到参数类型为Integer的Setter，则尝试Double型的（为了应对数据字段类型改变的情况）
								method= this.getSetterIgnoreException(_beanClass,fieldName,new Class[]{Double.class});
								method.invoke(bean,new Object[]{Double.valueOf(value)});
							}else {
								method.invoke(bean,new Object[]{Integer.valueOf(value)});
							}
						}
					}else if(type.equals("java.lang.Short")){
						if(JUtilMath.isShort(value)){
							this.getSetter(_beanClass,fieldName,new Class[]{Short.class}).invoke(bean,new Object[]{Short.valueOf(value)});
						}
					}else if(type.equals("java.lang.Float")){
						if(JUtilMath.isNumber(value)){
							this.getSetter(_beanClass,fieldName,new Class[]{Float.class}).invoke(bean,new Object[]{Float.valueOf(value)});
						}
					}else if(type.equals("java.lang.Double")){
						if(JUtilMath.isNumber(value)){
							Method method= this.getSetterIgnoreException(_beanClass,fieldName,new Class[]{Double.class});
							if(method==null) {
								//没找到参数类型为Double的Setter，则尝试Integer型的（为了应对数据字段类型改变的情况）
								method= this.getSetterIgnoreException(_beanClass,fieldName,new Class[]{Integer.class});
								method.invoke(bean,new Object[]{Integer.valueOf(value)});
							}else {
								method.invoke(bean,new Object[]{Double.valueOf(value)});
							}
						}
					}else if(type.equals("java.sql.Timestamp")){
						value=value.trim();
						if(value.matches("^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}$")){
							value+=":00";
						}else if(value.matches("^\\d{4}-\\d{2}-\\d{2} \\d{2}$")){
							value+=":00:00";
						}else if(JUtilString.isDate(value)){
							value+=" 00:00:00";
						}

						if(JUtilTimestamp.isTimestamp(value)){
							this.getSetter(_beanClass,fieldName,new Class[]{Timestamp.class}).invoke(bean,new Object[]{Timestamp.valueOf(value)});
						}
					}
				}

				beans.add(bean);
			}
		}catch (Exception e){
			//log.log(e, Logger.LEVEL_ERROR);
		}

		if(cache!=null) {
			cache.put(key,beans);
		}

		return beans;
	}

	/**
	 * 将from中变量值拷贝到to对应的变量中，但忽略from中为null的变量
	 * @param from
	 * @param to
	 * @return
	 * @throws Exception
	 */
	public Object copyObjectIgnoreNulls(Object from, Object to) throws Exception{
		if (from == null || to == null){
			return to;
		}
		Class beanClass = from.getClass();
		Field[] fs = getDeclaredFieldsWithDirectlyParent(beanClass);
		for (int i = 0; i < fs.length; i++){
			String name = fs[i].getName();
			try{
				Object v = this.getPropertyValue(from, name);
				if (v != null){
					this.setPropertyValue(to, name, new Object[] { v },new Class[] { v.getClass() });
				}
			}catch(Exception e){}
		}

		return to;
	}


	/**
	 * 将from中变量值拷贝到to对应的变量中
	 * @param from
	 * @param to
	 * @return
	 */
	public Object copyObject(Object from, Object to) {
		if (from == null || to == null) return to;

		Class beanClass = from.getClass();
		Field[] fs = getDeclaredFieldsWithDirectlyParent(beanClass);
		for (int i = 0; i < fs.length; i++) {
			String name = fs[i].getName();
			try {
				Object v = this.getPropertyValue(from, name);
				//System.out.println(name+"="+v);
				this.setPropertyValue(to, name, new Object[]{v}, new Class[]{v.getClass()});
			} catch (Exception e) {
				//e.printStackTrace();
			}
		}

		return to;
	}
}
