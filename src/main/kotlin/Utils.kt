import java.io.File

object Utils {
    fun readInput(file: String): List<String> {
        return File(file).readLines()
    }

    /**
     * Splits lists of lines separated by an empty line into a list of lists
     */
    fun blocksToLists(blocks: List<String>): List<List<String>> {
        val output = mutableListOf<List<String>>()

        var startIndex = 0
        for (i in blocks.indices) {
            if (blocks[i] == "") {
                output.add(blocks.subList(startIndex, i))
                startIndex = i + 1
            }
        }

        output.add(blocks.subList(startIndex, blocks.size))

        return output
    }
}