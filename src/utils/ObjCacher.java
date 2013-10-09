package utils;

import android.graphics.Bitmap;

public class ObjCacher {
	
	public static String lastQRScanned = null;
	public static Bitmap lastFaceBitmapAuth = null;
	public static Bitmap lastFaceBitmapReg = null;
	public static String lastPasswordAuth = null;
	public static String lastPasswordReg = null;
	public static Bitmap lastQRCreated = null;
	
	public static boolean hasLastQRScanned() {
		return lastQRScanned !=null;
	}
	
	public static boolean hasLastFaceBitmapAuth() {
		return lastFaceBitmapAuth !=null;
	}
	
	public static boolean hasLastPasswordAuth() {
		return lastPasswordAuth!=null;
	}
	
	public static boolean hasLastFaceBitmapReg() {
		return lastFaceBitmapAuth !=null;
	}
	
	public static boolean hasLastPasswordReg() {
		return lastPasswordAuth!=null;
	}

	public static boolean hasLastQRCreated() {
		return lastQRCreated!=null;
	}
}
