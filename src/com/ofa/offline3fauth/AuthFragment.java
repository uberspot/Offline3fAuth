package com.ofa.offline3fauth;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

import utils.LocalBinaryPattern;
import utils.ObjCacher;
import utils.ObjCrypter;
import utils.QRCodeEncoder;
import utils.SkinFaceDetector;
import utils.StringCompressor;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class AuthFragment extends TabFragment {
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View superView = super.onCreateView(inflater, container, savedInstanceState);
		
		// Do whatever change to the UI you want
		
		return superView;
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		  if(IntentIntegrator.REQUEST_CODE == requestCode && resultCode == Activity.RESULT_OK) {
			  IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
			  if (scanResult != null && scanResult.getContents()!=null) {
				 // Use qrcode string : scanResult.getContents() 
				  ObjCacher.lastQRScanned = scanResult.getContents();
				  fillImageViewWithQRCode(qrCodeView, scanResult.getContents());
				  validateAllFactors();
			  } 
		  }
		  if(requestCode == CAMREQ_CODE && resultCode == Activity.RESULT_OK) {
				Bundle extras = intent.getExtras();
				Bitmap picture = (Bitmap) extras.get("data");
				ObjCacher.lastFaceBitmap = picture;
				faceRecView.setImageBitmap(picture);
				validateAllFactors();
		  }
	}

	@Override
	protected boolean validateAllFactors() {
		LocalBinaryPattern lbp = new LocalBinaryPattern(new SkinFaceDetector(), 0.5, 0.5);
		String compressed = "";
		String password = "test";
		ArrayList<ArrayList<Integer>> col = null;
		if (ObjCacher.hasLastFaceBitmap()) {
			col = lbp.getDescriptor(ObjCacher.lastFaceBitmap);
			String descriptorString = ((col == null || col.size() == 0) ? "Empty"
					: col.toString()); 
			
			compressed = processFactors(descriptorString, password);
			System.out.println("Text: " + descriptorString);
			System.out.println("Compressed text: " + compressed);
			
		}
		if (ObjCacher.hasLastQRScanned()) {
			//System.out.println("decompressed1: " + ObjCacher.lastQRScanned);
			try { 
				System.out.println("compressed qr: " + ObjCacher.lastQRScanned);
				System.out.println("decompressed qr: " + this.deProcessFactors(compressed, password));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		

		if (ObjCacher.hasLastFaceArray() && col!=null) {
			System.out.println("Distance: " +
			lbp.distance(lbp.ArrayListToCollection(col),
			lbp.ArrayListToCollection(ObjCacher.lastFaceArray)));
		}
		 ObjCacher.lastFaceArray = col;

		if (!ObjCacher.hasLastFaceBitmap() || !ObjCacher.hasLastPassword()
				|| !ObjCacher.hasLastQRScanned())
			return false;
		// Else process ObjCacher.lastFaceBitmap , ObjCacher.lastQRScanned and
		// ObjCacher.lastPassword
		// and do something with the result

		return true;
	}
}
