#!/bin/bash
MASLMC="docker run -v $PWD:/root levistarrett/masl-compiler -skiptranslator Sqlite"
$MASLMC -domainpath /opt/masl/lib/masl -mod ALU_OOA/ALU.mod -test -output _build/src/ALU && \
$MASLMC -domainpath /opt/masl/lib/masl -mod Display_OOA/Display.mod -output _build/src/Display && \
$MASLMC -domainpath /opt/masl/lib/masl:ALU_OOA:DIsplay_OOA -prj calculator_proc/calculator.prj -output _build/src/calculator && \
cp CMakeLists.txt _build
