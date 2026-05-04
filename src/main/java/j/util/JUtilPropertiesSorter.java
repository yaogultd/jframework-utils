package j.util;

public class JUtilPropertiesSorter extends JUtilSorter {

    @Override
    public String compare(Object pre, Object after) {
        JUtilKeyValue beanPre = (JUtilKeyValue) pre;
        JUtilKeyValue beanAfter = (JUtilKeyValue) after;
        String beanPreId = (String) beanPre.getKey();
        String beanAfterId = (String) beanAfter.getKey();

        if (beanPre.getNo() < beanAfter.getNo()) {
            return JUtilSorter.SMALLER;
        } else if (beanPre.getNo() > beanAfter.getNo()) {
            return JUtilSorter.BIGGER;
        } else {
            if (beanPreId.compareTo(beanAfterId) > 0) {
                return JUtilSorter.BIGGER;
            } else {
                return JUtilSorter.SMALLER;
            }
        }
    }
}