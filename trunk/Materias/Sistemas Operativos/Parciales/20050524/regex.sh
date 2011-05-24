#!/bin/sh

if [ $# -ne 2 ];then
 	echo "Debe especificar la zona y el dia."
	exit 1
fi

LOCALIDAD=$1
DIA=$2
DATOS="DATOS.dat"

CANTIDAD=`grep "^[^;]*;\([^,^;]*,\)\{0,\}$DIA\([^,^;]*,\)\{0,\}*[^,^;]*;[0-2][0-9]:[0-5][0-9];[0-2][0-9]:[0-5][0-9]\_[A-Za-z ]*:$LOCALIDAD$" $DATOS -c`

echo "Pueden realizar una tarea en $LOCALIDAD un $DIA: $CANTIDAD técnicos."
