package j.util;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public abstract class JObjectFilter implements Serializable{
	protected JUtilSorter sorter;
	protected String sortType = JUtilSorter.ASC;
	
	
	/**
	 * 判断某对象是否符合给定的条件
	 * @param object
	 * @return
	 */
	public abstract boolean matches(Object object);
}