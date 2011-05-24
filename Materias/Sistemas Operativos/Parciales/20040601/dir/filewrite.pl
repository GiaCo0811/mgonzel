#!/usr/bin/perl
require 5;

$file = "archivo2.txt";

open(FILE, ">".$file);

print FILE "A line.\n";
print FILE "A line 2.\n";
print FILE "Another line.\n";
print FILE "File end.\n";

close(FILE);
