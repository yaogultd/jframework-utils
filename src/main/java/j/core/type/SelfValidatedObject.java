package j.core.type;


public interface SelfValidatedObject {
    /**
     * 判定对象自己是否有效（符合业务规则）
     * @return
     */
    public Result isValid();
}