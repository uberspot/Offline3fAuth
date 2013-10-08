package com.ofa.offline3fauth;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;

import utils.ObjCacher;
import utils.QRCodeEncoder;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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
		if(!ObjCacher.hasLastFaceBitmap() || !ObjCacher.hasLastPassword())
			return false;
		// Else process ObjCacher.lastFaceBitmap , ObjCacher.lastQRScanned and ObjCacher.lastPassword
		// and do something with the result
		
		//Generate QRCode and add button to save it
		String compressed = generateQRCodeText(ObjCacher.lastFaceBitmap, ObjCacher.lastPassword);
		Bitmap qrBitmap = null;
		try {
			if(compressed.length()!=0)
				qrBitmap = QRCodeEncoder.encodeAsBitmap(compressed, BarcodeFormat.QR_CODE, 400, 400);
			if (qrBitmap != null) {
				qrCodeView.setImageBitmap(qrBitmap);
				ObjCacher.lastQRCreated = qrBitmap;
			}
		} catch (WriterException e) {
			e.printStackTrace();
		}
		
		qrCodeButton.setVisibility(View.VISIBLE);
		qrCodeButton.setText(getString(R.string.save));
		qrCodeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss", Locale.US);
            	String qrName = dateFormat.format(new Date());
            	saveQRCode("/offline3fauth", qrName);
            }
        }); 
		return true;
	}

	private void saveQRCode(String directory, String fileName) {
		try {
			File dir = new File(Environment
					.getExternalStorageDirectory().getAbsolutePath()
					+ directory);
			if (!dir.exists())
				dir.mkdirs();
			File file = new File(dir, fileName + ".png");
			FileOutputStream out = new FileOutputStream(file);
			ObjCacher.lastQRCreated.compress(Bitmap.CompressFormat.PNG, 90, out);
			out.close();
			Toast.makeText(getActivity(), "QRCode saved to " + file.getAbsolutePath(), Toast.LENGTH_SHORT).show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
}
