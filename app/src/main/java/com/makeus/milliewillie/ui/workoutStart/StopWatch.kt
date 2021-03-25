package com.makeus.milliewillie.ui.workoutStart

class StopWatch {
    private var startTime: Long = 0
    private var running = false
    private var currentTime: Long = 0

    fun start() {
        this.startTime = System.currentTimeMillis()
        this.running = true
    }

    fun stop() {
        this.running = false
    }

    fun pause() {
        this.running = false
        currentTime = System.currentTimeMillis() - startTime
    }

    fun resume() {
        this.running = true
        this.startTime = System.currentTimeMillis() - currentTime
    }

    //elaspsed time in milliseconds
    fun getElapsedTimeMili(): Long {
        var elapsed: Long = 0
        if (running) {
            elapsed = (System.currentTimeMillis() - startTime) / 100 % 1000
        }
        return elapsed
    }

    //elaspsed time in seconds
    fun getElapsedTimeSecs(): Long {
        var elapsed: Long = 0
        if (running) {
            elapsed = (System.currentTimeMillis() - startTime) / 1000 % 60
        }
        return elapsed
    }

    //elaspsed time in minutes
    fun getElapsedTimeMin(): Long {
        var elapsed: Long = 0
        if (running) {
            elapsed = (System.currentTimeMillis() - startTime) / 1000 / 60 % 60
        }
        return elapsed
    }

    //elaspsed time in hours
    fun getElapsedTimeHour(): Long {
        var elapsed: Long = 0
        if (running) {
            elapsed = (System.currentTimeMillis() - startTime) / 1000 / 60 / 60
        }
        return elapsed
    }

    fun getResult(): String {
        return (getElapsedTimeHour().toString() + ":" + getElapsedTimeMin() + ":"
                + getElapsedTimeSecs() + "." + getElapsedTimeMili())
    }
}