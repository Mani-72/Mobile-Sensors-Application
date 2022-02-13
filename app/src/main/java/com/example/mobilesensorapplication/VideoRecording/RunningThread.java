package com.example.mobilesensorapplication.VideoRecording;


class RunningThread extends Thread {
    boolean isRunning;

    public boolean isRunning() {
        return isRunning;
    }

    public void stopRunning() {
        this.isRunning = false;
    }


}
