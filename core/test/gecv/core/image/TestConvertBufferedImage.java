package gecv.core.image;

import gecv.struct.image.ImageInt8;
import gecv.struct.image.ImageInterleavedInt8;
import gecv.testing.GecvTesting;
import org.junit.Test;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * @author Peter Abeles
 */
public class TestConvertBufferedImage {

	Random rand = new Random(234);

	int imgWidth = 10;
	int imgHeight = 20;

	@Test
	public void extractInterlacedInt8() {
		BufferedImage origImg = TestConvertRaster.createByteBuff(imgWidth, imgHeight, 3, rand);

		ImageInterleavedInt8 found = ConvertBufferedImage.extractInterlacedInt8(origImg);

		assertEquals(imgWidth, found.width);
		assertEquals(imgHeight, found.height);
		assertEquals(3, found.numBands);
		assertTrue(found.data != null);
		assertEquals(imgWidth * imgHeight * 3, found.data.length);
	}

	@Test
	public void extractInterlacedInt8_fail() {
		try {
			BufferedImage origImg = TestConvertRaster.createIntBuff(imgWidth, imgHeight, rand);
			ConvertBufferedImage.extractInterlacedInt8(origImg);
			fail("Should hbe the wrong type");
		} catch (IllegalArgumentException e) {
		}
	}

	@Test
	public void extractImageInt8() {
		BufferedImage origImg = TestConvertRaster.createByteBuff(imgWidth, imgHeight, 1, rand);

		ImageInt8 found = ConvertBufferedImage.extractImageInt8(origImg);

		assertEquals(imgWidth, found.width);
		assertEquals(imgHeight, found.height);
		assertTrue(found.data != null);
		assertEquals(imgWidth * imgHeight, found.data.length);
	}

	@Test
	public void extractImageInt8_fail() {
		try {
			BufferedImage origImg = TestConvertRaster.createByteBuff(imgWidth, imgHeight, 2, rand);
			ConvertBufferedImage.extractImageInt8(origImg);
			fail("Should have had an unexpected number of bands");
		} catch (IllegalArgumentException e) {
		}

		try {
			BufferedImage origImg = TestConvertRaster.createIntBuff(imgWidth, imgHeight, rand);
			ConvertBufferedImage.extractImageInt8(origImg);
			fail("Should hbe the wrong type");
		} catch (IllegalArgumentException e) {
		}
	}

	@Test
	public void extractBuffered_InterleavedInt8() {
		// test it with 3 bands
		ImageInterleavedInt8 srcImg = new ImageInterleavedInt8(imgWidth, imgHeight, 3);
		UtilImageInterleavedInt8.randomize(srcImg, rand);

		BufferedImage img = ConvertBufferedImage.extractBuffered(srcImg);

		GecvTesting.checkEquals(img, srcImg);

		// now test it with a single band
		srcImg = new ImageInterleavedInt8(imgWidth, imgHeight, 1);
		UtilImageInterleavedInt8.randomize(srcImg, rand);
		img = ConvertBufferedImage.extractBuffered(srcImg);
		GecvTesting.checkEquals(img, srcImg);

	}

	@Test
	public void extractBuffered_Int8() {
		ImageInt8 srcImg = new ImageInt8(imgWidth, imgHeight);
		UtilImageInt8.randomize(srcImg, rand);

		BufferedImage img = ConvertBufferedImage.extractBuffered(srcImg);

		GecvTesting.checkEquals(img, srcImg);
	}

	@Test
	public void convertFrom_Int8() {
		ImageInt8 dstImg = new ImageInt8(imgWidth, imgHeight);

		GecvTesting.checkSubImage(this, "convertFrom_Int8", false, dstImg);
	}

	public void convertFrom_Int8(ImageInt8 dstImg) {
		BufferedImage origImg = TestConvertRaster.createByteBuff(imgWidth, imgHeight, 1, rand);
		ConvertBufferedImage.convertFrom(origImg, dstImg);

		GecvTesting.checkEquals(origImg, dstImg);
	}

	@Test
	public void convertTo_Int8() {
		ImageInt8 srcImg = new ImageInt8(imgWidth, imgHeight);
		UtilImageInt8.randomize(srcImg, rand);

		GecvTesting.checkSubImage(this, "convertTo_Int8", true, srcImg);
	}

	public void convertTo_Int8(ImageInt8 srcImg) {
		BufferedImage dstImg = TestConvertRaster.createByteBuff(imgWidth, imgHeight, 1, rand);
		ConvertBufferedImage.convertTo(srcImg, dstImg);

		GecvTesting.checkEquals(dstImg, srcImg);
	}

	@Test
	public void convertTo_JComponent() {
		JLabel label = new JLabel("Hi");
		// need to give it a size
		label.setBounds(0, 0, imgWidth, imgHeight);
		label.setPreferredSize(new Dimension(imgWidth, imgHeight));

		BufferedImage found = ConvertBufferedImage.convertTo(label, null);

		// see if it is the expected size
		assertEquals(found.getWidth(), label.getWidth());
		assertEquals(found.getHeight(), label.getHeight());

		// see if it blows up with an input is provided
		ConvertBufferedImage.convertTo(label, found);

		// could check to see that the pixels are not all uniform....
	}
}
