library("scatterplot3d")
options(width=300)
#clean workspace
rm(list=ls(all=TRUE))

data = data.frame(
  x = rep( c(0.05, 0.1, 0.15, 0.2, 0.25,0.3, 0.35, 0.4, 0.45, 0.5, 0.55, 0.6,0.65,0.7, 0.75, 0.8 , 0.85, 0.9, 0.95)),
  y = rep(  c(0.05, 0.1, 0.15, 0.2, 0.25,0.3, 0.35, 0.4, 0.45, 0.5, 0.55, 0.6,0.65,0.7, 0.75, 0.8 ,0.85, 0.9, 0.95)),
  z = rep(  c(0.05, 0.1, 0.15, 0.2, 0.25,0.3, 0.35, 0.4, 0.45, 0.5, 0.55, 0.6,0.65,0.7, 0.75, 0.8 ,0.85, 0.9, 0.95))          
)

data2d = expand.grid(x = c(0,0.1, 0.2, 0.3,  0.4, 0.5, 0.6, 0.7, 0.8, 0.9,1), y =c(0,0.1, 0.2, 0.3,  0.4, 0.5, 0.6, 0.7, 0.8, 0.9,1))

data3d = expand.grid(x = c(0.1, 0.2, 0.3,  0.4, 0.5, 0.6, 0.7, 0.8, 0.9), y = c(0.1, 0.2, 0.3,  0.4, 0.5, 0.6, 0.7, 0.8, 0.9),  z = c(0.1, 0.2, 0.3,  0.4, 0.5, 0.6, 0.7, 0.8, 0.9))

plot(x = data2d$x, y = data2d$y, xlim=c(0.0,1.0),ylim=c(0.0,1.0),xlab="probability of innovation diffusion",ylab="probability of innovation creation", size =2 )

scatterplot3d(x = data3d$x, y = data3d$y, z = data3d$z, xlim=c(0.0,1.0),ylim=c(0.0,1.0),zlim=c(0.0,1.0),xlab="probability of innovation diffusion",ylab="probability of innovation creation", zlab= "innovation impact")

library(rgl)
persp3d(data3d$x, data3d$y, data3d$z, col="skyblue")
rgl.points(data3d$x, data3d$y, data3d$z, color = "blue", size = 5)
plot3d(data3d$x, data3d$y, data3d$z,  type ="p", xlim=c(0.0,1.0),ylim=c(0.0,1.0),zlim=c(0.0,1.0),col ="lightgray", size = 4)


rgl.postscript( "/home/srey/TRAVAUX/THESE/REPOSITORY_GIT/simprocess-these/Schema_originaux/scriptR/cursedimensionality/plot3d.svg", fmt = "svg", drawText = FALSE )
