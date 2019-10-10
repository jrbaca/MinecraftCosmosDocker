#!/bin/bash

clean_up() {
	# Perform program exit housekeeping
	echo "Received sigterm, requesting minecraft stops"
  screen -p 0 -S minecraft -X eval "stuff stop\015"
}

trap clean_up SIGTERM

cd /opt/minecraftcosmos/; java -jar libs/minecraftcosmos-1.0.jar -m setup
# Unblock sigterm since was in java
cd /opt/minecraft/; screen -dmS minecraft java -Xmx2G -jar minecraft_server.jar nogui
echo "Starting minecraft"
PID=$(screen -ls | awk '/\.minecraft\t/ {print $1}' | cut -d "." -f1)
while [[ -e /proc/${PID} ]]; do sleep 1; done
echo "Minecraft exited"
# Block sigterm since was in java
cd /opt/minecraftcosmos/; java -jar libs/minecraftcosmos-1.0.jar -m shutdown