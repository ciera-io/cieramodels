#!/bin/bash
CIERA_VERSION=3.0.0-beta1
VERSION=1.0.0-SNAPSHOT
MVN_REPO=$HOME/.m2/repository
MODULEPATH=$MVN_REPO/io/ciera/runtime-api/$CIERA_VERSION/runtime-api-$CIERA_VERSION.jar:$MVN_REPO/io/ciera/runtime/$CIERA_VERSION/runtime-$CIERA_VERSION.jar:$MVN_REPO/io/ciera/ALU/$VERSION/ALU-$VERSION.jar:$MVN_REPO/io/ciera/calculator_proc/$VERSION/calculator_proc-$VERSION.jar
PROPS="\
  -Dio.ciera.runtime.logLevel=FINEST \
  -Dio.ciera.runtime.useDeterministicIDs\
  -Dio.ciera.runtime.haltWhenIdle\
"
java $PROPS --module-path $MODULEPATH -m calculator/calculator.calculator $@
