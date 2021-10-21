package com.yj.system.utils.dialog;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.StringRes;
import android.text.TextUtils;
import android.view.Window;
import android.widget.Toast;


import com.yj.systemc.R;

import java.text.SimpleDateFormat;
import java.util.Date;




public class LoadingDialog {

	public static Dialog dialog;
	public static Date data;
	public static SimpleDateFormat format;
	

	public static void showToast(Context context, String msg){
		Toast.makeText(context,msg, 1).show();
	}
	
	public static void showDialog(Context context){
		dialog=new Dialog(context, R.style.dialog);
		dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.login_dialog_item);
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
	}
	public static void disDialog(){
		dialog.dismiss();
	}
	
	public static String getTime(){
		data=new Date(System.currentTimeMillis());
		format=new SimpleDateFormat("yyyy-MM-dd HH:mm:BaseFragment");
		String string=format.format(data);
		return string;
	}

	private static ProgressDialog mypDialog = null;

	public static void showSimpleLD(Context ctx, String msg) {
		if (TextUtils.isEmpty(msg)) {
			msg = ctx.getResources().getString(R.string.loading);
		}
		init(ctx, msg);
	}

	public static void showSimpleLD(Context ctx, @StringRes int stringId) {
		init(ctx, ctx.getResources().getString(stringId));
	}

	private static void init(Context ctx, String msg) {
		if (null == mypDialog) {
			mypDialog = new ProgressDialog(ctx);
		}
		mypDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mypDialog.setIndeterminate(false);
		mypDialog.setCancelable(true);
		mypDialog.setCanceledOnTouchOutside(false);
		mypDialog.setOnCancelListener(new Dialog.OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				// TODO 自动生成的方法存根
				mypDialog = null;
			}
		});
		mypDialog.setMessage(msg);
		mypDialog.show();
	}

	public static void closeSimpleLD() {
		if (null != mypDialog && mypDialog.isShowing()) {
			if (Looper.myLooper() == Looper.getMainLooper()) {
				mypDialog.cancel();
			} else {
				new Handler(Looper.getMainLooper()).post(new Runnable() {
					@Override
					public void run() {
						mypDialog.cancel();
					}
				});
			}

		}
	}
}
