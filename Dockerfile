FROM gradle:latest AS build
WORKDIR /home/gradle/src
COPY --chown=gradle:gradle . /home/gradle/src
RUN gradle build --no-daemon

FROM openjdk:11-jre-slim

RUN apt-get update && apt-get install -y procps screen

EXPOSE 25565
RUN mkdir /opt/minecraft
RUN mkdir /opt/minecraftcosmos
WORKDIR /opt/minecraftcosmos

ENV AWS_ACCESS_KEY_ID XXX
ENV AWS_SECRET_ACCESS_KEY XXX

COPY scripts/start.sh bin/start.sh
RUN chmod +x bin/start.sh

COPY --from=build /home/gradle/src/build/libs/*.jar libs/

CMD ["bin/start.sh"]