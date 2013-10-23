package utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

import android.graphics.Bitmap;

/**
 * Local Binary Pattern descriptor.
 * @author Alan Zanoni Peixinho
 */
public class LocalBinaryPattern {

	public static final int WIDTH = 75;
	public static final int HEIGHT = 150;

	double windowW, windowH;
	long time;
	int pixels[];

	/** Initialize some variables.
	 * @param windowW Window width.
	 * @param windowH Window height.
	 */
	private void initialize(double windowW, double windowH) {
		this.windowW = windowW;
		this.windowH = windowH;

		time = 0;
		pixels = null;
	}

	/** Creates an instance of {@link LocalBinaryPattern} without windows.
	 */
	public LocalBinaryPattern() {
		initialize(1.0, 1.0);
	}

	/** Creates an instance of {@link LocalBinaryPattern} with windows.
	 * @param detect Face detector used.
	 * @param windowPercentage Windows size percentage used in descriptor.
	 */
	public LocalBinaryPattern(FaceDetect detect, double windowPercentage) {
		initialize(windowPercentage, windowPercentage);
	}

	/** Creates an instance of {@link LocalBinaryPattern} with windows.
	 * @param detect Face detector used.
	 * @param windowW Window width.
	 * @param windowH Window height.
	 */
	public LocalBinaryPattern(FaceDetect detect, double windowW, double windowH) {
		initialize(windowW, windowH);
	}

	/** Creates an instance of {@link LocalBinaryPattern} with windows.
	 * @param detect Face detector used.
	 */
	public LocalBinaryPattern(FaceDetect detect) {
		initialize(1.0, 1.0);
	}

	/** Get the (x,y) pixel in the image array.
	 * @param pixels Image array.
	 * @param x Pixel line.
	 * @param y Pixel Column.
	 * @param width Image width.
	 * @return Return the pixel value at (x,y).
	 */
	private int getPixel(int pixels[], int x, int y, int width) {
		return pixels[y * width + x];
	}
	
	private int[] lbpToInt(Bitmap img) {
		int[] feats = new int[257];
		for (int i = 0; i < feats.length; ++i)
			feats[i] = 0;

		if (pixels == null
				|| pixels.length < (img.getHeight() * img.getWidth())) {
			pixels = new int[img.getWidth() * img.getHeight()];
		}

		int width = img.getWidth();
		int height = img.getHeight();

		img.getPixels(pixels, 0, width, 0, 0, width, height);

		for (int i = 0; i < pixels.length; i++) {
			pixels[i] = (int) FaceImage.rgb2gray(pixels[i]);
		}

		for (int i = 1; i < width - 1; ++i) {
			for (int j = 1; j < height - 1; ++j) {

				int output = 0;
				int cur = getPixel(pixels, i, j, width);

				if (getPixel(pixels, i - 1, j - 1, width) >= cur) {
					output = 1;
				} else {
					output = 0;
				}

				if (getPixel(pixels, i, j - 1, width) >= cur) {
					output = (output << 1) + 1;
				} else {
					output = (output << 1) + 0;
				}

				if (getPixel(pixels, i + 1, j - 1, width) >= cur) {
					output = (output << 1) + 1;
				} else {
					output = (output << 1) + 0;
				}

				if (getPixel(pixels, i + 1, j, width) >= cur) {
					output = (output << 1) + 1;
				} else {
					output = (output << 1) + 0;
				}

				if (getPixel(pixels, i + 1, j + 1, width) >= cur) {
					output = (output << 1) + 1;
				} else {
					output = (output << 1) + 0;
				}

				if (getPixel(pixels, i, j + 1, width) >= cur) {
					output = (output << 1) + 1;
				} else {
					output = (output << 1) + 0;
				}

				if (getPixel(pixels, i - 1, j + 1, width) >= cur) {
					output = (output << 1) + 1;
				} else {
					output = (output << 1) + 0;
				}

				if (getPixel(pixels, i - 1, j, width) >= cur) {
					output = (output << 1) + 1;
				} else {
					output = (output << 1) + 0;
				}
				feats[output]++;
			}
		}

		int sum = 0;

		for (int k = 0; k < feats.length; k++) {
			sum += feats[k];
		}
		
		feats[feats.length-1] = sum;
		return feats;
	}
	
	private ArrayList<Integer> intArrayToArrayList(int[] array) {
		ArrayList<Integer> list = new ArrayList<Integer>();
		for(int i : array) {
			list.add(i);
		}
		return list;
	}

	public ArrayList<ArrayList<Integer>> getDescriptor(Bitmap img) {
		Bitmap resImg = FaceImage.resizeBitmap(img,
				(double) WIDTH / img.getWidth());

		if (windowW == 1.0 && windowH == 1.0) {
			ArrayList<ArrayList<Integer>> list = new ArrayList<ArrayList<Integer>>();
			list.add(intArrayToArrayList(lbpToInt(resImg)));
			return list;
		}

		int wInc = (int) (resImg.getWidth() * windowW);
		int hInc = (int) (resImg.getHeight() * windowH);

		ArrayList<ArrayList<Integer>> list = new ArrayList<ArrayList<Integer>>();
		
		for (int i = 0; i + wInc <= resImg.getWidth(); i += wInc) {
			for (int j = 0; j + hInc <= resImg.getHeight(); j += hInc) {
				Bitmap subImg = Bitmap.createBitmap(resImg, i, j, wInc, hInc);
				list.add(intArrayToArrayList(lbpToInt(subImg)));
				subImg.recycle();
			}
		}
		resImg.recycle();

		return list;
	}

	public static double distance(Collection<Double> c1, Collection<Double> c2) {
		return MathUtils.chiSquareDistance(c1, c2);
	}
	
	public static Collection<Double> ArrayListToCollection(ArrayList<ArrayList<Integer>> arrayLists) {
		LinkedList<Double> list = new LinkedList<Double>();
		for(ArrayList<Integer> subArrayList: arrayLists) {
			for (int i = 0; i < subArrayList.size()-1; i++) {
				list.addLast((double)subArrayList.get(i)/(double)subArrayList.get(subArrayList.size()-1) );
			}
		}
		return list;
	}
}
