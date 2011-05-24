#!/bin/bash

BODEGA=$1
ANIOCOSECHA=$2
STOCK="WINE_STOCK.dat"
TMP1="/tmp/tmpfile1"
TMP2="/tmp/tmpfile2"

if [ $# -ne 2 ]; then
	echo "Cantidad incorrecta de parametros."
fi

# Filtro por bodega y año de cosecha:
grep "^[^|]*|$BODEGA|[^|]*|[0-3][0-9]-[01][0-9]-$ANIOCOSECHA|" $STOCK > $TMP1

# Filtro los que tienen hasta 95 botellas:
grep "|0*[0-8]\{0,1\}[0-9]|[^|]*$" $TMP1 > $TMP2
grep "|0*9[0-5]|[^|]*$" $TMP1 >> $TMP2

sed 's/$[^|]*|[^|]*|\([^|]*\)/\1/' $TMP2

#rm $TMP1
#rm $TMP2
