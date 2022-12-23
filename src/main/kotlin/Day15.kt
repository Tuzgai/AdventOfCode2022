import java.io.File
import kotlin.math.absoluteValue

fun day15ASolution(): Long {
    val input = File("src/main/resources/Day15Data.txt").readLines()
        .map { it.split(" ") }
        .map { listOf(it[2], it[3], it[8], it[9]) }
        .map {
            it.map { s ->
                s.split("=")[1]
                    .replace(",", "")
                    .replace(":", "").toLong()
            }
        }.map {
            listOf(it[0], it[1], manDist(it[0], it[1], it[2], it[3]))
        }

    val minX = input.minOf { it[0] - it[2] - 1 }
    val maxX = input.maxOf { it[0] + it[2] + 1 }
    val rowToCheck = 2000000L

    val set = mutableSetOf<Long>()
    for (i in minX..maxX) {
        input.forEach {
            if (manDist(it[0], it[1], i, rowToCheck) <= it[2]) {
                set.add(i)
            }
        }
    }

    return set.size - 1L
}

fun day15BSolution(): Long {
    val input = File("src/main/resources/Day15Data.txt").readLines()
        .map { it.split(" ") }
        .map { listOf(it[2], it[3], it[8], it[9]) }
        .map {
            it.map { s ->
                s.split("=")[1]
                    .replace(",", "")
                    .replace(":", "").toLong()
            }
        }.map {
            listOf(it[0], it[1], manDist(it[0], it[1], it[2], it[3]))
        }

    val ranges = emptyMap<Long, MutableList<Pair<Long, Long>>>().toMutableMap()

    input.forEach { populateRanges(ranges, it[0], it[1], it[2] + 1) }

    for (i in ranges.keys) {
        ranges[i] = combineRanges(ranges[i]!!)
    }

    return ranges.filter { it.key in 0..4000000 }
        .filter { it.value.size == 2 }
        .map {
            Pair(it.key, it.value[0].second + 1)
        }.map {
            4000000 * it.second + it.first
        }.first()
}

fun populateRanges(ranges: MutableMap<Long, MutableList<Pair<Long, Long>>>, x: Long, y: Long, dist: Long) {
    for (i in 0 until dist) {
        addRange(ranges, x - i, x + i, (y - dist) + i + 1)
        addRange(ranges, x - i, x + i, (y + dist) - i - 1)
    }
}

fun addRange(ranges: MutableMap<Long, MutableList<Pair<Long, Long>>>, start: Long, end: Long, row: Long) {
    if (ranges[row] != null) {
        ranges[row]!!.add(Pair(start, end))
    } else {
        ranges[row] = mutableListOf(Pair(start, end))
    }
}

fun combineRanges(ranges: MutableList<Pair<Long, Long>>): MutableList<Pair<Long, Long>> {
    val iterator = ranges.sortedBy { it.first }.iterator()

    val output = mutableListOf<Pair<Long, Long>>()
    var prev = iterator.next()
    while (iterator.hasNext()) {
        val new = iterator.next()

        prev = when {
            // Subset - ignore
            new.first <= prev.second && new.second <= prev.second -> continue
            // Overlap, combine
            new.first <= prev.second + 1 -> {
                Pair(prev.first, new.second)
            }
            // No overlap, save prev
            else -> {
                output.add(Pair(prev.first, prev.second))
                new
            }
        }
    }

    output.add(prev)

    return output
}

fun manDist(x1: Long, y1: Long, x2: Long, y2: Long): Long {
    return (x1 - x2).absoluteValue + (y1 - y2).absoluteValue
}

fun printRanges(rangesMap: MutableMap<Long, MutableList<Pair<Long, Long>>>) {
    for (i in rangesMap.keys) {
        rangesMap[i] = combineRanges(rangesMap[i]!!)
    }

    for (ranges in rangesMap.toSortedMap()) {
        val tmp = mutableListOf<Char>()
        for (range in ranges.value) {
            for (i in tmp.size until range.first + 10) {
                tmp += '.'
            }

            for (i in range.first + 10..range.second + 10) {
                tmp += '#'
            }
        }
        println(tmp)
    }
}
