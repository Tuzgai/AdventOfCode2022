import java.io.File

fun day18ASolution(): Int {
    val points = File("src/main/resources/Day18Data.txt").readLines()
        .map {
            val tokens = it.split(",")
            Point3d(tokens[0].toInt(), tokens[1].toInt(), tokens[2].toInt())
        }

    val pointSet = points.toSet()

    return points.map { collectOpenSurfaces(it, pointSet) }.flatten().toSet().size
}

fun day18BSolution(): Int {
    val points = File("src/main/resources/Day18Data.txt").readLines()
        .map {
            val tokens = it.split(",")
            Point3d(tokens[0].toInt(), tokens[1].toInt(), tokens[2].toInt())
        }

    val pointSet = points.toSet()

    val openSurfaces = points.map { collectOpenSurfaces(it, pointSet) }.flatten().toSet()

    return openSurfaces.map { walkSurface(it, openSurfaces) }.maxOf { it }
}

fun walkSurface(start: Surface, allSurfaces: Set<Surface>): Int {
    val surfacesToExplore = ArrayDeque(listOf(start))
    val visited = mutableSetOf<Surface>()
    var count = 0

    val directionToSide = mapOf(
        Direction.FORWARD to Side.FRONT,
        Direction.BACK to Side.BACK,
        Direction.UP to Side.TOP,
        Direction.DOWN to Side.BOTTOM,
        Direction.LEFT to Side.LEFT,
        Direction.RIGHT to Side.RIGHT
    )

    val sideToDirection = directionToSide.map { (k, v) -> v to k }.toMap()

    val opposites = mapOf(
        Side.TOP to Side.BOTTOM,
        Side.BOTTOM to Side.TOP,
        Side.LEFT to Side.RIGHT,
        Side.RIGHT to Side.LEFT,
        Side.BACK to Side.FRONT,
        Side.FRONT to Side.BACK
    )

    val adjacencies = mapOf(
        Side.TOP to listOf(Direction.FORWARD, Direction.BACK, Direction.LEFT, Direction.RIGHT),
        Side.BOTTOM to listOf(Direction.FORWARD, Direction.BACK, Direction.LEFT, Direction.RIGHT),
        Side.LEFT to listOf(Direction.FORWARD, Direction.BACK, Direction.UP, Direction.DOWN),
        Side.RIGHT to listOf(Direction.FORWARD, Direction.BACK, Direction.UP, Direction.DOWN),
        Side.FRONT to listOf(Direction.LEFT, Direction.RIGHT, Direction.UP, Direction.DOWN),
        Side.BACK to listOf(Direction.LEFT, Direction.RIGHT, Direction.UP, Direction.DOWN),
    )

    while (surfacesToExplore.isNotEmpty()) {
        val currentSurface = surfacesToExplore.removeLast()
        count++
        visited.add(currentSurface)

        // The goal here is to add each adjacent surface
        // Here are the adjacent surfaces for a surface:
        // Any side of the same point, except the opposite side (only if the neighboring point is open)
        // The same side of an adjacent point
        // A diagonally-adjacent surface, if the grid-adjacent point is not filled

        // Same point, different side
        Side.values().filter { it != currentSurface.s && it != opposites[currentSurface.s] }.forEach {
            val differentSurfaceSamePoint = Surface(currentSurface.p, it)
            if (!visited.contains(differentSurfaceSamePoint)
                && !surfacesToExplore.contains(differentSurfaceSamePoint)
                && allSurfaces.contains(differentSurfaceSamePoint)
                && !allSurfaces.contains(
                    // Block cutting corners
                    Surface(
                        currentSurface.p.add(sideToDirection[currentSurface.s]!!.p.add(sideToDirection[it]!!.p)),
                        opposites[currentSurface.s]!!
                    )
                )
            ) {
                surfacesToExplore.addLast(differentSurfaceSamePoint)
            }
        }

        // Adjacent points, same facing
        adjacencies[currentSurface.s]!!.forEach {
            val adjacentSurface = Surface(currentSurface.p.add(it.p), currentSurface.s)
            if (!visited.contains(adjacentSurface)
                && !surfacesToExplore.contains(adjacentSurface)
                && allSurfaces.contains(adjacentSurface)
            ) {
                surfacesToExplore.addLast(adjacentSurface)
            }
        }

        // Diagonally adjacent points, rotated facing
        adjacencies[currentSurface.s]!!.forEach {
            val diagonallyAdjacentSurface = Surface(
                currentSurface.p.add(it.p).add(sideToDirection[currentSurface.s]!!.p),
                opposites[directionToSide[it]!!]!!
            )

            if (!visited.contains(diagonallyAdjacentSurface)
                && !surfacesToExplore.contains(diagonallyAdjacentSurface)
                && allSurfaces.contains(diagonallyAdjacentSurface)
            ) {
                surfacesToExplore.addLast(diagonallyAdjacentSurface)
            }
        }
    }

    return count
}

fun collectOpenSurfaces(p: Point3d, pointSet: Set<Point3d>): Set<Surface> {

    val directionToSide = mapOf(
        Direction.FORWARD to Side.FRONT,
        Direction.BACK to Side.BACK,
        Direction.UP to Side.TOP,
        Direction.DOWN to Side.BOTTOM,
        Direction.LEFT to Side.LEFT,
        Direction.RIGHT to Side.RIGHT
    )

    return Direction.values().mapNotNull { if (!pointSet.contains(p.add(it.p))) Surface(p, directionToSide[it]!!) else null }.toSet()
}

data class Point3d(val x: Int, val y: Int, val z: Int) {
    fun add(p: Point3d): Point3d {
        return Point3d(x + p.x, y + p.y, z + p.z)
    }
}

data class Surface(val p: Point3d, val s: Side)

enum class Direction(val p: Point3d) {
    LEFT(Point3d(-1, 0, 0)),
    RIGHT(Point3d(1, 0, 0)),
    UP(Point3d(0, 1, 0)),
    DOWN(Point3d(0, -1, 0)),
    FORWARD(Point3d(0, 0, 1)),
    BACK(Point3d(0, 0, -1)),
}

enum class Side {
    TOP,
    BOTTOM,
    LEFT,
    RIGHT,
    FRONT,
    BACK
}