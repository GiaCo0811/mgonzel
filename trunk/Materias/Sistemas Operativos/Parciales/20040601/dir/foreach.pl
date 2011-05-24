#!/usr/bin/perl
require 5;

@array = (1,2,3,4,5,6,7,8,9);

foreach $elemento (@array)
{
	print "$elemento al cuadrado es ".$elemento*$elemento.".\n";
}
