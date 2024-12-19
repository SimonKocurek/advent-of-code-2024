import kotlin.math.min

fun main() {

    fun getFileSizes(input: List<String>) = input
        .first()
        .filterIndexed { index, _ -> index % 2 == 0 }
        .map { it.digitToInt() }
        .toMutableList()

    fun getFreeSpaces(input: List<String>) = input
        .first()
        .filterIndexed { index, _ -> index % 2 == 1 }
        .map { it.digitToInt() }
        .toMutableList()

    fun part1(input: List<String>): Long {
        val fileSizes = getFileSizes(input)
        val freeSpaces = getFreeSpaces(input)

        val blocks = mutableListOf<Pair<Int, Int>>()

        var fileEndIdx = fileSizes.size - 1

        for (i in fileSizes.indices) {
            if (fileSizes[i] > 0) {
                blocks.add(fileSizes[i] to i)
            }

            while (fileEndIdx > i && freeSpaces[i] > 0) {
                val moved = min(freeSpaces[i], fileSizes[fileEndIdx])

                blocks.add(moved to fileEndIdx)

                fileSizes[fileEndIdx] -= moved
                freeSpaces[i] -= moved

                if (fileSizes[fileEndIdx] == 0) {
                    fileEndIdx--
                }
            }
        }

        // There is a math formula for this... but we can just iterate
        var i = 0
        var result = 0L
        blocks.forEach { (count, num) ->
            for (j in 0 until count) {
                result += i * num.toLong()
                i++
            }
        }
        return result
    }

    fun part2(input: List<String>): Long {

        data class Block(
            var count: Int,
            var num: Int,
        ) {
            val isFile get() = num != -1
        }

        val fileSizes = getFileSizes(input)
        val freeSpaces = getFreeSpaces(input)

        val blocks = mutableListOf<Block>()

        for (i in fileSizes.indices) {
            blocks.add(Block(fileSizes[i], i))
            if (i < freeSpaces.size) {
                blocks.add(Block(freeSpaces[i], -1))
            }
        }

        for (i in blocks.size - 1 downTo 0) {
            val endBlock = blocks[i]
            if (!endBlock.isFile) {
                continue // free space
            }

            for (j in 0 until i) {
                val startBlock = blocks[j]
                if (startBlock.isFile) {
                    continue // taken by file
                }

                if (startBlock.count < endBlock.count) {
                    continue // won't fit
                }

                if (startBlock.count == endBlock.count) {
                    blocks[i] = startBlock
                    blocks[j] = endBlock
                } else {
                    blocks.add(j, Block(endBlock.count, endBlock.num))
                    startBlock.count -= endBlock.count
                    endBlock.num = -1 // File was moved to the first block and is now empty space
                }
                break
            }
        }

        // There is a math formula for this... but we can just iterate
        var i = 0
        var result = 0L
        blocks.forEach { (count, num) ->
            if (num == -1) {
                i += count
                return@forEach
            }
            for (j in 0 until count) {
                result += i * num.toLong()
                i++
            }
        }
        return result
    }

    val input = readInput("Day09")
    part1(input).println()
    part2(input).println()
}
