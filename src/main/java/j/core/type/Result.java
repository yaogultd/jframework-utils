package j.core.type;

import j.util.JUtilBean;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class Result implements Serializable {
    private Boolean ok;
    private String code;
    private String message;

    public Result(){
        this.ok=true;
    }

    public Result(String code, String message){
        this.ok=true;
        this.code=code;
        this.message=message;
    }

    public Result(Boolean ok, String code, String message){
        this.ok=ok;
        this.code=code;
        this.message=message;
    }

    @Override
    public String toString(){
        return JUtilBean.bean2Json(this);
    }
}