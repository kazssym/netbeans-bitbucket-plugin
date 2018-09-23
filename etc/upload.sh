#!/bin/sh

REPOSITORY=vx68k/netbeans-bitbucket-plugin

test -n "$USERNAME" || exit 0

for F in "$@"; do
    FILES="$FILES -F files=@\"$F\""
done
test -n "$FILES" || exit 1

exec curl -u "$USERNAME${PASSWORD+:$PASSWORD}" -X POST $FILES \
    https://api.bitbucket.org/2.0/repositories/$REPOSITORY/downloads
