package j.core.type.index;

import j.util.ConcurrentList;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Map;

@Getter
@Setter
public class IndexMapping implements Serializable {
    //匹配方式 - 小于（数字类型、字符串类型）
    public static final int MATCH_TYPE_SMALLER=-1;

    //匹配方式 - 等于
    public static final int MATCH_TYPE_EQUALS=0;

    //匹配方式 - 大于（数字类型、字符串类型）
    public static final int MATCH_TYPE_BIGGER=1;

    //包含（字符串类型，indexOf()>-1)
    public static final int MATCH_TYPE_CONTAINS=10;

    //模式匹配（字符串类型，*号为通配符)
    public static final int MATCH_TYPE_PATTERN=20;

    //索引分类（由业务定义）
    private String indexType;

    //匹配类型
    private int matchType;

    //索引值
    private Object indexValue;

    //匹配该索引的对象的键值列表
    private ConcurrentList keys=new ConcurrentList();

    /**
     *
     * @param indexType
     * @param indexValue
     * @param matchType
     */
    public IndexMapping(String indexType, Object indexValue, int matchType){
        this.indexType=indexType;
        this.indexValue=indexValue;
        this.matchType=matchType;
    }

    /**
     * 将符合索引条件的对象的键值添加进来
     * @param key
     */
    public void appendKey(Object key){
        if(!keys.contains(key)) keys.add(key);
    }

    /**
     * 移除键值
     * @param key
     */
    public void removeKey(Object key){
        keys.remove(key);
    }

    /**
     *
     * @param indexStore
     * @param indexType
     * @param matchType
     * @param indexValue
     * @return
     */
    public static IndexMapping find(Map<String, IndexMapping> indexStore, String indexType, int matchType, Object indexValue){
        String indexStoredKey=indexType+"->"+matchType+"->"+indexValue;
        return indexStore.get(indexStoredKey);
    }
}
