#!/bin/bash
CIERA_VERSION=3.0.0-SNAPSHOT
VERSION=1.0.0-SNAPSHOT
MVN_REPO=$HOME/.m2/repository
MODULEPATH=$MVN_REPO/io/ciera/runtime-api/$CIERA_VERSION/runtime-api-$CIERA_VERSION.jar:$MVN_REPO/io/ciera/runtime/$CIERA_VERSION/runtime-$CIERA_VERSION.jar:$MVN_REPO/io/ciera/runtime-util/$CIERA_VERSION/runtime-util-$CIERA_VERSION.jar:$MVN_REPO/org/apache/commons/commons-csv/1.9.0/commons-csv-1.9.0.jar:$MVN_REPO/io/ciera/SIS_OOA/$VERSION/SIS_OOA-$VERSION.jar:$MVN_REPO/io/ciera/SIS_proc/$VERSION/SIS_proc-$VERSION.jar
PROPS="\
  -Dio.ciera.runtime.logLevel=FINEST \
  -Dio.ciera.runtime.useDeterministicIDs\
  -Dio.ciera.runtime.haltWhenIdle\
  -Dio.ciera.runtime.objectStore=SIS.obj\
"
java $PROPS --module-path $MODULEPATH --add-modules commons.csv -m SIS_proc/sis_proc.SIS_proc "$@"
