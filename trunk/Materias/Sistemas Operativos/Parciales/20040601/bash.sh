#!/bin/sh
#
# Hipotesis:
# * Todos los archivos Perl tienen en la primera linea "#!/usr/bin/perl".
#

ARCHPARAMETROS="PARAMETROS.DAT"
ARCHIVOLOG="LOG.TXT"
TMPFILEDIRS="/tmp/cantdirs.tmp"
TMPFILEPERL="/tmp/cantperl.tmp"

touch $TMPFILEDIRS
touch $TMPFILEPERL


# 1)

REGEX1="$USER;\([^;]*;\)\{2\}[^;]*"

APARICIONES=`grep $REGEX1 $ARCHPARAMETROS | wc -l`

if [ "$APARICIONES" = "0" ]
then
	echo "$USER - usuario inexistente"
	exit
fi


# 2)

while read LINEA
do
	# 2.1)
	DIRORIGEN=`echo $LINEA | cut -f2 -d ';'`
	if [ ! -d "$DIRORIGEN" ]; then
		echo "$USER sin directorio origen" >> $ARCHIVOLOG
	else
		# 2.2)
		cd $DIRORIGEN
		for i in `ls`
		do 
			if [ -f $i ]; then 
				echo "$i es un archivo comun." >> $TMPFILEDIRS
			fi

			# 2.3)
			PRIMERALINEA=`head -1 $i`
			if [ "$PRIMERALINEA" = '#!/usr/bin/perl' ]; then
				echo "$i es un archivo Perl" >> $TMPFILEPERL
			fi
		done

		cd - >/dev/null

		ARCHIVOSCOMUNES=`cat $TMPFILEDIRS | wc -l`
		CANTMAXARCHIVOS=`echo $LINEA | cut -f4 -d ';'`

		if [ $ARCHIVOSCOMUNES -gt $CANTMAXARCHIVOS ]; then
			echo "$USER supera el limite de archivos" >> $ARCHIVOLOG
		fi

		CANTPROGRAMASPERL=`cat $TMPFILEPERL | wc -l`
		echo "$USER tiene $CANTPROGRAMASPERL programas PERL" >> $ARCHIVOLOG
	fi
done < $ARCHPARAMETROS

rm -rf $TMPFILEDIRS
rm -rf $TMPFILEPERL

