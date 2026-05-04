package j.util;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author 肖炯
 * @date 2023-06-07
 */
@Setter
@Getter
public class JUtilKeyValue implements Serializable {
	private Object key;
	private Object value;
	private Object desc;
	private Map<Object, Object> params = new HashMap<>();
	private int no=0;
	
	public JUtilKeyValue(Object key,Object value){
		this.setKey(key);
		this.setValue(value);
	}
	
	public JUtilKeyValue(Object key,Object value,int no){
		this.key=key;
		this.value=value;
		this.no=no;
	}
	
	public JUtilKeyValue(Object key,Object value,Object desc){
		this.key=key;
		this.value=value;
		this.desc=desc;
	}

	public void setParam(Object key, Object value){
		this.params.put(key, value);
	}

	public Object getParam(Object key){
		return key==null ? null : this.params.get(key);
	}

	@Deprecated
	public void setExtra(Object desc){
		this.desc=desc;
	}
}
