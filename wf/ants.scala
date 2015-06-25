val gPopulation = Val[Double]
val gDiffusionRate = Val[Double]
val gEvaporationRate = Val[Double]
val seed = Val[Int]

val i = Val[Double]

//val resPath = "/iscpif/users/rey/ANTS/RESULTS/"
val resPath = "/home/srey/results/"

// Define the output variables
val food1 = Val[Double]
val food2 = Val[Double]
val food3 = Val[Double]

// Define the NetlogoTask
val cmds = Seq("random-seed ${seed}", "run-to-grid")
val ants =
  NetLogo5Task("/home/srey/netlogo/ants/ants.nlogo", cmds) set (
    // Map the OpenMOLE variables to NetLogo variables
    netLogoInputs += (gPopulation, "gpopulation"),
    netLogoInputs += (gDiffusionRate, "gdiffusion-rate"),
    netLogoInputs += (gEvaporationRate, "gevaporation-rate"),
    netLogoOutputs += ("final-ticks-food1", food1),
    netLogoOutputs += ("final-ticks-food2", food2),
    netLogoOutputs += ("final-ticks-food3", food3),
    // The seed is used to control the initialisation of the random number generator of NetLogo
    inputs += seed,
    outputs += seed
  )

// Define the output variables
val medNumberFood1 = Val[Double]
val medNumberFood2 = Val[Double]
val medNumberFood3 = Val[Double]

// We compute three median
val agg =
  Capsule(StatisticTask() set (
    inputs += i,
    outputs += i,
    statistics += (food1, medNumberFood1, median),
    statistics += (food2, medNumberFood2, median),
    statistics += (food3, medNumberFood3, median)
  ), strainer = true)

val exploration = Capsule(
    ExplorationTask(i in (0.0 to 10.0 by 1.0)) set (
        gPopulation := 125.0,
        gDiffusionRate:= 25.0,
        gEvaporationRate := 25.0,
        outputs += (gPopulation,gDiffusionRate,gEvaporationRate))
    )

val env = LocalEnvironment(15)

val seedFactor = seed in (UniformDistribution[Int]() take 10)

// val replicateModel = Replicate(modelCapsule, seedFactor, statSlot)

val replication = Capsule(ExplorationTask(seed in (UniformDistribution[Int]() take 1000)), strainer = true)

val aggSlot = Slot(agg)

// Define the hooks to collect the results
val displayOutputs = ToStringHook(i, seed, food1, food2, food3)
val displayMedians = ToStringHook(medNumberFood1, medNumberFood2, medNumberFood3)
val saveHook = AppendToCSVFileHook(resPath + "replication.csv", i, gPopulation, gDiffusionRate, gEvaporationRate, medNumberFood1, medNumberFood2,medNumberFood3)

// Execute the workflow
//val ex = (exploration -< replicateModel -< modelCapsule on env hook displayOutputs >- (statSlot hook (displayMedians, saveHook))) + (replicateModel -- statSlot) start
val ex = (exploration -< replication  -< (ants on env) >- (aggSlot hook (displayMedians, saveHook))) + (replication -- aggSlot) start

-----

val i = Val[Double]
val seed = Val[Int]
val res = Val[Double]
val s = Val[Double]

val exploration = Capsule(ExplorationTask(i in (0.0 to 9.0 by 1.0)))

val model =
 ScalaTask("val res = i * 2") set (
   inputs += i,
   outputs += res
 )

val replication = Capsule(ExplorationTask(seed in (UniformDistribution[Int]() take 10)), strainer = true)

val agg =
 StatisticTask() set (
   inputs += i,
   outputs += i,
   statistics += (res, s, median)
 )

val aggSlot = Slot(agg)

val ex = (exploration < replication -< model > (aggSlot hook ToStringHook(i, s))) + (replication -- aggSlot) start

