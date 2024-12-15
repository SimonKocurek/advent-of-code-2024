fun main() {
    fun getAntenas(input: List<String>): Map<Char, List<Pair<Int, Int>>> {
        val antenas = mutableMapOf<Char, MutableList<Pair<Int, Int>>>()

        input.doubleLoop { y, x ->
            val frequency = input[y][x]
            if (frequency != '.') {
                antenas.putIfAbsent(frequency, mutableListOf())
                antenas[frequency]?.add(x to y)
            }
        }

        return antenas
    }

    fun part1(input: List<String>): Int {
        val antenas = getAntenas(input)

        val finalMap = input.map { it.toMutableList() }
        antenas.values.forEach { frequencyPairs ->
            for (firstIdx in frequencyPairs.indices) {
                val first = frequencyPairs[firstIdx]

                for (secondIdx in frequencyPairs.indices) {
                    if (firstIdx == secondIdx) {
                        continue
                    }
                    val second = frequencyPairs[secondIdx]

                    val xDiff = first.first - second.first
                    val yDiff = first.second - second.second

                    val newX = first.first + xDiff
                    val newY = first.second + yDiff

                    if (!finalMap.inBounds(newY, newX)) {
                        continue
                    }

                    finalMap[newY][newX] = '#'
                }
            }
        }

        return finalMap.sumOf { line -> line.count { it == '#' } }
    }

    fun part2(input: List<String>): Int {
        val antenas = getAntenas(input)

        val finalMap = input.map { it.toMutableList() }
        antenas.values.forEach { frequencyPairs ->
            for (firstIdx in frequencyPairs.indices) {
                val first = frequencyPairs[firstIdx]

                for (secondIdx in frequencyPairs.indices) {
                    if (firstIdx == secondIdx) {
                        continue
                    }
                    val second = frequencyPairs[secondIdx]

                    val xDiff = first.first - second.first
                    val yDiff = first.second - second.second

                    var newX = first.first
                    var newY = first.second

                    while (finalMap.inBounds(newY, newX)) {
                        finalMap[newY][newX] = '#'
                        newX += xDiff
                        newY += yDiff
                    }
                }
            }
        }

        return finalMap.sumOf { line -> line.count { it == '#' } }
    }

    val input = readInput("Day08")
    part1(input).println()
    part2(input).println()
}
