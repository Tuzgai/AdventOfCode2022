import java.io.File
import java.util.regex.Pattern

fun day22ASolution(): Int {
    val input = File("src/main/resources/Day22Data.txt").readText().split("\n\n")

    val distances = input[1].split(Pattern.compile("[LR]"))
    val directions = input[1].split(Pattern.compile("\\d*")).filter { it != "" }
    val directionsPadded = directions.map { it[0] }.toMutableList()
    directionsPadded.add('E')
    val instructions = distances.zip(directionsPadded.toList())

    val map = ArrayDeque(input[0].split("\n").map { "-$it-".toMutableList() })
    val maxSize = map.maxOf { it.size }
    map.forEach { t ->
        repeat(maxSize - t.size) {
            t.add(' ')
        }
    }

    map.addFirst(MutableList(map[0].size) { '-' })
    map.addLast(MutableList(map[0].size) { '-' })

    val startingPoint = map[1].indexOfFirst { it == '.' }

    val agent = Agent(Coordinate(startingPoint, 1), '>')

    instructions.forEach { agent.moveAndTurn(it, map) }

    val facingScore = when (agent.facing) {
        '>' -> 0
        'v' -> 1
        '<' -> 2
        '^' -> 3
        else -> throw IllegalStateException()
    }

    return (1000 * agent.coordinate.y) + (4 * agent.coordinate.x) + facingScore
}

fun day22BSolution(): Int {
    val input = File("src/main/resources/Day22Data.txt").readText().split("\n\n")

    val distances = input[1].split(Pattern.compile("[LR]"))
    val directions = input[1].split(Pattern.compile("\\d*")).filter { it != "" }
    val directionsPadded = directions.map { it[0] }.toMutableList()
    directionsPadded.add('E')
    val instructions = distances.zip(directionsPadded.toList())

    val map = ArrayDeque(input[0].split("\n").map { "-$it-".toMutableList() })
    val maxSize = map.maxOf { it.size }
    map.forEach { t ->
        repeat(maxSize - t.size) {
            t.add(' ')
        }
    }

    map.addFirst(MutableList(map[0].size) { '-' })
    map.addLast(MutableList(map[0].size) { '-' })

    val startingPoint = map[1].indexOfFirst { it == '.' }

    val agent = Agent(Coordinate(startingPoint, 1), '>')

    instructions.forEach { agent.cubeMoveAndTurn(it, map) }

    val facingScore = when (agent.facing) {
        '>' -> 0
        'v' -> 1
        '<' -> 2
        '^' -> 3
        else -> throw IllegalStateException()
    }

    return (1000 * agent.coordinate.y) + (4 * agent.coordinate.x) + facingScore
}

class Agent(var coordinate: Coordinate, var facing: Char) {
    private val rightTurn = mapOf(
        '>' to 'v',
        'v' to '<',
        '<' to '^',
        '^' to '>'
    )

    private val leftTurn = mapOf(
        '>' to '^',
        'v' to '>',
        '<' to 'v',
        '^' to '<'
    )

    fun moveAndTurn(distanceAndDirection: Pair<String, Char>, map: List<MutableList<Char>>) {
        this.gridMove(distanceAndDirection.first.toInt(), map)
        this.turn(distanceAndDirection.second, map)
    }

    fun cubeMoveAndTurn(distanceAndDirection: Pair<String, Char>, map: List<MutableList<Char>>) {
        this.cubeMove(distanceAndDirection.first.toInt(), map)
        this.turn(distanceAndDirection.second, map)
    }

