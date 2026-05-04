package j.core.type;


import org.json.JSONObject;

public interface SelfValidatedObject {
    /**
     * 判定对象自己是否有效（符合业务规则）
     * @return
     */
    public Result isValid();

    /**
     * 从json解析得到各变量的值
     * @param json
     * @throws Exception
     */
    public void fromJson(JSONObject json) throws Exception;
}