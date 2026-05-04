package j.util.media;

import java.awt.Color;
import java.awt.Font;
import java.io.Serializable;

/**
 *
 * @author 肖炯
 * @date 2023-06-07
 * 绘制内容
 */
public class PaintItem implements Serializable{
	private static final long serialVersionUID = 1L;

	//绘制内容，支持以下类型的对象：java.io.File/java.awt.Image/java.lang.String
	public Object object;

	//java.awt.Graphics类drawImage、drawString方法的绘制坐标(x)
	public int x;

	//java.awt.Graphics类drawImage、drawString方法的绘制坐标(y)
	public int y;

	public int width;
	public int height;
	public Color color;
	public Font font;
	
	/**
	 * 
	 * @param object
	 * @param color
	 * @param font
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 */
	public PaintItem(Object object, Color color, Font font, int x, int y, int width, int height) {
		this.object=object;
		this.color=color;
		this.font=font;
		this.x=x;
		this.y=y;
		this.width=width;
		this.height=height;
	}
}