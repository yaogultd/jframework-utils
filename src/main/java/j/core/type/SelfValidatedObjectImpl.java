package j.core.type;


import j.util.JUtilBean;
import org.json.JSONObject;

public class SelfValidatedObjectImpl implements SelfValidatedObject{

    @Override
    public Result isValid(){
        return new Result(true, "1", "");
    }

    @Override
    public void fromJson(JSONObject json) throws Exception{
        JUtilBean.json2Bean(this, json);
    }
}