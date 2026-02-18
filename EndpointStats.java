package com.logprocessor;

public class EndpointStats {

    long totalResponseTime = 0;
    long count = 0;
    long errorCount = 0;

    public void add(long responseTime, int statusCode) {
        totalResponseTime += responseTime;
        count++;

        if (statusCode >= 400) {
            errorCount++;
        }
    }

    public double getAverageResponseTime() {
        return (double) totalResponseTime / count;
    }

    public double getErrorRate() {
        return (double) errorCount / count;
    }
}
