setwd("/home/srey/TRAVAUX/THESE/REPOSITORY_GIT/simprocess-these/experiences/ANTS/stochasticite")
repli <- read.csv2("medNumberFood3_repli.csv")
colnames(repli) <- sub(pattern="X", replacement="", x=colnames(repli))

boxplot(repli,data=repli, main="Variations de la médiane sur l'objectif food1 de 10 séries de réplications",
        xlab="Nombre de réplications par série", ylab="Objectif medianFood3") 


setwd("/home/srey/insync_reyman64/PROJETS/GEOCITES/EXPERIENCE/exploration_scattrplot_ants")
sc <- read.csv2("selection_by_evapo_C_1.csv",sep = ";")
ggpairs(data=sc,c(3,4,5,6,7))

data(tips, package = "reshape")
pMat <- ggpairs(tips, c(1,3,2), color = "sex")
pMat
