fun main() {
    fun getAntenas(input: List<String>): Map<Char, List<Pair<Int, Int>>> {
        val antenas = mutableMapOf<Char, MutableList<Pair<Int, Int>>>()

        for (y in input.indices) {
            for (x in input[y].indices) {
                val frequency = input[y][x]
                if (frequency != '.') {
                    antenas.putIfAbsent(frequency, mutableListOf())
                    antenas[frequency]?.add(x to y)
                }
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

                    if (newY < 0 || newY >= finalMap.size || newX < 0 || newX >= finalMap[newY].size) {
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

                    while (newY >= 0 && newY < finalMap.size && newX >= 0 && newX < finalMap[newY].size) {
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
