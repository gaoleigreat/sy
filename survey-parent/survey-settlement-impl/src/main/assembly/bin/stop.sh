#! /bin/sh



# ps -ef | grep java |grep impl  | awk '{print $2;}' > pids
ps -ef | grep java |grep  survey | awk '{print $2;}' >> pids
cat pids | while read line
do
    kill $line
done
