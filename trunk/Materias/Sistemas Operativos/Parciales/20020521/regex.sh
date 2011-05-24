#!/bin/sh
#
# Hipotesis:
#  * El codigo de articulo no contiene comas.
#  * La descripcion no contiene numeros seguidos de una coma.
#
#

PRECIOS="PRECIOS.dat"
SALIDA="PRECIOS.new"

# Reemplazo los separadores de miles:
sed 's/\([0-9]\{1,3\}\),/\1\./g' $PRECIOS > $SALIDA

# Reemplazo el punto decimal:
sed -i 's/\.\([0-9][0-9]\)$/,\1/' $SALIDA
