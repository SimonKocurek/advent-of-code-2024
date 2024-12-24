fun main() {

    data class Gate(
        val a: String,
        val b: String,
        val op: (a: Long, b: Long) -> Long,
        val out: String
    )

    fun and(a: Long, b: Long) = a and b
    fun or(a: Long, b: Long) = a or b
    fun xor(a: Long, b: Long) = a xor b

    val input = readInput("Day24")

    val values = input
        .takeWhile { it.isNotEmpty() }
        .map { it.split(": ") }
        .associateBy({ it[0] }, { it[1].toLong() })

    val gates = input
        .takeLastWhile { it.isNotEmpty() }
        .map { it.split(" -> ", " ") }
        .map {
            Gate(
                a = it[0],
                op = when (it[1]) {
                    "AND" -> ::and
                    "OR" -> ::or
                    "XOR" -> ::xor
                    else -> error("Unknown operator")
                },
                b = it[2],
                out = it[3]
            )
        }
        .associateBy { it.out }

    fun part1(): Long {
        val cache = values.toMutableMap()

        fun getValue(output: String): Long {
            if (output !in cache) {
                val gate = gates[output]!!

                val a = getValue(gate.a)
                val b = getValue(gate.b)
                cache[output] = gate.op(a, b)
            }

            return cache[output]!!
        }

        return gates
            .keys
            .filter { it.startsWith("z") }
            .map { it.substring(1).toInt() to getValue(it) }
            .sortedByDescending { it.first }
            .joinToString("") { (_, value) -> value.toString() }
            .toLong(radix = 2)
    }

    fun part2() = 0

    part1().println()
    part2().println()
}
