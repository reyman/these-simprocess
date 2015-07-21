#!/bin/bash
for i in {1..2000}
do
   gnuplot -e "set terminal jpeg enhanced  font \"arial,8\"; set multiplot layout 3,2 title \"multiplot\" ; set title \"view1\"; 

set title \"Evaporation - Diffusion\"; unset key; set xlabel 'Evaporation'; set ylabel 'Diffusion';set datafile separator ','; set xrange [ 0 : 100 ] noreverse nowriteback ; set yrange [ 0 : 100 ] noreverse nowriteback; set datafile separator ','; plot 'population$i.csv' using (column(\"gDiffusionRate\")):(column(\"gEvaporationRate\"));

    set title \"Food1 - Food2\"; unset key; set xlabel 'Food1'; set ylabel 'Food2';set datafile separator ','; set xrange [ 100 : 800 ] noreverse nowriteback ; set yrange [ 100 : 1300 ] noreverse nowriteback; set datafile separator ','; plot 'population$i.csv' using (column(\"medNumberFood1\")):(column(\"medNumberFood2\")); set title \"Food 1 - Food 3\"; unset key; set xlabel 'Food1'; set ylabel 'Food3';set datafile separator ','; set xrange [ 100 : 800 ] noreverse nowriteback ; set yrange [ 300 : 2000 ] noreverse nowriteback; set datafile separator ','; plot 'population$i.csv' using (column(\"medNumberFood1\")):(column(\"medNumberFood3\")); ; set title \"Food 2 - Food 3\"; unset key; set xlabel 'Food2'; set ylabel 'Food3';set datafile separator ','; set xrange [ 100 : 1300 ] noreverse nowriteback ; set yrange [ 300 : 2000 ] noreverse nowriteback; set datafile separator ','; plot 'population$i.csv' using (column(\"medNumberFood2\")):(column(\"medNumberFood3\"));  set title \"zoom Food 1 - Food 3\"; unset key; set xlabel 'Food1'; set ylabel 'Food3';set datafile separator ','; set xrange [ 150 : 300 ] noreverse nowriteback ; set yrange [ 300 : 1500 ] noreverse nowriteback; set datafile separator ','; plot 'population$i.csv' using (column(\"medNumberFood1\")):(column(\"medNumberFood3\")); set title \" zoom Food 2 - Food 3\"; unset key; set xlabel 'Food2'; set ylabel 'Food3';set datafile separator ','; set xrange [ 250 : 600 ] noreverse nowriteback ; set yrange [ 300 : 1500 ] noreverse nowriteback; set datafile separator ','; plot 'population$i.csv' using (column(\"medNumberFood2\")):(column(\"medNumberFood3\"));unset multiplot" > pic$i.jpeg
done

avconv  -i pic%d.jpeg -r 50 video.webm


#set title \"3D view\"; unset key; set view 70,40 ; set xlabel 'Food1'; set ylabel 'Food2'; set zlabel 'Food3' ;set datafile separator ','; set xrange [ 100 : 800 ] noreverse nowriteback ; set yrange [ 100 : 1300 ] noreverse nowriteback; set zrange [ 300 : 2000 ] noreverse nowriteback;  splot 'population$i.csv' using (column(\"medNumberFood1\")):(column(\"medNumberFood2\")):(column(\"medNumberFood3\")) ;

# set xtics rotate; set multiplot layout 1,2 title "multiplot" ; set title "view1"; unset key; set view 60,0 ; set xlabel 'Food1'; set ylabel 'Food2'; set zlabel 'Food3' ;set datafile separator ',';  set xrange [ 100 : 800 ] noreverse nowriteback ; set yrange [ 100 : 1300 ] noreverse nowriteback; set zrange [ 300 : 2000 ] noreverse nowriteback; splot 'population5.csv' using (column("medNumberFood1")):(column("medNumberFood2")):(column("medNumberFood3")) ; set title "view2"; unset key; set view 0,0 ;set xlabel 'Food1'; set ylabel 'Food2'; set zlabel 'Food3' ;set datafile separator ',';  splot 'population5.csv' using (column("medNumberFood1")):(column("medNumberFood2")):(column("medNumberFood3")) ; unset multiplot

#gnuplot -e "set terminal jpeg enhanced size 300,300; set xtics rotate; set multiplot layout 1,3 title \"multiplot\" ; set title \"view1\"; unset key; set view 70,320 ; set xlabel  offset character -3, -2, 0; set xlabel 'Food1'; set ylabel 'Food2'; set zlabel 'Food3' ;set datafile separator ','; set xrange [ 100 : 800 ] noreverse nowriteback ; set yrange [ 100 : 1300 ] noreverse nowriteback; set zrange [ 300 : 2000 ] noreverse nowriteback;  splot 'population$i.csv' using (column(\"medNumberFood1\")):(column(\"medNumberFood2\")):(column(\"medNumberFood3\")) ; set title \"view2\"; unset key; set view 70,40 ; set xlabel 'Food1'; set ylabel 'Food2'; set zlabel 'Food3' ;set datafile separator ','; set xrange [ 100 : 800 ] noreverse nowriteback ; set yrange [ 100 : 1300 ] noreverse nowriteback; set zrange [ 300 : 2000 ] noreverse nowriteback;  splot 'population$i.csv' using (column(\"medNumberFood1\")):(column(\"medNumberFood2\")):(column(\"medNumberFood3\")) ; set title \"view3\"; unset key; set view 0,0 ;set xlabel 'Food1'; set ylabel 'Food2'; set zlabel 'Food3' ; ;set datafile separator ','; set xrange [ 100 : 800 ] noreverse nowriteback ; set yrange [ 100 : 1300 ] noreverse nowriteback; set zrange [ 300 : 2000 ] noreverse nowriteback; set datafile separator ',';  splot 'population$i.csv' using (column(\"medNumberFood1\")):(column(\"medNumberFood2\")):(column(\"medNumberFood3\")) ; unset multiplot" > pic$i.jpeg