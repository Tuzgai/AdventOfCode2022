import java.io.File

const val letters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"

fun day3ASolution(): Int {
    val input = File("src/main/resources/Day3Data.txt").readLines()

    return input.map {
        listOf(it.slice(0 until (it.length / 2)), it.slice((it.length / 2) until it.length))
    }.sumOf {
        var match = 0
        for (s in it[0]) {
            if (it[1].find { t -> t == s } != null) {
                match = rank(s)
                break
            }
        }
        match
    }
}

fun day3BSolution(): Int {
    val input = File("src/main/resources/Day3Data.txt").readLines()

    return input.chunked(3).sumOf {
        var match = 0
        for (s in it[0]) {
            if (it[1].find { t -> t == s } != null && it[2].find { t -> t == s } != null) {
                match = rank(s)
                break
            }
        }
        match
    }
}

fun rank(s: Char): Int {
    val ranks = mutableMapOf<Char, Int>()
    letters.forEachIndexed { i, l -> ranks[l] = i + 1 }

    return ranks[s]!!
}