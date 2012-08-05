#!/bin/bash
nohup java -cp eliza-2.0-SNAPSHOT-dependencies.jar eloquent.eliza.ElizaDaemon &
echo $! > .pid