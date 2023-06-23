#!/bin/bash
docker run -it -v $PWD/_build:/root levistarrett/masl-build
mkdir _build/bin
cp _build/src/HelloWorld/HelloWorld_transient_standalone _build/bin
