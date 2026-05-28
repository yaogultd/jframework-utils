package j.util;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONParserConfiguration;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author 肖炯
 * @date 2023-06-07
 */
public class JUtilJSON{
	/**
	 *
	 * @param s
	 * @return
	 */
	public static Object isJson(String s){
		return isJson(s, false);
	}

	/**
	 *
	 * @param s
	 * @param toFix
	 * @return
	 */
	public static Object isJson(String s, boolean toFix){
		if(toFix) s = fixJsonUnescapedNewlines(s);
		if(JUtilString.isBlank(s)) return null;
		s = s.trim();

		JSONObject object;
		try{
			object = new JSONObject(s, (new JSONParserConfiguration()).withStrictMode(true));
		}catch (Exception e){
			object = null;
		}


		JSONArray array;
		try{
			array = new JSONArray(s, (new JSONParserConfiguration()).withStrictMode(true));
		}catch (Exception e){
			array = null;
		}

		if(object==null && array==null) return null;

		if("{}".equals(s)){
			return new JSONObject(s);
		}

		if("[]".equals(s)){
			return new JSONArray(s);
		}

		s=s.trim();

		if(s.startsWith("{") && s.endsWith("}")){
			JSONObject json=parse(s);
			return json==null || !json.keys().hasNext()?null:json;
		}else if(s.startsWith("[") && s.endsWith("]")){
			JSONArray arr=array(s);
			return arr==null || arr.length()==0?null:arr;
		}
		return null;
	}

	/**
	 *
	 * @param s
	 * @return
	 */
	public static JSONObject parse(String s){
		return parse(s, false);
	}

	/**
	 *
	 * @param s
	 * @param toFix
	 * @return
	 */
	public static JSONObject parse(String s, boolean toFix){
		try{
			if(toFix) s = fixJsonUnescapedNewlines(s);
			if(JUtilString.isBlank(s)) s="{}";
			return new JSONObject(s, (new JSONParserConfiguration()).withStrictMode(false));
		}catch(Exception e){
			//throw new JSONException("Invalid JSON string");
			return new JSONObject("{}");
		}
	}

	/**
	 *
	 * @param s
	 * @return
	 */
	public static JSONArray array(String s){
		return array(s, false);
	}

	/**
	 *
	 * @param s
	 * @param toFix
	 * @return
	 */
	public static JSONArray array(String s, boolean toFix){
		try{
			if(toFix) s = fixJsonUnescapedNewlines(s);
			if(JUtilString.isBlank(s)) s="[]";
			return new JSONArray(s);
		}catch(Exception e){
			//throw new JSONException("Invalid JSON string");
			return new JSONArray("[]");
		}
	}

	/**
	 *
	 * @param js
	 * @param key
	 * @return
	 */
	public static String string(JSONObject js,String key){
		try{
			Object obj=js.get(key);
			if(obj==null) return null;

			String s="";
			String cls=obj.getClass().getCanonicalName();
			if("java.lang.String".equals(cls)) s=obj.toString();
			else if("java.lang.Integer".equals(cls)) s=obj.toString();
			else if("java.lang.Long".equals(cls)) s=obj.toString();
			else if("java.lang.Double".equals(cls)) s=JUtilMath.formatPrintPrecisionNoChange((Double)obj, (Double)obj, 0);
			else if("java.math.BigDecimal".equals(cls)) s=JUtilMath.formatPrintPrecisionNoChange(((BigDecimal)obj).doubleValue(), ((BigDecimal)obj).doubleValue(), 0);

			else s=obj.toString();

			if(s!=null&&s.startsWith("jis:")) s=JUtilString.intSequence2String(s);
			return s;
		}catch(Exception e){
			return null;
		}
	}

	/**
	 *
	 * @param js
	 * @param key
	 * @return
	 */
	public static Double getDouble(JSONObject js,String key){
		try{
			Object obj=js.get(key);
			if(obj==null) return null;

			String cls=obj.getClass().getCanonicalName();
			if("java.lang.String".equals(cls)){
				String s=obj.toString();
				if(s.startsWith("jis:")) s=JUtilString.intSequence2String(s);

				if(JUtilMath.isNumber(s)) return Double.valueOf(s);
			}else if("java.lang.Integer".equals(cls)){
				return ((Integer)obj).doubleValue();
			}else if("java.lang.Long".equals(cls)){
				return ((Long)obj).doubleValue();
			}else if("java.lang.Double".equals(cls)){
				return (Double)obj;
			}else if("java.math.BigDecimal".equals(cls)){
				return ((BigDecimal)obj).doubleValue();
			}

			return null;
		}catch(Exception e){
			return null;
		}
	}

