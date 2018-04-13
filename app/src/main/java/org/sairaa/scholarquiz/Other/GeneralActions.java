package org.sairaa.scholarquiz.Other;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by AndroidDreamz on 14-04-2018.
 */

public class GeneralActions {

    private static AlertDialog.Builder alertBuilder;

    public static boolean checkNetwork(Context context){
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if(netInfo != null && netInfo.isConnectedOrConnecting()){
            return true;
        }else{
            //Toast.makeText(context,"Network not Available",Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    public static void showDialog(Context context,String title, String message, String positiveBtnText, final ActivityConstants.SuccessCallBacks callBacks) {
        alertBuilder = new AlertDialog.Builder(context);
        alertBuilder.setTitle(title);
        alertBuilder.setMessage(message);
        alertBuilder.setPositiveButton(positiveBtnText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                callBacks.onSuccess();
            }
        });

        AlertDialog alertDialog = alertBuilder.create();
        alertDialog.show();

    }

    public static void showDialogWithNegativeBtn(Context context,String title, String message, String positiveBtnText, String negativeBtnText, final ActivityConstants.SuccessCallBacks callBacks) {
        alertBuilder = new AlertDialog.Builder(context);
        alertBuilder.setTitle(title);
        alertBuilder.setMessage(message);
        alertBuilder.setPositiveButton(positiveBtnText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                callBacks.onSuccess();
            }
        });

        alertBuilder.setNegativeButton(negativeBtnText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
               callBacks.onSuccess();
            }
        });

        AlertDialog alertDialog = alertBuilder.create();
        alertDialog.show();
    }
}
