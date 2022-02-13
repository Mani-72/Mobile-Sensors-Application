package com.example.mobilesensorapplication.VoiceToText;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.widget.Toast;

import java.util.Locale;

public class TextToVoiceClass {

    TextToSpeech textToSpeech;
    float heartRate=0f;
    Activity activity;
    Context context;
    Handler handler;
    int seconds=10;


    public TextToVoiceClass( Activity activity, Context context) {
        this.activity = activity;
        this.context = context;
    }

    public void setupTextToSpeech(){
        handler = new Handler(Looper.getMainLooper());

        // Init TextToSpeech
        //TextToSpeech.OnInitListener(): Interface definition of a callback to be invoked indicating the completion of the TextToSpeech engine initialization.
        textToSpeech = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
            @Override      // interface Called to signal the completion of the TextToSpeech engine initialization.
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = textToSpeech.setLanguage(Locale.ENGLISH);
                    if (result == TextToSpeech.LANG_MISSING_DATA ||
                            result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Toast.makeText(context, "This language is not supported!",
                                Toast.LENGTH_SHORT);
                    } else {
                        textToSpeech.setPitch(0.6f);
                        textToSpeech.setSpeechRate(1.0f);
                        speak("Heart Rate Monitoring Started");
                        textToSpeechTimer();
                    }
                }
            }
        });
    }

    public void storeHearthRate(float heartRate){
        this.heartRate = heartRate;
    }

    public void textToSpeechTimer(){
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                decideSpeakMessage();
                handler.postDelayed(this, 1000*seconds);
            }
        }, 1000*seconds);
    }

    public void decideSpeakMessage(){
        Log.i("Bakra", "decideSpeakMessage: ");
        if(heartRate>=120){
            speak("Your heart rate is  "+ heartRate+" Please exercise slow");
        }
        else if(heartRate<120){
            speak("Your Heart Rate is "+ heartRate+" Please exercise Fast");
        }

//        else if(heartRate>=120 && heartRate<=140){
//            speak("Your Heart Rate is "+ heartRate+" Danish Gandu You are doing good exercise");
//        }
//
//        else if(heartRate>=140 && heartRate<180){
//            speak("Your Heart Rate is "+ heartRate+" Danish Gandu You are doing Excellent exercise");
//        }

    }

    private void speak(String message) {
        String text = message;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
        } else {
            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null);
        }
    }




}
