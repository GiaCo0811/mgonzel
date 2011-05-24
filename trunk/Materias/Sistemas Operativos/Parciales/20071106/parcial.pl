#!/usr/bin/perl

#
# Hipotesis:
#

require 5;

$mascara = $ARGV[0];
$posicionmontocompra = $ARGV[1];
$archivosalida = $ARGV[2];


# 1)

if( scalar(@ARGV) < 2 ) {
	print "Pocos parametros recibidos por el programa.\n";
	exit(1);
}

opendir(DIR, ".");

$sin_novedades = 1;

# Leo los archivos en el directorio
while ($archivo = readdir(DIR)) 
{
	# Veo si es un archivo comun
	if ( -f $archivo ) 
	{
		# Veo si cumple con la mascara
		if ($archivo =~ /$mascara/) 
		{
			open(FILE, $archivo);

			$validarposicion = 1;
			# Leo el archivo
			while ($linea = <FILE>)
			{
				# Leo el monto
				@campos = split(",", $linea);

				if ($validarposicion == 1) {
                                        if ( !( $posicionmontocompra >= 1 && $posicionmontocompra < scalar(@campos) ) ) {
						print "La posicion indicada para buscar al monto del articulo esta fuera de rango en el archivo ".$archivo." \n";
						close(FILE);
						last;
					}
                                        $validarposicion = 0;
                                }


				$monto = $campos[$posicionmontocompra-1];
				$articulo =  $campos[$posicionmontocompra];
				chomp($articulo); # para el caso de que el articulo sea el ultimo campo

				$articulos{$articulo} += $monto;
				
				$sin_novedades = 0;
			}			
			
			close(FILE);
		}
	}
}

closedir(DIR);

if ($sin_novedades == 1)
{
	print "Sin novedades.\n";
	exit 0;
}

if ($archivosalida ne "")
{
	open(SALIDA, ">>".$archivosalida);
	
	foreach $clave (keys(%articulos)) {
		print SALIDA $articulos{$clave}.",".$clave."\n";
	}

	close(SALIDA);
}
else
{
	foreach $clave (keys(%articulos)) {
		print $articulos{$clave}.",".$clave."\n";
	}
}
