/*
 * Copyright (c) 2011-2012, Peter Abeles. All Rights Reserved.
 *
 * This file is part of BoofCV (http://boofcv.org).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package boofcv.alg.geo.rectify;

import boofcv.alg.geo.AssociatedPair;
import boofcv.alg.geo.UtilEpipolar;
import georegression.geometry.GeometryMath_F64;
import georegression.struct.point.Point3D_F64;
import georegression.struct.point.Vector3D_F64;
import org.ejml.data.DenseMatrix64F;
import org.ejml.simple.SimpleMatrix;

import java.util.List;

/**
 * <p>
 * Rectifies a stereo pair given a fundamental or essential matrix.  The rectification ensures that
 * the epipolar lines project to infinity along the x-axis. The computed transforms are designed to
 * minimize the range of disparity between the two images. See [1] for algorithmic details.
 * </p>
 *
 * <p>
 * [1] R. Hartley, "Theory and Practice of Projective Rectification", International Journal of Computer Vision,
 * vol 35, no 2, pages 115-127, 1999.
 * </p>
 *
 * @author Peter Abeles
 */
public class RectifyFundamental {

	// rectifying transform for left and right images
	DenseMatrix64F rect1 = new DenseMatrix64F(3,3);
	DenseMatrix64F rect2 = new DenseMatrix64F(3,3);

	/**
	 * Compute rectification transforms for the stereo pair given a fundamental matrix and its observations.
	 *
	 * @param F Fundamental matrix
	 * @param observations Observations used to compute F
	 * @param width Width of first image.
	 * @param height Height of first image.
	 */
	public void process( DenseMatrix64F F , List<AssociatedPair> observations ,
						 int width , int height ) {

		int centerX = width/2;
		int centerY = height/2;

		Point3D_F64 epipole1 = new Point3D_F64();
		Point3D_F64 epipole2 = new Point3D_F64();

		UtilEpipolar.extractEpipoles(F,epipole1,epipole2);

		// compute the transform H which will send epipole2 to infinity
		SimpleMatrix R = rotateEpipole(epipole2,centerX,centerY);
		SimpleMatrix T = translateToOrigin(centerX,centerY);
		SimpleMatrix G = computeG(epipole2,centerX,centerY);

		SimpleMatrix H = G.mult(R).mult(T);

		//Find the two matching transforms
		SimpleMatrix Hzero = computeHZero(F,epipole2,H);

		SimpleMatrix Ha = computeAffineH(observations,H.getMatrix(),Hzero.getMatrix());

		rect1.set(Ha.mult(Hzero).getMatrix());
		rect2.set(H.getMatrix());
	}

	/**
	 * Create a transform which will move the specified point to the origin
	 */
	private SimpleMatrix translateToOrigin( int x0 , int y0 ) {

		SimpleMatrix T = SimpleMatrix.identity(3);

		T.set(0, 2, -x0);
		T.set(1, 2, -y0);

		return T;
	}

	/**
	 * Apply a rotation such that the epipole  is equal to [f,0,1)\
	 */
	private SimpleMatrix rotateEpipole( Point3D_F64 epipole , int x0 , int y0 )
	{
		// compute rotation which will set
		// x * sin(theta) + y * cos(theta) = 0

		double x = epipole.x/epipole.z-x0;
		double y = epipole.y/epipole.z-y0;

		double theta = Math.atan2(-y,x);
		double c = Math.cos(theta);
		double s = Math.sin(theta);

		SimpleMatrix R = new SimpleMatrix(3,3);
		R.setRow(0, 0, c,-s);
		R.setRow(1, 0, s, c);
		R.set(2, 2, 1);

		return R;
	}

	private SimpleMatrix computeG( Point3D_F64 epipole , int x0 , int y0 ) {
		double x = epipole.x/epipole.z - x0;
		double y = epipole.y/epipole.z - y0;

		double f = Math.sqrt(x*x + y*y);
		SimpleMatrix G = SimpleMatrix.identity(3);
		G.set(2,0,-1.0/f);

		return G;
	}

	/**
	 * Finds the values of a,b,c which minimize
	 *
	 * sum (a*x(+)_i + b*y(+)_i + c - x(-)_i)^2
	 *
	 * See page 306
	 *
	 * @return Affine transform
	 */
	private SimpleMatrix computeAffineH( List<AssociatedPair> observations ,
										 DenseMatrix64F H , DenseMatrix64F Hzero ) {
		SimpleMatrix A = new SimpleMatrix(observations.size(),3);
		SimpleMatrix b = new SimpleMatrix(A.numRows(),1);

		Point3D_F64 c = new Point3D_F64();
		Point3D_F64 k = new Point3D_F64();

		for( int i = 0; i < observations.size(); i++ ) {
			AssociatedPair a = observations.get(i);

			GeometryMath_F64.mult(Hzero,a.keyLoc,k);
			GeometryMath_F64.mult(H,a.currLoc,c);

			A.setRow(i,0,k.x,k.y,1);
			b.set(i,0,c.x);
		}

		SimpleMatrix x = A.solve(b);

		SimpleMatrix Ha = SimpleMatrix.identity(3);
		Ha.setRow(0,0,x.getMatrix().data);

		return Ha;
	}

	/**
	 * H0 = H*M
	 * P=[M|m] from canonical camera
	 */
	private SimpleMatrix computeHZero( DenseMatrix64F F , Point3D_F64 e2 ,
									   SimpleMatrix H ) {

		Vector3D_F64 v = new Vector3D_F64(-4,3,0.5);

		// need to make sure M is not singular for this technique to work
		SimpleMatrix P = SimpleMatrix.wrap(UtilEpipolar.canonicalCamera(F, e2, v , 1.5));

		SimpleMatrix M = P.extractMatrix(0, 3, 0, 3);

		return H.mult(M);
	}

	/**
	 * Rectification transform for first camera
	 */
	public DenseMatrix64F getRect1() {
		return rect1;
	}

	/**
	 * Rectification transform for second camera
	 */
	public DenseMatrix64F getRect2() {
		return rect2;
	}
}