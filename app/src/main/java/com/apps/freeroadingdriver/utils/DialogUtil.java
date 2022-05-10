package com.apps.freeroadingdriver.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.apps.freeroadingdriver.R;
import com.apps.freeroadingdriver.activities.LoginActivity;
import com.apps.freeroadingdriver.manager.UserManager;


public class DialogUtil {

    public static Dialog mDialog;

    public interface AlertDialogInterface {
        interface TwoButtonDialogClickListener {
            void onPositiveButtonClick();

            void onNegativeButtonClick();
        }

        interface OneButtonDialogClickListener {
            void onButtonClick();
        }

        interface OpenCameraDialogListener {
            void onCameraClick();

            void onGalleryClick();
        }
    }

    public static void openChooseMediaDialog(final Context context, final AlertDialogInterface.OpenCameraDialogListener cameraListener) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_open_gallary);
        ImageView close = (ImageView) dialog.findViewById(R.id.img_close);
        RelativeLayout camLayout = (RelativeLayout) dialog.findViewById(R.id.cam_layout);
        RelativeLayout galLayout = (RelativeLayout) dialog.findViewById(R.id.gal_layout);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        camLayout.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                cameraListener.onCameraClick();
                dialog.dismiss();

            }
        });

        galLayout.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                cameraListener.onGalleryClick();
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public static void showOkCancelDialog(Context context, String title, String message, final AlertDialogInterface.TwoButtonDialogClickListener dialogInterface) {
        if (mDialog != null && mDialog.isShowing()) {
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(context,R.style.AlertDialogCustom);
        builder.setMessage(message)
                .setTitle(title)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        mDialog.dismiss();
                        dialogInterface.onPositiveButtonClick();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        mDialog.dismiss();
                        dialogInterface.onNegativeButtonClick();
                    }
                });
        mDialog = builder.create();
        mDialog.show();
    }

    public static void showTwoButtonDialog(Context context, String message, final AlertDialogInterface.TwoButtonDialogClickListener dialogInterface) {
        if (mDialog != null && mDialog.isShowing()) {
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(context,R.style.AlertDialogCustom);
        builder.setMessage(message)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        mDialog.dismiss();
                        dialogInterface.onPositiveButtonClick();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        mDialog.dismiss();
                        dialogInterface.onNegativeButtonClick();
                    }
                });
        mDialog = builder.create();
        mDialog.show();
    }

    public static void showTwoButtonDialog(Context context, String title, String message, String positiveBtnString, String negBtnString, final AlertDialogInterface.TwoButtonDialogClickListener dialogInterface) {
        if (mDialog != null && mDialog.isShowing()) {
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(context,R.style.AlertDialogCustom);
        builder.setMessage(message)
                .setTitle(title)
                .setPositiveButton(positiveBtnString, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        mDialog.dismiss();
                        dialogInterface.onPositiveButtonClick();
                    }
                })
                .setNegativeButton(negBtnString, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        mDialog.dismiss();
                        dialogInterface.onNegativeButtonClick();
                    }
                });
        mDialog = builder.create();
        mDialog.show();
    }

    public static void showTwoButtonDialog(Context context, String message, String positiveBtnString, String negBtnString, final AlertDialogInterface.TwoButtonDialogClickListener dialogInterface) {
        if (mDialog != null && mDialog.isShowing()) {
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(context,R.style.AlertDialogCustom);
        builder.setMessage(message)
                .setPositiveButton(positiveBtnString, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        mDialog.dismiss();
                        dialogInterface.onPositiveButtonClick();
                    }
                })
                .setNegativeButton(negBtnString, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        mDialog.dismiss();
                        dialogInterface.onNegativeButtonClick();
                    }
                });
        mDialog = builder.create();
        mDialog.show();
    }

    public static void showOneButtonDialog(Context context, String message, String btnString, final AlertDialogInterface.OneButtonDialogClickListener dialogInterface) {
        if (mDialog != null && mDialog.isShowing()) {
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(context,R.style.AlertDialogCustom);
        builder.setMessage(message)
                .setPositiveButton(btnString, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        mDialog.dismiss();
                        dialogInterface.onButtonClick();
                    }
                });
        mDialog = builder.create();
        mDialog.show();
    }

    public static void showOneButtonDialog(Context context, String title, String message, String btnString, final AlertDialogInterface.OneButtonDialogClickListener dialogInterface) {
        if (mDialog != null && mDialog.isShowing()) {
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(context,R.style.AlertDialogCustom);
        builder.setMessage(message)
                .setTitle(title)
                .setPositiveButton(btnString, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        mDialog.dismiss();
                        dialogInterface.onButtonClick();
                    }
                });
        mDialog = builder.create();
        mDialog.show();
    }

    public static void showOkButtonDialog(Context context, String title, String message, final AlertDialogInterface.OneButtonDialogClickListener dialogInterface) {
        if (mDialog != null && mDialog.isShowing()) {
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(context,R.style.AlertDialogCustom);
        builder.setCancelable(false);
        builder.setMessage(message)
                .setTitle(title)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        mDialog.dismiss();
                        dialogInterface.onButtonClick();
                    }
                });
        mDialog = builder.create();
        mDialog.show();
    }

    public static void showOkButtonDialog(final Context context, String title, String message, final String key) {
        if (mDialog != null && mDialog.isShowing()) {
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(context,R.style.AlertDialogCustom);
        builder.setMessage(message)
                .setTitle(title)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        if (key.equals("8")){
                            context.startActivity(LoginActivity.Companion.createIntent(context));
                            UserManager.getInstance().logout();
                        }
                        mDialog.dismiss();
                    }
                });
        mDialog = builder.create();
        mDialog.show();
    }

    public static void dismiss() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
            mDialog = null;
        }
    }

    public static void showToastShortLength(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void showToastLongLength(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }
}
