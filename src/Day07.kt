fun main() {

    fun part1(input: List<String>): Long {
        val operators = listOf<Long.(Long) -> Long>(
            Long::plus,
            Long::times
        )

        return input.sumOf { line ->
            val (expectedString, parts) = line.split(":")

            val expected = expectedString.toLong()
            val nums = parts.trim().split(" ").map { it.toLong() }

            fun canCombine(numsIdx: Int, got: Long): Boolean {
                if (numsIdx == nums.size) {
                    return got == expected
                }

                for (operator in operators) {
                    val newGot = operator(got, nums[numsIdx])
                    if (newGot <= expected && canCombine(numsIdx + 1, newGot)) {
                        return true
                    }
                }

                return false
            }

            if (canCombine(1, nums.first())) {
                expected
            } else {
                0L
            }
        }
    }

    fun part2(input: List<String>): Long {
        fun Long.concat(added: Long) = (this.toString() + added.toString()).toLong()

        val operators = listOf<Long.(Long) -> Long>(
            Long::plus,
            Long::times,
            Long::concat,
        )

        return input.sumOf { line ->
            val (expectedString, parts) = line.split(":")

            val expected = expectedString.toLong()
            val nums = parts.trim().split(" ").map { it.toLong() }

            fun canCombine(numsIdx: Int, got: Long): Boolean {
                if (numsIdx == nums.size) {
                    return got == expected
                }

                for (operator in operators) {
                    val newGot = operator(got, nums[numsIdx])
                    if (newGot <= expected && canCombine(numsIdx + 1, newGot)) {
                        return true
                    }
                }

                return false
            }

            if (canCombine(1, nums.first())) {
                expected
            } else {
                0L
            }
        }
    }

    val input = readInput("Day07")
    part1(input).println()
    part2(input).println()
}