    private fun cubeMove(distance: Int, map: List<MutableList<Char>>) {
        for (i in 0 until distance) {
            map[coordinate.y][coordinate.x] = facing

            when (facing) {
                '>' -> {
                    val new = Coordinate(coordinate.x + 1, coordinate.y)

                    coordinate = when {
                        isNavigable(map[new.y][new.x]) -> {
                            Coordinate(new.x, new.y)
                        }

                        map[new.y][new.x] == '#' -> {
                            break
                        }

                        else -> {
                            when (getCurrentFace(coordinate)) {
                                'B' -> {
                                    // Now on right side of E
                                    val newFace = Coordinate(100, (51-coordinate.y) + 100)
                                    if (map[newFace.y][newFace.x] == '#') break
                                    facing = '<'
                                    newFace
                                }

                                'C' -> {
                                    // Now on bottom of B
                                    val newFace = Coordinate(coordinate.y-50+100, 50)
                                    if (map[newFace.y][newFace.x] == '#') break
                                    facing = '^'
                                    newFace
                                }

                                'E' -> {
                                    // Now on right side of B
                                    val newFace = Coordinate(150, 51-(coordinate.y - 100))
                                    if (map[newFace.y][newFace.x] == '#') break
                                    facing = '<'
                                    newFace
                                }

                                'F' -> {
                                    // Now on bottom of E
                                    val newFace = Coordinate(coordinate.y - 150 + 50, 150)
                                    if (map[newFace.y][newFace.x] == '#') break
                                    facing = '^'
                                    newFace
                                }

                                else -> throw IllegalStateException()
                            }
                        }
                    }
                }

                '<' -> {
                    val new = Coordinate(coordinate.x - 1, coordinate.y)

                    coordinate = when {
                        isNavigable(map[new.y][new.x]) -> {
                            Coordinate(new.x, new.y)
                        }

                        map[new.y][new.x] == '#' -> {
                            break
                        }

                        else -> {
                            when (getCurrentFace(coordinate)) {
                                'A' -> {
                                    // Now on left side of D
                                    val newFace = Coordinate(1, (51-coordinate.y) + 100)
                                    if (map[newFace.y][newFace.x] == '#') break
                                    facing = '>'
                                    newFace
                                }

                                'C' -> {
                                    // Now on top side of D
                                    val newFace = Coordinate(coordinate.y - 50, 101)
                                    if (map[newFace.y][newFace.x] == '#') break
                                    facing = 'v'
                                    newFace
                                }

                                'D' -> {
                                    // Now on left side of A
                                    val newFace = Coordinate(51, 51-(coordinate.y - 100))
                                    if (map[newFace.y][newFace.x] == '#') break
                                    facing = '>'
                                    newFace
                                }

                                'F' -> {
                                    // Now on top side of A
                                    val newFace = Coordinate(coordinate.y - 150 + 50, 1)
                                    if (map[newFace.y][newFace.x] == '#') break
                                    facing = 'v'
                                    newFace
                                }

                                else -> throw IllegalStateException()
                            }
                        }
                    }

                }

                'v' -> {
                    val new = Coordinate(coordinate.x, coordinate.y + 1)

                    coordinate = when {
                        isNavigable(map[new.y][new.x]) -> {
                            Coordinate(new.x, new.y)
                        }

                        map[new.y][new.x] == '#' -> {
                            break
                        }

                        else -> {
                            when (getCurrentFace(coordinate)) {
                                'F' -> {
                                    // Now on top side of B
                                    val newFace = Coordinate(coordinate.x + 100, 1)
                                    if (map[newFace.y][newFace.x] == '#') break
                                    facing = 'v'
                                    newFace
                                }

                                'E' -> {
                                    // Now on right side of F
                                    val newFace = Coordinate(50, coordinate.x - 50 + 150)
                                    if (map[newFace.y][newFace.x] == '#') break
                                    facing = '<'
                                    newFace
                                }

                                'B' -> {
                                    // Now on right side of C
                                    val newFace = Coordinate(100, coordinate.x - 100 + 50)
                                    if (map[newFace.y][newFace.x] == '#') break
                                    facing = '<'
                                    newFace
                                }

                                else -> throw IllegalStateException()
                            }
                        }
                    }

                }

                '^' -> {
                    val new = Coordinate(coordinate.x, coordinate.y - 1)

                    coordinate = when {
                        isNavigable(map[new.y][new.x]) -> {
                            Coordinate(new.x, new.y)
                        }

                        map[new.y][new.x] == '#' -> {
                            break
                        }

                        else -> {
                            when (getCurrentFace(coordinate)) {
                                'D' -> {
                                    // Now on left side of C
                                    val newFace = Coordinate(51, coordinate.x + 50)
                                    if (map[newFace.y][newFace.x] == '#') break
                                    facing = '>'
                                    newFace
                                }

                                'A' -> {
                                    // Now on left side of F
                                    val newFace = Coordinate(1, coordinate.x - 50 + 150)
                                    if (map[newFace.y][newFace.x] == '#') break
                                    facing = '>'
                                    newFace
                                }

                                'B' -> {
                                    // Now on bottom side of F
                                    val newFace = Coordinate(coordinate.x - 100, 200)
                                    if (map[newFace.y][newFace.x] == '#') break
                                    facing = '^'
                                    newFace
                                }

                                else -> throw IllegalStateException()
                            }
                        }
                    }

                }
            }
        }
    }


