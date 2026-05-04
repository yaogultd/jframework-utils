package j.util;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;

public class JUtilPaging {
    public final static int RPP_DEFAULT = 10;
    public final static int PN_DEFAULT = 1;

    /**
     *
     * @param request
     * @param maxRpp
     * @return
     */
    public static JUtilPagingRppAndPn getRppAndPn(HttpServletRequest request, int maxRpp){
        int rpp=RPP_DEFAULT;
        int pn=PN_DEFAULT;

        String RPP = request.getParameter("rpp");
        if(JUtilMath.isInt(RPP)&&Integer.parseInt(RPP)>0 && Integer.parseInt(RPP)<=maxRpp){
            rpp=Integer.parseInt(RPP);
        }

        String PN = request.getParameter("pn");
        if(JUtilMath.isInt(PN)&&Integer.parseInt(PN)>0){
            pn=Integer.parseInt(PN);
        }

        return new JUtilPagingRppAndPn(rpp, pn);
    }

    /**
     *
     * @param params
     * @param maxRpp
     * @return
     */
    public static JUtilPagingRppAndPn getRppAndPn(Map<String, String> params, int maxRpp){
        int rpp=RPP_DEFAULT;
        int pn=PN_DEFAULT;

        String RPP = params.get("rpp");
        if(JUtilMath.isInt(RPP)&&Integer.parseInt(RPP)>0 && Integer.parseInt(RPP)<=maxRpp){
            rpp=Integer.parseInt(RPP);
        }

        String PN = params.get("pn");
        if(JUtilMath.isInt(PN)&&Integer.parseInt(PN)>0){
            pn=Integer.parseInt(PN);
        }

        return new JUtilPagingRppAndPn(rpp, pn);
    }

    /**
     *
     * @param RPP
     * @param PN
     * @param maxRpp
     * @return
     */
    public static JUtilPagingRppAndPn getRppAndPn(String RPP, String PN, int maxRpp){
        int rpp=RPP_DEFAULT;
        int pn=PN_DEFAULT;

        if(JUtilMath.isInt(RPP)&&Integer.parseInt(RPP)>0 && Integer.parseInt(RPP)<=maxRpp){
            rpp=Integer.parseInt(RPP);
        }

        if(JUtilMath.isInt(PN)&&Integer.parseInt(PN)>0){
            pn=Integer.parseInt(PN);
        }

        return new JUtilPagingRppAndPn(rpp, pn);
    }
}
