FROM alpine:latest AS builder

COPY ./mission2/USB/roylls_roce_ghoul_vii_firmware_update_2018 ./

RUN apk update && \
    apk add p7zip \
    coreutils && \
    7z x roylls_roce_ghoul_vii_firmware_update_2018 && \
    tail -c +309 FIRMWARE > firmware.jar && \
    if [ "$(sha1sum firmware.jar)" != "$(cat MANIFEST.TXT)" ]; then exit 1; fi

FROM openjdk:17

COPY --from=builder ./firmware.jar ./

EXPOSE 5555/tcp

CMD java -jar firmware.jar > /dev/null
