#!/bin/awk -f 

BEGIN {
  printf "#! /bin/sh\n";
}

/^[0-9][0-9][0-9][0-9][0-9][ ].*/ {
  printf "%s/bin/oocleanup -transaction %s -force %s; \n", exec, $1, boot;
}

/^[0-9][0-9][0-9][0-9][0-9][0-9]*[ ].*/ {
  printf "%s/bin/oocleanup -transaction %s -force %s; \n", exec, $1, boot;

}
