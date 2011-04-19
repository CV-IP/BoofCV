package gecv.struct.convolve;


/**
 * This is a kernel in a 2D convolution.  The convolution is performed by
 * convolving this kernel across a 2D array/image.  The kernel is square and has
 * the specified width.  To promote reuse of data structures the width of the kernel can be changed.
 * All elements in this kernel are integers.
 *
 * @author Peter Abeles
 */
public class Kernel2D_I32 {

	public int data[];
	public int width;

	/**
	 * Creates a new kernel whose initial values are specified by data and width.  The length
	 * of its internal data will be width*width.  Data must be at least as long as width*width.
	 *
	 * @param data  The value of the kernel. Not modified.  Reference is not saved.
	 * @param width The kernels width.  Must be odd.
	 */
	public Kernel2D_I32(int data[], int width) {
		if (width % 2 == 0 && width <= 0)
			throw new IllegalArgumentException("invalid width");

		this.width = width;

		this.data = new int[width * width];
		System.arraycopy(data, 0, this.data, 0, this.data.length);
	}

	/**
	 * Create a kernel whose elements are all equal to zero.
	 *
	 * @param width How wide the kernel is.  Must be odd.
	 */
	public Kernel2D_I32(int width) {
		if (width % 2 == 0 && width <= 0)
			throw new IllegalArgumentException("invalid width");

		data = new int[width * width];
		this.width = width;
	}

	protected Kernel2D_I32() {
	}

	/**
	 * Creates a kernel whose elements are the specified data array and has
	 * the specified width.
	 *
	 * @param data  The array who will be the kernel's data.  Reference is saved.
	 * @param width The kernel's width.
	 * @return A new kernel.
	 */
	public static Kernel2D_I32 wrap(int data[], int width) {
		if (width % 2 == 0 && width <= 0 && width * width > data.length)
			throw new IllegalArgumentException("invalid width");

		Kernel2D_I32 ret = new Kernel2D_I32();
		ret.data = data;
		ret.width = width;

		return ret;
	}

	public int[] getData() {
		return data;
	}

	/**
	 * The kernel's width.  This is an odd number.
	 *
	 * @return Kernel's width.
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * The radius is defined as the width divided by two.
	 *
	 * @return The kernel's radius.
	 */
	public int getRadius() {
		return width / 2;
	}

	public void print() {
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < width; j++) {
				System.out.printf("%3d ", data[i * width + j]);
			}
			System.out.println();
		}
	}

	public int get(int x, int y) {
		return data[y * width + x];
	}
}