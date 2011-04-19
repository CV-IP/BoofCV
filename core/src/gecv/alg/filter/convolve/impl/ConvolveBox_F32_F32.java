package gecv.alg.filter.convolve.impl;

import gecv.struct.image.ImageFloat32;
import gecv.struct.image.ImageFloat32;

/**
 * <p>
 * Convolves a box filter across an image.  A box filter is equivalent to convolving a kernel with all 1's.
 * <p/>
 * <p>
 * Do not modify.  Auto generated by GenerateConvolveBox.
 * </p>
 *
 * @author Peter Abeles
 */
public class ConvolveBox_F32_F32 {
	public static void horizontal(ImageFloat32 input, ImageFloat32 output, int radius, boolean includeBorder) {
		final int kernelWidth = radius * 2 + 1;

		final int startY = includeBorder ? 0 : radius;
		final int endY = includeBorder ? input.height : input.height - radius;

		for (int y = startY; y < endY; y++) {
			int indexIn = input.startIndex + input.stride * y;
			int indexOut = output.startIndex + output.stride * y + radius;

			float total = 0;

			int indexEnd = indexIn + kernelWidth;

			for (; indexIn < indexEnd; indexIn++) {
				total += input.data[indexIn];
			}
			output.data[indexOut++] = total;

			indexEnd = indexIn + input.width - kernelWidth;
			for (; indexIn < indexEnd; indexIn++) {
				total -= input.data[indexIn - kernelWidth];
				total += input.data[indexIn];

				output.data[indexOut++] = total;
			}
		}
	}

	public static void vertical(ImageFloat32 input, ImageFloat32 output, int radius, boolean includeBorder) {
		final int kernelWidth = radius * 2 + 1;

		final int startX = includeBorder ? 0 : radius;
		final int endX = includeBorder ? input.width : input.width - radius;

		final int backStep = kernelWidth * input.stride;

		for (int x = startX; x < endX; x++) {
			int indexIn = input.startIndex + x;
			int indexOut = output.startIndex + output.stride * radius + x;

			float total = 0;
			int indexEnd = indexIn + input.stride * kernelWidth;
			for (; indexIn < indexEnd; indexIn += input.stride) {
				total += input.data[indexIn];
			}

			output.data[indexOut] = total;
		}

		// change the order it is processed in to reduce cache misses
		for (int y = radius + 1; y < output.height - radius; y++) {
			int indexIn = input.startIndex + (y + radius) * input.stride + startX;
			int indexOut = output.startIndex + y * output.stride + startX;

			for (int x = startX; x < endX; x++, indexIn++, indexOut++) {
				float total = output.data[indexOut - output.stride] - (input.data[indexIn - backStep]);
				total += input.data[indexIn];

				output.data[indexOut] = total;
			}
		}
	}
}
