#!/bin/sh

CODPLAN=$1
DESCR_ESP=$2
ZONA=$3

COD_ESP=`sed -n "s/^\([^:]*\):$DESCR_ESP:$ZONA$/\1/p" Especialidades.dat`

sed -n "s/^[^+]*+\([^+]*\)+$COD_ESP+[^+]*$CODPLAN[^+]*+\([^+]*\)$/Nombre: \1 | Direccion: \2/p" Prestadores.lis
