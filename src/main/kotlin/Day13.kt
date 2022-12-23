import java.io.File

fun day13ASolution(): Int {
    return File("src/main/resources/Day13Data.txt").readText()
        .replace("10", "A")
        .split("\n\n")
        .map {
            it.split("\n")
                .map { t -> convertToList(t.iterator()) }
        }.mapIndexed { i, it ->
            if (compare(it[0], it[1]) == 1) i + 1 else 0
        }.sum()
}

fun day13BSolution(): Int {
    return (File("src/main/resources/Day13Data.txt").readText() + "\n[[2]]\n[[6]]")
        .replace("10", "A")
        .split("\n")
        .filter { it.isNotEmpty() }
        .map {
            it.split("\n")
                .map { t -> convertToList(t.iterator()) }
        }.sortedWith { a, b -> compare(a, b) }.reversed()
        .mapIndexed { i, it ->
            if (it.toString() == "[[[2]]]" || it.toString() == "[[[6]]]") i+1 else 0
        }.filter { it != 0 }
        .foldRight(1) { a, b -> a * b }
}

fun convertToList(str: Iterator<Char>): Any {
    val output = emptyList<Any>().toMutableList()

    while (str.hasNext()) {
        when (val c = str.next()) {
            ']' -> return output
            '[' -> output.add(convertToList(str))
            ',' -> continue
            else -> if (c == 'A') output.add(10) else output.add(c.digitToInt())
        }
    }

    return output[0]
}

fun compare(leftList: Any, rightList: Any): Int {
    if (leftList is List<*> && rightList is List<*>) {
        val sizeComparison = leftList.size.compareTo(rightList.size)
        val indices = if (sizeComparison >= 0) leftList.indices else rightList.indices

        for (i in indices) {
            when {
                i > leftList.size - 1 -> return 1
                i > rightList.size - 1 -> return -1
            }

            val left = leftList[i]
            val right = rightList[i]
            var result = 0
            when {
                left is Int && right is Int && left == right -> continue
                left is Int && right is Int && left < right -> return 1
                left is Int && right is Int && left > right -> return -1
                left is List<*> && right is List<*> -> result = compare(left, right)
                left is List<*> && right is Int -> result = compare(left, listOf(right))
                left is Int && right is List<*> -> result = compare(listOf(left), right)
            }

            if (result != 0) return result
        }
    }

    return 0
}