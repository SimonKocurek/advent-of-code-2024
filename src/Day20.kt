import kotlin.math.min

fun main() {

    val input = readInput("Day20")
        .map { it.toList() }

    val (sY, sX) = input.findPosition('S')
    val (eY, eX) = input.findPosition('E')

    fun distanceFrom(sourceX: Int, sourceY: Int): List<List<Int>> {
        val distance = MutableList(input.size) {
            MutableList(input[0].size) { -1 }
        }
        distance[sourceY][sourceX] = 0

        var wave = listOf(sourceY to sourceX)
        while (wave.isNotEmpty()) {
            val newWave = mutableListOf<Pair<Int, Int>>()

            wave.forEach { (y, x) ->
                directions4.forEach { (dy, dx) ->
                    val newY = y + dy
                    val newX = x + dx

                    if (input[newY][newX] == '#') {
                        return@forEach
                    }

                    if (distance[newY][newX] == -1) {
                        distance[newY][newX] = distance[y][x] + 1
                        newWave.add(newY to newX)
                    }
                }
            }

            wave = newWave
        }

        return distance
    }

    fun part1(): Int {
        val distancesFromEnd = distanceFrom(eX, eY)
        val distancesFromStart = distanceFrom(sX, sY)

        val nonCheatingTime = distancesFromEnd[sY][sX]

        val cheatsBySavedSeconds = mutableMapOf<Int, Int>()
        input.doubleLoop { cheatStartY, cheatStartX ->
            if (distancesFromEnd[cheatStartY][cheatStartX] == -1) {
                // Not reachable
                return@doubleLoop
            }

            directions4.forEach { (dy, dx) ->
                val cheatY = cheatStartY + dy
                val cheatX = cheatStartX + dx

                if (input[cheatY][cheatX] != '#') {
                    // Cheating on already empty space won't save any time
                    return@forEach
                }

                directions4.forEach { (dy2, dx2) ->
                    val cheatEndY = cheatY + dy2
                    val cheatEndX = cheatX + dx2

                    if (cheatEndX == cheatStartX && cheatEndY == cheatStartY) {
                        // Cheat returned back to the original position
                        return@forEach
                    }

                    if (!input.inBounds(cheatEndY, cheatEndX)) {
                        return@forEach
                    }

                    if (distancesFromEnd[cheatEndY][cheatEndX] == -1) {
                        // Cheat arrived at an unreachable position
                        return@forEach
                    }

                    val timeWithCheat = distancesFromStart[cheatStartY][cheatStartX] + 2 + distancesFromEnd[cheatEndY][cheatEndX]
                    val savedTime = nonCheatingTime - timeWithCheat
                    if (savedTime >= 100) {
                        cheatsBySavedSeconds.merge(savedTime, 1, Int::plus)
                    }
                }
            }
        }

        return cheatsBySavedSeconds.values.sum()
    }

    fun part2() = 0

    part1().println()
    part2().println()
}
