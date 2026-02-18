# Java Log Processing System using Streams API

## 📌 Problem Statement

Process a large server log file in a memory-efficient way and compute:

- Top 10 slowest endpoints
- Error rate per endpoint
- P95 response time
- Unique active users

Each log line format:

timestamp,userId,endpoint,responseTime,statusCode

---

# 🧠 Design Overview

We use:

- Java Streams API
- Lazy file streaming (Files.lines)
- Single-pass aggregation
- O(n) time complexity

---

# 📊 Metrics Computed

## 1️⃣ Top 10 Slowest Endpoints

Sorted by average response time.

Example Output:

/api/orders -> 1031.67  
/api/products -> 461.25  
/api/login -> 198.33  

### Insight:
Identifies performance bottlenecks.

---

## 2️⃣ Error Rate Per Endpoint

Definition:

errorRate = errorCount / totalRequests  
(statusCode >= 400)

Example:

/api/login -> 0.33  
/api/orders -> 0.33  

### Insight:
Helps detect unstable APIs.

---

## 3️⃣ P95 Response Time

Definition:

95% of requests complete below this latency.

Example:

P95 = 1150 ms

### Why P95?
Average hides tail latency.
P95 captures user experience degradation.

---

## 4️⃣ Unique Active Users

Computed using HashSet.

Example:

Unique Active Users: 546

---

# ⚡ Streaming vs Loading Entire File

## ❌ Loading Entire File

