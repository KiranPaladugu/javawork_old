#!/bin/awk -f 

BEGIN {
  printf "#! /bin/sh\n\n";
}

/^[0-9][0-9][0-9][0-9][0-9][ ].*/ {
  printf "kill -0 %s\n", $2;
  printf "if [ $? -ne 0 ];\n"
  printf "then\n"
  printf "  %s/bin/oocleanup -transaction %s -force %s; \n", exec, $1, boot;
  printf "fi\n\n"
}

/^[0-9][0-9][0-9][0-9][0-9][0-9]*[ ].*/ {
  printf "kill -0 %s\n", $2;
  printf "if [ $? -ne 0 ];\n"
  printf "then\n"
  printf "  %s/bin/oocleanup -transaction %s -force %s; \n", exec, $1, boot;
  printf "fi\n\n"

}
