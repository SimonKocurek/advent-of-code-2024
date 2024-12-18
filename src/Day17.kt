fun main() {

    fun part1(input: List<String>): String {
        var a = input[0].takeLastWhile { it.isDigit() }.toInt()
        var b = input[1].takeLastWhile { it.isDigit() }.toInt()
        var c = input[2].takeLastWhile { it.isDigit() }.toInt()

        val program = input
            .last()
            .substring("Program: ".length)
            .split(",")
            .map { it.toInt() }

        fun Int.combo() = when (this) {
            in 0..3 -> this
            4 -> a
            5 -> b
            6 -> c
            else -> error("Unexpected combo ")
        }

        val output = mutableListOf<Int>()

        var i = 0
        while (i < program.size) {
            val op = program[i++]
            when (op) {
                0 -> {
                    if (i == program.size) break
                    a /= (1 shl program[i++].combo())
                }

                1 -> {
                    if (i == program.size) break
                    b = b.xor(program[i++])
                }

                2 -> {
                    if (i == program.size) break
                    b = program[i++].combo() % 8
                }

                3 -> if (a != 0) {
                    i = program[i]
                }

                4 -> {
                    if (i == program.size) break
                    b = b.xor(c)
                    i++
                }

                5 -> {
                    if (i == program.size) break
                    output.add(program[i++].combo() % 8)
                }

                6 -> {
                    if (i == program.size) break
                    b = a / (1 shl program[i++].combo())
                }

                7 -> {
                    if (i == program.size) break
                    c = a / (1 shl program[i++].combo())
                }
            }
        }
        return output.joinToString(",")
    }

    fun part2(input: List<String>): Int {
        // 0,3,5,4,3,0

        // 5 -> print
        // 3 -> jump
        //
        // combo % 8 == 0
        // ...
        // combo % 8 == 3
        // ..
        // combo % 8 == 4
        // ..
        return 0
    }

    val input = readInput("Day17")
    part1(input).println()
    part2(input).println()
}
