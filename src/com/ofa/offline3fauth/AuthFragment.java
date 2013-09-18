package com.ofa.offline3fauth;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import android.content.Intent;
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

	@Override
	protected void onQRCodeButtonClicked() {
		IntentIntegrator integrator = new IntentIntegrator(getActivity());
    	integrator.initiateScan();
	}

	@Override
	protected void onFaceRecButtonClicked() {
		
	}

	@Override
	protected void processCode() {
		
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		  IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
		  if (scanResult != null && scanResult.getContents()!=null) {
			 // Use qrcode string : scanResult.getContents() 
			  fillImageViewWithQRCode(qrCodeView, scanResult.getContents());
		  }
	}
}
