#!/bin/sh

generate_secret() {
    tr -dc A-Za-z0-9 </dev/urandom | head -c "$1"
}

create_default_config() {
  hmac="hmac256"
  secret_length=15
  unit="HOURS"
  lifetime=1
  secret=$(generate_secret $secret_length)
  printf \
    "{\n  \"hmac\": \"%s\",\n  \"secret\": \"%s\",\n  \"timeUnit\": \"%s\",\n  \"lifetime\": %d\n}\n" \
    "$hmac" \
    "$secret" \
    "$unit" \
    "$lifetime" > "$1"
}

create_config() {
  printf "Input hmac version [default=hmac256, hmac384, hmac512]? "
  read -r hmac
  if [ "$hmac" = "" ]; then
    hmac="hmac256"
  fi
  printf "Input secret key length [default=15]? "
  read -r secret_length
  if [ "$secret_length" = "" ]; then
    secret_length=15
  fi
  secret=$(generate_secret $secret_length)
  printf "Input timeunit [default=HOURS, MILLISECONDS, SECONDS, MINUTES, HOURS, DAYS]? "
  read -r unit
  if [ "$unit" = "" ]; then
    unit="HOURS"
  fi
  printf "Input token lifetime [default=1]? "
  read -r lifetime
  if [ "$lifetime" = "" ]; then
    lifetime=1
  fi
  printf \
  "{\n  \"hmac\": \"%s\",\n  \"secret\": \"%s\",\n  \"timeUnit\": \"%s\",\n  \"lifetime\": %d\n}\n" \
  "$hmac" \
  "$secret" \
  "$unit" \
  "$lifetime" > "$1"
}

config="auth.json"

if [ "$1" = "--default" ] || [ "$1" = "-d" ]; then
  if [ ! -f "$config" ]; then
    create_default_config $config
  fi
  exit 0
fi

if [ ! -f "$config" ]; then
  create_config $config
  exit 0
fi

printf "Auth config exists, do u want to reconfigure it [y/n]? "
read -r flag

if [ "$flag" != 'y' ] && [ "$flag" != 'Y' ] && [ "$flag" != 'Д' ] && [ "$flag" != 'д' ]; then
  exit 1
fi

rm $config
create_config $config

exit 0