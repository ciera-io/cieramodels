#!/bin/bash
CIERA_VERSION=3.0.0-beta1
GPS_VERSION=1.0.0-SNAPSHOT
MVN_REPO=$HOME/.m2/repository
MODULEPATH=$MVN_REPO/io/ciera/runtime-api/$CIERA_VERSION/runtime-api-$CIERA_VERSION.jar:$MVN_REPO/io/ciera/runtime/$CIERA_VERSION/runtime-$CIERA_VERSION.jar:$MVN_REPO/io/ciera/runtime-util/$CIERA_VERSION/runtime-util-$CIERA_VERSION.jar:$MVN_REPO/io/ciera/GPS_Watch/$GPS_VERSION/GPS_Watch-$GPS_VERSION.jar:$MVN_REPO/io/ciera/HeartRateMonitor/$GPS_VERSION/HeartRateMonitor-$GPS_VERSION.jar:$MVN_REPO/io/ciera/Location/$GPS_VERSION/Location-$GPS_VERSION.jar:$MVN_REPO/io/ciera/Tracking/$GPS_VERSION/Tracking-$GPS_VERSION.jar:$MVN_REPO/io/ciera/UI/$GPS_VERSION/UI-$GPS_VERSION.jar
PROPS="\
  -Dio.ciera.runtime.logLevel=FINEST \
  -Dio.ciera.runtime.useDeterministicIDs\
  -Dio.ciera.runtime.haltWhenIdle\
  -Dio.ciera.runtime.objectStore=GPS.obj\
"
java $PROPS --module-path $MODULEPATH -m GPS_Watch/gps_watch.GPS_WatchApplication $@
