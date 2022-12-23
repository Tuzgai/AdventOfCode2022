import java.io.File

fun day7ASolution(): Int {
    val input = File("src/main/resources/Day7Data.txt").readLines()
    val tree = populateTree(input)
    return tree.sumDirectoriesLessThanThreshold(100000)
}

fun day7BSolution(): Int {
    val input = File("src/main/resources/Day7Data.txt").readLines()
    val tree = populateTree(input)

    val spaceToFree = 30000000 - (70000000 - tree.size)

    return tree.findFree(spaceToFree)
}

private fun populateTree(input: List<String>): TreeNode {
    val tree = TreeNode(0, "/", true)
    val history = ArrayDeque<TreeNode>()
    history.addLast(tree)

    input.map { it.split(" ") }
        .forEach {
            when {
                it[0] == "$" && it[1] == "ls" -> {}
                it[0] == "$" && it[1] == "cd" && it[2] == ".." -> history.removeLast()
                it[0] == "$" && it[1] == "cd" -> {
                    history.last().leaves.find { leaf -> leaf.name == it[2] }.let { s ->
                        when (s) {
                            null -> {
                                val newTreeNode = TreeNode(0, it[2], true)
                                history.last().addLeaf(newTreeNode)
                                history.addLast(newTreeNode)
                            }
                            else -> history.add(s)
                        }
                    }
                }
                it[0] == "dir" -> {
                    val newTreeNode = TreeNode(0, it[1], true)
                    history.last().addLeaf(newTreeNode)
                }
                else -> {
                    history.last().addLeaf(TreeNode(it[0].toInt(), it[1]))
                }
            }
        }

    tree.updateSizes()
    return tree
}

class TreeNode(var size: Int = 0, var name: String = "", val isDirectory: Boolean = false) {
    val leaves = mutableListOf<TreeNode>()

    fun addLeaf(node: TreeNode) {
        leaves.add(node)
    }

    fun updateSizes(): Int {
        if (leaves.size == 0) return size

        size += leaves.sumOf { it.updateSizes() }
        return size
    }

    fun sumDirectoriesLessThanThreshold(threshold: Int): Int {
        return when {
            !isDirectory -> 0
            size < threshold -> size + leaves.sumOf { it.sumDirectoriesLessThanThreshold(threshold) }
            else -> leaves.sumOf { it.sumDirectoriesLessThanThreshold(threshold) }
        }
    }

    fun findFree(spaceToFree: Int): Int {
        val directoryLeaves = leaves.filter { it.isDirectory }
        val validLeaves =
            if (directoryLeaves.isNotEmpty()) directoryLeaves.map { it.findFree(spaceToFree) }.filter { it > 0 } else emptyList()
        val smallestValidLeaf = if(validLeaves.isNotEmpty()) validLeaves.minOf { it } else 0
        val currentMatches = size - spaceToFree > 0
        val leafMatches = smallestValidLeaf - spaceToFree > 0

        return when {
            leafMatches -> smallestValidLeaf
            currentMatches -> size
            else -> 0
        }
    }
}