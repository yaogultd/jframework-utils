package j.util;

/**
 *
 * @author 肖炯
 * @date 2023-06-07
 * Bitmap的简单实现
 */
public class JUtilBitmap {
    private static final int N = 1000000;
    private int volume;
    private byte[] bytes;

    @Deprecated
    public JUtilBitmap(){
        this.volume=N;
        this.bytes=new byte[this.getIndex(this.volume) + 1];
    }

    /**
     *
     * @param volume
     */
    public JUtilBitmap(int volume){
        this.volume=volume;
        this.bytes=new byte[this.getIndex(this.volume) + 1];
    }

    /**
     *
     * @param n
     * @return
     */
    private int getIndex(int n){
        return n>>3;
    }

    /**
     *
     * @param n
     * @return
     */
    private int getPosition(int n){
        return n%8;
    }

    /**
     *
     * @param n
     */
    synchronized public void add(int n){
        this.bytes[this.getIndex(n)] |= 1<<this.getPosition(n);
    }

    /**
     *
     * @param n
     */
    synchronized public void remove(int n){
        this.bytes[this.getIndex(n)] &= ~(1<<this.getPosition(n));
    }

    /**
     *
     * @param n
     * @return
     */
    synchronized public boolean contains(int n){
        return (this.bytes[this.getIndex(n)] & 1<<this.getPosition(n)) != 0;
    }

    public static  void main(String[] args){
        System.out.println(Integer.MAX_VALUE);
        JUtilBitmap bm=new JUtilBitmap(Integer.MAX_VALUE);
        bm.add(1000);
        System.out.println(bm.contains(1000));
    }
}
