package io.eidukas.fivethirtyeight;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ContextThemeWrapper;

public class PermissionManager {
    private Context context;
    public PermissionManager(Context context){
        this.context = context;
    }
    /**
     * Show explanation to user why they need to give a permission.
     * @param title Title of dialog
     * @param message Message inside Dialog
     * @param permission Permission being requested
     * @param permissionRequestCode Callback int
     */
    private void showExplanation(String title, String message, final String permission, final int permissionRequestCode) {
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(context, R.style.myDialog));
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        requestPermission(permission, permissionRequestCode);
                    }
                });
        builder.create().show();
    }

    private void requestPermission(String permissionName, int permissionRequestCode) {
        ActivityCompat.requestPermissions((Activity)context,
                new String[]{permissionName}, permissionRequestCode);
    }

    public boolean checkPermission(String permission, String title, String explanation, int callback){
        if (ContextCompat.checkSelfPermission(context.getApplicationContext(), permission) == PackageManager.PERMISSION_DENIED){
            if(ActivityCompat.shouldShowRequestPermissionRationale((Activity)context, permission)){
                showExplanation(title, explanation, permission, callback);
            } else {
                requestPermission(permission, callback);
            }
        }
        return ContextCompat.checkSelfPermission(context.getApplicationContext(), permission) == PackageManager.PERMISSION_GRANTED;
    }
}
