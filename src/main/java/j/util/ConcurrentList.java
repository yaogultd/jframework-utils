package j.util;

import java.io.Serializable;
import java.util.*;

/**
 *
 * @author 肖炯
 * @date 2023-06-07
 * 线程安全（按需）List，除提供java.util.List的功能外，还可设置某集合的元素总数、业务自定义数据等
 */
public class ConcurrentList<E> implements List<E>, Serializable {
	private static final long serialVersionUID = 1L;
	private String lock = null;

	private List container = null;// 实际数据存储对象
	private volatile int total=-1;//当container为某个子集时，total表示父集元素总数
	private boolean _synchronized=true;
	private Map<Object, Object> extras=new HashMap<>();//自定义数据
	
	/**
	 * 
	 * 
	 */
	public ConcurrentList() {
		this.lock = JUtilUUID.genUUID();
		this.container = new ArrayList<E>();
	}

	/**
	 *
	 * @param _synchronized
	 * @param container
	 */
	public ConcurrentList(boolean _synchronized, List<E> container) {
		if(container==null) container= new ArrayList<E>();

		this.lock = JUtilUUID.genUUID();
		this.container = container;
		this._synchronized=(container instanceof Vector)?false:_synchronized;
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
	 * @return
	 */
	public Object getLock() {
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

	/**
	 * 
	 * @param e
	 * @return
	 */
	public boolean add(E e) {
		if(e==null) return false;

		if(!_synchronized){
			return container.add(e);
		}else{
			synchronized(lock){
				return container.add(e);
			}
		}
	}

	/**
	 * 
	 * @param index
	 * @param e
	 */
	public void add(int index, Object e) {
		if(e==null) return;

		if(!_synchronized){
			container.add(index,e);
		}else{
			synchronized(lock){
				container.add(index,e);
			}
		}
	}

	/**
	 * 
	 * @param c
	 * @return
	 */
	public boolean addAll(Collection c) {
		if(c==null) return false;

		if(!_synchronized){
			return container.addAll(c);
		}else{
			synchronized(lock){
				return container.addAll(c);
			}
		}
	}

	/**
	 * 
	 * @param index
	 * @param c
	 * @return
	 */
	public boolean addAll(int index, Collection c) {
		if(c==null) return false;

		if(!_synchronized){
			return container.addAll(index,c);
		}else{
			synchronized(lock){
				return container.addAll(index,c);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Collection#clear()
	 */
	public void clear() {
		if(!_synchronized){
			container.clear();
		}else{
			synchronized (lock) {
				container.clear();
			}
		}
	}

	/*
	 *  (non-Javadoc)
	 * @see java.util.Collection#contains(java.lang.Object)
	 */
	public boolean contains(Object o) {
		if(o==null) return false;

		if(!_synchronized){
			return container.contains(o);
		}else{
			synchronized (lock) {
				return container.contains(o);
			}
		}
	}

	/**
	 * 
	 * @param c
	 * @return
	 */
	public boolean containsAll(Collection c) {
		if(c==null) return false;

		if(!_synchronized){
			return container.containsAll(c);
		}else{
			synchronized(lock){
				return container.containsAll(c);
			}
		}
	}

	/*
	 *  (non-Javadoc)
	 * @see java.util.List#get(int)
	 */
	public E get(int index) {
		try{
			if(!_synchronized){
				return (E)container.get(index);
			}else{
				synchronized(lock){
					return (E)container.get(index);
				}
			}
		}catch(Exception e){
			return null;
		}
	}

	/**
	 *
	 * @param filter
	 * @return
	 */
	public E get(JObjectFilter filter){
		List<E> matched = this.filter(filter);
		return matched == null || matched.isEmpty() ? null : matched.get(0);
	}

	/*
	 *  (non-Javadoc)
	 * @see java.util.List#indexOf(java.lang.Object)
	 */
	public int indexOf(Object o) {
		if(o==null) return -1;
		if(!_synchronized){
			return container.indexOf(o);
		}else{
			synchronized(lock){
				return container.indexOf(o);
			}
		}
	}

	/*
	 *  (non-Javadoc)
	 * @see java.util.Collection#isEmpty()
	 */
	public boolean isEmpty() {
		if(!_synchronized){
			return container.isEmpty();
		}else{
			synchronized(lock){
				return container.isEmpty();
			}
		}
	}

	/*
	 *  (non-Javadoc)
	 * @see java.lang.Iterable#iterator()
	 */
	public Iterator iterator() {
		if(!_synchronized){
			return container.iterator();
		}else{
			synchronized(lock){
				return container.iterator();
			}
		}
	}

	/*
	 *  (non-Javadoc)
	 * @see java.util.List#lastIndexOf(java.lang.Object)
	 */
	public int lastIndexOf(Object o) {
		if(o==null) return -1;
		if(!_synchronized){
			return container.lastIndexOf(o);
		}else{
			synchronized(lock){
				return container.lastIndexOf(o);
			}
		}
	}

	/*
	 *  (non-Javadoc)
	 * @see java.util.List#listIterator()
	 */
	public ListIterator listIterator() {
		if(!_synchronized){
			return container.listIterator();
		}else{
			synchronized(lock){
				return container.listIterator();
			}
		}
	}

	/*
	 *  (non-Javadoc)
	 * @see java.util.List#listIterator(int)
	 */
	public ListIterator listIterator(int index) {
		if(!_synchronized){
			return container.listIterator(index);
		}else{
			synchronized(lock){
				return container.listIterator(index);
			}
		}
	}

	/*
	 *  (non-Javadoc)
	 * @see java.util.List#remove(int)
	 */
	public E remove(int index) {
		try{
			if(!_synchronized){
				return (E)container.remove(index);
			}else{
				synchronized(lock){
					return (E)container.remove(index);
				}
			}
		}catch(Exception e){
			return null;
		}
	}

	/*
	 *  (non-Javadoc)
	 * @see java.util.Collection#remove(java.lang.Object)
	 */
	public boolean remove(Object o) {
		if(o==null) return false;
		if(!_synchronized){
			return container.remove(o);
		}else{
			synchronized(lock){
				return container.remove(o);
			}
		}
	}

	/**
	 * 
	 * @param c
	 * @return
	 */
	public boolean removeAll(Collection c) {
		if(c==null) return false;
		if(!_synchronized){
			return container.removeAll(c);
		}else{
			synchronized(lock){
				return container.removeAll(c);
			}
		}
	}

	/**
	 * 
	 * @param c
	 * @return
	 */
	public boolean retainAll(Collection c) {
		if(c==null) return false;
		if(!_synchronized){
			return container.retainAll(c);
		}else{
			synchronized(lock){
				return container.retainAll(c);
			}
		}
	}

	/**
	 * 
	 * @param index
	 * @param e
	 * @return
	 */
	public E set(int index, Object e) {
		if(e==null) return null;
		if(!_synchronized){
			return (E)container.set(index,e);
		}else{
			synchronized(lock){
				return (E)container.set(index,e);
			}
		}
	}

	/*
	 *  (non-Javadoc)
	 * @see java.util.Collection#size()
	 */
	public int size() {
		if(!_synchronized){
			return container.size();
		}else{
			synchronized(lock){
				return container.size();
			}
		}
	}

	/*
	 *  (non-Javadoc)
	 * @see java.util.List#subList(int, int)
	 */
	public List<E> subList(int fromIndex, int toIndex) {
		if(!_synchronized){
			return container.subList(fromIndex,toIndex);
		}else{
			synchronized(lock){
				return container.subList(fromIndex,toIndex);
			}
		}
	}

	/*
	 *  (non-Javadoc)
	 * @see java.util.Collection#toArray()
	 */
	public E[] toArray() {
		if(!_synchronized){
			return (E[])container.toArray();
		}else{
			synchronized(lock){
				return (E[])container.toArray();
			}
		}
	}

	/**
	 * 
	 * @param a
	 * @return
	 */
	public E[] toArray(Object[] a) {
		if(!_synchronized){
			return (E[])container.toArray(a);
		}else{
			synchronized(lock){
				return (E[])container.toArray(a);
			}
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public ConcurrentList<E> snapshot() {
		return this;
	}


	/**
	 *
	 * @param filter
	 * @return
	 */
	public ConcurrentList<E> filter(JObjectFilter filter){
		try{
			if(filter==null) return null;
			ConcurrentList<E> sub=new ConcurrentList<>();

			for(int i=0; i<this.size(); i++){
				E o=this.get(i);
				if(filter.matches(o)) sub.add(o);
			}
			if(filter.getSorter()!=null) sub = (ConcurrentList<E>)filter.getSorter().mergeSort(sub, filter.getSortType());

			return sub;
		}catch(Exception e){
			return null;
		}
	}

	/**
	 *
	 * @param filter
	 * @param recordsPerPage
	 * @param pageNum
	 * @return
	 */
	public ConcurrentList<E> filter(JObjectFilter filter, int recordsPerPage, int pageNum){
		try{
			if(filter==null) return null;
			ConcurrentList<E> sub=new ConcurrentList<>();

			for(int i=0; i<this.size(); i++){
				E o=this.get(i);
				if(filter.matches(o)) sub.add(o);
			}
			if(filter.getSorter()!=null) sub = (ConcurrentList<E>)filter.getSorter().mergeSort(sub, filter.getSortType());

			if(recordsPerPage>0 && pageNum>0){//分页
				int start=recordsPerPage*(pageNum-1);
				int to=recordsPerPage*pageNum;

				if(start>=0){
					if(sub.size()>start){
						sub=ConcurrentList.subConcurrentList(sub,start,to>sub.size()?sub.size():to);
					}else{
						sub.clear();
					}
				}
			}
			sub.setTotal(this.getTotal());

			return sub;
		}catch(Exception e){
			return null;
		}
	}

	/**
	 *
	 * @param updater
	 */
	public void update(JObjectUpdater updater) {
		if(updater!=null) updater.update(this);
	}

	/**
	 *
	 * @param parent
	 * @param from include
	 * @param to exclude
	 * @return
	 */
	public static ConcurrentList subConcurrentList(ConcurrentList parent,int from,int to){
		try{
			ConcurrentList sub=new ConcurrentList(true, (List)parent.getContainerType().newInstance());
			for(int i=from;i<parent.size()&&i<to;i++){
				sub.add(parent.get(i));
			}
			return sub;
		}catch(Exception e){
			return null;
		}
	}
}
