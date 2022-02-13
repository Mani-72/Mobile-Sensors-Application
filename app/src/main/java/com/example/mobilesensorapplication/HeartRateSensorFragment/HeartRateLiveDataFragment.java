package com.example.mobilesensorapplication.HeartRateSensorFragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mobilesensorapplication.MainActivity;
import com.example.mobilesensorapplication.ObjectClasses.AccelerometerAxisData;
import com.example.mobilesensorapplication.OperationalClasses.ConfirmationDialogAndSaveData;
import com.example.mobilesensorapplication.OperationalClasses.DataGraph;
import com.example.mobilesensorapplication.OperationalClasses.FileLabelDialogBox;
import com.example.mobilesensorapplication.OperationalClasses.TimerForDataCollection;
import com.example.mobilesensorapplication.R;
import com.example.mobilesensorapplication.VideoRecording.CameraListener;
import com.example.mobilesensorapplication.VideoRecording.RecordClass;
import com.example.mobilesensorapplication.VoiceToText.TextToVoiceClass;

import java.util.LinkedList;
import java.util.List;

public class HeartRateLiveDataFragment extends Fragment implements CameraListener {

    OnSelectedListener callback;
    private TextView xValue, xValueGyroscope, xValueLinearAccelerometer, xhr;
    private TextView yValue, yValueGyroscope, yValueLinearAccelerometer;
    private TextView zValue, zValueGyroscope, zValueLinearAccelerometer;
    private TextView name;
    private String accelerometerName = "";
    DataGraph dataGraph, dataGraphGyroscope;
    public static Boolean saveDataFlag = true;
    boolean frequencyFlag = true;
    List<AccelerometerAxisData> accelerometerAxisDataLinkedList = new LinkedList<>();
    List<AccelerometerAxisData> accelerometerLinearAxisDataLinkedList = new LinkedList<>();
    List<AccelerometerAxisData> HeartRateDataLinkedList = new LinkedList<>();
    List<AccelerometerAxisData> gyroscopeAxisDataLinkedList = new LinkedList<>();
    private TimerForDataCollection timerForDataCollection;
    Button btnStart, btnStop, btnPause, btnCycle;
    TextView txtSamplingFrequency;
    int samplingFrequencyCounter = 0;
    Handler timeOutAccelerometerListener;
    Runnable runnableCode;
    RecordClass recordClass;
    TextToVoiceClass textToVoiceClass;


    public HeartRateLiveDataFragment(String accelerometerName) {
        this.accelerometerName = accelerometerName;
    }

    public void registerListener(OnSelectedListener callback) {
        /*20200803: This call back is for dialog box, for saving data*/
        this.callback = callback;
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        accelerometerAxisDataLinkedList = new LinkedList<>();
        HeartRateDataLinkedList = new LinkedList<>();
        accelerometerLinearAxisDataLinkedList = new LinkedList<>();
        gyroscopeAxisDataLinkedList = new LinkedList<>();
        Log.i("Lifecycle", "Accelerometer Fragment: onAttach");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.i("Mani", "stopDataCollection Parent: ");
        Log.i("Lifecycle", "Accelerometer Fragment: onDetach");
        resetData(true);
        accelerometerAxisDataLinkedList = new LinkedList<>();
        HeartRateDataLinkedList = new LinkedList<>();
        accelerometerLinearAxisDataLinkedList = new LinkedList<>();
        gyroscopeAxisDataLinkedList = new LinkedList<>();
        saveDataFlag = false;
    }

