#!/bin/sh
ln -s /home/srey/Copy/these/bib/A_INPHD.bib library.bib
xelatex These.tex
biber These
xelatex These.tex
