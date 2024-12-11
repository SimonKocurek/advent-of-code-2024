import java.math.BigInteger

fun main() {

    fun part1(input: List<String>): Int {
        var stones = input.first().split(" ")

        for (i in 1..25) {
            val newStones = mutableListOf<String>()
            for (stone in stones) {
                when {
                    stone == "0" -> newStones.add("1")
                    stone.length % 2 == 0 -> {
                        newStones.add(stone.substring(0, stone.length / 2).toBigInteger().toString())
                        newStones.add(stone.substring(stone.length / 2).toBigInteger().toString())
                    }

                    else -> newStones.add(
                        stone.toBigInteger().times(BigInteger.valueOf(2024L)).toString()
                    )
                }
            }
            stones = newStones
        }

        return stones.size
    }

    fun part2(input: List<String>): Long {
        var stoneCounts = input
            .first()
            .split(" ")
            .associateBy({ it }) { 1L }

        val stoneGenerates = mutableMapOf<String, List<String>>().apply {
            put("0", listOf("1"))
        }

        for (i in 1..75) {
            val newStoneCounts = mutableMapOf<String, Long>()

            for ((stone, count) in stoneCounts) {
                if (stone !in stoneGenerates) {
                    if (stone.length % 2 == 0) {
                        stoneGenerates[stone] = listOf(
                            stone.substring(0, stone.length / 2).toBigInteger().toString(),
                            stone.substring(stone.length / 2).toBigInteger().toString()
                        )
                    } else {
                        stoneGenerates[stone] = listOf(
                            stone.toBigInteger().times(BigInteger.valueOf(2024L)).toString()
                        )
                    }
                }

                stoneGenerates[stone]!!.forEach { generatedStone ->
                    newStoneCounts[generatedStone] = (newStoneCounts[generatedStone] ?: 0) + count
                }
            }

            stoneCounts = newStoneCounts
        }

        return stoneCounts.values.sum()
    }

    val input = readInput("Day11")
    part1(input).println()
    part2(input).println()
}
