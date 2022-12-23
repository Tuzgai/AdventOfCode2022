import java.io.File

fun day4ASolution(): Int {
    val input = File("src/main/resources/Day4Data.txt").readLines()

    return input.map {
        it.split(",")
    }.map {
        it.map { s ->
            s.split("-")
                .map { t -> t.toInt() }
        }
    }.map {
        val a = (it[0][0]..it[0][1]).toSet()
        val b = (it[1][0]..it[1][1]).toSet()

        if (a.intersect(b).size == a.size || a.intersect(b).size == b.size) {
            1
        } else {
            0
        }
    }.sum()
}

fun day4BSolution(): Int {
    val input = File("src/main/resources/Day4Data.txt").readLines()

    return input.map {
        it.split(",")
    }.map {
        it.map { s ->
            s.split("-")
                .map { t -> t.toInt() }
        }
    }.map {
        val a = (it[0][0]..it[0][1]).toSet()
        val b = (it[1][0]..it[1][1]).toSet()

        if (a.intersect(b).isNotEmpty()) {
            1
        } else {
            0
        }
    }.sum()
}
