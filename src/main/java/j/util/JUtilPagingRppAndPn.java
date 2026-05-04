package j.util;

import lombok.Getter;

@Getter
public class JUtilPagingRppAndPn {
    private int rpp=10;
    private int pn=1;

    public JUtilPagingRppAndPn(int rpp, int pn){
        this.rpp = rpp;
        this.pn = pn;
    }
}
