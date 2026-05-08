package j.core.type;


import j.util.JUtilBean;
import org.json.JSONObject;

public class SelfValidatedObjectImpl implements SelfValidatedObject{

    @Override
    public Result isValid(){
        return new Result(true, "1", "");
    }
}