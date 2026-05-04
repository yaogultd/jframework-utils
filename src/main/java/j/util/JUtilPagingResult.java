package j.util;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class JUtilPagingResult<T> implements Serializable {
    private List<T> list;
    private int rpp;
    private int pn;
    private int total;

    public JUtilPagingResult(List<T> list, int total, int rpp, int pn){
        this.list = list;
        this.total = total;
        this.rpp = rpp;
        this.pn = pn;
    }
}