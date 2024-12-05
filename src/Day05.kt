fun main() {

    fun isValid(
        line: List<Int>,
        shouldBeAfter: Map<Int, Set<Int>>,
    ): Boolean {
        val seen = mutableSetOf<Int>()
        return line.all { num ->
            seen.add(num)
            shouldBeAfter[num]?.none { shouldBeLater -> shouldBeLater in seen } ?: true
        }
    }

    fun part1(input: List<String>): Long {
        val rules = input
            .takeWhile { it.isNotBlank() }
            .map { it.split("|") }
            .map { it.map { num -> num.toInt() } }

        val orderings = input
            .takeLastWhile { it.isNotBlank() }
            .map { it.split(",") }
            .map { it.map { num -> num.toInt() } }

        val shouldBeAfter = mutableMapOf<Int, MutableSet<Int>>()
        rules.forEach { (sooner, later) ->
            shouldBeAfter.putIfAbsent(sooner, mutableSetOf())
            shouldBeAfter[sooner]?.add(later)
        }

        return orderings.filter { line ->
            isValid(line, shouldBeAfter)
        }.sumOf { it[it.size / 2].toLong() }
    }

    fun part2(input: List<String>): Long {
        val rules = input
            .takeWhile { it.isNotBlank() }
            .map { it.split("|") }
            .map { it.map { num -> num.toInt() } }

        val orderings = input
            .takeLastWhile { it.isNotBlank() }
            .map { it.split(",") }
            .map { it.map { num -> num.toInt() } }
            .map { it.toMutableList() }

        // Safety check:
        orderings
            .filter { it.distinct().size != it.size }
            .forEach { println("Line has repeating elements $it") }

        val shouldBeAfter = mutableMapOf<Int, MutableSet<Int>>()
        rules.forEach { (sooner, later) ->
            shouldBeAfter.putIfAbsent(sooner, mutableSetOf())
            shouldBeAfter[sooner]?.add(later)
        }

        return orderings.filter { line ->
            !isValid(line, shouldBeAfter)
        }.onEach { line ->
            loop@while (!isValid(line, shouldBeAfter)) {

                val numSeenAt = mutableMapOf<Int, Int>()
                for (i in line.indices) {
                    val num = line[i]
                    numSeenAt[num] = i

                    val shouldBeAfter = shouldBeAfter[num] ?: continue
                    for (shouldBeLaterNum in shouldBeAfter) {
                        if (shouldBeLaterNum in numSeenAt) {
                            line[i] = shouldBeLaterNum
                            line[numSeenAt[shouldBeLaterNum]!!] = num
                            continue@loop
                        }
                    }
                }
            }
        }.sumOf { it[it.size / 2].toLong() }
    }

    val input = readInput("Day05")
    part1(input).println()
    part2(input).println()
}
