package com.example.mobilesensorapplication.OperationalClasses;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mobilesensorapplication.R;
import com.example.mobilesensorapplication.VideoRecording.CameraListener;

public class FileLabelDialogBox {

    Context context;
    Activity activity;
    CameraListener cameraListener;

    public FileLabelDialogBox(Context context, Activity activity,  CameraListener cameraListener) {
        this.context = context;
        this.activity = activity;
        this.cameraListener = cameraListener;
    }

    public void showDialog() {
        androidx.appcompat.app.AlertDialog alertDialog;

        String Title = "Enter File Label";
        int Icon = 0;
        Icon = R.mipmap.user_edit;

        final androidx.appcompat.app.AlertDialog.Builder alert = new androidx.appcompat.app.AlertDialog.Builder(context);
        View mView = activity.getLayoutInflater().inflate(R.layout.file_label_dialog, null);

        EditText fileLabel_check = (EditText) mView.findViewById(R.id.file_label);
        fileLabel_check.setText(Labels.fileLabel);
        Button btnok = (Button) mView.findViewById(R.id.btn_ok);
        Button btncancel = (Button) mView.findViewById(R.id.btn_cancel);
        alert.setView(mView);
        alertDialog = alert.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setTitle(Title);
        alertDialog.setIcon(Icon);
        alertDialog.setCancelable(false);
        alertDialog.show();

        btnok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                if (fileLabel_check.getText().toString().isEmpty()) {
                    showDialog();
                    Toast.makeText(context, "Please enter file label to continue", Toast.LENGTH_LONG).show();
                } else {
                    Labels.fileLabel = removeSpaesFileLabel(fileLabel_check.getText().toString());
                    cameraListener.setupCameraListener();
                }
                Toast.makeText(context, "Saved", Toast.LENGTH_LONG).show();
            }
        });

        btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                cameraListener.setupCameraListener();
                Toast.makeText(context, "Cancel", Toast.LENGTH_LONG).show();
            }
        });
    }

    private String removeSpaesFileLabel(String label) {
        String temp = label.replaceAll("[\\\\/:*?\"<>|]", "");
        temp = temp.replaceAll("\\s+", "");
        return temp;
    }

}
