package com.ofa.offline3fauth;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
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
		
		return rootView;
	}
	
	public void hideSoftKeyboard(EditText input){
        if(getActivity().getCurrentFocus()!=null && getActivity().getCurrentFocus() instanceof EditText){
            InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(input.getWindowToken(), 0);
        }
    }
	
	protected abstract void onQRCodeButtonClicked();

	protected abstract void onFaceRecButtonClicked();

	protected abstract void processCode();
	
	protected static void fillImageViewWithQRCode(ImageView imageView, String textToEncode) {
		try {
    	    Bitmap bm = QRCodeEncoder.encodeAsBitmap(textToEncode, BarcodeFormat.QR_CODE, 250, 250);
    	    if(bm != null) {
    	    	//TODO: cache this internally
    	    	imageView.setImageBitmap(bm);
    	    }
    	} catch (WriterException e) { e.printStackTrace(); }
	}
}
