#!/bin/bash
MASLMC="docker run -v $PWD:/workspace levistarrett/masl-compiler -skiptranslator Sqlite"
$MASLMC -domainpath /root/masl/utils -mod masl/HelloWorld_OOA/HelloWorld.mod -output cpp-gen/src/HelloWorld && \
$MASLMC -domainpath /root/masl/utils:masl/HelloWorld_OOA -prj masl/HelloWorld_proc/HelloWorld_proc.prj -output cpp-gen/src/HelloWorld_proc && \
docker run -it -v $PWD/cpp-gen:/workspace levistarrett/masl-build
