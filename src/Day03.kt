fun main() {
    fun part1(input: List<String>): Long {
        val line = input.joinToString("\n")
        return "mul\\((\\d+),(\\d+)\\)"
            .toRegex()
            .findAll(line)
            .sumOf { it.groupValues[1].toLong() * it.groupValues[2].toInt() }
    }

    fun part2(input: List<String>): Long {
        val line = input.joinToString("\n")

        var enabled = true
        var result = 0L

        "(mul\\((\\d+),(\\d+)\\))|(don\'t\\(\\))|(do\\(\\))"
            .toRegex()
            .findAll(line)
            .map { it.groupValues }
            .forEach { values ->
                if (values[0] == "don't()") {
                    enabled = false
                } else if (values[0] == "do()") {
                    enabled = true
                } else if (enabled) {
                    result += values[2].toLong() * values[3].toInt()
                }
            }

        return result
    }

    val input = readInput("Day03")
    part1(input).println()
    part2(input).println()
}
