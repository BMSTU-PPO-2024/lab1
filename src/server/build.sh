#!/bin/sh

run_script() {
  tr -d '\015' <"$1" >"$1".tmp
  mv "$1".tmp "$1"
  chmod +x "$1"
  ./"$1" "$2"
}

run_script conf_auth.sh --default
gradle shadowJar