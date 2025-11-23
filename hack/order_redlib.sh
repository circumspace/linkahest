#!/bin/bash

# Fetch uptime data from UptimeRobot API
response=$(curl -s "https://stats.uptimerobot.com/api/getMonitorList/mpmqAs1G2Q")

# Define known redlib instances
known_redlib_instances=(
  "rl.bloat.cat"
  "redlib.tux.pizza"
  "redlib.ducks.party"
  "redlib.privadency.com"
  "redlib.catsarch.com"
  "redlib.r4fo.com"
  "red.ngn.tf"
)

# Parse JSON and create associative array of instance -> uptime
declare -A uptime_map

while IFS= read -r line; do
  name=$(echo "$line" | jq -r '.name')
  uptime_ratio=$(echo "$line" | jq -r '.90dRatio.ratio')

  # Check if name matches known redlib instances
  if [[ " ${known_redlib_instances[@]} " =~ " $name " ]]; then
    uptime_map["$name"]=$(( ${uptime_ratio%%.*} ))  # convert to int
  fi
done < <(echo "$response" | jq -c '.data[]')

# Sort instances by uptime descending and output
for instance in "${!uptime_map[@]}"; do
  echo "${uptime_map[$instance]} $instance"
done | sort -nr | awk '{print $2}'
