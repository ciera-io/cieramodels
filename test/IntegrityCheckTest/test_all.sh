#!/bin/bash
for ((i=1;i<=10;i++)); 
do 
  bash run.sh -t $i
  docker run --rm -v $PWD:/workspace levistarrett/masl-exe _build/bin/IntegrityTest_transient -t $i -postinit dummy &
  PID=$!
  sleep 1
  kill $PID &> /dev/null
done
