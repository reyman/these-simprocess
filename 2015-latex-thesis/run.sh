#!/bin/sh
xelatex -shell-escape These.tex
biber These
xelatex -shell-escape These.tex
