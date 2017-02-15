#!/usr/bin/python

import numpy as np
from scipy import misc

matrix = misc.imread('../heightmap1.jpg',flatten=True)
x,y = matrix.shape
matrix = misc.imresize(matrix,(x/5,y/5))
x,y = matrix.shape

mesh = ""
for i in range(0,x):
    for j in range(0,y):
        mesh += "%d\t%d\t%d\n" % (i,j,matrix[i,j])
    mesh += "\n"

print mesh
