package com.example.mobilesensorapplication.OperationalClasses;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.Toast;


import com.example.mobilesensorapplication.HeartRateSensorFragment.HeartRateLiveDataFragment;
import com.example.mobilesensorapplication.HeartRateSensorFragment.OnSelectedListener;
import com.example.mobilesensorapplication.ObjectClasses.AccelerometerAxisData;
import com.example.mobilesensorapplication.R;
import com.example.mobilesensorapplication.VideoRecording.CameraListener;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;


public class ConfirmationDialogAndSaveData {
    Context context;
    List<AccelerometerAxisData> accelerometerDataList;
    List<AccelerometerAxisData> gyroscopeAxisDataLinkedList;
    List<AccelerometerAxisData> accelerometerLinearAxisDataLinkedList;
    List<AccelerometerAxisData> heartRateSensor;
    OnSelectedListener callback;
    CameraListener cameraListener;
    String name;

    public ConfirmationDialogAndSaveData(Context context, List<AccelerometerAxisData> accelerometerDataList, List<AccelerometerAxisData> gyroscopeAxisDataLinkedList, List<AccelerometerAxisData> accelerometerLinearAxisDataLinkedList ,OnSelectedListener callback, String name, CameraListener cameraListener, List<AccelerometerAxisData> heartRateSensor) {
        this.context = context;
        this.accelerometerDataList = accelerometerDataList;
        this.accelerometerLinearAxisDataLinkedList = accelerometerLinearAxisDataLinkedList;
        this.gyroscopeAxisDataLinkedList = gyroscopeAxisDataLinkedList;
        this.callback = callback;
        this.cameraListener = cameraListener;
        this.name = name;
        this.heartRateSensor = heartRateSensor;
    }

    public void showConformationDialog() {
        new AlertDialog.Builder(context)
                .setTitle("Save Sensor Data")
                .setMessage("Your Data is being saved !!")
                .setIcon(R.mipmap.microchip)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {

                       // cameraListener.stopCameraListener();
                        callback.proceedSelction("Second");
                        saveDataCSV();

                    }
                })
                .show();
    }

    private void saveDataCSV() {
        HeartRateLiveDataFragment.saveDataFlag = false;
        try {
            OpenCSVWriter.writeFromListOfObjects(accelerometerDataList, Labels.fileLabel + "_AccelerometerData" + SystemDateTime.getDate() + SystemDateTime.recordingStartTime + ".csv", context);
            OpenCSVWriter.writeFromListOfObjects(accelerometerLinearAxisDataLinkedList, Labels.fileLabel + "_LinearAccelerometerData" + SystemDateTime.getDate() + SystemDateTime.recordingStartTime + ".csv", context);
            OpenCSVWriter.writeFromListOfObjects(gyroscopeAxisDataLinkedList, Labels.fileLabel + "_GyroscopeData" + SystemDateTime.getDate() + SystemDateTime.recordingStartTime + ".csv", context);
            OpenCSVWriter.writeFromListOfObjects(gyroscopeAxisDataLinkedList, Labels.fileLabel + "_HeartRate" + SystemDateTime.getDate() + SystemDateTime.recordingStartTime + ".csv", context);
            //  callback.proceedSelction("Second");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CsvDataTypeMismatchException e) {
            e.printStackTrace();
        } catch (CsvRequiredFieldEmptyException e) {
            e.printStackTrace();
        }
    }

    private void removeFirstCSVFileLine() {
        // This is only to delete the first entity in CSV file which contain the old Firebase CSV data
        accelerometerDataList.remove(0);
        accelerometerLinearAxisDataLinkedList.remove(0);
        gyroscopeAxisDataLinkedList.remove(0);
    }
}
