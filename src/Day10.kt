import java.util.Stack

fun main() {

    fun getZeros(map: List<List<Int>>): MutableList<Pair<Int, Int>> {
        val zeros = mutableListOf<Pair<Int, Int>>()
        map.doubleLoop { y, x ->
            if (map[y][x] == 0) {
                zeros.add(y to x)
            }
        }
        return zeros
    }

    fun part1(input: List<String>): Int {
        val map = input.map { line -> line.map { it.digitToInt() } }
        val zeros = getZeros(map)

        var result = 0

        for ((startY, startX) in zeros) {
            val seen = map.map { line -> line.map { false }.toMutableList() }

            val following = Stack<Pair<Int, Int>>().apply {
                add(startY to startX)
                seen[startY][startX] = true
            }

            while (following.isNotEmpty()) {
                val (y, x) = following.pop()
                for ((diffY, diffX) in directions4) {
                    val newY = y + diffY
                    val newX = x + diffX

                    if (!map.inBounds(newY, newX)) {
                        continue
                    }

                    if (seen[newY][newX]) {
                        continue
                    }

                    if (map[newY][newX] - 1 != map[y][x]) {
                        continue
                    }

                    seen[newY][newX] = true
                    if (map[newY][newX] == 9) {
                        result++
                    } else {
                        following.add(newY to newX)
                    }
                }
            }
        }

        return result
    }

    fun part2(input: List<String>): Long {
        val map = input.map { line -> line.map { it.digitToInt() } }

        val waysToReach = map.map { line -> line.map { 0 }.toMutableList() }
        getZeros(map).forEach { (y, x) -> waysToReach[y][x] = 1 }

        var result = 0L
        for (i in 1 .. 9) {

            map.doubleLoop { y, x ->
                if (map[y][x] != i) {
                    return@doubleLoop
                }

                for ((diffY, diffX) in directions4) {
                    val fromY = y + diffY
                    val fromX = x + diffX

                    if (!map.inBounds(fromY, fromX)) {
                        continue
                    }

                    if (map[fromY][fromX] + 1 != map[y][x]) {
                        continue
                    }

                    waysToReach[y][x] += waysToReach[fromY][fromX]
                    if (i == 9) {
                        result += waysToReach[fromY][fromX]
                    }
                }
            }
        }

        return result
    }

    val input = readInput("Day10")
    part1(input).println()
    part2(input).println()
}
