import java.io.File
import java.util.PriorityQueue

fun day12ASolution() {
    val input = File("src/main/resources/Day12Data.txt").readLines()
        .map {
            it.map { s -> if (s == 'E') 27 else if (s == 'S') 0 else letters.indexOfFirst { t -> t == s } + 1 }
        }
        .map { ArrayDeque(it.toList()) }
        .map {
            it.addLast(100)
            it.addFirst(100)
            it
        }.toCollection(ArrayDeque())

    val rowLength = input[0].size
    // Padding to make array indexing slightly easier
    input.addFirst(ArrayDeque(List(rowLength) { 100 }))
    input.addLast(ArrayDeque(List(rowLength) { 100 }))

    val graph = Graph(input.size * input[0].size)
    var start = 0
    var target = 0
    val lowPoints = mutableListOf<Int>()

    for (i in 1 until input.size - 1) {
        for (j in 1 until input[0].size - 1) {
            val currentPosition = j + i * rowLength

            if (input[i][j] == 0) {
                start = currentPosition
                lowPoints.add(currentPosition)
            }

            if (input[i][j] == 1) {
                lowPoints.add(currentPosition)
            }

            if (input[i][j] == 27) {
                target = currentPosition
            }

            if (input[i][j] - input[i][j - 1] >= -1) {
                graph.addEdge(currentPosition, (j - 1) + i * rowLength, 1)
            }
            if (input[i][j] - input[i][j + 1] >= -1) {
                graph.addEdge(currentPosition, (j + 1) + i * rowLength, 1)
            }
            if (input[i][j] - input[i - 1][j] >= -1) {
                graph.addEdge(currentPosition, j + (i - 1) * rowLength, 1)
            }
            if (input[i][j] - input[i + 1][j] >= -1) {
                graph.addEdge(currentPosition, j + (i + 1) * rowLength, 1)
            }
        }
    }

    val distances = djikstra(graph, start)

    // Optimization would be to just run djikstra backward from target and find low point with shortest distance
    // But it wasn't working for me on first attempt and I'm out of time for the day
    val result = lowPoints.mapNotNull {
        graph.visited.clear()
        val dists = djikstra(graph, it)
        dists[target]
    }.minByOrNull { it }!!

    println("Day12A: ${distances[target]}")
    println("Day12B: $result")
}

// Ported from an online resource
// Credit: https://stackabuse.com/courses/graphs-in-python-theory-and-implementation/lessons/dijkstras-algorithm/
fun djikstra(graph: Graph, startNode: Int): Map<Int, Int> {
    val distances = emptyMap<Int, Int>().toMutableMap()
    distances[startNode] = 0

    val pq = PriorityQueue<NodeDistance> { x, y -> x.distance - y.distance }
    pq.add(NodeDistance(startNode, 0))

    while (pq.isNotEmpty()) {
        val currentNode = pq.poll()
        graph.visited.add(currentNode.node)

        for (neighbor in 0 until graph.size) {
            val distance = graph.edges[currentNode.node][neighbor]
            if (distance != -1) {
                if (!graph.visited.contains(neighbor)) {
                    val oldCost = distances.getOrDefault(neighbor, Int.MAX_VALUE)
                    val newCost = distances[currentNode.node]!! + distance

                    if (newCost < oldCost) {
                        pq.add(NodeDistance(neighbor, newCost))
                        distances[neighbor] = newCost
                    }
                }
            }
        }
    }

    return distances
}

class Graph(val size: Int) {
    val edges: MutableList<MutableList<Int>> = MutableList(size) { MutableList(size) { -1 } }
    val visited: MutableList<Int> = mutableListOf()

    fun addEdge(a: Int, b: Int, weight: Int) {
        edges[a][b] = weight
    }
}

data class NodeDistance(val node: Int, val distance: Int)