//import java.io.File
//
//fun day17ASolution(): Int {
//    val input = File("src/main/resources/Day17Test.txt").readText()
//
//    val grid = ArrayDeque<String>()
//    val blankLine = "|.......|"
//    grid.addFirst("+-------+")
//    repeat(4) { grid.addLast(blankLine) }
//
//
//
//    return 0
//}
//
//fun day17BSolution(): Int {
//    val input = File("src/main/resources/Day17Test.txt").readText()
//
//    return 0
//}
//
//data class Point(var x: Int, var y: Int) {
//    fun moveLeft() {
//        x--
//    }
//
//    fun moveRight() {
//        x++
//    }
//
//    fun moveUp() {
//        y++
//    }
//
//    fun moveDown() {
//        y--
//    }
//}
//
//data class Tile(val points: Set<Point>, val xOffset: Int, val yOffset: Int) {
//    fun moveLeft() {
//        points.forEach { it.moveLeft() }
//    }
//
//    fun moveRight() {
//        points.forEach { it.moveRight() }
//    }
//
//    fun moveUp() {
//        points.forEach { it.moveUp() }
//
//    }
//
//    fun moveDown() {
//        points.forEach { it.moveDown() }
//    }
//}
//
//fun makeO(): Tile {
//    return Tile(setOf(Point(0, 0), Point(0, 1), Point(1, 0), Point(1, 1)))
//}
//
//fun makeL(): Tile {
//    return Tile(setOf(Point(0, 0), Point(1, 0), Point(2, 0), Point(2, 1), Point(2, 2)))
//}
//
//fun makeCross(): Tile {
//    return Tile(setOf(Point(0, 1), Point(1, 0), Point(1, 1), Point(1, 2), Point(2, 1)))
//}
//
//fun makeI(): Tile {
//    return Tile(setOf(Point(0, 0), Point(0, 1), Point(0, 2), Point(0, 3)))
//}
//
//fun makeBar(): Tile {
//    return Tile(setOf(Point(0, 0), Point(1, 0), Point(2, 0), Point(3, 0)))
//}