#!/bin/sh
#
# Hipotesis:
# * El campo ACTIVO puede valer SI o NO.
# * Una fecha en nulo es "".
# * Despues del ultimo campo no hay un separador de campos.

TMPFILE="/tmp/lista"

REGEX="^\([^\]]*\]\)\{3\}0*[2-9][0-9][0-9]\]\([^\]]*\]\)\{2\}SI\]\([^\]]*\]\)\{3\}JUBILACION\]\([^\]]*\]\)\{1\}$"

grep $REGEX TramitesAportantes > $TMPFILE

REGEX="^\([^\]]*\]\)\{3\}[0-9]*[1-9][0-9][0-9][0-9]\]\([^\]]*\]\)\{2\}SI\]\([^\]]*\]\)\{3\}JUBILACION\]\([^\]]*\]\)\{1\}$"

grep $REGEX TramitesAportantes >> $TMPFILE

CANTIDAD=`wc -l $TMPFILE`

echo "Hay que pagar $CANTIDAD jubilaciones."

rm -rf $TMPFILE
