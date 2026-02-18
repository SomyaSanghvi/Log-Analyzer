# 📊 Log Processing System using Streams API

## 📌 Problem Statement

Build a system that processes a large server log file using Java Streams and computes:

- Top 10 slowest endpoints  
- Error rate per endpoint  
- P95 response time  
- Unique active users  

Each log line format:

timestamp,userId,endpoint,responseTime,statusCode

Example:

2026-02-17T10:15:32Z,user123,/api/login,120,200

---

# 🏗 Architecture Overview

The system:

- Uses Files.lines() for lazy streaming
- Processes logs in a single pass
- Aggregates metrics using Maps and Sets
- Avoids loading the entire file into memory

Time Complexity:

O(n)

Where n = number of log lines

---

# 📊 Metrics Computed

---

## 1️⃣ Top 10 Slowest Endpoints

Sorted by average response time.

Formula:

average = totalResponseTime / requestCount

Example Output:

/api/orders -> 1031.67  
/api/products -> 461.25  
/api/login -> 198.33  

Insight:
Identifies performance bottlenecks.

---

## 2️⃣ Error Rate Per Endpoint

Definition:

errorRate = errorCount / totalRequests

Where:

statusCode >= 400 → considered error

Example:

/api/login -> 0.33  
/api/orders -> 0.33  
/api/products -> 0.25  

Insight:
Highlights unstable or failing APIs.

---

## 3️⃣ P95 Response Time

Definition:

95% of requests complete below this value.

Example:

P95 = 1200 ms

Why P95?
- Average hides tail latency
- P95 captures real user experience
- Industry standard metric for SLAs

---

## 4️⃣ Unique Active Users

Computed using:

Set<String> uniqueUsers = new HashSet<>();

Example:

Unique Active Users: 546

Insight:
Measures traffic diversity and system reach.

---

# ⚡ Streaming vs Loading Entire File

---

## ❌ Loading Entire File (Not Scalable)

```java
List<String> lines = Files.readAllLines(Path.of("server.log"));
```

Problems:

- Loads full file into memory
- Memory usage = O(n)
- Not scalable for GB-sized logs
- High GC pressure

---

## ✅ Streaming Approach (Memory Efficient)

```java
try (Stream<String> lines = Files.lines(Path.of("server.log"))) {
    lines.map(LogEntry::parse)
         .forEach(...);
}
```

Advantages:

- Lazy loading
- Line-by-line processing
- Constant memory for file reading
- Suitable for large logs

---

# 🔄 Stream API Usage

---

## map()

Transforms raw log string → structured object

```java
lines.map(LogEntry::parse)
```

Purpose:
Converts unstructured data into domain objects.

---

## filter()

Used to isolate errors

```java
lines.map(LogEntry::parse)
     .filter(entry -> entry.statusCode >= 400)
     .count();
```

Purpose:
Selects only error responses.

---

## reduce-style Aggregation

Example:

```java
long totalResponseTime =
    lines.map(LogEntry::parse)
         .mapToLong(entry -> entry.responseTime)
         .sum();
```

Purpose:
Combines multiple values into a single aggregated result.

---

# 🗂 Grouping Collectors

Alternative functional approach:

```java
Map<String, Double> avgByEndpoint =
    lines.map(LogEntry::parse)
         .collect(Collectors.groupingBy(
             entry -> entry.endpoint,
             Collectors.averagingLong(entry -> entry.responseTime)
         ));
```

Purpose:

- Groups logs by endpoint
- Automatically computes aggregates
- Declarative and expressive

---

# 🧠 Memory Awareness

## What Uses Constant Memory

✔ Files.lines() (lazy streaming)  
✔ Map for endpoint stats  
✔ Set for unique users  

Memory scales with:

O(e + u)

Where:
- e = number of endpoints
- u = unique users

---

## What Uses O(n) Memory

List<Long> responseTimes

Used for P95 calculation.

If:
- 1 million logs → ~8MB memory
- 100 million logs → ~800MB memory

---

## Production Optimization Options

To make P95 fully scalable:

- Streaming percentile algorithm
- Histogram bucket approximation
- T-Digest
- Fixed-size heap approach

---

# 📈 Time & Space Complexity

Time Complexity:

O(n)

Single-pass processing.

Space Complexity:

O(e + u + n_for_P95)

Mostly memory-efficient except percentile storage.

---

## Streams API Pros

✔ Declarative  
✔ Maintainable  
✔ Single-pass processing  
✔ Easy aggregation  
✔ Supports parallel streams  

---

## Streams API Cons

⚠ Lambda allocation overhead  
⚠ Harder debugging  
⚠ P95 requires memory optimization  
⚠ Slightly slower than tight loops for CPU-bound workloads  

---

# 🎯 Conclusion

This implementation:

✔ Processes large log files efficiently  
✔ Uses lazy streaming  
✔ Demonstrates map/filter/reduce  
✔ Shows grouping collectors  
✔ Maintains memory awareness  
✔ Produces production-relevant metrics  

---

# 📌 Final Verdict

Streams API is suitable for:

- Medium-scale log analytics
- Monitoring tools
- Backend analytics systems

For massive distributed workloads consider:

- Apache Spark
- Flink
- Kafka Streams

---

# 🚀 Project Highlights

- Functional programming with Streams
- Real-world log analytics metrics
- Memory-conscious design
- Scalable architecture pattern
- Production-level thinking
