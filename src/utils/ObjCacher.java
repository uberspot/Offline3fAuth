package utils;

import android.graphics.Bitmap;

public class ObjCacher {
	
	public static String lastQRScanned = null;
	public static Bitmap lastFaceBitmap = null;
	public static String lastPassword = null;
	
	public static boolean hasLastQRScanned() {
		return lastQRScanned !=null;
	}
	
	public static boolean hasLastFaceBitmap() {
		return lastFaceBitmap !=null;
	}
	
	public static boolean hasLastPassword() {
		return lastPassword!=null;
	}
}
