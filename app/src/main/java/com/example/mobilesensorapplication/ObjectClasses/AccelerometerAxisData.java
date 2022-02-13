package com.example.mobilesensorapplication.ObjectClasses;

public class AccelerometerAxisData {
    String a_Stopwatch = "";
    String b_TimeStamp = "";
    float c_xValue = 0f;
    float d_yValue = 0f;
    float e_zValue = 0f;
    int  f_triggerCount = 0;

    public AccelerometerAxisData() {

    }


    public AccelerometerAxisData(String a_Stopwatch, String b_TimeStamp, float c_xValue, float d_yValue, float e_zValue, int f_triggerCount) {
        this.a_Stopwatch = a_Stopwatch;
        this.b_TimeStamp = b_TimeStamp;
        this.c_xValue = c_xValue;
        this.d_yValue = d_yValue;
        this.e_zValue = e_zValue;
        this.f_triggerCount = f_triggerCount;
    }

    public String getA_Stopwatch() {
        return a_Stopwatch;
    }

    public void setA_Stopwatch(String a_Stopwatch) {
        this.a_Stopwatch = a_Stopwatch;
    }

    public String getB_TimeStamp() {
        return b_TimeStamp;
    }

    public void setB_TimeStamp(String b_TimeStamp) {
        this.b_TimeStamp = b_TimeStamp;
    }

    public float getC_xValue() {
        return c_xValue;
    }

    public void setC_xValue(float c_xValue) {
        this.c_xValue = c_xValue;
    }

    public float getD_yValue() {
        return d_yValue;
    }

    public void setD_yValue(float d_yValue) {
        this.d_yValue = d_yValue;
    }

    public float getE_zValue() {
        return e_zValue;
    }

    public void setE_zValue(float e_zValue) {
        this.e_zValue = e_zValue;
    }

    public int getF_triggerCount() {
        return f_triggerCount;
    }

    public void setF_triggerCount(int f_triggerCount) {
        this.f_triggerCount = f_triggerCount;
    }
}
