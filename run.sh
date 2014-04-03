#!/bin/sh
ln -s /home/srey/Copy/these/bib/A_INPHD.bib ./Library/library.bib
xelatex These.tex
biber These
xelatex These.tex
