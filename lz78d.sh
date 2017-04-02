#!/bin/bash      
if [ $# -eq 1 ]
then
    java bitunpacker $1 | java decoder 

else
    echo $"Please enter the name of a file to decode."
fi