    @Override
    public void onPause() {
        super.onPause();
        resetData(true);
        //   recordClass.onPause();
        Log.i("Lifecycle", "Accelerometer Fragment: onPause");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //  recordClass.onDestroy();
        Log.i("Lifecycle", "Accelerometer Fragment: onDestroy");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("Lifecycle", "Accelerometer Fragment: onResume");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.i("Bakra", "onCreateView: ");
        return inflater.inflate(R.layout.fragment_livedata_accelerometer, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        showFileLabelDialogBox();
        defineInstances(view);
        setupViews(view);
        setupOnClickListener();
    }


    private void showFileLabelDialogBox() {
        if (accelerometerName.equals("Second")) {
            FileLabelDialogBox fileLabelDialogBox = new FileLabelDialogBox(getContext(), getActivity(), this);
            fileLabelDialogBox.showDialog();
        } else {
            setUpVideoRecorder();
        }
    }


    public void message(String message) {
        performOperations(message);
    }

    private void performOperations(String message) {
        if (message.length() < 30)
            return;

        if (frequencyFlag) {
            calculateSamplingFrequency();
        }
        //  Log.i("ManiTest", message);

        String temp = message;
        int index = temp.indexOf('/');
        String sensor = temp.substring(0, index);

        String Value = temp.substring(index + 1);
        index = Value.indexOf('/');
        String timestamp = Value.substring(0, index);


        Value = Value.substring(index + 1);
        index = Value.indexOf('/');
        String xValue = Value.substring(0, index);

        Value = Value.substring(index + 1);
        index = Value.indexOf('/');
        String yValue = Value.substring(0, index);

        Value = Value.substring(index + 1);
        String zValue = Value.substring(0);

        //  Log.i("Mani", sensor + " : " + xValue + " " + yValue + " " + zValue);
        if (sensor.contains("ACC")) {
            downloadSuccessfulAccelerometer(new AccelerometerAxisData(TimerForDataCollection.stopWatch, timestamp, Float.valueOf(xValue), Float.valueOf(yValue), Float.valueOf(zValue), 0));
        } else if (sensor.contains("GYR")) {
            downloadSuccessfulGyroscope(new AccelerometerAxisData(TimerForDataCollection.stopWatch, timestamp, Float.valueOf(xValue), Float.valueOf(yValue), Float.valueOf(zValue), 0));
        } else if (sensor.contains("ACLINEAR")) {
            downloadSuccessfulAccelerometerLinear(new AccelerometerAxisData(TimerForDataCollection.stopWatch, timestamp, Float.valueOf(xValue), Float.valueOf(yValue), Float.valueOf(zValue), 0));
        } else if (sensor.contains("HRR")) {
            downloadSuccessfulHeartRate(new AccelerometerAxisData(TimerForDataCollection.stopWatch, timestamp, Float.valueOf(xValue), Float.valueOf(yValue), Float.valueOf(zValue), 0));
        }

    }

    private void calculateSamplingFrequency() {
        play();
        frequencyFlag = false;
        timerForDataCollection.startTimer();

        timeOutAccelerometerListener = new Handler();
        int accelerometerTimerDataCollection = 1000; // 1 sec (in milliseconds)

        runnableCode = new Runnable() {

            @Override
            public void run() {
                samplingFrequencyCounter++;
                txtSamplingFrequency.setText("Sampling Frequency: " + accelerometerAxisDataLinkedList.size() / samplingFrequencyCounter + " Hz");
                timeOutAccelerometerListener.postDelayed(this, accelerometerTimerDataCollection); /*2 Minutes Timer For Data collection first time*/
            }
        };

        // Start the initial runnable task by posting through the handler
        timeOutAccelerometerListener.postDelayed(runnableCode, accelerometerTimerDataCollection); /*2 Minutes Timer For Data collection first time*/
    }


    private void defineInstances(View view) {
        timerForDataCollection = new TimerForDataCollection(getContext(), view);
        textToVoiceClass = new TextToVoiceClass(getActivity(), getContext());

    }


    private void setupViews(View view) {
        //retrieve widgets
        name = (TextView) view.findViewById(R.id.title);
        xValue = (TextView) view.findViewById(R.id.xvalue);
        yValue = (TextView) view.findViewById(R.id.yvalue);
        zValue = (TextView) view.findViewById(R.id.zvalue);
        txtSamplingFrequency = (TextView) view.findViewById(R.id.frequency_count);

        xValueGyroscope = (TextView) view.findViewById(R.id.xvalue_gyroscope);
        yValueGyroscope = (TextView) view.findViewById(R.id.yvalue_gyroscope);
        zValueGyroscope = (TextView) view.findViewById(R.id.zvalue_gyroscope);

        xValueLinearAccelerometer = (TextView) view.findViewById(R.id.xvalue_linear_accelerometer);
        yValueLinearAccelerometer = (TextView) view.findViewById(R.id.yvalue_linear_accelerometer);
        zValueLinearAccelerometer = (TextView) view.findViewById(R.id.zvalue_linear_accelerometer);

        xhr = (TextView) view.findViewById(R.id.xvalue_hr);

        //  LinearLayout chartLayout = (LinearLayout) view.findViewById(R.id.chart);
        //  LinearLayout chartLayoutGyroscope = (LinearLayout) view.findViewById(R.id.chart_gyroscope);

        btnStart = (Button) view.findViewById(R.id.start);
        btnPause = (Button) view.findViewById(R.id.pause);
        btnStop = (Button) view.findViewById(R.id.stop);
        btnCycle = (Button) view.findViewById(R.id.cycle);

        btnStop.setVisibility(View.GONE);
        btnPause.setVisibility(View.GONE);

        // name.setText(accelerometerName);
        // setupGrphView(view, chartLayout, chartLayoutGyroscope);
    }

    private void clientDisconnect() {
        //TODO
        btnStart.setVisibility(View.GONE);
        btnStop.setVisibility(View.GONE);
        btnPause.setVisibility(View.GONE);
    }


    private void setupGrphView(View view, LinearLayout chartLayout, LinearLayout chartLayoutGyroscope) {
        dataGraph = new DataGraph(getContext(), view, chartLayout);
        dataGraph.initChart();

        dataGraphGyroscope = new DataGraph(getContext(), view, chartLayoutGyroscope);
        dataGraphGyroscope.initChart();
    }

    private void setupOnClickListener() {


        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startDataCollection();
            }
        });

        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pauseDataCollection();
            }
        });

        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showDataSavingConfirmationDialog();
                resetData(false);
            }
        });



        btnCycle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchCamera();
            }
        });

    }

    private void startDataCollection() {
        saveDataFlag = true;
        frequencyFlag = true;
        btnStart.setVisibility(View.GONE);
        btnStop.setVisibility(View.VISIBLE);
        btnPause.setVisibility(View.VISIBLE);
        //TODO
        Log.i("Bakra", "startDataCollection: ");
        textToVoiceClass.setupTextToSpeech();
        ((MainActivity) getActivity()).sendMessage("Phone Server Start");
        //  playOrPause();
    }

    private void pauseDataCollection() {
        // saveDataFlag = false;
        btnStart.setVisibility(View.VISIBLE);
        btnPause.setVisibility(View.GONE);
        // TODO
        ((MainActivity) getActivity()).sendMessage("Phone Server Pause");
        if (timerForDataCollection != null) {
            timerForDataCollection.pauseTimer();
            Pause();
        }
        removeCallbackFrequencyCheck();
    }

    private void stopDataCollection(boolean resetFlag) {
        //TODO
        if (resetFlag) {
            saveDataFlag = false;
            ((MainActivity) getActivity()).sendMessage("Phone Server Stop");
        } else {
            Log.i("ErrorCheck", "stopDataCollection: ");
            stopCameraListener();
        }

        btnStart.setVisibility(View.VISIBLE);
        btnStop.setVisibility(View.GONE);
        btnPause.setVisibility(View.GONE);
    }

    private void stopCamera() {
        Log.i("LagTest", "stopCamera: recordclass");
        recordClass.stopCamera();
    }

    private void play() {
        recordClass.play();
    }

    private void Pause() {
        recordClass.pause();
    }

    private void switchCamera() {
        recordClass.switchCamera();
    }


    private void resetData(boolean resetFlag) {

        if (!frequencyFlag)
            timerForDataCollection.resetTimer();
        stopDataCollection(resetFlag);
        removeCallbackFrequencyCheck();

    }

    private void removeCallbackFrequencyCheck() {
        if (timeOutAccelerometerListener != null)
            timeOutAccelerometerListener.removeCallbacks(runnableCode);
    }


    public void downloadSuccessfulAccelerometer(AccelerometerAxisData accelerometerAxisData) {
        setupLiveAxisValuesAccelerometer(accelerometerAxisData);
    }

    public void downloadSuccessfulAccelerometerLinear(AccelerometerAxisData accelerometerAxisData) {
        setupLiveAxisValuesAccelerometerLinear(accelerometerAxisData);
    }

    public void downloadSuccessfulHeartRate(AccelerometerAxisData accelerometerAxisData) {
        setupLiveHeartRate(accelerometerAxisData);
    }


    public void downloadSuccessfulGyroscope(AccelerometerAxisData accelerometerAxisData) {
        setupLiveAxisValuesGyroscope(accelerometerAxisData);
    }

    private void setupLiveAxisValuesAccelerometer(AccelerometerAxisData accelerometerAxisData) {
        xValue.setText("X Value: " + accelerometerAxisData.getC_xValue());
        yValue.setText("Y Value: " + accelerometerAxisData.getD_yValue());
        zValue.setText("Z Value: " + accelerometerAxisData.getE_zValue());

        //  setUpGraphData(accelerometerAxisData);
        if (saveDataFlag)
            accelerometerAxisDataLinkedList.add(accelerometerAxisData);
    }

    private void setupLiveAxisValuesAccelerometerLinear(AccelerometerAxisData accelerometerAxisData) {
        xValueLinearAccelerometer.setText("X Value: " + accelerometerAxisData.getC_xValue());
        yValueLinearAccelerometer.setText("Y Value: " + accelerometerAxisData.getD_yValue());
        zValueLinearAccelerometer.setText("Z Value: " + accelerometerAxisData.getE_zValue());

        //  setUpGraphData(accelerometerAxisData);
        if (saveDataFlag)
            accelerometerLinearAxisDataLinkedList.add(accelerometerAxisData);
    }

    private void setupLiveHeartRate(AccelerometerAxisData accelerometerAxisData) {
        Log.i("Abdul", "setupLiveHeartRate: ");
        xhr.setText("HR Value: " + accelerometerAxisData.getC_xValue());
        //  setUpGraphData(accelerometerAxisData);
        if (saveDataFlag)
        {accelerometerLinearAxisDataLinkedList.add(accelerometerAxisData);
            textToSpeech(accelerometerAxisData.getC_xValue());
        }
    }


    private void setupLiveAxisValuesGyroscope(AccelerometerAxisData accelerometerAxisData) {
        xValueGyroscope.setText("X Value: " + accelerometerAxisData.getC_xValue());
        yValueGyroscope.setText("Y Value: " + accelerometerAxisData.getD_yValue());
        zValueGyroscope.setText("Z Value: " + accelerometerAxisData.getE_zValue());

        //  setUpGraphDataGyroscope(accelerometerAxisData);
        if (saveDataFlag)
            gyroscopeAxisDataLinkedList.add(accelerometerAxisData);
    }

    private void setUpGraphData(AccelerometerAxisData accelerometerAxisData) {
        dataGraph.setUpdata(accelerometerAxisData);
    }

    private void setUpGraphDataGyroscope(AccelerometerAxisData accelerometerAxisData) {
        dataGraphGyroscope.setUpdata(accelerometerAxisData);
    }

    private void showDataSavingConfirmationDialog() {
        ConfirmationDialogAndSaveData confirmationDialogAndSaveData = new ConfirmationDialogAndSaveData(getContext(), accelerometerAxisDataLinkedList, gyroscopeAxisDataLinkedList, accelerometerLinearAxisDataLinkedList, callback, accelerometerName, this,HeartRateDataLinkedList );
        confirmationDialogAndSaveData.showConformationDialog();
    }


    @Override
    public void stopCameraListener() {
        ((MainActivity) getActivity()).sendMessage("Server Stop");
        stopCamera();
    }

    @Override
    public void setupCameraListener() {
        setUpVideoRecorder();
    }

    private void setUpVideoRecorder() {
        recordClass = ((MainActivity) getActivity()).recordClass;
        recordClass.onCreate();
        recordClass.doAfterAllPermissionsGranted();
    }

    private void textToSpeech(float HR){
        textToVoiceClass.storeHearthRate(HR);
    }
}
