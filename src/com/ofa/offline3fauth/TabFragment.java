package com.ofa.offline3fauth;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import utils.LocalBinaryPattern;
import utils.ObjCacher;
import utils.ObjCompressor;
import utils.ObjCrypter;
import utils.QRCodeEncoder;
import utils.SkinFaceDetector;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.integration.android.IntentIntegrator;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public abstract class TabFragment extends Fragment {
	
	protected Button faceRecButton, qrCodeButton;
	protected ImageView faceRecView, qrCodeView;
	protected EditText codeInput, codeInputValidate;
	protected final static int CAMREQ_CODE = 12221;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.worker_fragment,
				container, false);
		
		faceRecButton = (Button) rootView.findViewById(R.id.faceRecButton);
		qrCodeButton = (Button) rootView.findViewById(R.id.qrCodeButton);
		faceRecView = (ImageView) rootView.findViewById(R.id.faceRecView);
		qrCodeView = (ImageView) rootView.findViewById(R.id.qrCodeView);
		codeInput = (EditText) rootView.findViewById(R.id.codeInput);
		codeInputValidate = (EditText) rootView.findViewById(R.id.codeInputValidate);
		
		faceRecButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	onFaceRecButtonClicked();
            }
        });
		qrCodeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	onQRCodeButtonClicked();
            }
        }); 
		codeInput.addTextChangedListener(new TextWatcher() {
		    @Override
		    public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
		        // When user changes the Text
		    	processCode();
		    }
		    @Override
		    public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
		            int arg3) { }
		    @Override public void afterTextChanged(Editable arg0) { }
		});
		codeInputValidate.addTextChangedListener(new TextWatcher() {
		    @Override
		    public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
		        // When user changes the Text
		    	processCode();
		    }
		    @Override
		    public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
		            int arg3) { }
		    @Override public void afterTextChanged(Editable arg0) { }
		});
		codeInput.setOnEditorActionListener(new OnEditorActionListener() {
  		    @Override
  		    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
  		        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
  		        	hideSoftKeyboard(codeInput);
  		        	processCode();
  		            return true;
  		        }
  		        return false;
  		    }
  		});
		codeInputValidate.setOnEditorActionListener(new OnEditorActionListener() {
  		    @Override
  		    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
  		        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
  		        	hideSoftKeyboard(codeInputValidate);
  		        	processCode();
  		            return true;
  		        }
  		        return false;
  		    }
  		});
		
		if(ObjCacher.hasLastFaceBitmap()) {
			faceRecView.setImageBitmap(ObjCacher.lastFaceBitmap);
		}
		if(ObjCacher.hasLastPassword()) {
			codeInput.setText(ObjCacher.lastPassword);
			codeInputValidate.setText(ObjCacher.lastPassword);
		}
		if(ObjCacher.hasLastQRScanned()) {
			fillImageViewWithQRCode(qrCodeView, ObjCacher.lastQRScanned);
		}
		
		// validateAllFactors();
		return rootView;
	}
	
	public void hideSoftKeyboard(EditText input){
        if(getActivity().getCurrentFocus()!=null && getActivity().getCurrentFocus() instanceof EditText){
            InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(input.getWindowToken(), 0);
        }
    }
	
	protected void onQRCodeButtonClicked() {
		IntentIntegrator integrator = new IntentIntegrator(getActivity());
    	integrator.initiateScan();
	}

	protected void onFaceRecButtonClicked() {
		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		
		PackageManager packageManager = getActivity().getApplicationContext().getPackageManager();
	    List<ResolveInfo> list =
	            packageManager.queryIntentActivities(takePictureIntent, PackageManager.MATCH_DEFAULT_ONLY);
	    if(list.size() > 0) {
	    	getActivity().startActivityForResult(takePictureIntent, CAMREQ_CODE);
	    }
	}

	protected void processCode() {
		String codeIn = codeInput.getText().toString().trim();
		String codeInVal = codeInputValidate.getText().toString().trim();
		if(codeIn==null || "".equals(codeIn) || codeInVal==null || "".equals(codeInVal)) {
			// notify user that one is empty
		} else if(!codeIn.equals(codeInVal)) {
			// notify user that they don't match
		} else {
			ObjCacher.lastPassword = codeIn;
			validateAllFactors();
		}
	}
	
	protected static void fillImageViewWithQRCode(ImageView imageView, String textToEncode) {
		try {
    	    Bitmap bm = QRCodeEncoder.encodeAsBitmap(textToEncode, BarcodeFormat.QR_CODE, 250, 250);
    	    if(bm != null) {
    	    	imageView.setImageBitmap(bm);
    	    }
    	} catch (WriterException e) { e.printStackTrace(); }
	}
	
	protected abstract boolean validateAllFactors();
	
	protected ArrayList<ArrayList<Integer>> deProcessFactors(String processedString, String password) {
		try {
			return ObjCompressor.decompress(
											ObjCrypter.decrypt3DES(
													Base64.decode( 
															processedString.getBytes("ISO-8859-1")
															, Base64.NO_WRAP)
													, password));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<ArrayList<Integer>>();
	}
	
	protected String processFactors(ArrayList<ArrayList<Integer>> faceDescriptor, String password) {
		try { 
			return new String(Base64.encode(
									ObjCrypter.encrypt3DES(
												ObjCompressor.compress(faceDescriptor)
											, password)
							  , Base64.NO_WRAP)
					, "ISO-8859-1");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	
	protected String generateQRCodeText(Bitmap faceBitmap, String password) {
		ArrayList<ArrayList<Integer>> faceDes = getFaceDescriptor(faceBitmap);
		String compressed = "";
		if(faceDes!=null && faceDes.size()!=0) {
			compressed = processFactors(faceDes, password);
			System.out.println("Last Face Descriptor: " + faceDes);
			System.out.println("Encrypted QR Text: " + compressed);
		}
		return compressed;
	}
	
	protected ArrayList<ArrayList<Integer>> getFaceDescriptor(Bitmap faceBitmap) {
		LocalBinaryPattern lbp = new LocalBinaryPattern(new SkinFaceDetector(), 0.5, 0.5);
		return lbp.getDescriptor(faceBitmap);
	}
	
	protected boolean saveStringToStorage(String obj, String directory, String fileName) {
		if(!directory.startsWith(File.separator))
			directory = File.separator + directory;

		File dir = new File (Environment.getExternalStorageDirectory().getAbsolutePath() + directory);
		if(!dir.exists()) dir.mkdirs();
		
		File file = new File(dir, fileName);
		
		BufferedOutputStream output = null;
		try {
			output = new BufferedOutputStream( new FileOutputStream(file) );
			output.write(obj.getBytes());
			output.flush();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(output!=null)
				try { output.close(); } catch (IOException e) { }
		}
		return false;
	}
}
