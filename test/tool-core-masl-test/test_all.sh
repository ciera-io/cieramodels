#!/bin/bash
runs=0
successes=0
failures=0
for t in $(ls masl/tests); do
    ((runs++))
    mvn clean install -Ddomain=$t && bash run.sh
    if [ "$?" -eq "0" ]; then
        ((successes++))
    else
        ((failures++))
    fi
done
echo
echo "RESULTS:"
echo "Runs: $runs, Failures: $failures, Successes: $successes, % Passing: $((100*successes/runs))%"
