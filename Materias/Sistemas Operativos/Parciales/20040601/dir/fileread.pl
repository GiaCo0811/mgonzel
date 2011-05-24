#!/usr/bin/perl
require 5;

$file = "archivo.txt";
$i=0;

open(FILE, $file);

print "Contenido del archivo $file\n";

while ($linea = <FILE>)
{
	$i++;
	print $i." - ".$linea;
}

close(FILE);
