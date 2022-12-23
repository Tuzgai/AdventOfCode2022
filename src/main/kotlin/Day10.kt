import java.io.File
import kotlin.math.absoluteValue

fun day10ASolution(): Int {
    val input = File("src/main/resources/Day10Data.txt").readLines()
        .map { it.split(" ") }
        .map { Instruction(it[0], if (it.size == 1) 1 else it[1].toInt()) }

    val phone = Phone()
    input.forEach {
        when (it.name) {
            "noop" -> {
                phone.incrementCycle()
            }
            "addx" -> {
                phone.incrementCycle()
                phone.incrementAndAdd(it.value)
            }
        }
    }

    phone.display.output.windowed(40, 40).forEach { println(it) }
    return phone.strengths.sum()
}

data class Instruction(val name: String, val value: Int = 1)

class Phone(var x: Int = 1, var cycle: Int = 1, val strengths: MutableList<Int> = mutableListOf()) {
    val display = Display()

    fun incrementCycle() {
        display.paint(x)

        if (cycle == 20 || ((cycle - 20) % 40) == 0) strengths.add(x * cycle)
        cycle++
    }

    fun incrementAndAdd(value: Int) {
        incrementCycle()
        x += value
    }
}

class Display(var x: Int = 0) {
    var output = ""

    fun paint(sprite: Int) {
        output += if ((x % 40).minus(sprite).absoluteValue <= 1) {
            "⬜"
        } else {
            "⬛"
        }

        x++
    }
}