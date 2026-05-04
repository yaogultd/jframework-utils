package j.util;

import com.github.jaiimageio.stream.RawImageInputStream;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.stream.ImageInputStream;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

/**
 *
 * @author 肖炯
 * @date 2023-06-07
 * raw、jpg互转
 */
public class JUtilImageRaw extends JUtilImage {
	/**
	 *
	 * @param srcPath
	 * @param destPath
	 * @return
	 * @throws IOException
	 */
	public void jpg2Raw(String srcPath, String destPath) throws IOException {
		BufferedImage im = ImageIO.read(new File(srcPath));
		ImageIO.write(im, "raw", new File(destPath));
	}

	/**
	 *
	 * @param srcPath
	 * @param destPath
	 * @throws IOException
	 */
	public void raw2Jpg(String srcPath, String destPath) throws IOException {
		Image src = Toolkit.getDefaultToolkit().getImage(srcPath);
		src.flush();
		src = new ImageIcon(src).getImage();
		raw2Jpg(srcPath, destPath, src.getWidth(this), src.getHeight(this));
	}

	/**
	 *
	 * @param srcPath
	 * @param destPath
	 * @param width
	 * @param height
	 * @throws IOException
	 */
	public void raw2Jpg(String srcPath, String destPath, int width, int height) throws IOException {
		raw2Jpg(new File(srcPath), new File(destPath), new Dimension(width, height));
	}

	/**
	 *
	 * @param srcFile
	 * @param destFile
	 * @param dimension
	 * @throws IOException
	 */
	private static void raw2Jpg(File srcFile, File destFile, Dimension dimension) throws IOException {
		Iterator<ImageReader> readers = ImageIO.getImageReadersByFormatName("dng");
		ImageInputStream iis = null;
		RawImageInputStream ris = null;
		try {
	        ImageReader reader = readers.next();

	        iis = ImageIO.createImageInputStream(srcFile);
	        ImageTypeSpecifier type =  ImageTypeSpecifier.createFromBufferedImageType(BufferedImage.TYPE_3BYTE_BGR);
	        long[] index = {0};
	        if(dimension == null) dimension = guess(iis.length());
	        Dimension[] dimensions = {dimension};
	        ris = new RawImageInputStream(iis, type, index, dimensions);
	        reader.setInput(ris, true);
			BufferedImage image=reader.read(0);

			ImageIO.write(image, "jpg", destFile);
		} finally {
			if(ris != null) {
				try {
					ris.close();
				} catch (IOException e) {}
			}
			if(iis != null) {
				try {
					iis.close();
				} catch (IOException e) {}
			}
		}
	}

	/**
	 *
	 * @param length
	 * @return
	 */
	private static Dimension guess(long length) {
		long wh = length / 3;
		int w = (int)Math.sqrt(wh);
		int h = (int)(wh / w);
		while(w <= wh) {
			if(wh - w * h == 0) break;
			h = (int)(wh / ++ w);
		}
		return new Dimension(w, h);
	}
}