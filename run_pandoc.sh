#!/bin/sh
pandoc Partie1.tex -o Partie1.odt --bibliography=library.bib --csl=/home/srey/Copy/these/manuscrit/chicago-author-date.csl
pandoc Partie0.tex -o Partie0.odt --bibliography=library.bib --csl=/home/srey/Copy/these/manuscrit/chicago-author-date.csl


