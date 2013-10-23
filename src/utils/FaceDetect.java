package utils;

import java.util.List;

import android.graphics.Bitmap;
import android.graphics.Rect;

/**
 * Face Detector interface
 * @author Everton Fernandes da Silva
 * @author Alan Zanoni Peixinho
 *
 */
public interface FaceDetect {
	/**
	 * Find all  the faces in the image.
	 * @param img Image submitted to face detection.
	 * @return Return a list containing the boxes that contain each face found.
	 */
	public List<Rect> findFaces(Bitmap img);
	
	/**
	 * Time elapsed in face detection.
	 * @return Return the time spent in last face detection.
	 */
	public double timeElapsed();
	
	public int normalSize();
	public Bitmap normalizeSize(Bitmap b);
}
