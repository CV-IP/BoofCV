package gecv.alg.detect.corner;

import gecv.core.image.UtilImageInt8;
import gecv.struct.image.ImageFloat32;
import gecv.struct.image.ImageInt8;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class TestFastCorner12NoThresh_B {

	ImageInt8 img;

	public TestFastCorner12NoThresh_B() {
		img = new ImageInt8(50, 50);
	}

	@Before
	public void zapImage() {
		UtilImageInt8.fill(img, (byte) 0);
	}

	/**
	 * See if it computes an intensity for the one valid corner in the image.  Multiple
	 * pixels would have an intensity if it wasn't for the min pixel threshold.
	 */
	@Test
	public void testDetection() {
		addFeature(10, 12, 50);

		FastCorner12NoThresh_B fast = new FastCorner12NoThresh_B(img, 11);
		fast.process();

		ImageFloat32 intensity = fast.getIntensity();

		int numNotZero = 0;
		for (int i = 0; i < intensity.data.length; i++) {
			if (intensity.data[i] > 0) {
				numNotZero++;
			}
		}
		assertEquals(1, numNotZero);

		assertTrue(intensity.data[12 * intensity.getWidth() + 10] != 0);
	}

	private void addFeature(int x, int y, int magnitutde) {
		img.set(x, y, magnitutde);
	}

	@Test
	public void testSubImage() {
		fail("implement");
	}
}
