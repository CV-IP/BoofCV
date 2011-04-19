package gecv.alg.detect.corner.impl;

import gecv.alg.detect.corner.impl.KltCorner_F32;
import gecv.alg.detect.corner.impl.SsdCornerNaive_I16;
import gecv.alg.filter.derivative.GradientSobel;
import gecv.core.image.ConvertImage;
import gecv.core.image.UtilImageInt8;
import gecv.struct.image.ImageFloat32;
import gecv.struct.image.ImageInt16;
import gecv.struct.image.ImageInt8;
import gecv.testing.GecvTesting;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.fail;


/**
 * @author Peter Abeles
 */
public class TestKltCorner_F32 {

	int width = 15;
	int height = 15;

	/**
	 * Sees if the integer version and this version produce the same results.
	 * <p/>
	 * Creates a random image and looks for corners in it.  Sees if the naive
	 * and fast algorithm produce exactly the same results.
	 */
	@Test
	public void compareToNaive() {
		ImageInt8 img = new ImageInt8(width, height);
		UtilImageInt8.randomize(img, new Random(0xfeed));

		ImageInt16 derivX_I = new ImageInt16(img.getWidth(), img.getHeight());
		ImageInt16 derivY_I = new ImageInt16(img.getWidth(), img.getHeight());

		GradientSobel.process_I8(img, derivX_I, derivY_I);

		ImageFloat32 derivX_F = ConvertImage.convert(derivX_I, null, true);
		ImageFloat32 derivY_F = ConvertImage.convert(derivY_I, null, true);

		GecvTesting.checkSubImage(this, "compareToNaive", true, derivX_I, derivY_I, derivX_F, derivY_F);
	}

	public void compareToNaive(ImageInt16 derivX_I, ImageInt16 derivY_I, ImageFloat32 derivX_F, ImageFloat32 derivY_F) {
		SsdCornerNaive_I16 ssd_I = new SsdCornerNaive_I16(width, height, 3);
		ssd_I.process(derivX_I, derivY_I);

		KltCorner_F32 ssd_F = new KltCorner_F32(width, height, 3);
		ssd_F.process(derivX_F, derivY_F);

		GecvTesting.assertEquals(ssd_I.getIntensity(), ssd_F.getIntensity(), 0, 1f);
	}
}
