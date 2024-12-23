fun main() {

    val input = readInput("Day23")
        .map { it.split("-") }

    fun buildGraph() = buildMap<String, MutableSet<String>> {
        input.forEach { (from, to) ->
            getOrPut(from) { mutableSetOf() }.add(to)
            getOrPut(to) { mutableSetOf() }.add(from)
        }
    }

    val graph = buildGraph()

    fun part1() = buildSet {
        graph.forEach { (a, aNeighbours) ->
            aNeighbours.forEach { b ->
                val bNeighbours = graph[b]!!
                bNeighbours.forEach { c ->
                    if (c in aNeighbours) {
                        if (a.startsWith("t") || b.startsWith("t") || c.startsWith("t")) {
                            add(listOf(a, b, c).sorted().joinToString(","))
                        }
                    }
                }
            }
        }
    }.count()

    fun part2() = 0

    part1().println()
    part2().println()
}
