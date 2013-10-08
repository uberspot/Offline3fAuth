package utils;

import java.util.ArrayList;
import java.util.Collection;

import android.graphics.Bitmap;

/**
 * Face Descriptor interface.
 * @author Alan Zanoni Peixinho
 *
 */
public interface FaceDescriptor {
	
	/**
	 * Extract descriptor from a face image.
	 * @param img Face image.
	 * @return Return the face descriptor.
	 */
	public ArrayList<ArrayList<Integer>> getDescriptor(Bitmap img);
	
	/**
	 * Time spent in features extraction.
	 * @return Return the time elapsed in the feature extraction.
	 */
	public double timeElapsed();
	
	/**
	 * Compute the distance between descriptor samples.
	 * @param c1
	 * @param c2
	 * @return
	 */
	public double distance(Collection<Double> c1, Collection<Double> c2);
}
