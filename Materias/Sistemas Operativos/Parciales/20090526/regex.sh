#!/bin/bash
CODIGO_NOVEDAD=`sed -n "s/^\([^/]*\)\/$1\/[^/]*\/S\/[^/]*$/\1/p" TRADUCTOR.info`
echo $CODIGO_NOVEDAD
sed -n "s/^.*-\([^-]*\)-\([^-]*\)-$CODIGO_NOVEDAD-\([^-]*\)-\([^-]*\)-\([^-]*\)$/\5:$1:\3:\4:\2:\1/p" NOVEDADES.dat > NOVEDADES_INBOUND.dat
