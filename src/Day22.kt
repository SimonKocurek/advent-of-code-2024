import kotlin.collections.ArrayDeque

fun main() {

    val input = readInput("Day22")
        .map { it.toLong() }

    fun Long.nextSecretNumber(): Long {
        var secretNumber = this

        secretNumber = (secretNumber xor (secretNumber * 64)) % 16777216
        secretNumber = (secretNumber xor (secretNumber / 32)) % 16777216
        secretNumber = (secretNumber xor (secretNumber * 2048)) % 16777216

        return secretNumber
    }

    fun part1() = input.sumOf { number ->
        var secretNumber = number

        for (i in 1..2000) {
            secretNumber = secretNumber.nextSecretNumber()
        }

        secretNumber
    }

    fun part2(): Long {
        val seenSequences = mutableSetOf<List<Long>>()

        val sequenceSellPrices = input.map { number ->
            val sequenceSellPrice = mutableMapOf<List<Long>, Long>()

            var secretNumber = number
            val changes = ArrayDeque<Long>(4)
            for (i in 1..2000) {
                val newSecretNumber = secretNumber.nextSecretNumber()
                val price = newSecretNumber % 10

                if (changes.size == 4) {
                    changes.removeFirst()
                }
                changes.addLast(price - (secretNumber % 10))

                if (changes.size == 4) {
                    val sequence = changes.toList()
                    if (sequence !in sequenceSellPrice) {
                        seenSequences.add(sequence)
                        sequenceSellPrice[sequence] = price
                    }
                }

                secretNumber = newSecretNumber
            }

            sequenceSellPrice
        }

        return seenSequences.maxOf { sequence ->
            sequenceSellPrices.sumOf { it[sequence] ?: 0 }
        }
    }

    part1().println()
    part2().println()
}