	/**
	 *
	 * @param js
	 * @param key
	 * @return
	 */
	public static Integer getInteger(JSONObject js,String key){
		try{
			Object obj=js.get(key);
			if(obj==null) return null;

			String cls=obj.getClass().getCanonicalName();
			if("java.lang.String".equals(cls)){
				String s=obj.toString();
				if(s.startsWith("jis:")) s=JUtilString.intSequence2String(s);

				if(JUtilMath.isInt(s)) return Integer.valueOf(s);
			}else if("java.lang.Integer".equals(cls)){
				return (Integer)obj;
			}else if("java.lang.Long".equals(cls)){
				return ((Long)obj).intValue();
			}else if("java.lang.Double".equals(cls)){
				return ((Double)obj).intValue();
			}else if("java.math.BigDecimal".equals(cls)){
				return ((BigDecimal)obj).intValue();
			}

			return null;
		}catch(Exception e){
			return null;
		}
	}

	/**
	 *
	 * @param js
	 * @param key
	 * @return
	 */
	public static Long getLong(JSONObject js,String key){
		try{
			Object obj=js.get(key);
			if(obj==null) return null;

			String cls=obj.getClass().getCanonicalName();
			if("java.lang.String".equals(cls)){
				String s=obj.toString();
				if(s.startsWith("jis:")) s=JUtilString.intSequence2String(s);

				if(JUtilMath.isLong(s)) return Long.valueOf(s);
			}else if("java.lang.Integer".equals(cls)){
				return ((Integer)obj).longValue();
			}else if("java.lang.Long".equals(cls)){
				return (Long)obj;
			}else if("java.lang.Double".equals(cls)){
				return ((Double)obj).longValue();
			}else if("java.math.BigDecimal".equals(cls)){
				return ((BigDecimal)obj).longValue();
			}

			return null;
		}catch(Exception e){
			return null;
		}
	}

	/**
	 *
	 * @param js
	 * @param key
	 * @return
	 */
	public static Boolean getBoolean(JSONObject js,String key){
		try{
			return js.getBoolean(key);
		}catch(Exception e){
			return null;
		}
	}

	/**
	 *
	 * @param js
	 * @param key
	 * @return
	 */
	public static JSONArray array(JSONObject js,String key){
		try{
			return js.getJSONArray(key);
		}catch(Exception e){
			return null;
		}
	}

	/**
	 *
	 * @param array
	 * @param index
	 * @return
	 */
	public static JSONObject get(JSONArray array,int index){
		try{
			return array.getJSONObject(index);
		}catch(Exception e){
			return null;
		}
	}

	/**
	 *
	 * @param array
	 * @param index
	 * @return
	 */
	public static Object getObject(JSONArray array,int index){
		try{
			return array.get(index);
		}catch(Exception e){
			return null;
		}
	}

	/**
	 *
	 * @param js
	 * @param key
	 * @return
	 */
	public static JSONObject object(JSONObject js,String key){
		try{
			return js.getJSONObject(key);
		}catch(Exception e){
			return null;
		}
	}

	/**
	 *
	 * @param js
	 * @param key
	 * @return
	 */
	public static Object get(JSONObject js,String key){
		try{
			return js.get(key);
		}catch(Exception e){
			return null;
		}
	}

	/**
	 *
	 * @param s
	 * @return
	 */
	public static String format(String s){
		return "jis:"+JUtilString.string2IntSequence(s);
	}

	/**
	 *
	 * @param s
	 * @return
	 */
	public static String encode(String s){
		if(s==null||"".equals(s)) return s;
		return JUtilString.encodeURI(s, "UTF-8");
	}

	/**
	 *
	 * @param s
	 * @return
	 */
	public static String convert(String s){
		if(s==null||"".equals(s)) return "";

		s=JUtilString.replaceAll(s, "\\", "\\\\");
		s=JUtilString.replaceAll(s, "\"", "\\\"");
		s=JUtilString.replaceAll(s, "\b", "\\b");
		s=JUtilString.replaceAll(s, "\f", "\\f");
		s=JUtilString.replaceAll(s, "\n", "\\n");
		s=JUtilString.replaceAll(s, "\r", "\\r");
		s=JUtilString.replaceAll(s, "\t", "\\t");

		return s;
	}

