FROM mysql:8.0.21

ENV MYSQL_DATABASE=db_stoneoverflow \
    MYSQL_ROOT_PASSWORD=mysql

ADD ./init/StoneOverflow_Creation.sql /docker-entrypoint-initdb.d

EXPOSE 3306