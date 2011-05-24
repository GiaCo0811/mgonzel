#!/bin/sh

INPUT="CAVA.stk"
TMP="/tmp/tmpfile"

grep "^[^+]*+\{5\}[0-1]\{0,1\}[0-9]\{1,3\}+" $INPUT > $TMP
grep "^[^+]*+\{5\}200[01]+" $INPUT >> $TMP

sed -n "s/^\([^+]*\)+[^+]*++[^+]*\{3\}+\([^+]*\)/\1 \2 CORTE/p" $TMP
sed -n "s/^\([^+]*\)+[^+]*+[^+]\{1,\}+[^+]*\{3\}+\([^+]*\)/\1 \2 NO CORTE/p" $TMP

rm $TMP
