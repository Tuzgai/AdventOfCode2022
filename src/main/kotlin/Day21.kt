import java.io.File

fun day21ASolution(): Long {
    val monkeyMap = File("src/main/resources/Day21Data.txt").readLines()
        .map { it.replace(":", "") }
        .associate {
            val v = it.split(" ")
            v[0] to v.subList(1, v.size)
        }

    val root = populateMonkeyTree("root", monkeyMap)
    println("Day21A: ${root.value}")

    findHuman(root)

    val targetBranch = root.children.first { !it.onHumanPath }
    val answerBranch = root.children.first { it.onHumanPath }

    return findAnswer(answerBranch, targetBranch.value)
}

fun findAnswer(monkeyNode: MonkeyNode, constraint: Long): Long {
    if (monkeyNode.name == "humn") {
        return constraint
    }

    val fixedValue = if (!monkeyNode.children[0].onHumanPath) monkeyNode.children[0].value else monkeyNode.children[1].value
    val answerBranch = if (monkeyNode.children[0].onHumanPath) monkeyNode.children[0] else monkeyNode.children[1]
    val answerOnRight = answerBranch == monkeyNode.children[1]

    return when {
        monkeyNode.operator == "+" -> findAnswer(answerBranch, constraint - fixedValue)
        monkeyNode.operator == "-" && answerOnRight -> findAnswer(answerBranch, -1 * constraint + fixedValue)
        monkeyNode.operator == "-" && !answerOnRight -> findAnswer(answerBranch, constraint + fixedValue)
        monkeyNode.operator == "*" -> findAnswer(answerBranch, constraint / fixedValue)
        monkeyNode.operator == "/" && answerOnRight -> findAnswer(answerBranch, fixedValue / constraint)
        monkeyNode.operator == "/" && !answerOnRight -> findAnswer(answerBranch, constraint * fixedValue)
        else -> throw IllegalStateException()
    }
}

fun findHuman(monkeyNode: MonkeyNode): Int {
    if (monkeyNode.name == "humn") {
        monkeyNode.onHumanPath = true
        return 1
    }
    if (monkeyNode.children.isEmpty()) return 0

    val test = monkeyNode.children.sumOf { findHuman(it) }
    if (test > 0) monkeyNode.onHumanPath = true

    return test
}

fun populateMonkeyTree(name: String, monkeyMap: Map<String, List<String>>): MonkeyNode {
    val operation = monkeyMap[name]!!

    if (operation.size == 1) {
        return MonkeyNode(name, operation[0].toLong(), "", emptyList())
    }

    val operand1 = populateMonkeyTree(operation[0], monkeyMap)
    val operand2 = populateMonkeyTree(operation[2], monkeyMap)

    val value = when (operation[1]) {
        "+" -> operand1.value + operand2.value
        "-" -> operand1.value - operand2.value
        "*" -> operand1.value * operand2.value
        "/" -> operand1.value / operand2.value
        else -> throw IllegalStateException()
    }

    return MonkeyNode(name, value, operation[1], listOf(operand1, operand2))
}

data class MonkeyNode(val name: String, val value: Long, val operator: String, val children: List<MonkeyNode>, var onHumanPath: Boolean = false)