// 100 replications
// mu = 100,
// termination = 2000,
// lambda (offsprings) = 15 
// population fixed 125.0
// no replicate

// Define the input variables
val gPopulation = Val[Double]
val gDiffusionRate = Val[Double]
val gEvaporationRate = Val[Double]
val seed = Val[Int]

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
    gPopulation := 125.0,
    inputs += seed,
    outputs += seed
  )

// Define the hooks to collect the results
val displayHook = ToStringHook(food1, food2, food3)

val modelCapsule = Capsule(ants)

// Define the output variables
val medNumberFood1 = Val[Double]
val medNumberFood2 = Val[Double]
val medNumberFood3 = Val[Double]

// We compute three median
val statistic =
  StatisticTask() set (
    statistics += (food1, medNumberFood1, median),
    statistics += (food2, medNumberFood2, median),
    statistics += (food3, medNumberFood3, median)
  )

val statisticCapsule = Capsule(statistic)

val seedFactor = seed in (UniformDistribution[Int]() take 100)
val replicateModel = Replicate(modelCapsule, seedFactor, statisticCapsule)

// Define the hooks to collect the results
val displayOutputs = ToStringHook(food1, food2, food3)
val displayMedians = ToStringHook(medNumberFood1, medNumberFood2, medNumberFood3)

val evolution =
  NSGA2(
    mu = 100,
    termination = 2000,
    inputs = Seq(gDiffusionRate -> (0.0, 99.0), gEvaporationRate -> (0.0, 99.0)),
    objectives = Seq(medNumberFood1, medNumberFood2, medNumberFood3)
  )

// Define a builder to use NSGA2 generational EA algorithm.
// replicateModel is the fitness function to optimise.
// lambda is the size of the offspring (and the parallelism level).
val (puzzle, nsga2) = SteadyGA(evolution)(
   replicateModel,
   lambda = 15
 )

val env = LocalEnvironment(20)

// Define a hook to save the Pareto frontier
val savePopulationHook = SavePopulationHook(nsga2, "/iscpif/users/rey/ANTS/RESULTS/")

val ex = (puzzle hook savePopulationHook) + (modelCapsule on env) start