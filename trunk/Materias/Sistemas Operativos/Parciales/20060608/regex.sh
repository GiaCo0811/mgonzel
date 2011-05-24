#!/bin/bash
MAESTRO="MAESTRO.dat"
MAESTROOK="MAESTRO.ok"
sed 's/^\([^;]*\);\([^;^\.]*\)\.\([^;^\.]*\)\.\([^;^\.]*\);/\1;\3\.\4\.\2;/' $MAESTRO > $MAESTROOK
