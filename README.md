CacheHitRatioAnalyzer
=====================

Java module to analyze cache hit ratio from incoming search logs

The Big Picture
===============
1. Parses logs and generates unique hash using murmur hash for every incoming query (q-param from the log)
2. Constructs a final data structure based on the cache TTL value set in the CacheAnalysis.java and finally writes the data structure contents to a flat file
3. Running the attached awk script on the final flat file tells you how much of your cache is really used

Usage
=====
1. Do necessary customizations(cache ttl, incoming query param name etc) to the  CacheAnalysis.java
2. Run runcache.sh
3. Finally run awkscript.txt
