package utils;

import java.util.LinkedList;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.PointF;
import android.graphics.Rect;
import android.media.FaceDetector;
import android.media.FaceDetector.Face;
import android.util.Log;

/**
 * 
 * @author Everton Fernandes da Silva
 * @author Alan Zanoni Peixinho
 *
 */
public class SkinFaceDetector implements FaceDetect {

	public static final int NUMBER_OF_FACES = 4;
	
	FaceDetector.Face myFace[];
	FaceDetector myFaceDetect;
	LinkedList<Rect> faces;
	PointF p;
	long time;
	
	int width, height;
	
	/**
	 * Creates an instance of {@link SkinFaceDetector}. 
	 */
	public SkinFaceDetector(){
		width = 640;
		height = 480;
		
		myFace = new FaceDetector.Face [NUMBER_OF_FACES];//acha ateh 4 faces numa imagem
        myFaceDetect = new FaceDetector(width, height, NUMBER_OF_FACES);
        faces = new LinkedList<Rect>();
		p = new PointF();
		time = 0;
	}
	
	/**
	 * The Android face detector returns a {@link Face} object, this function converts it to a rectangle.
	 * @param f Android Face parameter.
	 * @param imgwidth Image width.
	 * @param imgheight Image height.
	 * @return Return the rectangle that contains the face
	 */
	private Rect face2Rect(Face f, int imgwidth, int imgheight)
	{
		f.getMidPoint(p);
		double eyesDistance = f.eyesDistance();
		
		int x, y, width, height;
		x = (int)(Math.floor(p.x-(1.0*eyesDistance)));
    	y = (int)(Math.floor(p.y-(1.0*eyesDistance)));
    	width = (int)Math.ceil(2.0*eyesDistance);
    	height = (int)Math.ceil(3.0f*eyesDistance);
    	
    	
    	//verifica se o retangulo da face nao estah definido fora da imagem
    	if(x < 0)
    		x = 0;
    	if(y < 0)
    		y = 0;
    	if((y + height) > imgheight)
    		height = (int)(imgheight - y);
    	if((x + width) > imgwidth)
    		width = (int)(imgwidth - x);
    	
    	Rect r = new Rect();
		r.set(x, y, x+width, y+height);
		
		return r;
	}
	
	@Override
	public List<Rect> findFaces(Bitmap img) {
		
		if(img.getWidth()!=width || img.getHeight()!=height)
		{
			width = img.getWidth();
			height = img.getHeight();
		
			Log.v("SkinFaceDetector", String.format("Changing face detector resolution to %dx%d", width, height));
			
			myFaceDetect = new FaceDetector(width, height, NUMBER_OF_FACES);
		}
		
		faces.clear();
		
		time = System.currentTimeMillis();
		int n = myFaceDetect.findFaces(img, myFace);
		time = System.currentTimeMillis() - time;
		
    	for (int i = 0; i < n; i++) {
    		Rect r = face2Rect(myFace[i], img.getWidth(), img.getHeight());
    		faces.add(r);
		}
		
		return faces;
	}
	
	@Override
	public double timeElapsed() {
		// TODO Auto-generated method stub
		return ((double)time)/1000;
	}

	@Override
	public int normalSize() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Bitmap normalizeSize(Bitmap b) {
		// TODO Auto-generated method stub
		return null;
	}

}
