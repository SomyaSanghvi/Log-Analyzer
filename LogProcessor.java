package com.logprocessor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Stream;

public class LogProcessor {

    public static void main(String[] args) throws IOException {

        Map<String, EndpointStats> endpointStats = new HashMap<>();
        Set<String> uniqueUsers = new HashSet<>();
        List<Long> responseTimes = new ArrayList<>();

        try (Stream<String> lines = Files.lines(Path.of("server.log"))) {

            lines.map(LogEntry::parse)
                    .forEach(entry -> {

                        uniqueUsers.add(entry.userId);

                        responseTimes.add(entry.responseTime);

                        endpointStats
                                .computeIfAbsent(entry.endpoint, k -> new EndpointStats())
                                .add(entry.responseTime, entry.statusCode);
                    });
        }

        System.out.println("Top 10 Slowest Endpoints:");

        endpointStats.entrySet().stream()
                .sorted((a, b) -> Double.compare(
                        b.getValue().getAverageResponseTime(),
                        a.getValue().getAverageResponseTime()))
                .limit(10)
                .forEach(e ->
                        System.out.println(e.getKey() +
                                " -> " + e.getValue().getAverageResponseTime())
                );

        System.out.println("\nError Rate Per Endpoint:");

        endpointStats.forEach((endpoint, stats) ->
                System.out.println(endpoint +
                        " -> " + stats.getErrorRate())
        );

        Collections.sort(responseTimes);
        int index = (int) (0.95 * responseTimes.size());
        long p95 = responseTimes.get(index);

        System.out.println("\nP95 Response Time: " + p95);

        System.out.println("\nUnique Active Users: " + uniqueUsers.size());
    }
}
