/*
 * Copyright 2011 Peter Abeles
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package gecv.alg.transform.ii;

import gecv.alg.InputSanityCheck;
import gecv.alg.transform.ii.impl.ImplIntegralImageOps;
import gecv.struct.image.ImageFloat32;
import gecv.struct.image.ImageSInt32;
import gecv.struct.image.ImageUInt8;


/**
 * <p>
 * Common operations for dealing with integral images.
 * </p>
 *
 * @author Peter Abeles
 */
public class IntegralImageOps {

	/**
	 * Converts a regular image into an integral image.
	 *
	 * @param input Regular image. Not modified.
	 * @param transformed Integral image. If null a new image will be created. Modified.
	 * @return Integral image.
	 */
	public static ImageFloat32 transform( ImageFloat32 input , ImageFloat32 transformed ) {
		transformed = InputSanityCheck.checkDeclare(input,transformed);

		ImplIntegralImageOps.transform(input,transformed);

		return transformed;
	}

	/**
	 * Converts a regular image into an integral image.
	 *
	 * @param input Regular image. Not modified.
	 * @param transformed Integral image. If null a new image will be created. Modified.
	 * @return Integral image.
	 */
	public static ImageSInt32 transform( ImageUInt8 input , ImageSInt32 transformed ) {
		transformed = InputSanityCheck.checkDeclare(input,transformed,ImageSInt32.class);

		ImplIntegralImageOps.transform(input,transformed);

		return transformed;
	}

	/**
	 * Converts a regular image into an integral image.
	 *
	 * @param input Regular image. Not modified.
	 * @param transformed Integral image. If null a new image will be created. Modified.
	 * @return Integral image.
	 */
	public static ImageSInt32 transform( ImageSInt32 input , ImageSInt32 transformed ) {
		transformed = InputSanityCheck.checkDeclare(input,transformed,ImageSInt32.class);

		ImplIntegralImageOps.transform(input,transformed);

		return transformed;
	}

	/**
	 * General code for convolving a box filter across an image using the integral image.
	 *
	 * @param integral Integral image.
	 * @param kernel Convolution kernel.
	 * @param output The convolved image. If null a new image will be declared and returned. Modified.
	 * @return Convolved image.
	 */
	public static ImageFloat32 convolve( ImageFloat32 integral ,
										 IntegralKernel kernel ,
										 ImageFloat32 output )
	{
		output = InputSanityCheck.checkDeclare(integral,output);

		ImplIntegralImageOps.convolve(integral,kernel.blocks,kernel.scales,output);

		return output;
	}

	/**
	 * General code for convolving a box filter across an image using the integral image.
	 *
	 * @param integral Integral image.
	 * @param kernel Convolution kernel.
	 * @param output The convolved image. If null a new image will be declared and returned. Modified.
	 * @return Convolved image.
	 */
	public static ImageSInt32 convolve( ImageSInt32 integral ,
										IntegralKernel kernel ,
										ImageSInt32 output )
	{
		output = InputSanityCheck.checkDeclare(integral,output);

		ImplIntegralImageOps.convolve(integral,kernel.blocks,kernel.scales,output);

		return output;
	}

	/**
	 * Convolves the kernel only across the image's border.
	 *
	 * @param integral Integral image. Not modified.
	 * @param kernel Convolution kernel.
	 * @param output The convolved image. If null a new image will be created. Modified.
	 * @param borderX Size of the image border along the horizontal axis.
	 * @param borderY size of the image border along the vertical axis.
	 */
	public static ImageFloat32 convolveBorder( ImageFloat32 integral ,
											   IntegralKernel kernel ,
											   ImageFloat32 output , int borderX , int borderY )
	{
		output = InputSanityCheck.checkDeclare(integral,output);

		ImplIntegralImageOps.convolveBorder(integral,kernel.blocks,kernel.scales,output,borderX,borderY);

		return output;
	}

	/**
	 * Convolves the kernel only across the image's border.
	 *
	 * @param integral Integral image. Not modified.
	 * @param kernel Convolution kernel.
	 * @param output The convolved image. If null a new image will be created. Modified.
	 * @param borderX Size of the image border along the horizontal axis.
	 * @param borderY size of the image border along the vertical axis.
	 */
	public static ImageSInt32 convolveBorder( ImageSInt32 integral ,
											  IntegralKernel kernel ,
											  ImageSInt32 output , int borderX , int borderY )
	{
		output = InputSanityCheck.checkDeclare(integral,output);

		ImplIntegralImageOps.convolveBorder(integral,kernel.blocks,kernel.scales,output,borderX,borderY);

		return output;
	}

