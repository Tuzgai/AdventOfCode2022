fun day2ASolution(): Int {
    val input = Utils.readInput("src/main/resources/Day2Data.txt")

    return input.map { it.split(" ") }
        .sumOf { scoreGame(it[0], it[1]) }
}


fun scoreGame(yours: String, mine: String): Int {
    return when {
        (mine == "X") and (yours == "A") -> 3 + 1
        (mine == "X") and (yours == "B") -> 0 + 1
        (mine == "X") and (yours == "C") -> 6 + 1
        (mine == "Y") and (yours == "A") -> 6 + 2
        (mine == "Y") and (yours == "B") -> 3 + 2
        (mine == "Y") and (yours == "C") -> 0 + 2
        (mine == "Z") and (yours == "A") -> 0 + 3
        (mine == "Z") and (yours == "B") -> 6 + 3
        (mine == "Z") and (yours == "C") -> 3 + 3
        else -> throw IllegalStateException()
    }
}

fun day2BSolution(): Int {
    val input = Utils.readInput("src/main/resources/Day2Data.txt")

    return input.map { it.split(" ") }
        .sumOf {
            when (it[1]) {
                "X" -> loses(it[0])
                "Y" -> draws(it[0])
                "Z" -> beats(it[0])
                else -> throw IllegalStateException()
            }
        }
}

fun beats(yours: String): Int {
    return when (yours) {
        "A" -> 6 + 2
        "B" -> 6 + 3
        "C" -> 6 + 1
        else -> throw IllegalStateException()
    }
}

fun draws(yours: String): Int {
    return when (yours) {
        "A" -> 3 + 1
        "B" -> 3 + 2
        "C" -> 3 + 3
        else -> throw IllegalStateException()
    }
}

fun loses(yours: String): Int {
    return when (yours) {
        "A" -> 0 + 3
        "B" -> 0 + 1
        "C" -> 0 + 2
        else -> throw IllegalStateException()
    }
}