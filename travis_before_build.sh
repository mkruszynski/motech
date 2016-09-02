#!/usr/bin/env bash

if [ "$DB" = "mysql" ]; then
    echo "USE mysql;\nUPDATE user SET password=PASSWORD('password') WHERE user='root';\nFLUSH PRIVILEGES;\n" | mysql -u root
elif [ "$DB" = "psql" ]; then
    ls ~/
    sed -i 's/motech.sql.dbtype=mysql/motech.sql.dbtype=psql/g' ~/motech/maven.properties
    cat ~/motech/maven.properties
fi