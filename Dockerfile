FROM postgres:13.4

ENV POSTGRES_USER=myuser
ENV POSTGRES_PASSWORD=mypassword
ENV POSTGRES_DB=mydb

COPY database.sql /docker-entrypoint-initdb.d/

EXPOSE 5432