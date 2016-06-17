#!/bin/awk -f
BEGIN { FS = "[][]"; file=""}
#/Arena/ {print $2}
/^\[.*\][.]/ {
    if (file != "")
      {
        printf("\n") > file
      }
    process=$1
    pid=$2
    file=$2".log"
    date = $4"."$5
    printf("%s\n", $0) > file
  }

/^> /{
  printf("%s\n", $0) > file
}

