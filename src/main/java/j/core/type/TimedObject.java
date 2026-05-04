package j.core.type;

import j.util.JUtilBean;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class TimedObject implements Serializable {
    //对象
    private Object object;

    //创建时间
    private long createdAt;

    //更新时间
    private long updatedAt;

    //生存时间（从创建时间开始计算）
    private long survival;

    //超时时间（从更新时间开始计算）
    private long timeout;

    /**
     *
     * @param object
     */
    public TimedObject(Object object){
        this.object=object;
        this.createdAt=System.currentTimeMillis();
        this.updatedAt=this.createdAt;
    }

    /**
     *
     * @param object
     * @param createdAt
     * @param updatedAt
     */
    public TimedObject(Object object, long createdAt, long updatedAt){
        this.object=object;
        this.createdAt=createdAt;
        this.updatedAt=updatedAt;
    }

    /**
     *
     * @param object
     * @param createdAt
     * @param updatedAt
     * @param survival
     * @param timeout
     */
    public TimedObject(Object object, long createdAt, long updatedAt, long survival, long timeout){
        this.object=object;
        this.createdAt=createdAt;
        this.updatedAt=updatedAt;
        this.survival=survival;
        this.timeout=timeout;
    }

    /**
     *
     * @return
     */
    public boolean isDead(){
        return this.survival > 0 && System.currentTimeMillis() - this.createdAt > this.survival;
    }

    /**
     *
     * @return
     */
    public boolean isTimeout(){
        return this.timeout > 0 && System.currentTimeMillis() - this.updatedAt > this.timeout;
    }

    @Override
    public String toString(){
        return JUtilBean.bean2Json(this);
    }
}