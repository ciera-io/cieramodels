#!/bin/bash
MASLMC="docker run -v $PWD:/workspace levistarrett/masl-compiler -skiptranslator Sqlite"
$MASLMC -domainpath /root/masl/utils:. -mod STRING_OOA/STRING.mod -output _build/src/STRING && \
$MASLMC -domainpath /root/masl/utils:. -mod CMD_OOA/CMD.mod -output _build/src/CMD && \
$MASLMC -domainpath /root/masl/utils:. -mod Integrity_OOA/Integrity.mod -output _build/src/Integrity && \
$MASLMC -domainpath /root/masl/utils:. -prj IntegrityTest/IntegrityTest.prj -output _build/src/IntegrityTest -custombuildfile IntegrityTest/custom.cmake && \
cp CMakeLists.txt _build
docker run -it -v $PWD/_build:/workspace -v $PWD/IntegrityTest:/workspace/IntegrityTest levistarrett/masl-build
