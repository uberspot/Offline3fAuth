package com.ofa.offline3fauth;

import java.util.ArrayList;

import utils.LocalBinaryPattern;
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
import android.widget.Toast;

public class AuthFragment extends TabFragment {
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View superView = super.onCreateView(inflater, container, savedInstanceState);
		
		
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
				ObjCacher.lastFaceBitmapAuth = picture;
				faceRecView.setImageBitmap(picture);
				validateAllFactors();
		  }
	}

	@Override
	protected boolean validateAllFactors() {
		setLayoutColors();
		if (!ObjCacher.hasLastFaceBitmapAuth() || !ObjCacher.hasLastPasswordAuth()
				|| !ObjCacher.hasLastQRScanned())
			return false;
		// Else process ObjCacher.lastFaceBitmap , ObjCacher.lastQRScanned and
		// ObjCacher.lastPassword
		
		System.out.println("Scanned QR Text: " + ObjCacher.lastQRScanned);
		ArrayList<ArrayList<Integer>> faceDes = getFaceDescriptor(ObjCacher.lastFaceBitmapAuth);
		ArrayList<ArrayList<Integer>> scannedFaceDes= deProcessFactors(ObjCacher.lastQRScanned, ObjCacher.lastPasswordAuth);
		System.out.println("Decompressed FaceDescriptor: " + scannedFaceDes);
		
		Double distance = LocalBinaryPattern.distance(LocalBinaryPattern.ArrayListToCollection(scannedFaceDes),
													  LocalBinaryPattern.ArrayListToCollection(faceDes));
		System.out.println("Distance: " + distance);
		// 1.0 is arbitrary for now
		if(distance==0.0)
			Toast.makeText(getActivity(), "Error in authentication. Try again", Toast.LENGTH_SHORT).show();
		else if(distance<1.0)
			Toast.makeText(getActivity(), "User authenticated. Distance: " + distance, Toast.LENGTH_SHORT).show();
		else
			Toast.makeText(getActivity(), "User not authenticated. Distance " + distance, Toast.LENGTH_SHORT).show();
		return true;
	}

	@Override
	protected void setLayoutColors() {
		if(ObjCacher.hasLastPasswordAuth())
			codeLayout.setBackgroundResource(R.drawable.green_color);
		else
			codeLayout.setBackgroundResource(R.drawable.red_color);
		if(ObjCacher.hasLastFaceBitmapAuth())
			faceRecLayout.setBackgroundResource(R.drawable.green_color);
		else
			faceRecLayout.setBackgroundResource(R.drawable.red_color);
		if(ObjCacher.hasLastQRScanned())
			qrCodeLayout.setBackgroundResource(R.drawable.green_color);
		else
			qrCodeLayout.setBackgroundResource(R.drawable.red_color);
	}

	@Override
	protected void displayAcceptedInputs() {
		if(ObjCacher.hasLastFaceBitmapAuth()) {
			faceRecView.setImageBitmap(ObjCacher.lastFaceBitmapAuth);
		}
		if(ObjCacher.hasLastPasswordAuth()) {
			codeInput.setText(ObjCacher.lastPasswordAuth);
			codeInputValidate.setText(ObjCacher.lastPasswordAuth);
		}
		if(ObjCacher.hasLastQRScanned()) {
			fillImageViewWithQRCode(qrCodeView, ObjCacher.lastQRScanned);
		}
	}

	@Override
	protected void setPassword(String codeIn) {
		ObjCacher.lastPasswordAuth = codeIn;
	}
}
