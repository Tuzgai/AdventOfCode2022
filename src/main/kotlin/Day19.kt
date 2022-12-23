import java.io.File

fun day19ASolution(): Int {
    val factories = File("src/main/resources/Day19Data.txt").readLines()
        .map { it.replace(".", " ") }
        .map {
            val values = it.split(" ")
            Factory(
                RobotRecipe(values[6].toInt(), type = RobotType.ORE),
                RobotRecipe(values[13].toInt(), type = RobotType.CLAY),
                RobotRecipe(values[20].toInt(), values[23].toInt(), type = RobotType.OBSIDIAN),
                RobotRecipe(values[30].toInt(), 0, values[33].toInt(), type = RobotType.GEODE)
            )
        }

    return factories.mapIndexed() { i, it -> (i + 1) * maximumGeodeScore(it, 24) }.sum()
}

fun day19BSolution(): Int {
    val factories = File("src/main/resources/Day19Data.txt").readLines()
        .map { it.replace(".", " ") }
        .map {
            val values = it.split(" ")
            Factory(
                RobotRecipe(values[6].toInt(), type = RobotType.ORE),
                RobotRecipe(values[13].toInt(), type = RobotType.CLAY),
                RobotRecipe(values[20].toInt(), values[23].toInt(), type = RobotType.OBSIDIAN),
                RobotRecipe(values[30].toInt(), 0, values[33].toInt(), type = RobotType.GEODE)
            )
        }

    return factories.take(3)
        .map { maximumGeodeScore(it, 32) }
        .foldRight(1) { l, r -> l * r }
}

fun maximumGeodeScore(start: Factory, turns: Int): Int {
    val buildPaths = ArrayDeque(listOf(start))

    var result = 0

    var factory = buildPaths.last()
    while (buildPaths.isNotEmpty()) {
        val buildQueue = mutableListOf<RobotType>()

        if (factory.canBuyRobot(factory.oreRobotRecipe)
            && (factory.clayRobotRecipe.oreCost > factory.oreRobots
                    || factory.obsidianRobotRecipe.oreCost > factory.oreRobots
                    || factory.geodeRobotRecipe.oreCost > factory.oreRobots)
        ) {
            if (factory.buyNextOre) {
                val newFactory = factory.copy()
                newFactory.buyNextOre = false
                buildPaths.addLast(newFactory)

                factory.buyRobot(factory.oreRobotRecipe)
                buildQueue.add(RobotType.ORE)

                factory.buyNextClay = true
                factory.buyNextObsidian = true
                factory.buyNextGeode = true
            }
        }

        if (factory.canBuyRobot(factory.clayRobotRecipe) && buildQueue.isEmpty() && factory.obsidianRobotRecipe.clayCost > factory.clayRobots) {
            if (factory.buyNextClay) {
                val newFactory = factory.copy()
                newFactory.buyNextClay = false
                buildPaths.addLast(newFactory)

                factory.buyRobot(factory.clayRobotRecipe)
                buildQueue.add(RobotType.CLAY)

                factory.buyNextOre = true
                factory.buyNextObsidian = true
                factory.buyNextGeode = true
            }
        }

        if (factory.canBuyRobot(factory.obsidianRobotRecipe) && buildQueue.isEmpty() && factory.geodeRobotRecipe.obsidianCost > factory.obsidianRobots) {
            if (factory.buyNextObsidian) {
                val newFactory = factory.copy()
                newFactory.buyNextObsidian = false
                buildPaths.addLast(newFactory)

                factory.buyRobot(factory.obsidianRobotRecipe)
                buildQueue.add(RobotType.OBSIDIAN)

                factory.buyNextOre = true
                factory.buyNextClay = true
                factory.buyNextGeode = true
            }
        }

        if (factory.canBuyRobot(factory.geodeRobotRecipe) && buildQueue.isEmpty()) {
            if (factory.buyNextGeode) {
                val newFactory = factory.copy()
                newFactory.buyNextGeode = false
                buildPaths.addLast(newFactory)

                factory.buyRobot(factory.geodeRobotRecipe)
                buildQueue.add(RobotType.GEODE)

                factory.buyNextOre = true
                factory.buyNextClay = true
                factory.buyNextObsidian = true
            }
        }

        factory.tick(buildQueue)

        if (factory.minute == turns) {
            result = maxOf(result, factory.geodes)
            factory = buildPaths.removeLast()
        }
    }

    return result
}

data class Factory(
    val oreRobotRecipe: RobotRecipe,
    val clayRobotRecipe: RobotRecipe,
    val obsidianRobotRecipe: RobotRecipe,
    val geodeRobotRecipe: RobotRecipe,
    var oreRobots: Int = 1,
    var clayRobots: Int = 0,
    var obsidianRobots: Int = 0,
    var geodeRobots: Int = 0,
    var minute: Int = 0,
    var ore: Int = 0,
    var clay: Int = 0,
    var obsidian: Int = 0,
    var geodes: Int = 0,
    var buyNextOre: Boolean = true,
    var buyNextClay: Boolean = true,
    var buyNextObsidian: Boolean = true,
    var buyNextGeode: Boolean = true
) {
    fun tick(typesToBuild: List<RobotType>) {
        minute++
        ore += oreRobots
        clay += clayRobots
        obsidian += obsidianRobots
        geodes += geodeRobots

        typesToBuild.forEach {
            when (it) {
                RobotType.ORE -> oreRobots++
                RobotType.CLAY -> clayRobots++
                RobotType.OBSIDIAN -> obsidianRobots++
                RobotType.GEODE -> geodeRobots++
            }
        }
    }

    fun canBuyRobot(recipe: RobotRecipe): Boolean {
        return recipe.oreCost <= ore && recipe.clayCost <= clay && recipe.obsidianCost <= obsidian
    }

    fun buyRobot(recipe: RobotRecipe) {
        ore -= recipe.oreCost
        clay -= recipe.clayCost
        obsidian -= recipe.obsidianCost
    }
}

data class RobotRecipe(val oreCost: Int, val clayCost: Int = 0, val obsidianCost: Int = 0, val type: RobotType)

enum class RobotType {
    ORE,
    CLAY,
    OBSIDIAN,
    GEODE
}