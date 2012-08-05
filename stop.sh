#!/bin/bash

# Read the file and read the process id
cat .pid | while read PID
do
	echo "Stopping process with PID: $PID"
	kill $PID
done
rm .pid