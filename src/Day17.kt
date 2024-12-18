fun main() {

    fun parseProgram(input: List<String>) = input
        .last()
        .substring("Program: ".length)
        .split(",")
        .map { it.toLong() }

    fun runProgram(
        initA: Long,
        initB: Long,
        initC: Long,
        program: List<Long>
    ) = sequence {
        var a = initA
        var b = initB
        var c = initC

        fun Long.combo() = when (this) {
            in 0L..3L -> this
            4L -> a
            5L -> b
            6L -> c
            else -> error("Unexpected combo ")
        }

        var i = 0
        while (i < program.size) {
            val op = program[i++]
            when (op) {
                0L -> {
                    if (i == program.size) break
                    a /= (1L shl program[i++].combo().toInt())
                }

                1L -> {
                    if (i == program.size) break
                    b = b.xor(program[i++])
                }

                2L -> {
                    if (i == program.size) break
                    b = program[i++].combo() % 8L
                }

                3L -> if (a != 0L) {
                    i = program[i].toInt()
                }

                4L -> {
                    if (i == program.size) break
                    b = b.xor(c)
                    i++
                }

                5L -> {
                    if (i == program.size) break
                    yield(program[i++].combo() % 8)
                }

                6L -> {
                    if (i == program.size) break
                    b = a / (1L shl program[i++].combo().toInt())
                }

                7L -> {
                    if (i == program.size) break
                    c = a / (1L shl program[i++].combo().toInt())
                }
            }
        }
    }

    fun part1(input: List<String>): String {
        val a = input[0].takeLastWhile { it.isDigit() }.toLong()
        val b = input[1].takeLastWhile { it.isDigit() }.toLong()
        val c = input[2].takeLastWhile { it.isDigit() }.toLong()

        val program = parseProgram(input)

        val output = runProgram(a, b, c, program)
        return output.joinToString(",")
    }

    fun part2(input: List<String>): Long {
        val b = input[1].takeLastWhile { it.isDigit() }.toLong()
        val c = input[2].takeLastWhile { it.isDigit() }.toLong()

        val program = parseProgram(input)

        // Note: There is a guarantee in the problem
        // description that jump will jump to instructions
        // and not to operands (treating them like instructions)

        // 3 -> jump -> will be skipped when 'a' = 0
        // for the program to terminate, we need to stop
        // jumping, so at the end:
        var possibleA = mutableSetOf<Long>()
        possibleA.add(0L)

        for (matchFromEnd in 1..program.size) {
            val newPossibleA = mutableSetOf<Long>()

            possibleA.forEach { a ->
                // 5 -> print -> We know that at the printed
                // `combo() % 8` was between 7 digits:
                for (triedA in a..a + 7) {
                    val output = runProgram(triedA, b, c, program)

                    if (output.toList().takeLast(matchFromEnd) != program.takeLast(matchFromEnd)) {
                        continue
                    }

                    val foundA = if (matchFromEnd == program.size) triedA else triedA shl 3
                    newPossibleA.add(foundA)
                }
            }

            possibleA = newPossibleA
        }

        return possibleA.min()
    }

    val input = readInput("Day17")
    part1(input).println()
    part2(input).println()
}
