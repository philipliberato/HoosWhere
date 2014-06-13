package edu.virginia.cs.hooswhere;

import android.app.Dialog;
import android.content.Context;

public class CustomDialog extends Dialog {

	public CustomDialog(Context context) {
		super(context);
		setContentView(R.layout.customdialog);
		
		// TODO Auto-generated constructor stub
	}

	public CustomDialog(Context context, int theme) {
		super(context, theme);
		// TODO Auto-generated constructor stub
	}

	public CustomDialog(Context context, boolean cancelable,OnCancelListener cancelListener) 
	{
		super(context, cancelable, cancelListener);
		
	}

}
