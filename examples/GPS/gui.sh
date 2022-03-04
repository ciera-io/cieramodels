#!/bin/bash
CIERA_VERSION=3.0.0-SNAPSHOT
GPS_VERSION=1.0.0-SNAPSHOT
MVN_REPO=$HOME/.m2/repository
CLASSPATH=$MVN_REPO/io/ciera/runtime-api/$CIERA_VERSION/runtime-api-$CIERA_VERSION.jar:$MVN_REPO/io/ciera/runtime/$CIERA_VERSION/runtime-$CIERA_VERSION.jar:$MVN_REPO/com/googlecode/lanterna/lanterna/3.1.1/lanterna-3.1.1.jar:$MVN_REPO/io/ciera/UI/$GPS_VERSION/UI-$GPS_VERSION.jar
PROPS=-Dio.ciera.runtime.logLevel="FINEST"
java $PROPS -cp $CLASSPATH gui.Gui $@
