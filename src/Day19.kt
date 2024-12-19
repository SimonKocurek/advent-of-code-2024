fun main() {

    val input = readInput("Day19")

    val patterns = input
        .takeWhile { it.isNotBlank() }
        .flatMap { it.split(", ") }
        .groupBy { it.length }
        .mapValues { it.value.toSet() }

    val designs = input
        .takeLastWhile { it.isNotBlank() }

    fun waysToBuild(design: String): Long {
        val waysToBuild = Array(design.length + 1) { 0L }
        waysToBuild[0] = 1

        for (i in 0..design.length) {
            for ((length, patternsOfLength) in patterns) {
                if (i + length > design.length) {
                    continue
                }

                val subDesign = design.substring(i, i + length)
                if (subDesign in patternsOfLength) {
                    waysToBuild[i + length] += waysToBuild[i]
                }
            }
        }

        return waysToBuild.last()
    }

    fun part1() = designs.count { design -> waysToBuild(design) > 0 }

    fun part2() = designs.sumOf { design -> waysToBuild(design) }

    part1().println()
    part2().println()
}
