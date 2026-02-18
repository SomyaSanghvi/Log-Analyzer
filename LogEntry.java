package com.logprocessor;

public class LogEntry {

    String userId;
    String endpoint;
    long responseTime;
    int statusCode;

    public LogEntry(String userId, String endpoint, long responseTime, int statusCode) {
        this.userId = userId;
        this.endpoint = endpoint;
        this.responseTime = responseTime;
        this.statusCode = statusCode;
    }

    public static LogEntry parse(String line) {
        String[] parts = line.split(",");

        return new LogEntry(
                parts[1].trim(),
                parts[2].trim(),
                Long.parseLong(parts[3].trim()),
                Integer.parseInt(parts[4].trim())
        );
    }
}
