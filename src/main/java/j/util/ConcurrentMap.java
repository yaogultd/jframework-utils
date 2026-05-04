package j.util;

import j.core.type.index.IndexCreator;
import j.core.type.index.IndexMapping;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author 肖炯
 * @date 2023-06-07
 * 线程安全（按需）Map，除提供java.util.Map的功能外，还可设置某集合的元素总数、业务自定义数据等，并支持倒排索引
 */
public class ConcurrentMap<K,V> implements Map<K,V>,Serializable{
	private static final long serialVersionUID = 1L;

	//同步锁
	private String lock=null;

	//实际数据存储对象
	private Map container=null;

	//倒排索引
	private ConcurrentHashMap<String, IndexMapping> indexStore=new ConcurrentHashMap<>();

	//倒排索引生成器
	private IndexCreator indexCreator;

	//元素总数
	private volatile int total=-1;//当container为某个子集时，total表示父集元素总数

	//是否需要同步锁
	private boolean _synchronized=true;

	//自定义数据
	private Map<Object, Object> extras=new HashMap<>();

	/**
	 *
	 */
	public ConcurrentMap(){
		this.lock = JUtilUUID.genUUID();
		this.container=new ConcurrentHashMap<K,V>();
		this._synchronized=false;
	}

	/**
	 *
	 * @param _synchronized
	 * @param container
	 */
	public ConcurrentMap(boolean _synchronized, Map<K,V> container){
		if(container==null){
			container=new java.util.concurrent.ConcurrentHashMap<K,V>();
		}

		this.lock = JUtilUUID.genUUID();
		this.container=container;
		this._synchronized=(container instanceof java.util.concurrent.ConcurrentMap)?false:_synchronized;
	}

	/**
	 *
	 * @param key
	 * @param value
	 */
	public void setExtra(Object key, Object value) {
		this.extras.put(key, value);
	}

	/**
	 *
	 * @param key
	 * @return
	 */
	public Object getExtra(Object key) {
		return this.extras.get(key);
	}

	/**
	 *
	 * @return
	 */
	public Class getContainerType(){
		return this.container.getClass();
	}

	/**
	 *
	 * @param indexCreator
	 */
	public void setIndexCreator(IndexCreator indexCreator){
		this.indexCreator=indexCreator;
	}

	/**
	 *
	 * @param value
	 */
	private void appendIndex(Object value){
		if(this.indexCreator!=null){
			this.indexCreator.append(this.indexStore, value);
		}
	}

	/**
	 *
	 * @param value
	 */
	private void removeIndex(Object value){
		if(this.indexCreator!=null){
			this.indexCreator.remove(this.indexStore, value);
		}
	}

	/**
	 * 同步锁，即实际存储数据的LinkedList对象，当外部程序需要与该LinkedList对象的操作同步的话，需通过getLock()得到锁
	 * @return
	 */
	public Object getLock(){
		return lock;
	}

	/**
	 *
	 * @param total
	 */
	public void setTotal(int total){
		this.total=total;
	}

	/**
	 *
	 * @return
	 */
	public int getTotal(){
		return this.total==-1?this.size():this.total;
	}


	@Override
	public void clear(){
		if(!_synchronized){
			indexStore.clear();
			container.clear();
		}else{
			synchronized(lock){
				indexStore.clear();
				container.clear();
			}
		}
	}

	@Override
	public boolean containsKey(Object key){
		if(!_synchronized){
			return container.containsKey(key);
		}else{
			synchronized(lock){
				return container.containsKey(key);
			}
		}
	}

	@Override
	public boolean containsValue(Object value){
		if(!_synchronized){
			return container.containsValue(value);
		}else{
			synchronized(lock){
				return container.containsValue(value);
			}
		}
	}

	@Override
	public Set entrySet(){
		if(!_synchronized){
			return container.entrySet();
		}else{
			synchronized(lock){
				return container.entrySet();
			}
		}
	}

	@Override
	public V get(Object key){
		if(key==null) return null;
		if(!_synchronized){
			return (V)container.get(key);
		}else{
			synchronized(lock){
				return (V)container.get(key);
			}
		}
	}

	/**
	 *
	 * @param filter
	 * @return
	 */
	public V get(JObjectFilter filter){
		ConcurrentList<V> values=this.listValues(filter);
		return values == null || values.isEmpty() ? null : values.get(0);
	}

	/**
	 *
	 * @param indexType
	 * @param matchType
	 * @param indexValue
	 * @return
	 */
	public V get(String indexType, int matchType, Object indexValue){
		IndexMapping index=IndexMapping.find(this.indexStore, indexType, matchType, indexValue);
		if(index!=null && !index.getKeys().isEmpty()){
			return this.get(index.getKeys().get(0));
		}
		return null;
	}

	@Override
	public boolean isEmpty(){
		if(!_synchronized){
			return container.isEmpty();
		}else{
			synchronized(lock){
				return container.isEmpty();
			}
		}
	}

	@Override
	public Set keySet(){
		if(!_synchronized){
			return container.keySet();
		}else{
			synchronized(lock){
				return container.keySet();
			}
		}
	}

	@Override
	public V put(Object key,Object value){
		if(key==null || value==null) return null;

		if(!_synchronized){
			this.removeIndex(this.get(key));//移除旧的倒排索引（如果存在）
			this.appendIndex(value);
			return (V)container.put(key,value);
		}else{
			synchronized(lock){
				this.removeIndex(this.get(key));//移除旧的倒排索引（如果存在）
				this.appendIndex(value);
				return (V)container.put(key,value);
			}
		}
	}

