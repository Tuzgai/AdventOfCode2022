import java.io.File

fun day5ASolution(): List<String> {
    val stacksTest = listOf(
        ArrayDeque(listOf("_")),
        ArrayDeque(listOf("Z", "N")),
        ArrayDeque(listOf("M", "C", "D")),
        ArrayDeque(listOf("P"))
    )

    val stacksProd = listOf(
        ArrayDeque(listOf("_")),
        ArrayDeque(listOf("H", "T", "Z", "D")),
        ArrayDeque(listOf("Q", "R", "W", "T", "G", "C", "S")),
        ArrayDeque(listOf("P", "B", "F", "Q", "N", "R", "C", "H")),
        ArrayDeque(listOf("L", "C", "N", "F", "H", "Z")),
        ArrayDeque(listOf("G", "L", "F", "Q", "S")),
        ArrayDeque(listOf("V", "P", "W", "Z", "B", "R", "C", "S")),
        ArrayDeque(listOf("Z", "F", "J")),
        ArrayDeque(listOf("D", "L", "V", "Z", "R", "H", "Q")),
        ArrayDeque(listOf("B", "H", "G", "N", "F", "Z", "L", "D"))
    )

    val input = File("src/main/resources/Day5Data.txt").readText()
        .split("\n\n")[1]
        .split("\n")
        .map {
            val values = it.split(" ")
            listOf(values[1], values[3], values[5])
        }.map { it.map { s -> s.toInt() } }

    for (it in input) {
        for (i in 0 until it[0]) {
            stacksProd[it[2]].addLast(stacksProd[it[1]].removeLast())
        }
    }

    return stacksProd.map { it.last() }.toList()
}

fun day5BSolution(): List<String> {
    val stacks = listOf(
        ArrayDeque(listOf("_")),
        ArrayDeque(listOf("H", "T", "Z", "D")),
        ArrayDeque(listOf("Q", "R", "W", "T", "G", "C", "S")),
        ArrayDeque(listOf("P", "B", "F", "Q", "N", "R", "C", "H")),
        ArrayDeque(listOf("L", "C", "N", "F", "H", "Z")),
        ArrayDeque(listOf("G", "L", "F", "Q", "S")),
        ArrayDeque(listOf("V", "P", "W", "Z", "B", "R", "C", "S")),
        ArrayDeque(listOf("Z", "F", "J")),
        ArrayDeque(listOf("D", "L", "V", "Z", "R", "H", "Q")),
        ArrayDeque(listOf("B", "H", "G", "N", "F", "Z", "L", "D"))
    )

    val input = File("src/main/resources/Day5Data.txt").readText()
        .split("\n\n")[1]
        .split("\n")
        .map {
            val values = it.split(" ")
            listOf(values[1], values[3], values[5])
        }.map { it.map { s -> s.toInt() } }

    val crane = ArrayDeque(listOf("_"))
    for (it in input) {
        for (i in 0 until it[0]) {
            crane.addLast(stacks[it[1]].removeLast())
        }

        for (i in 0 until it[0]) {
            stacks[it[2]].addLast(crane.removeLast())
        }
    }

    return stacks.map { it.last() }.toList()
}