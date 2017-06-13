FROM openjdk:8u131-jre-alpine
CMD ["mkdir", "-p", "/etc/javapp"]
CMD ["mkdir", "-p", "/usr/share/javapp/bin"]
CMD ["mkdir", "-p", "/var/log/javapp"]
ENTRYPOINT ["sh", "-c", "$(tr '\n' ' ' < /etc/javapp/app.config)"]