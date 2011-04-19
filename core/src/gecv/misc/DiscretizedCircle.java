package gecv.misc;

/**
 * Functions related to discretized circles for image processing
 *
 * @author Peter Abeles
 */
public class DiscretizedCircle {

	/**
	 * Computes the offsets for a discretized circle of the specfied radius for an
	 * image with the specified width.
	 *
	 * @param radius   The radius of the circle in pixels.
	 * @param imgWidth The row step of the image
	 * @return A list of offsets that describe the circle
	 */
	public static int[] imageOffsets(double radius, int imgWidth) {


		double PI2 = Math.PI * 2.0;

		double circumference = PI2 * radius;
		int num = (int) Math.ceil(circumference);
		num = num - num % 4;

		double angleStep = PI2 / num;


		int temp[] = new int[(int) Math.ceil(circumference)];
		int i = 0;

		int prev = 0;

		for (double ang = 0; ang < PI2; ang += angleStep) {
			int x = (int) Math.round(Math.cos(ang) * radius);
			int y = (int) Math.round(Math.sin(ang) * radius);

			int pixel = y * imgWidth + x;

			if (pixel != prev) {
//                System.out.println("i = "+i+"  x = "+x+"  y = "+y);
				temp[i++] = pixel;
			}
			prev = pixel;
		}

		if (i == temp.length)
			return temp;
		else {
			int ret[] = new int[i];
			System.arraycopy(temp, 0, ret, 0, i);

			return ret;
		}
	}

	public static int[][] imageOffsets2(double radius) {


		double PI2 = Math.PI * 2.0;

		double circumference = PI2 * radius;
		int num = (int) Math.ceil(circumference);
		num = num - num % 4;

		double angleStep = PI2 / num;


		int temp[][] = new int[num][2];

		for (int i = 0; i < num; i++) {
			double ang = angleStep * i;
			int x = (int) Math.round(Math.cos(ang) * radius);
			int y = (int) Math.round(Math.sin(ang) * radius);

			temp[i][0] = x;
			temp[i][1] = y;


//            System.out.println("i = "+i+"  x = "+x+"  y = "+y);
		}

		return temp;
	}
}
