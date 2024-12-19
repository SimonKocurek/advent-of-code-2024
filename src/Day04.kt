fun main() {
    val input = readInput("Day04")

    fun part1(): Long {
        val word = "XMAS"
        var result = 0L

        fun crawl(i: Int, x: Int, y: Int, xDiff: Int, yDiff: Int) {
            val newX = x + xDiff
            val newY = y + yDiff

            if (!input.inBounds(newY, newX)) {
                return
            }

            if (input[newY][newX] != word[i + 1]) {
                return
            }

            if (i + 1 == word.length - 1) {
                result++
            } else {
                crawl(i + 1, newX, newY, xDiff, yDiff)
            }
        }

        input.doubleLoop { y, x ->
            if (input[y][x] != word[0]) {
                return@doubleLoop
            }

            directions8.forEach { (xDiff, yDiff) ->
                crawl(0, x, y, xDiff, yDiff)
            }
        }

        return result
    }

    fun part2(): Long {
        val rotations = listOf(
            listOf(
                Triple(-1, -1, 'M'),
                Triple(-1, 1, 'M'),
                Triple(1, 1, 'S'),
                Triple(1, -1, 'S'),
            ),

            listOf(
                Triple(-1, -1, 'S'),
                Triple(-1, 1, 'M'),
                Triple(1, 1, 'M'),
                Triple(1, -1, 'S'),
            ),

            listOf(
                Triple(-1, -1, 'S'),
                Triple(-1, 1, 'S'),
                Triple(1, 1, 'M'),
                Triple(1, -1, 'M'),
            ),

            listOf(
                Triple(-1, -1, 'M'),
                Triple(-1, 1, 'S'),
                Triple(1, 1, 'S'),
                Triple(1, -1, 'M'),
            ),
        )

        var result = 0L

        input.doubleLoop { y, x ->
            if (input[y][x] != 'A') {
                return@doubleLoop
            }

            val found = rotations.any { rotation ->
                rotation.all { expected ->
                    input[y + expected.first][x + expected.second] == expected.third
                }
            }

            if (found) {
                result++
            }
        }

        return result
    }

    part1().println()
    part2().println()
}
