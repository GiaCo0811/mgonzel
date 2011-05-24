#!/bin/sh

ARCHIVO="Catalogo_de_Premios.dat"
PRODUCTO=$1
NUMERO=$2

sed -n "s/^[^;]*;$PRODUCTO;\([0-9]*;\)*$NUMERO\(;[0-9]*\)*$/GANO!/p" $ARCHIVO
