import kotlin.math.abs

fun main() {

    val input = readInput("Day20")
        .map { it.toList() }

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

    fun distance(x: Int, y: Int, x2: Int, y2: Int) = abs(x - x2) + abs(y - y2)

    val (sY, sX) = input.findPosition('S')
    val (eY, eX) = input.findPosition('E')

    val distancesFromEnd = distanceFrom(eX, eY)
    val distancesFromStart = distanceFrom(sX, sY)

    val nonCheatingTime = distancesFromEnd[sY][sX]

    fun cheatsThatSaveTime(needsToSaveTime: Int, cheatLength: Int): Long {
        val cheatsBySavedSeconds = mutableMapOf<Int, Long>()
        input.doubleLoop { cheatStartY, cheatStartX ->
            if (distancesFromEnd[cheatStartY][cheatStartX] == -1) {
                // Not reachable
                return@doubleLoop
            }

            for (dy in -cheatLength..cheatLength) {
                for (dx in -cheatLength..cheatLength) {
                    if (dy == 0 && dx == 0) {
                        continue
                    }
                    val cheatEndY = cheatStartY + dy
                    val cheatEndX = cheatStartX + dx

                    val cheatDistance = distance(cheatStartX, cheatStartY, cheatEndX, cheatEndY)
                    if (cheatDistance > cheatLength) {
                        continue
                    }

                    if (!input.inBounds(cheatEndY, cheatEndX)) {
                        continue
                    }

                    if (distancesFromEnd[cheatEndY][cheatEndX] == -1) {
                        // Cheat arrived at an unreachable position
                        continue
                    }

                    val timeWithCheat = distancesFromStart[cheatStartY][cheatStartX] + cheatDistance + distancesFromEnd[cheatEndY][cheatEndX]
                    val savedTimeByCheat = nonCheatingTime - timeWithCheat
                    if (savedTimeByCheat >= needsToSaveTime) {
                        cheatsBySavedSeconds.merge(needsToSaveTime, 1L, Long::plus)
                    }
                }
            }
        }

        return cheatsBySavedSeconds.values.sum()
    }

    fun part1() = cheatsThatSaveTime(needsToSaveTime = 100, cheatLength = 2)

    fun part2()  = cheatsThatSaveTime(needsToSaveTime = 100, cheatLength = 20)

    part1().println()
    part2().println()
}
