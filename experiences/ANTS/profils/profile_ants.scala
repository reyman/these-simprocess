
val gPopulation = Val[Double]
val gDiffusionRate = Val[Double]
val gEvaporationRate = Val[Double]
val seed = Val[Int]


//val resPath = "/iscpif/users/rey/ANTS/RESULTS/"
val resPath = "/home/srey/results/"

// Define the output variables
val food1 = Val[Double]
val food2 = Val[Double]
val food3 = Val[Double]

// Define the output variables
val medNumberFood1 = Val[Double]
val medNumberFood2 = Val[Double]
val medNumberFood3 = Val[Double]

// NetLogo5Task("/home/srey/netlogo/ants/ants.nlogo", cmds) set (

// Define the NetlogoTask
val cmds = Seq("random-seed ${seed}", "run-to-grid")

val domains = 
  Seq(
    gDiffusionRate -> (0.0, 1.0),
    gEvaporationRate -> (0.0, 1.0)
  )

val toCompute = 
  Seq(
    ("gDiffusionRate", 0, domains),
    ("gEvaporationRate", 1, domains))


def build(name: String, number: Int, scales: Seq[(Prototype[Double], (Double, Double))]) = {

  val path = resPath + s"profile_$name/"

  // Define the task which runs the model
  val modelTask =  NetLogo5Task("/home/srey/netlogo/ants/ants.nlogo", cmds) set (
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

  modelTask.addInput(gDiffusionRate)
  modelTask.addInput(gEvaporationRate)
  modelTask.addOutput(food1)
  modelTask.addOutput(food2)
  modelTask.addOutput(food3)

 val stat = StatisticsTask() set (    
    statistics += (food1, medNumberFood1, median),
    statistics += (food2, medNumberFood2, median),
    statistics += (food3, medNumberFood3, median))

  val modelCapsule = Capsule(modelTask)

  val seedFactor = seed in (UniformDistribution[Int]() take 100)
  val replicateModel = Replicate( modelCapsule, seedFactor, stat)

    import org.openmole.plugin.method.evolution._
  import ga._

  val profile = 
    GenomeProfile(
      x = number,
      nX = 1000,
      termination = Timed(2 hours),
      inputs = scales,
      objectives = Seq(medNumberFood1, medNumberFood2, medNumberFood3),
      cloneProbability = 0.01
    )

  val env = LocalEnvironment(5)

  val islandModel = islandSteadyGA(profile, replicateModel)("island", 1000, Counter(100000), 500)
  val savePopulationHook = SavePopulationHook(islandModel, s"./populations/$name") condition hookCondition
  val saveProfile = SaveProfileHook(islandModel, s"./profiles/$name") condition hookCondition
  val display = DisplayHook("generation ${" + islandModel.generation.name + "} for " + mame)

  islandModel + (islandModel.island on env) + (islandModel.output hook profileHook hook display hook saveParetoHook)
}

val executions = toCompute.map{ case(name, n, s) => build(name, n, s) }

executions.foreach(_.start)


