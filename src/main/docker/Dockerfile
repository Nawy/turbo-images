FROM frolvlad/alpine-oraclejdk8:slim
VOLUME /data
RUN sh -c 'touch /app.jar'
ENTRYPOINT [ "sh", "-c", "$(tr '\n' ' ' < /data/app.config)" ]
