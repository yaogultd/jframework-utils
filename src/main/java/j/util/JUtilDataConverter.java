package j.util;

import java.io.Serializable;

/**
 * 对象转换
 */
public interface JUtilDataConverter extends Serializable {
    /**
     * 将对象original转换其它任何想要的
     * @param identity 对象的标识（可能是字段名、Map里对象的key......等待，根据业务需求来定）
     * @param original 原始对象
     * @param params 数据转换所需信息，有业务需求确定
     * @return
     */
    public Object convert(Object identity, Object original, Object params);
}
