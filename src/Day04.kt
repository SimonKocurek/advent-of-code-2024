fun main() {
    fun part1(input: List<String>): Long {
        val word = "XMAS"
        val directions = listOf(
            listOf(1, 0),
            listOf(1, 1),
            listOf(0, 1),
            listOf(-1, 1),
            listOf(-1, 0),
            listOf(-1, -1),
            listOf(0, -1),
            listOf(1, -1),
        )

        var result = 0L

        fun crawl(i: Int, x: Int, y: Int, xDiff: Int, yDiff: Int) {
            val newX = x + xDiff
            val newY = y + yDiff

            if (newY < 0 || newY >= input.size || newX < 0 || newX >= input[newY].length) {
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

        for (y in input.indices) {
            for (x in input[y].indices) {
                if (input[y][x] != word[0]) {
                    continue
                }

                directions.forEach { (xDiff, yDiff) ->
                    crawl(0, x, y, xDiff, yDiff)
                }
            }
        }

        return result
    }

    fun part2(input: List<String>): Long {
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

        for (y in 1 until input.size - 1) {
            for (x in 1 until input[y].length - 1) {
                if (input[y][x] != 'A') {
                    continue
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
        }

        return result
    }

    val input = readInput("Day04")
    part1(input).println()
    part2(input).println()
}
