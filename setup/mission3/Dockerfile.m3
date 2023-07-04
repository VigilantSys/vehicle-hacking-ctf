FROM openjdk:17

COPY ./setup/mission3/TrafficSim.jar ./
COPY ./setup/mission3/firmware.jar ./

EXPOSE 5555/tcp
EXPOSE 5000/udp

CMD java -jar firmware.jar > /dev/null & java -jar TrafficSim.jar > /dev/null
