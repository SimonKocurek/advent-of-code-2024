import java.util.Stack

fun main() {

    fun part1(input: List<String>): Long {
        var result = 0L

        val seen = input.map { it.map { false }.toMutableList() }

        input.doubleLoop { y, x ->
            if (seen[y][x]) {
                return@doubleLoop
            }

            var size = 1
            var edge = 0
            val stack = Stack<Pair<Int, Int>>().apply {
                add(x to y)
            }
            seen[y][x] = true
            while (stack.isNotEmpty()) {
                val (currX, currY) = stack.pop()

                for ((xDiff, yDiff) in directions4) {
                    val newX = currX + xDiff
                    val newY = currY + yDiff

                    if (!input.inBounds(newY, newX)) {
                        edge++
                        continue
                    }

                    if (input[newY][newX] == input[currY][currX]) {
                        if (seen[newY][newX]) {
                            continue
                        }

                        size++
                        stack.add(newX to newY)
                        seen[newY][newX] = true
                    } else {
                        edge++
                    }
                }

            }

            result += size * edge
        }

        return result
    }

    fun part2(input: List<String>): Long {
        val maxY = input.size - 1
        val maxX = input[0].length - 1

        val plotId = input.map { it.map { -1 }.toMutableList() }
        var currId = 0

        input.doubleLoop { y, x ->
            if (plotId[y][x] != -1) {
                return@doubleLoop
            }

            val stack = Stack<Pair<Int, Int>>().apply {
                add(x to y)
            }
            plotId[y][x] = currId
            while (stack.isNotEmpty()) {
                val (currX, currY) = stack.pop()

                for ((xDiff, yDiff) in directions4) {
                    val newX = currX + xDiff
                    val newY = currY + yDiff

                    if (!input.inBounds(newY, newX)) {
                        continue
                    }

                    if (input[newY][newX] == input[currY][currX]) {
                        if (plotId[newY][newX] != -1) {
                            continue
                        }

                        stack.add(newX to newY)
                        plotId[newY][newX] = currId
                    }
                }

            }

            currId++
        }

        val corners = mutableMapOf<Int, Int>()

        input.doubleLoop { y, x ->
            var currentCorners = 0

            val c = plotId[y][x]

            // outer corners
            val topEdge = x == maxX || c != plotId[y][x + 1]
            val bottomEdge = x == 0 || c != plotId[y][x - 1]
            val rightEdge = y == maxY || c != plotId[y + 1][x]
            val leftEdge = y == 0 || c != plotId[y - 1][x]

            if (topEdge && leftEdge) {
                currentCorners++
            }
            if (leftEdge && bottomEdge) {
                currentCorners++
            }
            if (bottomEdge && rightEdge) {
                currentCorners++
            }
            if (rightEdge && topEdge) {
                currentCorners++
            }

            // inner corners
            val topSame = y > 0 && c == plotId[y - 1][x]
            val bottomSame = y < maxY && c == plotId[y + 1][x]
            val rightSame = x < maxX && c == plotId[y][x + 1]
            val leftSame = x > 0 && c == plotId[y][x - 1]
            if (topSame && leftSame && plotId[y - 1][x - 1] != c) {
                currentCorners++
            }
            if (leftSame && bottomSame && plotId[y + 1][x - 1] != c) {
                currentCorners++
            }
            if (bottomSame && rightSame && plotId[y + 1][x + 1] != c) {
                currentCorners++
            }
            if (rightSame && topSame && plotId[y - 1][x + 1] != c) {
                currentCorners++
            }

            corners.merge(c, currentCorners) { a, b -> a + b }
        }

        return plotId
            .flatten()
            .groupingBy { it }
            .eachCount()
            .map { (id, size) -> size * corners[id]!! }
            .sum()
            .toLong()
    }

    val input = readInput("Day12")
    part1(input).println()
    part2(input).println()
}
