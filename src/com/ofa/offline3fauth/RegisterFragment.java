package com.ofa.offline3fauth;

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

	@Override
	protected void onQRCodeButtonClicked() {
		
	}

	@Override
	protected void onFaceRecButtonClicked() {
		
	}

	@Override
	protected void processCode() {
		
	}
}
