package com.ofa.offline3fauth;

import utils.ObjCacher;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
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
		if(!ObjCacher.hasLastFaceBitmap() || !ObjCacher.hasLastPassword() || 
				!ObjCacher.hasLastQRScanned())
			return false;
		// Else process ObjCacher.lastFaceBitmap , ObjCacher.lastQRScanned and ObjCacher.lastPassword
		// and do something with the result
		return true;
	}
	
}
