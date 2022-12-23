import java.io.File

fun day20ASolution(): Long {
    val instructions = File("src/main/resources/Day20Test.txt").readLines()
        .map { it.toLong() + 24 }
        .mapIndexed { i, it -> Node(i, it) }

    val workingCopy = instructions.toMutableList()

    instructions.forEach {
        val current = workingCopy.indexOf(it)

        val rescaled = it.value % workingCopy.size

        var new = current + rescaled
        while (new >= workingCopy.size) {
            new -= workingCopy.size - 1
        }
        while (new <= 0) {
            new += workingCopy.size - 1
        }
        workingCopy.removeAt(current)
        workingCopy.add(new.toInt(), it)
    }

//    instructions.forEach {
//        val current = workingCopy.indexOf(it)
//        var new = current + it.value
//
//        // This issue in the original solution is that using a loop to rotate through the list is too slow with big numbers
//        // Instead we need to do a little modular arithmetic
//
//        if ((new >= workingCopy.size)) new %= (workingCopy.size - 1)
//        if ((new <= 0)) new = new.absoluteValue % workingCopy.size
//
//        workingCopy.removeAt(current)
//
//        if (new == 0L) {
//            workingCopy.add(it)
//        } else {
//            workingCopy.add(new.toInt(), it)
//        }
//    }

    val test = workingCopy.map { it.value / 7 }

    val breakpoint = workingCopy.indexOfFirst { it.value == 0L }

    return workingCopy[(1000 + breakpoint) % workingCopy.size].value +
            workingCopy[(2000 + breakpoint) % workingCopy.size].value +
            workingCopy[(3000 + breakpoint) % workingCopy.size].value
}


// shoulda used git, i messed this up somewhere
fun day20BSolution(): Long {
    val instructions = File("src/main/resources/Day20Test.txt").readLines()
        .map { it.toInt() }
        .mapIndexed { i, it -> Node(i, it * 1L) }

    //instructions.forEach { it.amountToMove = it.value % instructions.size }

    val workingCopy = instructions.toMutableList()

    repeat(1) {
        instructions.forEach {
            val current = workingCopy.indexOf(it)
            var new = current + it.amountToMove
            while (new >= workingCopy.size) {
                new -= workingCopy.size - 1
            }
            while (new <= 0) {
                new += workingCopy.size - 1
            }
            workingCopy.removeAt(current)
            workingCopy.add(new.toInt(), it)
        }
    }

    val breakpoint = workingCopy.indexOfFirst { it.value == 0L }

    return workingCopy[(1000 + breakpoint) % workingCopy.size].value +
            workingCopy[(2000 + breakpoint) % workingCopy.size].value +
            workingCopy[(3000 + breakpoint) % workingCopy.size].value
}

data class Node(val id: Int, val value: Long, var amountToMove: Long = 0)
