import java.io.File

fun day6ASolution(): Int {
    val input = File("src/main/resources/Day6Data.txt").readText()

    input.windowed(size = 4, step = 1)
        .forEachIndexed { i, x ->
            if (allUnique(x)) {
                return i + 4
            }
        }

    return 0
}

fun day6BSolution(): Int {
    val input = File("src/main/resources/Day6Data.txt").readText()

    input.windowed(size = 14, step = 1)
        .forEachIndexed { i, x ->
            if (allUnique(x)) {
                return i + 14
            }
        }

    return 0
}

fun allUnique(s: String): Boolean {
    s.forEachIndexed { i, x ->
        s.slice(i+1 until s.length)
            .forEach { y -> if (x == y) return false }
    }

    return true
}