	/**
	 * Convolves a kernel around a single point in the integral image.
	 *
	 * @param integral Input integral image. Not modified.
	 * @param kernel Convolution kernel.
	 * @param x Pixel the convolution is performed at.
	 * @param y Pixel the convolution is performed at.
	 * @return Value of the convolution
	 */
	public static float convolveSparse( ImageFloat32 integral , IntegralKernel kernel , int x , int y )
	{
		return ImplIntegralImageOps.convolveSparse(integral,kernel,x,y);
	}

	/**
	 * Convolves a kernel around a single point in the integral image.
	 *
	 * @param integral Input integral image. Not modified.
	 * @param kernel Convolution kernel.
	 * @param x Pixel the convolution is performed at.
	 * @param y Pixel the convolution is performed at.
	 * @return Value of the convolution
	 */
	public static int convolveSparse( ImageSInt32 integral , IntegralKernel kernel , int x , int y )
	{
		return ImplIntegralImageOps.convolveSparse(integral,kernel,x,y);
	}

	/**
	 * <p>
	 * Computes the value of a block inside an integral image without bounds checking.  The block is
	 * defined as follows: x0 < x <= x1 and y0 < y < y1.
	 * </p>
	 *
	 * @param integral Integral image.
	 * @param x0 Lower bound of the block.  Exclusive.
	 * @param y0 Lower bound of the block.  Exclusive.
	 * @param x1 Upper bound of the block.  Inclusive.
	 * @param y1 Upper bound of the block.  Inclusive.
	 * @return Value inside the block.
	 */
	public static float block_unsafe( ImageFloat32 integral , int x0 , int y0 , int x1 , int y1 )
	{
		return ImplIntegralImageOps.block_unsafe(integral,x0,y0,x1,y1);
	}

	/**
	 * <p>
	 * Computes the value of a block inside an integral image without bounds checking.  The block is
	 * defined as follows: x0 < x <= x1 and y0 < y < y1.
	 * </p>
	 *
	 * @param integral Integral image.
	 * @param x0 Lower bound of the block.  Exclusive.
	 * @param y0 Lower bound of the block.  Exclusive.
	 * @param x1 Upper bound of the block.  Inclusive.
	 * @param y1 Upper bound of the block.  Inclusive.
	 * @return Value inside the block.
	 */
	public static int block_unsafe( ImageSInt32 integral , int x0 , int y0 , int x1 , int y1 )
	{
		return ImplIntegralImageOps.block_unsafe(integral,x0,y0,x1,y1);
	}

	/**
	 * <p>
	 * Computes the value of a block inside an integral image and treats pixels outside of the
	 * image as zero.  The block is defined as follows: x0 < x <= x1 and y0 < y < y1.
	 * </p>
	 *
	 * @param integral Integral image.
	 * @param x0 Lower bound of the block.  Exclusive.
	 * @param y0 Lower bound of the block.  Exclusive.
	 * @param x1 Upper bound of the block.  Inclusive.
	 * @param y1 Upper bound of the block.  Inclusive.
	 * @return Value inside the block.
	 */
	public static float block_zero( ImageFloat32 integral , int x0 , int y0 , int x1 , int y1 )
	{
		return ImplIntegralImageOps.block_zero(integral,x0,y0,x1,y1);
	}

	/**
	 * <p>
	 * Computes the value of a block inside an integral image and treats pixels outside of the
	 * image as zero.  The block is defined as follows: x0 < x <= x1 and y0 < y < y1.
	 * </p>
	 *
	 * @param integral Integral image.
	 * @param x0 Lower bound of the block.  Exclusive.
	 * @param y0 Lower bound of the block.  Exclusive.
	 * @param x1 Upper bound of the block.  Inclusive.
	 * @param y1 Upper bound of the block.  Inclusive.
	 * @return Value inside the block.
	 */
	public static int block_zero( ImageSInt32 integral , int x0 , int y0 , int x1 , int y1 )
	{
		return ImplIntegralImageOps.block_zero(integral,x0,y0,x1,y1);
	}
}