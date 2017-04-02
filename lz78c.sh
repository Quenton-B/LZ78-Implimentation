#!/bin/bash      

if [ $# -eq 2 ]
then
    INPUT_FILE_NAME="$1"
    MAX_PHRASE_LENGTH="$2"

    java Encoder $INPUT_FILE_NAME $MAX_PHRASE_LENGTH | java BitPacker 

elif [ $# -eq 1 ]
then
    INPUT_FILE_NAME="$1"

    java Encoder $INPUT_FILE_NAME 16 | java BitPacker

else
    echo $"Please enter the name of a file to compress. (And a max phrase length if desired)"
fi
