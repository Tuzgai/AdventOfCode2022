import java.io.File

fun day16ASolution(): Int {

    val valves = File("src/main/resources/Day16Data.txt").readText()
        .replace("Valve ", "")
        .replace("has flow rate=", "")
        .replace("; tunnels lead to valves ", " ")
        .replace("; tunnel leads to valve ", " ")
        .replace(",", "")
        .split("\n")
        .map {
            val tokens = it.split(" ")
            Valve(tokens[0], tokens[1].toInt(), tokens.subList(2, tokens.size))
        }

    val nameIndex = mutableMapOf<String, String>()

    // Make sure all our nodes have numeric names for graph indexing
    valves.forEachIndexed { i, it ->
        nameIndex[it.name] = i.toString()
        nameIndex[i.toString()] = it.name
    }
    val intValves = valves.map {
        Valve(
            nameIndex[it.name].toString(),
            it.rate,
            it.connections.map { s -> nameIndex[s].toString() })
    }

    val graph = Graph(valves.size)

    // Populate graph
    intValves.forEach {
        graph.addEdge(it.name.toInt(), it.name.toInt(), 1)
        it.connections.forEach { s ->
            graph.addEdge(it.name.toInt(), s.toInt(), 1)
        }
    }

    val valveToConnectionsByDistance = mutableMapOf<String, MutableList<ValveDistance>>()
    valves.forEach { valveToConnectionsByDistance[it.name] = mutableListOf() }

    // Find the shortest route from each valve to each other valve
    intValves.forEach { s ->
        intValves.forEach { t ->
            graph.visited.clear()
            val dists = djikstra(graph, s.name.toInt())
            valveToConnectionsByDistance[nameIndex[s.name]]!!.add(ValveDistance(nameIndex[t.name]!!, dists[t.name.toInt()]!!))
        }
    }

    val deadNodes = valves.filter { it.rate == 0 }.map { it.name }.toSet()

    val valveToConnectionsByDistanceMap = valveToConnectionsByDistance.filter {
        !deadNodes.contains(it.key) || it.key == "AA"
    }.map {
        (it.key to it.value.filter { t ->
            !deadNodes.contains(t.name) || it.key == "AA"
        }.map { t -> t.name to t.distance }.toMap())
    }.toMap()

    val flowMap = mutableMapOf<String, Int>()
    valves.forEach { flowMap[it.name] = it.rate }

    return findHighestScoringRoute(
        "AA",
        "AA",
        0,
        0,
        0,
        valveToConnectionsByDistanceMap,
        valveToConnectionsByDistanceMap.keys.filter { it != "AA" },
        flowMap
    ).second
}

private fun findHighestScoringRoute(
    prevPosition: String,
    position: String,
    prevMinute: Int,
    prevFlow: Int,
    score: Int,
    valveToConnectionsByDistance: Map<String, Map<String, Int>>,
    valvesRemaining: List<String>,
    flowMap: Map<String, Int>
): Pair<String, Int> {
    val duration = valveToConnectionsByDistance[prevPosition]!![position]!! + 1
    val minute = prevMinute + duration
    val updatedFlow = prevFlow + flowMap[position]!!
    val updatedScore = score + duration * prevFlow

    if (valvesRemaining.size == 1 || valveToConnectionsByDistance[position]!!.entries.none { it.value + 1 < (30 - minute) }) {
        val finalScore = updatedScore + updatedFlow * (30 - minute + 1)
        return Pair("Distance moved: ${duration - 1} New position: $position Score: $finalScore Flow: $updatedFlow Minute: $minute", finalScore)
    }

    return valvesRemaining.filter { it != position }
        .map {
            val route =
                findHighestScoringRoute(
                    position,
                    it,
                    minute,
                    updatedFlow,
                    updatedScore,
                    valveToConnectionsByDistance,
                    valvesRemaining.filter { s ->
                        s != position
                    },
                    flowMap
                )
            Pair(
                "Distance moved: ${duration - 1} New position: $position Score: $updatedScore Flow: $updatedFlow Minute: $minute\n ${route.first}",
                route.second
            )
        }.maxByOrNull { it.second }!!
}

fun day16BSolution(): Int {
    val input = File("src/main/resources/Day16Test.txt").readText()

    return 0
}

data class Valve(val name: String, val rate: Int, val connections: List<String>)

data class ValveDistance(val name: String, val distance: Int)

