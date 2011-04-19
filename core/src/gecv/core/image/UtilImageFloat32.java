package gecv.core.image;

import gecv.struct.image.ImageFloat32;
import gecv.struct.image.ImageInt8;

import java.util.Random;

/**
 * @author Peter Abeles
 */
public class UtilImageFloat32 {

	/**
	 * Fills the whole image with the specified pixel value
	 *
	 * @param img   An image.
	 * @param value The value that the image is being filled with.
	 */
	public static void fill(ImageFloat32 img, float value) {
		final int h = img.getHeight();
		final int w = img.getWidth();

		float[] data = img.data;

		for (int y = 0; y < h; y++) {
			int index = img.getStartIndex() + y * img.getStride();
			for (int x = 0; x < w; x++) {
				data[index++] = value;
			}
		}
	}

	/**
	 * Fills the whole image with random values
	 *
	 * @param img  An image.
	 * @param rand The value that the image is being filled with.
	 */
	public static void randomize(ImageFloat32 img, Random rand,
								 float min, float max) {
		final int h = img.getHeight();
		final int w = img.getWidth();

		float[] data = img.data;
		float range = max - min;

		for (int y = 0; y < h; y++) {
			int index = img.getStartIndex() + y * img.getStride();
			for (int x = 0; x < w; x++) {
				data[index++] = rand.nextFloat() * range + min;
			}
		}
	}
}
