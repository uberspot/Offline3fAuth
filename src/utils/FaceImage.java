package utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

/**
 * Image utilities for face recognition.
 * @author Alan Zanoni Peixinho.
 * @author Everton Fernandes da Silva.
 *
 */
public class FaceImage {
	
	/**
	 * Load an image. Duuhr.
	 * @param filename File containing image.
	 * @return Return the image.
	 */
	public static Bitmap loadImage(String filename)
	{
		Bitmap b = BitmapFactory.decodeFile(filename);
		return b;
	}
	
	/**
	 * Load an image. Duuhr.
	 * @param filename File containing image.
	 * @return Return the image.
	 */
	public static Bitmap loadImage(File f)
	{
		return loadImage(f.getAbsolutePath());
	}
	
	/**
	 * Resize a bitmap in the indicated scale.
	 * @param bm Image to be resized.
	 * @param scale Scale used in resize.
	 * @return Return a scaled image.
	 */
	public static Bitmap resizeBitmap(Bitmap bm, double scale) {
		return resizeBitmap(bm, scale, scale);
	}
	
	/**
	 * Resize a bitmap in the indicated scale width and height.
	 * @param bm Image to be resized.
	 * @param scaleWidth Scale width used in resize.
	 * @param scaleHeight Scale height used in resize.
	 * @return Return a scaled image.
	 */
	public static Bitmap resizeBitmap(Bitmap bm, double scaleWidth, double scaleHeight) {
		int width = bm.getWidth();
		int height = bm.getHeight();
		
		// create a matrix for the manipulation
		Matrix matrix = new Matrix();
	
		// resize the bit map	
		matrix.postScale((float)scaleWidth, (float)scaleHeight);
	
		Log.v("Scale","Matrix ready");
		// recreate the new Bitmap
		Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
		
		Log.v("Scale", "Bitmap resized");
		
		return resizedBitmap;
	}
	
	/**
	 * Resize a rect size.
	 * @param r Rect to be resized.
	 * @param scale Scale used in resize.
	 * @return Return a scaled Rect.
	 */
	public static Rect resizeRect(Rect r, double scale){
		
		return new Rect((int)(r.left*scale), (int)(r.top*scale), (int)(r.right*scale), (int)(r.bottom*scale));
	}
	
	/**
	 * Save an image in file.
	 * @param b Image to be saved.
	 * @param filename File to store image.
	 */
	public static void saveImage(Bitmap b, String filename)
	{
		saveImage(b, new File(filename));
	}
	
	/**
	 * Save an image in file.
	 * @param b Image to be saved.
	 * @param file File to store image.
	 */
	public static void saveImage(Bitmap bitmap, File file) {
		// TODO Auto-generated method stub
		
		if(file.exists()){
			try {
				file.delete();
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		//uma saga epica para apenas salvar a imagem apos marcar a face
			try {
				FileOutputStream fout = new FileOutputStream(file);
		    	bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fout);
		    	fout.flush();
				fout.close();
		    	
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	
	/**
	 * Crop a region of the image.
	 * @param b Image to be cropped.
	 * @param r Region to crop.
	 * @return Return a cropped image.
	 */
	public static Bitmap cropFace(Bitmap b, Rect r)
	{
		Log.v("Croping", "Rect: ("+r.left+", "+r.top+") ("+r.right+", "+r.bottom+")");
		Log.v("Croping", "Image: ("+b.getWidth()+", "+b.getHeight()+")");
		
    	return Bitmap.createBitmap(b, r.left, r.top, r.width(), r.height());
	}
	
	/**
	 * Draw a rect in an image (Usefull to see the region in the image).
	 * @param b Image to be drawn.
	 * @param r Rect to be drawn.
	 * @param color Color of drawn rectangle.
	 * @return
	 */
	public static Bitmap drawRect(Bitmap b, Rect r, int color){
		
		Bitmap bitmap = Bitmap.createBitmap(b.getWidth(), b.getHeight(), b.getConfig());
		Canvas canvas = new Canvas(bitmap);
		canvas.drawBitmap(b, new Matrix(), null);
		
		Paint paint = new Paint();
        paint.setColor(color);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(3);
        
		canvas.drawRect(r, paint);
		
		return bitmap;
	}
	
	/**
	 * Draw a rect in an image (Usefull to see the region in the image).
	 * @param b Image to be drawn.
	 * @param r List of rects to be drawn.
	 * @param color Color of drawn rectangle.
	 * @return
	 */
	public static Bitmap drawRects(Bitmap b, Collection<Rect> r, int color){
		
		Bitmap bitmap = Bitmap.createBitmap(b.getWidth(), b.getHeight(), b.getConfig());
		Canvas canvas = new Canvas(bitmap);
		canvas.drawBitmap(b, new Matrix(), null);
		
		Paint paint = new Paint();
        paint.setColor(color);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(3);
        
		for (Iterator<Rect> iterator = r.iterator(); iterator.hasNext();) {
			Rect rect = (Rect) iterator.next();
			canvas.drawRect(rect, paint);
		}
		
		return bitmap;
	}
	
	/**
	 * Get a pixel array from image.
	 * @param img Image
	 * @param outArray Output array.
	 */
	public static void getPixelArray(Bitmap img, int outArray[]){
		int width = img.getWidth();
		int height = img.getHeight();
		assert(outArray.length>=width*height):"The array is too small.";
		img.getPixels(outArray, 0, width, 0, 0, width, height);
	}
	
	/**
	 * Get a pixel array from image.
	 * @param img Image.
	 * @return Output array.
	 */
	public static int[] getPixelArray(Bitmap img){
		int []a = new int[img.getWidth()*img.getHeight()];
		getPixelArray(img, a);
		return a;
	}
	
	/**
	 * Converts a RGB image to grayscale.
	 * @param pixels Image in array format.
	 */
	public static void rgb2gray(int pixels[]){
		for (int i = 0; i < pixels.length; i++) {
			pixels[i] = (int)rgb2gray(pixels[i]);
		}
	}
	
	/**
	 * Converts a RGB color to grayscale.
	 * @param color RGB color.
	 * @return Return the corresponding grayscale color.
	 */
	public static double rgb2gray(int color)
	{
		return  Math.min(0.2126*Color.red(color)+0.7152*Color.green(color)+0.0722*Color.blue(color), 256.0);
	}

}
