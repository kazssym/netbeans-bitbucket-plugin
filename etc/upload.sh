#!/bin/sh
# upload.sh - file uploader for Bitbucket Cloud
# Copyright (C) 2018 Kaz Nishimura
#
# Copying and distribution of this file, with or without modification, are
# permitted in any medium without royalty provided the copyright notice and
# this notice are preserved.  This file is offered as-is, without any warranty.

REPOSITORY=vx68k/netbeans-bitbucket-plugin

test -n "$USERNAME" || exit 0

for file in "$@"; do
    FILES="$FILES --form files=@\"$file\""
done
test -n "$FILES" || exit 1

exec curl --silent --show-error --user "$USERNAME${PASSWORD+:$PASSWORD}" \
    --request POST $FILES \
    https://api.bitbucket.org/2.0/repositories/$REPOSITORY/downloads
