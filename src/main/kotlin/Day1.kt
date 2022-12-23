import java.io.File
import java.lang.Integer.max
import java.lang.Integer.parseInt

fun day1ASolution(): Int {
    return getInput()
        .map { it.sum() }
        .maxOf { it }
}

fun day1BSolution(): Int {
    return getInput()
        .map { it.sum() }
        .sorted()
        .takeLast(3)
        .sum()
}

fun getInput(): List<List<Int>> {
    return File("src/main/resources/Day1Data.txt").readText()
        .split("\n\n")
        .map {
            it.split("\n")
                .map { s -> parseInt(s) }
        }
}

fun day1ASolutionOld(): Int {
    val blocks = Utils.readInput("src/main/resources/Day1Data.txt")
    val loads = Utils.blocksToLists(blocks).map { it.map { s -> parseInt(s) } }

    return maxLoad(loads)
}

fun day1BSolutionOld(): Int {
    val blocks = Utils.readInput("src/main/resources/Day1Data.txt")
    val loads = Utils.blocksToLists(blocks).map { it.map { s -> parseInt(s) } }

    return topNLoads(loads, 3)
}

fun maxLoad(loads: List<List<Int>>): Int {
    return loads.map { load -> load.sum() }
        .foldRight(0) { first, second -> max(first, second) }
}

fun topNLoads(loads: List<List<Int>>, topN: Int): Int {
    return loads.map { load -> load.sum() }
        .sorted()
        .subList(loads.size - topN, loads.size)
        .sum()
}