    private fun gridMove(distance: Int, map: List<MutableList<Char>>) {
        for (i in 0 until distance) {
            map[coordinate.y][coordinate.x] = facing

            when (facing) {
                '>' -> {
                    val new = Coordinate(coordinate.x + 1, coordinate.y)

                    coordinate = when {
                        isNavigable(map[new.y][new.x]) -> {
                            Coordinate(new.x, new.y)
                        }

                        map[new.y][new.x] == '#' -> {
                            break
                        }

                        else -> {
                            val rotatedCoord = map[new.y].indexOfFirst { isNavigable(it) || it == '#' }
                            if (map[new.y][rotatedCoord] == '#') break
                            Coordinate(rotatedCoord, new.y)
                        }
                    }
                }

                '<' -> {
                    val new = Coordinate(coordinate.x - 1, coordinate.y)

                    coordinate = when {
                        isNavigable(map[new.y][new.x]) -> {
                            Coordinate(new.x, new.y)
                        }

                        map[new.y][new.x] == '#' -> {
                            break
                        }

                        else -> {
                            val rotatedCoord = map[new.y].indexOfLast { isNavigable(it) || it == '#' }
                            if (map[new.y][rotatedCoord] == '#') break
                            Coordinate(rotatedCoord, new.y)
                        }
                    }

                }

                'v' -> {
                    val new = Coordinate(coordinate.x, coordinate.y + 1)

                    coordinate = when {
                        isNavigable(map[new.y][new.x]) -> {
                            Coordinate(new.x, new.y)
                        }

                        map[new.y][new.x] == '#' -> {
                            break
                        }

                        else -> {
                            val rotatedCoord = findSpaceFromTop(map, new.x)
                            if (map[rotatedCoord][new.x] == '#') break
                            Coordinate(new.x, rotatedCoord)
                        }
                    }

                }

                '^' -> {
                    val new = Coordinate(coordinate.x, coordinate.y - 1)

                    coordinate = when {
                        isNavigable(map[new.y][new.x]) -> {
                            Coordinate(new.x, new.y)
                        }

                        map[new.y][new.x] == '#' -> {
                            break
                        }

                        else -> {
                            val rotatedCoord = findSpaceFromBottom(map, new.x)
                            if (map[rotatedCoord][new.x] == '#') break
                            Coordinate(new.x, rotatedCoord)
                        }
                    }

                }
            }
        }
    }

    private fun turn(direction: Char, map: List<MutableList<Char>>) {
        facing = when (direction) {
            'L' -> leftTurn[facing]
            'R' -> rightTurn[facing]
            'E' -> facing
            else -> throw IllegalStateException()
        }!!
        map[coordinate.y][coordinate.x] = this.facing
    }

    private fun isNavigable(c: Char): Boolean {
        return c == '.' || c == '>' || c == '<' || c == 'v' || c == '^'
    }

    private fun findSpaceFromTop(map: List<MutableList<Char>>, x: Int): Int {
        for (i in 0..map.size) {
            if (isNavigable(map[i][x]) || map[i][x] == '#') return i
        }

        throw IllegalStateException()
    }

    private fun findSpaceFromBottom(map: List<MutableList<Char>>, x: Int): Int {
        for (i in map.size - 1 downTo 0) {
            if (isNavigable(map[i][x]) || map[i][x] == '#') return i
        }

        throw IllegalStateException()
    }

    private fun getCurrentFace(coordinate: Coordinate): Char {
        with(coordinate) {
            return when {
                x in 51..100 && y in 1..50 -> 'A'
                x in 101..150 && y in 1..50 -> 'B'
                x in 51..100 && y in 51..100 -> 'C'
                x in 51..100 && y in 101..150 -> 'E'
                x in 1..50 && y in 101..150 -> 'D'
                x in 1..50 && y in 151..200 -> 'F'
                else -> throw IllegalStateException()
            }
        }
    }
}
