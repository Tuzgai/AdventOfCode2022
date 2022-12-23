import java.io.File
import kotlin.math.sign

fun day14ASolution(): Int {
    val grid = getGrid()

    val startX = 500
    var moreSand = true
    var count = 0
    while (moreSand) {
        try {
            count++
            moreSand = dropSand(grid, startX)
        } catch (e: Exception) {
            return count - 1
        }
    }

    return count
}

fun day14BSolution(): Int {
    val grid = getGrid()

    drawWall(grid, listOf(listOf(0, grid.size - 1), listOf(grid[0].size - 1, grid.size - 1)))

    val startX = 500
    var moreSand = true
    var count = 0
    while (moreSand) {
        count++
        moreSand = dropSand(grid, startX)
    }

    return count
}

fun getGrid(): List<MutableList<Char>> {
    val walls = File("src/main/resources/Day14Data.txt").readLines()
        .map {
            val coords = it.split(" -> ")
            coords.mapIndexed { i, s ->
                if (i < coords.size - 1) {
                    listOf(s.split(",").map { t -> t.toInt() }, coords[i + 1].split(",").map { t -> t.toInt() })
                } else {
                    null
                }
            }.filterNotNull()
        }.flatten()

    val maxX = walls.maxOf { it.maxOf { t -> t[0] } } + 200
    val maxY = walls.maxOf { it.maxOf { t -> t[1] } } + 1

    val grid = List(maxY + 2) { MutableList(maxX) { '.' } }

    walls.forEach { drawWall(grid, it) }

    return grid
}

fun drawWall(grid: List<MutableList<Char>>, wall: List<List<Int>>) {
    var x1 = wall[0][0]
    var y1 = wall[0][1]
    val x2 = wall[1][0]
    val y2 = wall[1][1]

    grid[y1][x1] = '#'
    while (x1 != x2 || y1 != y2) {
        val xDiff = x1 - x2
        val yDiff = y1 - y2

        x1 += -1 * xDiff.sign
        y1 += -1 * yDiff.sign
        grid[y1][x1] = '#'
    }
}

fun dropSand(grid: List<MutableList<Char>>, startX: Int): Boolean {
    var x = startX
    var y = 0

    var stop = false
    while (!stop) {
        when {
            grid[y + 1][x] == '.' -> y++
            grid[y + 1][x - 1] == '.' -> {
                y++
                x--
            }
            grid[y + 1][x + 1] == '.' -> {
                y++
                x++
            }
            else -> {
                grid[y][x] = 'o'
                stop = true
            }
        }

        if (x == startX && y == 0) return false
    }

    return true
}

fun printGrid(grid: List<MutableList<Char>>) {
    grid.forEach { println(it) }
}
