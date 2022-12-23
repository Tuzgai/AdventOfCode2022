import java.io.File
import java.math.BigInteger
import kotlin.math.floor

fun day11ASolution(): Int {
    val monkeys = parseMonkeys()

    for (round in 1..20) {
        monkeys.forEach { it.takeTurn(monkeys) }
    }

    return monkeys.map { it.inspectionCount }.sorted().slice(monkeys.size - 2 until monkeys.size).reduceRight { acc, new -> acc * new }
}

fun day11BSolution(): BigInteger {
    val monkeys = parseStressMonkeys()

    val divisor = monkeys.map { it.divisor }.reduceRight { acc, next -> acc * next }

    for (round in 1..10000) {
        monkeys.forEach { it.takeStressfulTurn(divisor, monkeys) }
    }

    return monkeys.map { it.inspectionCount }.sorted().slice(monkeys.size - 2 until monkeys.size).reduceRight { acc, new -> acc * new }
}

private fun parseMonkeys() = File("src/main/resources/Day11Data.txt").readText()
    .split("\n\n")
    .map {
        val input = it.split("\n").drop(1)

        val operationInput = input[1].split(" ").drop(6)
        val operation = when {
            operationInput[1] == "old" && operationInput[0] == "+" -> { t: Int -> t + t }
            operationInput[1] == "old" && operationInput[0] == "*" -> { t: Int -> t * t }
            operationInput[0] == "+" -> { t: Int -> t + operationInput[1].toInt() }
            operationInput[0] == "*" -> { t: Int -> t * operationInput[1].toInt() }
            else -> throw NotImplementedError()
        }

        val testInput = input[2].split(" ").last().toInt()

        Monkey(
            ArrayDeque(input[0].split(" ").drop(2).mapNotNull { t -> t.replace(",", "").toIntOrNull() }.toMutableList()),
            operation,
            { t -> t % testInput == 0 },
            input[3].split(" ").last().toInt(),
            input[4].split(" ").last().toInt()
        )
    }

private fun parseStressMonkeys() = File("src/main/resources/Day11Data.txt").readText()
    .split("\n\n")
    .map {
        val input = it.split("\n").drop(1)

        val operationInput = input[1].split(" ").drop(6)
        val operation = when {
            operationInput[1] == "old" && operationInput[0] == "+" -> { t: BigInteger -> t + t }
            operationInput[1] == "old" && operationInput[0] == "*" -> { t: BigInteger -> t * t }
            operationInput[0] == "+" -> { t: BigInteger -> t + operationInput[1].toBigInteger() }
            operationInput[0] == "*" -> { t: BigInteger -> t * operationInput[1].toBigInteger() }
            else -> throw NotImplementedError()
        }

        StressMonkey(
            ArrayDeque(input[0].split(" ").drop(2).mapNotNull { t -> t.replace(",", "").toBigIntegerOrNull() }.toMutableList()),
            operation,
            input[2].split(" ").last().toBigInteger(),
            input[3].split(" ").last().toBigInteger(),
            input[4].split(" ").last().toBigInteger()
        )
    }

class Monkey(
    val items: ArrayDeque<Int> = ArrayDeque(),
    val operation: (input: Int) -> Int,
    val test: (input: Int) -> Boolean,
    val trueTarget: Int,
    val falseTarget: Int,
    var inspectionCount: Int = 0
) {
    fun takeTurn(monkeys: List<Monkey>) {
        while (items.isNotEmpty()) {
            inspectionCount++
            items[0] = operation(items.first())
            items[0] = floor(items[0].toDouble() / 3).toInt()
            val test = test(items.first())
            val target = if (test) trueTarget else falseTarget
            monkeys[target].items.add(items.removeFirst())
        }
    }
}

class StressMonkey(
    val items: ArrayDeque<BigInteger> = ArrayDeque(),
    val operation: (input: BigInteger) -> BigInteger,
    val divisor: BigInteger,
    val trueTarget: BigInteger,
    val falseTarget: BigInteger,
    var inspectionCount: BigInteger = BigInteger.ZERO
) {
    fun takeStressfulTurn(commonDivisor: BigInteger, monkeys: List<StressMonkey>) {
        while (items.isNotEmpty()) {
            inspectionCount++
            items[0] = operation(items.first())
            if (items[0] > commonDivisor) {
                items[0] %= commonDivisor
            }
            val test = items.first() % divisor == BigInteger.ZERO
            val target = if (test) trueTarget else falseTarget
            monkeys[target.toInt()].items.add(items.removeFirst())
        }
    }
}