	/**
	 *
	 * @param s
	 * @return
	 */
	public static String convertChars(String s){
		if(s==null||"".equals(s)) return "";

		s=JUtilString.replaceAll(s, "\\", "\\\\");
		s=JUtilString.replaceAll(s, "\"", "\\\"");
		s=JUtilString.replaceAll(s, "\b", "\\b");
		s=JUtilString.replaceAll(s, "\f", "\\f");
		s=JUtilString.replaceAll(s, "\n", "\\n");
		s=JUtilString.replaceAll(s, "\r", "\\r");
		s=JUtilString.replaceAll(s, "\t", "\\t");

		return s;
	}

	/**
	 *
	 * @param cls
	 * @param json
	 * @return
	 */
	public static Object json2Bean(Class cls, JSONObject json){
		try{
			return JUtilBean.json2Bean(cls, json);
		}catch (Exception ignored){
			//ignored.printStackTrace();
			return null;
		}
	}

	/**
	 *
	 * @param object
	 * @param json
	 * @return
	 */
	public static Object json2Bean(Object object, JSONObject json){
		try{
			return JUtilBean.json2Bean(object, json);
		}catch (Exception ignored){
			return null;
		}
	}

	/**
	 *
	 * @param cls
	 * @param json
	 * @return
	 */
	public static Object json2Bean(Class cls, String json){
		try{
			return JUtilBean.json2Bean(cls, parse(json));
		}catch (Exception ignored){
			//ignored.printStackTrace();
			return null;
		}
	}

	/**
	 *
	 * @param object
	 * @param json
	 * @return
	 */
	public static Object json2Bean(Object object, String json){
		try{
			return JUtilBean.json2Bean(object, parse(json));
		}catch (Exception ignored){
			return null;
		}
	}

	/**
	 *
	 * @param cls
	 * @param array
	 * @return
	 */
	public static List json2Beans(Class cls, JSONArray array){
		List objects = new ArrayList<>();
		if(array == null) return objects;

		String clsName = cls.getCanonicalName();
		for(int i=0; i<array.length(); i++){
			if("java.lang.String".equals(clsName)){
				objects.add(array.getString(i));
			}else if("java.lang.Integer".equals(clsName) || "int".equals(clsName)){
				objects.add(array.getInt(i));
			}else if("java.lang.Long".equals(clsName) || "long".equals(clsName)){
				objects.add(array.getLong(i));
			}else if("java.lang.Double".equals(clsName) || "double".equals(clsName)){
				objects.add(array.getDouble(i));
			}else if("java.lang.Float".equals(clsName) || "float".equals(clsName)){
				objects.add(array.getFloat(i));
			}else if("java.lang.Short".equals(clsName) || "short".equals(clsName)){
				objects.add((short)array.getInt(i));
			}else if("java.lang.Boolean".equals(clsName) || "boolean".equals(clsName)){
				objects.add(array.getBoolean(i));
			}else{
				objects.add(json2Bean(cls, get(array, i)));
			}
		}
		return objects;
	}

	/**
	 *
	 * @param cls
	 * @param jsonArrayString
	 * @return
	 */
	public static List json2Beans(Class cls, String jsonArrayString){
		return json2Beans(cls, array(jsonArrayString));
	}

	/**
	 *
	 * @param json
	 * @return
	 */
	public static String fixJsonUnescapedNewlines(String json) {
		if (json == null || json.isEmpty()) return json;

		StringBuilder out = new StringBuilder(json.length() + 16);
		boolean inString = false;
		boolean escaping = false;

		for (int i = 0; i < json.length(); i++) {
			char c = json.charAt(i);

			if (!inString) {
				if (c == '"') inString = true;
				out.append(c);
				continue;
			}

			// inString == true
			if (escaping) {
				out.append(c);
				escaping = false;
				continue;
			}

			if (c == '\\') {
				out.append(c);
				escaping = true;
				continue;
			}

			if (c == '"') {
				out.append(c);
				inString = false;
				continue;
			}

			if (c == '\n') {
				out.append("\\n");
				continue;
			}
			if (c == '\r') {
				out.append("\\r");
				continue;
			}

			out.append(c);
		}

		return out.toString();
	}

	public static void main(String[] args) throws Exception {
		String s="{\n" +
				"\t\"translation\": \"## 个人机器人市场规模与份额\n" +
				"示例文本\n" +
				"示例文本\n" +
				"示例文本\n" +
				"示例文本\n" +
				"\n" +
				"示例文本\"\n" +
				"}";

		s = fixJsonUnescapedNewlines(s);
		System.out.println(s);

		JSONObject jsonObject = parse(s);
		System.out.println(isJson(jsonObject.toString(2)));
	}
}
