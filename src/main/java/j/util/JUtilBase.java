package j.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

public class JUtilBase {
    /**
     * @param request
     * @param name
     * @return
     */
    public static String getCookie(HttpServletRequest request, String name) {
        Cookie[] cs = request == null ? null : request.getCookies();
        if (cs == null) return null;

        String value = null;
        for (int i = 0; i < cs.length; i++) {
            if (cs[i].getName().equalsIgnoreCase(name)) value = JUtilString.decodeURI(cs[i].getValue(), "UTF-8");
        }
        return value;
    }
}
