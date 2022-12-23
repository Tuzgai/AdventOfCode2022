import java.io.File

fun day8ASolution(): Int {
    val input = File("src/main/resources/Day8Data.txt").readLines()
        .map { it.map { s -> s.digitToInt() } }
        .map {
            it.toIntArray()
        }.toTypedArray()


    val output = MutableList(input.size) { MutableList(input[1].size) { 0 } }

    // set edges to 1 (this catches the case of 0-height trees
    output[0] = MutableList(input[1].size) { 1 }
    output[output.size - 1] = MutableList(input[1].size) { 1 }
    output.forEach {
        it[0] = 1
        it[it.size - 1] = 1
    }

    // left to right
    for (i in input.indices) {
        var max = 0
        for (j in input[i].indices) {
            if (input[i][j] > max) {
                output[i][j] = 1
                max = input[i][j]
            }
        }
    }

    // right to left
    for (i in input.indices) {
        var max = 0
        for (j in input[i].indices.reversed()) {
            if (input[i][j] > max) {
                output[i][j] = 1
                max = input[i][j]
            }
        }
    }

    // top to bottom
    for (i in input.indices) {
        var max = 0
        for (j in input[i].indices) {
            if (input[j][i] > max) {
                output[j][i] = 1
                max = input[j][i]
            }
        }
    }

    // bottom to top
    for (i in input.indices) {
        var max = 0
        for (j in input[i].indices.reversed()) {
            if (input[j][i] > max) {
                output[j][i] = 1
                max = input[j][i]
            }
        }
    }

    return output.sumOf { it.sum() }
}

fun day8BSolution(): Int {
    val input = File("src/main/resources/Day8Data.txt").readLines()
        .map { it.map { s -> s.digitToInt() } }
        .map {
            it.toIntArray()
        }.toTypedArray()

    var max = 0
    input.forEachIndexed { x, ints ->
        ints.forEachIndexed { y, _ ->
            val new = scenicScore(x, y, input)
            if (new > max) {
                max = new
            }
        }
    }

    return max
}

fun scenicScore(x: Int, y: Int, map: Array<IntArray>): Int {
    // Left to right
    var countRight = 0
    for (i in y+1 until map[0].size) {
        countRight++
        if(map[x][y] <= map[x][i]) break
    }

    // Right to left
    var countLeft = 0
    for (i in y-1 downTo 0) {
        countLeft++
        if(map[x][y] <= map[x][i]) break
    }

    // Top to bottom
    var countBottom = 0
    for (i in x+1 until map.size) {
        countBottom++
        if(map[x][y] <= map[i][y]) break
    }

    // Bottom to top
    var countTop = 0
    for (i in x-1 downTo 0) {
        countTop++
        if(map[x][y] <= map[i][y]) break
    }

    return countLeft * countRight * countBottom * countTop
}