package com.ofa.offline3fauth;

import utils.ObjCacher;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class RegisterFragment extends TabFragment {
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View superView = super.onCreateView(inflater, container, savedInstanceState);
		
		// Do whatever change to the UI you want
		qrCodeButton.setVisibility(View.GONE);
		
		return superView;
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
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
