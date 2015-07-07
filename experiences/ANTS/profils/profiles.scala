import org.openmole.plugin.domain.distribution._
import org.openmole.plugin.domain.modifier._
import org.openmole.plugin.task.groovy._
import org.openmole.plugin.hook.file._
import org.openmole.plugin.hook.display._
import org.openmole.plugin.builder.stochastic._
import org.openmole.plugin.grouping.batch._
import org.openmole.plugin.environment.glite._
import org.openmole.plugin.environment.desktopgrid._
import org.openmole.plugin.hook.modifier._

import fr.geocite.simpoplocal.exploration._

val basePath = "./profiles/"
val env = GliteEnvironment("biomed", openMOLEMemory = 1400, wallTime = 4 hours)
//val env = LocalEnvironment()

val seed = Prototype[Long]("seed")

val populationRate = Prototype[Double]("populationRate")
val rMax = Prototype[Double]("rMax")
val distanceDecay = Prototype[Double]("distanceDecay")
val pCreation = Prototype[Double]("pCreation")
val pDiffusion = Prototype[Double]("pDiffusion")
val disasterProbability = Prototype[Double]("disasterProbability")
val innovationImpact = Prototype[Double]("innovationImpact")
val innovationLife = Prototype[Double]("innovationLife")
val modelResult = Prototype[ModelResult]("modelResult")

val deltaTime = Prototype[Double]("deltaTime")
val deltaPop = Prototype[Double]("deltaPop")
val ksValue = Prototype[Double]("ksValue")

val sumKsFailValue = Prototype[Double]("sumKsFailValue")
val medPop = Prototype[Double]("medPop")
val medTime = Prototype[Double]("medTime")

val medADDeltaPop = Prototype[Double]("medADDeltaPop")
val medADDeltaTime = Prototype[Double]("medADDeltaTime")

val domains = 
  Seq(
    rMax -> (1.0, 80000.0),
    distanceDecay -> (0.0, 4.0),
    pCreation -> (0.0 -> 0.01),
    pDiffusion -> (0.0, 0.01),
    innovationImpact -> (0.0, 2.0)
    //innovationLife -> (0.0, 4000.0)
  )

val toCompute = 
  Seq(
    ("rMax", 0, domains),
    ("distanceDecay", 1, domains),
    ("pCreation", 2, domains),
    ("pDiffusion", 3, domains),
    ("innovationImpact", 4, domains),
    //("innovationLife", 5, domains),
    ("pCreation_zoomed", 2, domains.updated(2, pCreation -> (0.0 -> 1e-5))),
    ("pDiffusion_zoomed", 3, domains.updated(3, pDiffusion -> (0.0 -> 1e-5))),
    ("innovationImpact_zoomed", 4, domains.updated(4, innovationImpact -> (0.0 -> 2e-2)))
  )

def build(name: String, number: Int, scales: Seq[(Prototype[Double], (Double, Double))]) = {

  val path = basePath + s"profile_$name/"

  // Define the task which runs the model
  val modelTask = 
    GroovyTask(
      "modelTask", "modelResult = Model.run(rMax, innovationImpact, distanceDecay, pCreation, pDiffusion, 10000, (int) innovationLife, newRNG(seed)) \n")

  modelTask.addImport("fr.geocite.simpoplocal.exploration.*")
  
  modelTask.addInput(rMax)
  modelTask.addInput(distanceDecay)
  modelTask.addInput(seed)
  modelTask.addInput(pCreation)
  modelTask.addInput(pDiffusion)
  modelTask.addInput(innovationImpact)
  modelTask.addInput(innovationLife)
  modelTask.addOutput(modelResult)
  
  modelTask.addParameter(innovationLife -> 4000.0)

  // Define the task which evaluate a single replication
  val evalTask = 
    GroovyTask("EvalTask",
      "ksValue =  new Double(LogNormalKSTest.test(modelResult.population).count(false)) \n" + 
      "deltaPop = DeltaTest.delta(modelResult.population, 10000) \n" + 
      "deltaTime = DeltaTest.delta(modelResult.time, 4000) \n"
    )

  evalTask.addImport("fr.geocite.simpoplocal.exploration.*")
  evalTask.addImport("org.apache.commons.math.random.*")
  evalTask.addImport("umontreal.iro.lecuyer.probdist.*")

  evalTask.addInput(modelResult)
  evalTask.addOutput(ksValue)
  evalTask.addOutput(deltaPop)
  evalTask.addOutput(deltaTime)
    
  val modelCapsule = Capsule(modelTask)
  val evalCapsule = Capsule(evalTask) 

  val eval = modelCapsule -- evalCapsule

  val stat = StatisticsTask("stat")
  stat add (ksValue, sumKsFailValueg, sum)
  stat add (deltaPop, medPop, median)
  stat add (deltaTime, medTime, median)

  val seedFactor = Factor(seed, UniformLongDistribution() take 100)
  val replicateModel = Replicate("replicateModel", eval, seedFactor, stat)

  val relativizeTask = GroovyTask(
    "relativize",
    "sumKsFailValue = sumKsFailValue / 200 \n" +
    "medPop = medPop / 10000 \n" +
    "medTime = medTime / 4000 \n")

  relativizeTask addInput sumKsFailValue
  relativizeTask addInput medPop
  relativizeTask addInput medTime

  relativizeTask addOutput sumKsFailValue
  relativizeTask addOutput medPop
  relativizeTask addOutput medTime

  import org.openmole.plugin.method.evolution._
  import ga._

  val profile = 
    GenomeProfile(
      x = number,
      nX = 1000,
      termination = Timed(2 hours),
      inputs = scales,
      objectives = Seq(sumKsFailValue, medPop, medTime),
      cloneProbability = 0.01
    )

  val islandModel = islandSteadyGA(profile, replicateModel -- relativize)("island", 1000, Counter(100000), 500)
  val savePopulationHook = SavePopulationHook(islandModel, s"./populations/$name") condition hookCondition
  val saveProfile = SaveProfileHook(islandModel, s"./profiles/$name") condition hookCondition
  val display = DisplayHook("generation ${" + islandModel.generation.name + "} for " + mame)

  islandModel + (islandModel.island on env) + (islandModel.output hook profileHook hook display hook saveParetoHook)
}

val executions = toCompute.map{ case(name, n, s) => build(name, n, s) }

executions.foreach(_.start)



