package j.core.type.index;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 *
 * @author 肖炯
 * @date 2023-06-07
 * 索引创建
 */
public abstract class IndexCreator implements Serializable {
    /**
     * 具体业务实现该方法生成倒排索引
     * @param object
     * @return
     */
    public abstract List<IndexMapping> createIndexes(Object object);

    /**
     * 具体业务实现该方法，返回某个对象的键值
     * @param object
     * @return
     */
    public abstract Object getKey(Object object);

    /**
     * 添加索引
     * @param indexStore
     * @param object
     */
    public void append(Map<String, IndexMapping> indexStore, Object object){
        if(object==null) return;

        List<IndexMapping> indexes=this.createIndexes(object);
        if(indexes==null || indexes.isEmpty()) return;

        for(int i=0; i<indexes.size(); i++){
            IndexMapping index=indexes.get(i);

            String indexStoredKey=index.getIndexType()+"->"+index.getMatchType()+"->"+index.getIndexValue();

            IndexMapping exists=indexStore.get(indexStoredKey);
            if(exists==null){
                index.appendKey(this.getKey(object));
                indexStore.put(indexStoredKey, index);
            }else{
                exists.appendKey(this.getKey(object));
            }
        }
    }

    /**
     * 移除索引
     * @param indexStore
     * @param object
     */
    public void remove(Map<String, IndexMapping> indexStore, Object object){
        if(object==null) return;

        List<IndexMapping> indexes=this.createIndexes(object);
        if(indexes==null || indexes.isEmpty()) return;

        for(int i=0; i<indexes.size(); i++){
            IndexMapping index=indexes.get(i);

            String indexStoredKey=index.getIndexType()+"->"+index.getMatchType()+"->"+index.getIndexValue();

            IndexMapping exists=indexStore.get(indexStoredKey);
            if(exists==null){
                continue;
            }

            exists.removeKey(this.getKey(object));
        }
    }
}
