import java.io.File

fun day9ASolution(): Int {
    val input = File("src/main/resources/Day9Data.txt").readLines()
        .map { it.split(" ") }
        .map { Vector(it[0], it[1].toInt()) }

    val head = Coordinate(0, 0)
    val tail = Coordinate(0, 0)
    val visits = mutableSetOf<Coordinate>()
    visits.add(Coordinate(tail.x, tail.y))

    input.forEach { vector ->
        for (i in 1..vector.dist) {
            move(head, Vector(vector.dir, 1))
            move(tail, tail.findStep(head))
            visits.add(Coordinate(tail.x, tail.y))
        }
    }
    return visits.size
}

fun day9BSolution(): Int {
    val input = File("src/main/resources/Day9Data.txt").readLines()
        .map { it.split(" ") }
        .map { Vector(it[0], it[1].toInt()) }

    val head = Coordinate(0, 0)
    val tail = List(9) { Coordinate(0, 0) }
    val visits = mutableSetOf<Coordinate>()
    visits.add(Coordinate(0, 0))

    input.forEach { vector ->
        for (i in 1..vector.dist) {
            move(head, Vector(vector.dir, 1))
            tail.forEachIndexed { j, knot ->
                if (j == 0) {
                    move(knot, knot.findStep(head))
                } else {
                    move(knot, knot.findStep(tail[j-1]))
                }
            }

            visits.add(Coordinate(tail.last().x, tail.last().y))
        }
    }
    return visits.size
}

fun move(coords: Coordinate, vec: Vector) {
    when (vec.dir) {
        "R" -> coords.x += vec.dist
        "L" -> coords.x -= vec.dist
        "U" -> coords.y += vec.dist
        "D" -> coords.y -= vec.dist
        "UR" -> {
            coords.x += vec.dist
            coords.y += vec.dist
        }
        "UL" -> {
            coords.x -= vec.dist
            coords.y += vec.dist
        }
        "DR" -> {
            coords.x += vec.dist
            coords.y -= vec.dist
        }
        "DL" -> {
            coords.x -= vec.dist
            coords.y -= vec.dist
        }
    }
}

data class Coordinate(var x: Int, var y: Int) {
    fun findStep(other: Coordinate): Vector {
        val xDiff = (this.x - other.x) * -1
        val yDiff = (this.y - other.y) * -1

        // Middle spaces
        if (-1 <= xDiff && xDiff <= 1 && -1 <= yDiff && yDiff <= 1) return Vector("", 0)

        // Not my proudest moment here
        val out: Vector
        Coordinate(xDiff, yDiff).let {
            when {
                it.x == -2 && it.y == 2 -> out = Vector("UL", 1)
                it.x == -1 && it.y == 2 -> out = Vector("UL", 1)
                it.x == 0 && it.y == 2 -> out = Vector("U", 1)
                it.x == 1 && it.y == 2 -> out = Vector("UR", 1)
                it.x == 2 && it.y == 2 -> out = Vector("UR", 1)
                it.x == 2 && it.y == 1 -> out = Vector("UR", 1)
                it.x == 2 && it.y == 0 -> out = Vector("R", 1)
                it.x == 2 && it.y == -1 -> out = Vector("DR", 1)
                it.x == 2 && it.y == -2 -> out = Vector("DR", 1)
                it.x == 1 && it.y == -2 -> out = Vector("DR", 1)
                it.x == 0 && it.y == -2 -> out = Vector("D", 1)
                it.x == -1 && it.y == -2 -> out = Vector("DL", 1)
                it.x == -2 && it.y == -2 -> out = Vector("DL", 1)
                it.x == -2 && it.y == -1 -> out = Vector("DL", 1)
                it.x == -2 && it.y == 0 -> out = Vector("L", 1)
                it.x == -2 && it.y == 1 -> out = Vector("UL", 1)
                else -> out = Vector("", 0)
            }
        }
        return out
    }
}

data class Vector(val dir: String, val dist: Int)