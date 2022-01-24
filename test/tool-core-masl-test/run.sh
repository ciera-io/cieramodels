#!/bin/bash
CIERA_VERSION=3.0.0-SNAPSHOT
CLASSPATH=$HOME/.m2/repository/io/ciera/runtime-util/$CIERA_VERSION/runtime-util-$CIERA_VERSION.jar:$HOME/.m2/repository/io/ciera/runtime-api/$CIERA_VERSION/runtime-api-$CIERA_VERSION.jar:$HOME/.m2/repository/io/ciera/runtime/$CIERA_VERSION/runtime-$CIERA_VERSION.jar:$HOME/.m2/repository/io/ciera/tool-core-masl-test/1.0.0-SNAPSHOT/tool-core-masl-test-1.0.0-SNAPSHOT.jar
PROPS="-Dio.ciera.runtime.haltWhenIdle=true -Dio.ciera.runtime.logLevel=FINEST -Dio.ciera.runtime.useDeterministicIDs"
java $PROPS -cp $CLASSPATH io.ciera.tool.coremasl.test.CoreMaslTest $@
