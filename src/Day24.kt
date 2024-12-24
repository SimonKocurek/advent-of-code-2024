import kotlin.experimental.and
import kotlin.experimental.or
import kotlin.experimental.xor

fun main() {

    data class Gate(
        val a: String,
        val b: String,
        val op: (a: Byte, b: Byte) -> Byte,
        val out: String
    )

    fun and(a: Byte, b: Byte) = a and b
    fun or(a: Byte, b: Byte) = a or b
    fun xor(a: Byte, b: Byte) = a xor b

    val input = readInput("Day24")

    val inputValues = input
        .takeWhile { it.isNotEmpty() }
        .map { it.split(": ") }
        .associateBy({ it[0] }, { it[1].toByte() })

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

    fun MutableMap<String, Byte>.getMemoized(output: String): Byte {
        if (output !in this) {
            val gate = gates[output]!!

            val a = getMemoized(gate.a)
            val b = getMemoized(gate.b)
            this[output] = gate.op(a, b)
        }

        return this[output]!!
    }

    fun getDigits(name: String, cache: MutableMap<String, Byte>) = gates
        .keys
        .filter { it.startsWith(name) }
        .map { it.substring(1).toInt() to cache.getMemoized(it) }
        .sortedByDescending { it.first }
        .map { it.second }

    fun List<Byte>.digitsToLong() = joinToString("") { it.toString() }.toLong(radix = 2)

    fun getCircuitResult(values: Map<String, Byte>): Long {
        val cache = values.toMutableMap()

        return getDigits(name = "z", cache).digitsToLong()
    }

    fun validate() {
        // Helps find errors in the manually fixed circuit
        for (i in 0..1000) {
            val randomInputs = inputValues.toMutableMap().apply {
                for (key in keys) {
                    this[key] = (0..1).random().toByte()
                }
            }

            val result = getCircuitResult(values = randomInputs)

            val x = randomInputs
                .filterKeys { it.startsWith("x") }
                .mapKeys { it.key.substring(1).toInt() }
                .toList()
                .sortedByDescending { it.first }
                .map { it.second }
                .digitsToLong()

            val y = randomInputs
                .filterKeys { it.startsWith("y") }
                .mapKeys { it.key.substring(1).toInt() }
                .toList()
                .sortedByDescending { it.first }
                .map { it.second }
                .digitsToLong()

            if (result != x + y) {
                val got = result.toString(radix = 2)
                val expected = (x + y).toString(radix = 2)

                error("Validation failed - got: $got != $expected :expected")
            }
        }
    }

    fun part1() = getCircuitResult(values = inputValues)

    fun part2(): String {
        val cache = inputValues.toMutableMap()
        getDigits(name = "z", cache) // Populate the cache

        fun Gate.opName() = when (op) {
            ::and -> "AND"
            ::or -> "OR"
            ::xor -> "XOR"
            else -> error("Unknown operator")
        }

        fun Gate.opClass() = when (op) {
            ::and -> "add"
            ::or -> "or"
            ::xor -> "xor"
            else -> error("Unknown operator")
        }

        fun Gate.name() = "${a}_${opName()}_${b}:::${opClass()}"

        // It's easier to correct the errors by hand than to write a program to do it
        return buildString {
            appendLine("stateDiagram-v2")

            appendLine("\tclassDef output fill:#f00,color:white,font-weight:bold,stroke-width:2px,stroke:yellow")

            appendLine("\tclassDef add stroke-width:2px,stroke:red")
            appendLine("\tclassDef or stroke-width:2px,stroke:green")
            appendLine("\tclassDef xor stroke-width:2px,stroke:blue")

            fun String.keyClass() = if (first() == 'z') ":::output" else ""

            gates.forEach { (_, gate) ->
                // Some gates may not be used
                if (gate.a !in cache) {
                    return@forEach
                }
                if (gate.b !in cache) {
                    return@forEach
                }

                val operation = gate.name()

                if (!gate.a.startsWith("x") && !gate.a.startsWith("y")) {
                    appendLine("\t${gate.a} --> $operation")
                }
                if (!gate.b.startsWith("x") && !gate.b.startsWith("y")) {
                    appendLine("\t${gate.b} --> $operation")
                }

                appendLine("\t${operation} --> ${gate.out}${gate.out.keyClass()}")
            }
        }
    }

    part1().println()
    part2().println()
}
