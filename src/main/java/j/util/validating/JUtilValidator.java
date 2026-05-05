package j.util.validating;

import j.core.type.Result;
import j.util.JUtilBean;
import j.util.JUtilString;
import j.util.validating.anno.Validator;

import java.lang.reflect.Field;
import java.math.BigDecimal;

/**
 * 数据校验
 */
public final class JUtilValidator {
    /**
     *
     * @param object
     * @return
     */
    public static Result validate(Object object) {
        return validate(object, null);
    }

    /**
     *
     * @param object
     * @param stringEncoding
     * @return
     */
    public static Result validate(Object object, String stringEncoding) {
        if(object == null) return new Result(false, "", "");

        //获取字段
        Class beanClass = object.getClass();
        Field[] fields=beanClass.getDeclaredFields();

        for(Field field : fields) {
            //获取注解
            Validator fieldAnnotation = field.getAnnotation(Validator.class);
            if(fieldAnnotation==null) continue;

            boolean notBlank = fieldAnnotation.notBlank();
            boolean notNull = fieldAnnotation.notNull();
            boolean notZero = fieldAnnotation.notZero();
            boolean ignoreCase = fieldAnnotation.ignoreCase();
            int minChars = fieldAnnotation.minChars();
            int maxChars = fieldAnnotation.maxChars();
            int minBytes = fieldAnnotation.minBytes();
            int maxBytes = fieldAnnotation.maxBytes();
            String regex = fieldAnnotation.regex();
            String errCode = fieldAnnotation.errCode();
            String errMessage = fieldAnnotation.errMessage();
            String candidateValues = fieldAnnotation.candidateValues();

            //字段值
            Object val = JUtilBean.getPropertyValue(object, field.getName());

            //字段类型名称
            String typeName = field.getGenericType().getTypeName();

            //非空
            if(notNull) {
                if(!JUtilString.equals(typeName, "int")
                        && !JUtilString.equals(typeName, "long")
                        && !JUtilString.equals(typeName, "float")
                        && !JUtilString.equals(typeName, "double")
                        && !JUtilString.equals(typeName, "short")
                        && !JUtilString.equals(typeName, "char")
                        && !JUtilString.equals(typeName, "boolean")) {//非primitive类型
                    if(null == val) return new Result(false, errCode, errMessage);
                }
            }

            //非空非”“
            if(notBlank) {
                if(JUtilString.equals(typeName, "java.lang.String")
                        || JUtilString.equals(typeName, "char")) {
                    if(JUtilString.isBlank((String)val)) return new Result(false, errCode, errMessage);
                }
            }

            //非0值
            if(notZero) {
                if(JUtilString.equals(typeName, "java.lang.Integer")
                        || JUtilString.equals(typeName, "int")
                        || JUtilString.equals(typeName, "java.lang.Long")
                        || JUtilString.equals(typeName, "long")
                        || JUtilString.equals(typeName, "java.lang.Float")
                        || JUtilString.equals(typeName, "float")
                        || JUtilString.equals(typeName, "java.lang.Double")
                        || JUtilString.equals(typeName, "double")
                        || JUtilString.equals(typeName, "java.lang.Short")
                        || JUtilString.equals(typeName, "short")
                        || JUtilString.equals(typeName, "java.math.BigDecimal")) {
                    if(null == val || JUtilString.equals(String.valueOf(val), "0")) {
                        return new Result(false, errCode, errMessage);
                    }
                }
            }

            //正则匹配
            if(!JUtilString.isBlank(regex)) {
                if((JUtilString.equals(typeName, "java.lang.String") || JUtilString.equals(typeName, "char"))
                    && !String.valueOf(val).matches(regex)) {
                    return new Result(false, errCode, errMessage);
                }
            }

            //长度检验
            if (minChars > 0
                    && JUtilString.equals(typeName, "java.lang.String")
                    && ((String)val).length() < minChars) {
                return new Result(false, errCode, errMessage);
            }
            if (maxChars > 0
                    && JUtilString.equals(typeName, "java.lang.String")
                    && ((String)val).length() > maxChars) {
                return new Result(false, errCode, errMessage);
            }
            if (minBytes > 0
                    && JUtilString.equals(typeName, "java.lang.String")
                    && JUtilString.bytes((String)val, stringEncoding) < minBytes) {
                return new Result(false, errCode, errMessage);
            }
            if (maxBytes > 0
                    && JUtilString.equals(typeName, "java.lang.String")
                    && JUtilString.bytes((String)val, stringEncoding) > maxBytes) {
                return new Result(false, errCode, errMessage);
            }

            //可选值检查
            if(!JUtilString.isBlank(candidateValues)) {
                String[] values = JUtilString.getTokens(candidateValues, "\f");

                //字符串
                if(JUtilString.equals(typeName, "java.lang.String")){
                    if(ignoreCase){
                        if(!JUtilString.containIgnoreCase(values, (String)val)){
                            return new Result(false, errCode, errMessage);
                        }
                    }

                    if(!JUtilString.contain(values, (String)val)){
                        return new Result(false, errCode, errMessage);
                    }
                }

                //数值型
                if(JUtilString.equals(typeName, "java.lang.Integer")
                        || JUtilString.equals(typeName, "int")) {
                    boolean exists = false;
                    for(int i=0; i<values.length; i++){
                        if(((Integer)val) == Integer.parseInt(values[i])) {
                            exists=true;
                            break;
                        }
                    }
                    if(!exists) return new Result(false, errCode, errMessage);
                }

                if(JUtilString.equals(typeName, "java.lang.Long")
                        || JUtilString.equals(typeName, "long")) {
                    boolean exists = false;
                    for(int i=0; i<values.length; i++){
                        if(((Long)val) == Long.parseLong(values[i])) {
                            exists=true;
                            break;
                        }
                    }
                    if(!exists) return new Result(false, errCode, errMessage);
                }

                if(JUtilString.equals(typeName, "java.lang.Float")
                        || JUtilString.equals(typeName, "float")) {
                    boolean exists = false;
                    for(int i=0; i<values.length; i++){
                        if(((Float)val) == Float.parseFloat(values[i])) {
                            exists=true;
                            break;
                        }
                    }
                    if(!exists) return new Result(false, errCode, errMessage);
                }

                if(JUtilString.equals(typeName, "java.lang.Double")
                        || JUtilString.equals(typeName, "double")) {
                    boolean exists = false;
                    for(int i=0; i<values.length; i++){
                        if(((Double)val) == Double.parseDouble(values[i])) {
                            exists=true;
                            break;
                        }
                    }
                    if(!exists) return new Result(false, errCode, errMessage);
                }

                if(JUtilString.equals(typeName, "java.lang.Short")
                        || JUtilString.equals(typeName, "short")) {
                    boolean exists = false;
                    for(int i=0; i<values.length; i++){
                        if(((Short)val) == Short.parseShort(values[i])) {
                            exists=true;
                            break;
                        }
                    }
                    if(!exists) return new Result(false, errCode, errMessage);
                }

                if(JUtilString.equals(typeName, "java.math.BigDecimal")) {
                    boolean exists = false;
                    for(int i=0; i<values.length; i++){
                        if(((BigDecimal)val).compareTo(new BigDecimal(values[i])) == 0) {
                            exists=true;
                            break;
                        }
                    }
                    if(!exists) return new Result(false, errCode, errMessage);
                }
            }
        }

        return new Result(true, "", "");
    }
}
