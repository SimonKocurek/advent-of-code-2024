fun main() {

    val input = readInput("Day23")
        .map { it.split("-") }

    fun buildGraph(): Map<String, Set<String>> = buildMap<String, MutableSet<String>> {
        input.forEach { (from, to) ->
            getOrPut(from) { mutableSetOf() }.add(to)
            getOrPut(to) { mutableSetOf() }.add(from)
        }
    }

    val graph = buildGraph()

    fun joinOneToLanParty(groups: Map<Set<String>, Set<String>>) = buildMap<Set<String>, Set<String>> {
        groups.forEach { (aGroup, aNeighbours) ->
            aNeighbours.forEach { b ->
                val entry = aGroup + b
                if (entry in this) return@forEach

                val bNeighbours = graph[b]!!
                val commonNeighbours = aNeighbours.intersect(bNeighbours)
                this[entry] = commonNeighbours
            }
        }
    }

    fun part1(): Int {
        val groupsOf2 = joinOneToLanParty(groups = graph.mapKeys { setOf(it.key) })
        val groupsOf3 = joinOneToLanParty(groups = groupsOf2)

        return groupsOf3
            .keys
            .filter { parts -> parts.any { it.startsWith("t") } }
            .count()
    }

    fun part2(): String {
        var lanParty = graph.mapKeys { setOf(it.key) }

        while (true) {
            val newLanParty = joinOneToLanParty(groups = lanParty)

            if (newLanParty.isEmpty()) {
                break
            }

            lanParty = newLanParty
        }

        return lanParty
            .keys
            .first()
            .sorted()
            .joinToString(",")
    }

    part1().println()
    part2().println()
}
