fun main() {

    val input = readInput("Day03")

    val line = input.joinToString("\n")

    fun part1() = "mul\\((\\d+),(\\d+)\\)"
        .toRegex()
        .findAll(line)
        .sumOf { it.groupValues[1].toLong() * it.groupValues[2].toInt() }

    fun part2(): Long {
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

    part1().println()
    part2().println()
}
