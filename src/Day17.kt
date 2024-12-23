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

        // jump will be skipped when 'a' = 0
        // for the program to terminate, we need to stop jumping,
        // so at the end, 'a' has to be 0
        var possibleA = mutableSetOf<Long>()
        possibleA.add(0L)

        // Go from the back of the program and try to find all possible 'a' values
        // that would match the ending of the program
        for (matchFromEnd in 1..program.size) {
            val newPossibleA = mutableSetOf<Long>()

            possibleA.forEach { a ->
                // Only digits between 0..7 can be printed due to `% 8`
                for (moduloOffset in 0..7) {
                    val triedA = a + moduloOffset
                    val output = runProgram(triedA, b, c, program)

                    if (output.toList().takeLast(matchFromEnd) != program.takeLast(matchFromEnd)) {
                        continue
                    }

                    val foundA = if (matchFromEnd == program.size) triedA else triedA * 8
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
