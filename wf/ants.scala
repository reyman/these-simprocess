val gPopulation = Val[Double]
val gDiffusionRate = Val[Double]
val gEvaporationRate = Val[Double]
val seed = Val[Int]

val i = Val[Double]

val resPath = "/iscpif/users/rey/ANTS/RESULTS/"

// Define the output variables
val food1 = Val[Double]
val food2 = Val[Double]
val food3 = Val[Double]

// Define the NetlogoTask
val cmds = Seq("random-seed ${seed}", "run-to-grid")
val ants =
  NetLogo5Task("/iscpif/users/rey/ANTS/ants.nlogo", cmds) set (
    // Map the OpenMOLE variables to NetLogo variables
    netLogoInputs += (gPopulation, "gpopulation"),
    netLogoInputs += (gDiffusionRate, "gdiffusion-rate"),
    netLogoInputs += (gEvaporationRate, "gevaporation-rate"),
    netLogoOutputs += ("final-ticks-food1", food1),
    netLogoOutputs += ("final-ticks-food2", food2),
    netLogoOutputs += ("final-ticks-food3", food3),
    // The seed is used to control the initialisation of the random number generator of NetLogo
    inputs += seed,
    outputs += seed,
    // Define default values for inputs of the model
    seed := 42,
    gPopulation := 125.0,
    gDiffusionRate := 25.0,
    gEvaporationRate := 25.0
  )

val modelCapsule = Capsule(ants)

// Define the output variables
val medNumberFood1 = Val[Double]
val medNumberFood2 = Val[Double]
val medNumberFood3 = Val[Double]

// We compute three median
val statistic =
  StatisticTask() set (
    inputs += i,
    outputs += i,
    inputs += gPopulation,
    inputs += gDiffusionRate,
    inputs += gEvaporationRate,
    outputs += gPopulation,
    outputs += gDiffusionRate,
    outputs += gEvaporationRate,
    statistics += (food1, medNumberFood1, median),
    statistics += (food2, medNumberFood2, median),
    statistics += (food3, medNumberFood3, median)
  )

val exploration = Capsule(ExplorationTask(i in (0.0 to 5.0 by 1.0)))

//val statSlot = Capsule(statistic)
val statSlot = Slot(Capsule(statistic))
exploration -- statSlot

val env = LocalEnvironment(10)

val seedFactor = seed in (UniformDistribution[Int]() take 2)
val replicateModel = Replicate(modelCapsule, seedFactor, statSlot)

// Define the hooks to collect the results
val displayOutputs = ToStringHook(i, seed, food1, food2, food3)
val displayMedians = ToStringHook(medNumberFood1, medNumberFood2, medNumberFood3)
val saveHook = AppendToCSVFileHook(resPath + "replication.csv", i, gPopulation, gDiffusionRate, gEvaporationRate, medNumberFood1, medNumberFood2,medNumberFood3)

// Execute the workflow
val ex = exploration -< (replicateModel  + (modelCapsule on env hook displayOutputs) + (statSlot hook (displayMedians, saveHook)) ) start

