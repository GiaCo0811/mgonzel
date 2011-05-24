#!/bin/bash

STOCK="StockValirozado.dat"
TMP="/tmp/resultados"
OUT="listado"

grep "^[^=]*=[^=]*=987,6[5-9]=" $STOCK > $TMP
grep "^[^=]*=[^=]*=987,[7-9][0-9]=" $STOCK >> $TMP
grep "^[^=]*=[^=]*=98[7-9],[0-9][0-9]=" $STOCK >> $TMP
grep "^[^=]*=[^=]*=99[0-9],[0-9][0-9]=" $STOCK >> $TMP
grep "^[^=]*=[^=]*=[0-9]\{4,\},[0-9]\{2\}=" $STOCK >> $TMP

sed 's/^[^=]*=\([^=]*\)=\([^=]\)=/\2/' $TMP > $OUT

CANTIDAD=`wc -l $OUT`

echo "Cantidad = $CANTIDAD"
