package j.util;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

/**
 *
 * @author 肖炯
 * @date 2023-06-07
 *
 */
public class JUtilBean {
	//默认实例
	@Getter
	private static final JUtilBeanImpl instance = new JUtilBeanImpl();

	//获取list声明时的泛型类型
	public static Class getActualType(List<?> list) throws Exception{
		Type type = list.getClass().getGenericSuperclass();
		if (type instanceof ParameterizedType) {
			ParameterizedType pt = (ParameterizedType)type;
			Type[] actualTypeArguments = pt.getActualTypeArguments();
			if (actualTypeArguments.length > 0) return Class.forName(actualTypeArguments[0].getTypeName());
		}
		return null;
	}

	//获取泛型类型
	public static Class getActualType(Type type) throws Exception{
		if (type instanceof ParameterizedType) {
			ParameterizedType pt = (ParameterizedType)type;
			Type[] actualTypeArguments = pt.getActualTypeArguments();
			if (actualTypeArguments.length > 0) return Class.forName(actualTypeArguments[0].getTypeName());
		}
		return null;
	}

	/**
	 *
	 * @param cls
	 * @param fieldName
	 * @return
	 * @throws Exception
	 */
	public static Field getField(Class cls, String fieldName)throws Exception {
		return instance.getField(cls, fieldName);
	}

