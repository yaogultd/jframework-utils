package j.util;

import j.util.ConcurrentList;
import j.util.ConcurrentMap;

import java.io.Serializable;

/**
 * 
 * @author 肖炯
 *
 */
public interface JObjectUpdater extends Serializable{
	/**
	 * 根据实现类自定义的规则对缓存单元中的元素进行更新
	 * @param map
	 */
	public void update(ConcurrentMap map);
	
	/**
	 * 根据实现类自定义的规则对缓存单元中的一组元素进行更新
	 * @param collection
	 */
	public void updateCollection(ConcurrentMap collection);
	
	/**
	 * 根据实现类自定义的规则对缓存单元中的元素进行更新
	 * @param list
	 */
	public void update(ConcurrentList list);
	
	/**
	 * 根据实现类自定义的规则对缓存单元中的一组元素进行更新
	 * @param list
	 */
	public void updateCollection(ConcurrentList list);
}
