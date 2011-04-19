package gecv.struct.image;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Peter Abeles
 */
public class TestImageBase {

	@Test
	public void setTo() {
		DummyImage a = new DummyImage(10, 20);

		// test it against a regular matrix
		DummyImage b = new DummyImage(10, 20);
		assertEquals(0, b.data[5]);

		a.data[5] = 6;
		b.setTo(a);
		assertEquals(6, b.data[5]);

		// test it against a submatrix
		DummyImage c = new DummyImage(20, 20);
		c = c.subimage(10, 0, 20, 20);
		c.setTo(a);
		assertEquals(0, c.data[5]);
		assertEquals(6, c.data[15]);
	}

	/**
	 * The two matrices do not have the same shape
	 */
	@Test(expected = IllegalArgumentException.class)
	public void setTo_mismatch() {
		DummyImage a = new DummyImage(10, 20);
		DummyImage b = new DummyImage(11, 20);

		a.setTo(b);
	}

	@Test
	public void isSubmatrix() {
		DummyImage a = new DummyImage(10, 20);

		assertFalse(a.isSubimage());

		assertTrue(a.subimage(0, 5, 0, 5).isSubimage());
		assertTrue(a.subimage(2, 5, 2, 5).isSubimage());
	}

	/**
	 * Test the constructor where the width,height and number of bands is specified.
	 */
	@Test
	public void constructor_w_h_n() {
		DummyImage a = new DummyImage(10, 20);

		assertEquals(10 * 20, a.data.length);
		assertEquals(10, a.getWidth());
		assertEquals(20, a.getHeight());
		assertEquals(10, a.getStride());
		assertEquals(0, a.getStartIndex());
	}

	@Test
	public void subimage() {
		DummyImage a = new DummyImage(10, 20).subimage(2, 3, 8, 10);

		assertEquals(10 * 20, a.data.length);
		assertEquals(6, a.getWidth());
		assertEquals(7, a.getHeight());
		assertEquals(10, a.getStride());
		assertEquals(3 * 10 + 2, a.getStartIndex());
	}

	@Test
	public void isInBounds() {
		DummyImage a = new DummyImage(10, 20);

		assertTrue(a.isInBounds(0, 0));
		assertTrue(a.isInBounds(9, 19));

		assertFalse(a.isInBounds(-1, 0));
		assertFalse(a.isInBounds(0, -1));
		assertFalse(a.isInBounds(10, 0));
		assertFalse(a.isInBounds(0, 20));
	}

	@Test
	public void getIndex() {
		DummyImage a = new DummyImage(10, 20);

		assertEquals(4 * 10 + 3, a.getIndex(3, 4));
	}

	private static class DummyImage extends ImageBase<DummyImage> {
		int data[];

		private DummyImage(int width, int height) {
			super(width, height);
		}

		private DummyImage() {
		}

		@Override
		protected Object _getData() {
			return data;
		}

		@Override
		protected Class<?> _getPrimitiveType() {
			return int.class;
		}

		@Override
		protected void _setData(Object data) {
			this.data = (int[]) data;
		}

		@Override
		public DummyImage _createNew(int imgWidth, int imgHeight) {
			return new DummyImage();
		}
	}
}