	/**
	 *
	 * @param propertyName
	 * @return
	 */
	public static String getSetterName(String propertyName) {
		return instance.getSetterName(propertyName);
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
	public static Method getSetter(Class cls, String propertyName, Class[] paras)throws Exception {
		return instance.getSetter(cls, propertyName, paras);
	}

	/**
	 * 根据字段名和参数，得到setter方法，不抛异常
	 * @param cls
	 * @param propertyName
	 * @param paras
	 * @return
	 */
	public static Method getSetterIgnoreException(Class cls, String propertyName, Class[] paras){
		return instance.getSetterIgnoreException(cls, propertyName, paras);
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
	public static Method getGetter(Class cls, String propertyName, Class[] paras)throws Exception {
		return instance.getGetter(cls, propertyName, paras);
	}

	/**
	 *
	 * @param cls
	 * @param propertyName
	 * @param paras
	 * @return
	 */
	public static Method getGetterIgnoreException(Class cls, String propertyName, Class[] paras){
		return instance.getGetterIgnoreException(cls, propertyName, paras);
	}

	/**
	 * 得到obj对象，propertyName变量的值，int等基本类型会返回为Integer等对应的类类型
	 *
	 * @param obj
	 * @param propertyName
	 * @return
	 */
	public static Object getPropertyValue(Object obj, String propertyName){
		return instance.getPropertyValue(obj, propertyName);
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
	public static Object setPropertyValue(Object obj, String propertyName,Object[] value, Class[] valueType) throws Exception {
		return instance.setPropertyValue(obj, propertyName, value, valueType);
	}

	/**
	 * 将第一个字符小写
	 * @param str
	 * @return
	 */
	public static String lowerFirstChar(String str){
		return instance.lowerFirstChar(str);
	}

	/**
	 * 将第一个字符大写
	 * @param str
	 * @return
	 */
	public static String upperFirstChar(String str){
		return instance.upperFirstChar(str);
	}

	/**
	 * 数据库列名转换为变量名
	 * @param s
	 * @return
	 */
	public static String colNameToVariableName(String s){
		return instance.colNameToVariableName(s);
	}

	/**
	 * 全部转小写 -> 下划线后第一个字母大写(如果是字母) -> 去掉下划线
	 * 数据库列名需遵循规则：1，仅使用大写字母+数字+下划线；2，数字前不要加下划线
	 *
	 * @param s
	 * @return
	 */
	public static String dbNameToVariableName(String s){
		return instance.dbNameToVariableName(s);
	}

	/**
	 * 数据库列名转换为变量名
	 * @param s
	 * @return
	 */
	public static String colNameToVariableNameOld(String s){
		return instance.colNameToVariableNameOld(s);
	}

	/**
	 * 全部转小写 -> 下划线后第一个字母大写(如果是字母) -> 去掉下划线
	 * 数据库列名需遵循规则：1，仅使用大写字母+数字+下划线；2，数字前不要加下划线
	 *
	 * @param s
	 * @return
	 */
	public static String dbNameToVariableNameOld(String s){
		return instance.dbNameToVariableNameOld(s);
	}

	/**
	 * 变量名转换成选小写的带下划线的格式（大写字母前加下划线）
	 * @param src
	 * @return
	 */
	public static String variableNameToDbColumn(String src){
		return instance.variableNameToDbColumn(src);
	}

	/**
	 * 通过命名规则对应，将客户端提交的数据转换为beanClass的对象
	 * @param beanClass
	 * @param request
	 * @return
	 */
	public static Object form2Bean(Class beanClass, HttpServletRequest request) throws Exception{
		return instance.form2Bean(beanClass, request);
	}

	/**
	 * 通过命名规则对应，将客户端提交的数据赋值给bean的相应变量
	 * @param bean
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public static Object form2Bean(Object bean, HttpServletRequest request) throws Exception{
		return instance.form2Bean(bean, request);
	}


	/**
	 * 通过命名规则对应，将客户端提交的数据转换为beanClass的对象
	 * @param beanClass
	 * @param request
	 * @return
	 */
	public static Object form2BeanTrim(Class beanClass, HttpServletRequest request) throws Exception{
		return instance.form2BeanTrim(beanClass, request);
	}

	/**
	 * 通过命名规则对应，将客户端提交的数据赋值给bean的相应变量
	 * @param bean
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public static Object form2BeanTrim(Object bean, HttpServletRequest request) throws Exception{
		return instance.form2BeanTrim(bean, request);
	}

	/**
	 * 通过命名规则对应(map的key对应字段名)，将map中的数据转换为beanClass的对象
	 * @param beanClass
	 * @param m
	 * @return
	 * @throws Exception
	 */
	public static Object map2Bean(Class beanClass, Map m) throws Exception{
		return instance.map2Bean(beanClass, m);
	}

	/**
	 * 通过命名规则对应(map的key对应字段名)，将map中的数据赋值给bean的相应变量
	 * @param bean
	 * @param m
	 * @return
	 * @throws Exception
	 */
	public static Object map2Bean(Object bean, Map m) throws Exception{
		return instance.map2Bean(bean, m);
	}

	/**
	 *
	 * @param bean
	 * @return
	 * @throws Exception
	 */
	public static Map bean2Map(Object bean) throws Exception{
		return instance.bean2Map(bean);
	}

	/**
	 *
	 * @param bean
	 * @return
	 */
	public static String bean2Json(Object bean){
		return instance.bean2Json(bean);
	}

	/**
	 *
	 * @param bean
	 * @param encode
	 * @return
	 */
	public static String bean2Json(Object bean, boolean encode){
		return instance.bean2Json(bean, encode);
	}

	/**
	 *
	 * @param bean
	 * @param encode
	 * @param extraKeyValues
	 * @return
	 */
	public static String bean2Json(Object bean, boolean encode, Map extraKeyValues){
		return instance.bean2Json(bean, encode, extraKeyValues);
	}

	/**
	 *
	 * @param bean
	 * @param encode
	 * @param extraKeyValues
	 * @param excludes
	 * @return
	 */
	public static String bean2Json(Object bean, boolean encode, Map extraKeyValues, List<String> excludes){
		return instance.bean2Json(bean, encode, extraKeyValues, excludes);
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
	public static String bean2Json(Object bean, boolean encode, Map extraKeyValues, List<String> excludes, JUtilDataConverter converter, Object converterParams){
		return instance.bean2Json(bean, encode, extraKeyValues, excludes, converter, converterParams);
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
	public static String bean2Json(Object bean, boolean encode, Map extraKeyValues, List<String> excludes, JUtilDataConverter converter, Object converterParams, boolean ignoreNulls){
		return instance.bean2Json(bean, encode, extraKeyValues, excludes, converter, converterParams, ignoreNulls);
	}

	/**
	 *
	 * @param beans
	 * @return
	 */
	public static String beans2Json(List beans){
		return instance.beans2Json(beans);
	}

	/**
	 *
	 * @param beans
	 * @param excludes List中的对象转json时，排除哪些字段
	 * @return
	 */
	public static String beans2Json(List beans, List<String> excludes){
		return instance.beans2Json(beans, excludes);
	}

	/**
	 *
	 * @param beans
	 * @param excludes List中的对象转json时，排除哪些字段
	 * @param converter
	 * @param converterParams
	 * @return
	 */
	public static String beans2Json(List beans, List<String> excludes, JUtilDataConverter converter, Object converterParams){
		return instance.beans2Json(beans, excludes, converter, converterParams);
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
	public static String beans2Json(List beans, List<String> excludes, JUtilDataConverter converter, Object converterParams, boolean ignoreNulls){
		return instance.beans2Json(beans, excludes, converter, converterParams, ignoreNulls);
	}

	/**
	 * 通过命名规则对应，将json（不考虑子对象）转换为beanClass的对象
	 * @param beanClass
	 * @param json
	 * @return
	 * @throws Exception
	 */
	public static Object json2Bean(Class beanClass, JSONObject json) throws Exception{
		return instance.json2Bean(beanClass, json);
	}

	/**
	 * 通过命名规则对应，将json（不考虑子对象）的变量赋值给bean的相应变量
	 * @param bean
	 * @param json
	 * @return
	 * @throws Exception
	 */
	public static Object json2Bean(Object bean, JSONObject json) throws Exception{
		return instance.json2Bean(bean, json);
	}

	/**
	 *
	 * @param json
	 * @return
	 */
	public static Map<String, String> jsonPlain2Map(JSONObject json){
		return instance.jsonPlain2Map(json);
	}

	/**
	 *
	 * @param datas
	 * @return
	 */
	public static String map2Json(Map datas){
		return instance.map2Json(datas);
	}

	/**
	 *
	 * @param bean
	 * @return
	 * @throws Exception
	 */
	public static Map bean2MapString(Object bean) throws Exception{
		return instance.bean2MapString(bean);
	}

	/**
	 *
	 * @param beans
	 * @param encoding
	 * @return
	 * @throws Exception
	 */
	public static String beans2Xml(List beans,String encoding) throws Exception{
		return instance.beans2Xml(beans, encoding);
	}

	/**
	 *
	 * @param beans
	 * @param encoding
	 * @param beanClass
	 * @return
	 * @throws Exception
	 */
	public static String beans2Xml(List beans,String encoding,Class beanClass) throws Exception{
		return instance.beans2Xml(beans, encoding, beanClass);
	}

	/**
	 *
	 * @param xml
	 * @param encoding
	 * @return
	 */
	public static List xml2Beans(String xml,String encoding){
		return instance.xml2Beans(xml, encoding);
	}

	/**
	 *
	 * @param xml
	 * @param encoding
	 * @param beanClass
	 * @return
	 */
	public static List xml2Beans(String xml,String encoding,Class beanClass){
		return instance.xml2Beans(xml, encoding, beanClass);
	}

	/**
	 *
	 * @param xml
	 * @param encoding
	 * @return
	 */
	public static List xml2Beans(Map cache, String xml,String encoding){
		return instance.xml2Beans(cache, xml, encoding);
	}

	/**
	 *
	 * @param cache
	 * @param xml
	 * @param encoding
	 * @param beanClass
	 * @return
	 */
	public static List xml2Beans(Map cache, String xml,String encoding,Class beanClass){
		return instance.xml2Beans(cache, xml, encoding, beanClass);
	}

	/**
	 * 将from中变量值拷贝到to对应的变量中，但忽略from中为null的变量
	 * @param from
	 * @param to
	 * @return
	 * @throws Exception
	 */
	public static Object copyObjectIgnoreNulls(Object from, Object to) throws Exception{
		return instance.copyObjectIgnoreNulls(from, to);
	}


	/**
	 * 将from中变量值拷贝到to对应的变量中
	 * @param from
	 * @param to
	 * @return
	 */
	public static Object copyObject(Object from, Object to) {
		return instance.copyObject(from, to);
	}
}