	@Override
	public void putAll(Map mappings){
		if(mappings==null) return;

		for(Iterator i = mappings.keySet().iterator(); i.hasNext();){
			Object k=i.next();
			if(k==null) continue;

			this.put(k, mappings.get(k));
		}
	}


	@Override
	public V remove(Object key){
		if(key==null) return null;

		Object value=this.get(key);

		if(!_synchronized){
			this.removeIndex(value);
			return (V)container.remove(key);
		}else{
			synchronized(lock){
				this.removeIndex(value);
				return (V)container.remove(key);
			}
		}
	}


	@Override
	public int size(){
		if(!_synchronized){
			return container.size();
		}else{
			synchronized(lock){
				return container.size();
			}
		}
	}


	@Override
	public Collection values(){
		if(!_synchronized){
			return container.values();
		}else{
			synchronized(lock){
				return container.values();
			}
		}
	}

	/**
	 *
	 * @param indexType
	 * @param matchType
	 * @param indexValue
	 * @return
	 */
	public ConcurrentList<V> values(String indexType, int matchType, Object indexValue){
		ConcurrentList<V> values=new ConcurrentList();

		IndexMapping index=IndexMapping.find(this.indexStore, indexType, matchType, indexValue);
		if(index!=null && !index.getKeys().isEmpty()){
			for(int i=0; i<index.getKeys().size(); i++){
				values.add(this.get(index.getKeys().get(i)));
			}
		}
		return values;
	}

	/**
	 *
	 * @return
	 */
	public ConcurrentList listKeys(){
		ConcurrentList keys=new ConcurrentList();
		keys.addAll(container.keySet());
		return keys;
	}

	/**
	 *
	 * @return
	 */
	public ConcurrentList listValues(){
		ConcurrentList values=new ConcurrentList();
		values.addAll(container.values());
		return values;
	}

	/**
	 *
	 * @return
	 */
	public ConcurrentList listValues(JObjectFilter filter){
		if(filter==null) return listValues();

		ConcurrentList values=new ConcurrentList();
		values.addAll(this.listValues());
		for(int i=0; i<values.size(); i++){
			if(!filter.matches(values.get(i))){
				values.remove(i);
				i--;
			}
		}
		if(filter.getSorter()!=null) values = (ConcurrentList)filter.getSorter().mergeSort(values, filter.getSortType());

		return values;
	}

	/**
	 */
	public ConcurrentList listValues(JObjectFilter filter, int recordsPerPage, int pageNum){
		if(filter==null) return listValues();

		ConcurrentList values=new ConcurrentList();
		values.addAll(this.listValues());
		for(int i=0; i<values.size(); i++){
			if(!filter.matches(values.get(i))){
				values.remove(i);
				i--;
			}
		}
		int total=values.size();

		if(filter.getSorter()!=null) values = (ConcurrentList)filter.getSorter().mergeSort(values, filter.getSortType());

		if(recordsPerPage>0 && pageNum>0){//分页
			int start=recordsPerPage*(pageNum-1);
			int to=recordsPerPage*pageNum;

			if(start>=0){
				if(values.size()>start){
					values=ConcurrentList.subConcurrentList(values,start,to>values.size()?values.size():to);
				}else{
					values.clear();
				}
			}
		}
		values.setTotal(total);
		return values;
	}

	/**
	 *
	 * @return
	 */
	public ConcurrentMap sub(JObjectFilter filter){
		if(filter==null) return this.snapshot();

		ConcurrentMap values=new ConcurrentMap();
		values.putAll(this.container);

		List keys = this.listKeys();
		for(int i=0; i<keys.size(); i++){
			Object key = keys.get(i);
			Object value = this.get(key);
			if(!filter.matches(value)) values.remove(key);
		}

		return values;
	}

	/**
	 *
	 * @param updater
	 */
	public void update(JObjectUpdater updater) {
		if(updater!=null) updater.update(this);
	}


	/**
	 * 移除cache中的全部元素但保留leftKeys所包含的key
	 * @param leftKeys
	 */
	public void removeExcept(List leftKeys){
		List keys=listKeys();
		for(int i=0;i<keys.size();i++){
			Object key=keys.get(i);
			if(!leftKeys.contains(key)){
				remove(key);
			}
		}
	}

	/**
	 *
	 * @return
	 */
	public ConcurrentMap snapshot() {
		return this;
	}

	/**
	 *
	 * @return
	 */
	public String toXml(){
		StringBuffer sb=new StringBuffer();
		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n");
		sb.append("<root>\r\n");
		List keys=listKeys();
		for(int i=0;i<keys.size();i++){
			Object key=keys.get(i);
			Object val=get(key);

			sb.append("<"+key+"><![CDATA["+val+"]]></"+key+">\r\n");
		}
		sb.append("</root>");
		return sb.toString();
	}

	/**
	 *
	 * @param parent
	 * @param from include
	 * @param to exclude
	 * @return
	 */
	public static ConcurrentMap subConcurrentMap(ConcurrentMap parent,int from,int to) {
		try{
			ConcurrentMap sub=new ConcurrentMap(true, (Map)parent.getContainerType().newInstance());

			List keys=parent.listKeys();
			for(int i=from;i<keys.size()&&i<to;i++){
				Object key=keys.get(i);
				sub.put(key, parent.get(key));
			}
			return sub;
		}catch(Exception e){
			return null;
		}
	}

	/**
	 *
	 * @param map
	 * @return
	 */
	public static List keys(Map map){
		List list=new LinkedList();
		if(map!=null) list.addAll(map.keySet());
		return list;
	}

	/**
	 *
	 * @param map
	 * @return
	 */
	public static List values(Map map){
		List list=new LinkedList();
		if(map!=null) list.addAll(map.values());
		return list;
	}
}