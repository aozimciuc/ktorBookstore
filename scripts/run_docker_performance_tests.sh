#!/bin/bash
# Run the performance tests using the k6 docker image
# The performance tests are defined in the performance_tests.js file
# The performance tests are run with 5 virtual users

# set current directory to the directory of the script
# shellcheck disable=SC2164
cd "$(dirname "$0")"

# run the performance tests using the k6 docker image with 5 virtual users
docker run -i --network host loadimpact/k6 run --vus 5 - < ./performance_tests.js
