#!/bin/bash
echo -e "STARTING TIMESTAMP:"$(date)
echo -e "Running the script. .\b"
export PATH=/usr/java/jdk1.6.0_16/bin/:$PATH
export CLASSPATH=/ask/CacheAnalysis/murmurhash-1.0.jar:./commons-logging-api-1.0.4.jar:./commons-logging-1.0.4.jar:./log4j-1.2.15.jar:./conf:./bin:$CLASSPATH
javac -d bin src/*.java
if [ $? -eq 0 ];then
        echo "Compiled.."
        java -Xms5g -Xmx6g -XX:NewSize=2g -XX:MaxNewSize=2g -XX:+UseParNewGC -XX:-UseGCOverheadLimit CacheAnalysis 1 2 3 1>./policy_all.txt 2>errors_all.txt &
        echo "PARSING COMPLETED ON FE001MWH" | mail -s "PARSING STATUS:FE001MWH" shanvalleru@gmail.com
        echo -e "ENDING TIMESTAMP:"$(date)i
else
        echo "Compile failed"
fi
