fun main() {

    fun part1(input: List<String>): Long {
        val lines = input.iterator()

        var result = 0L

        while (lines.hasNext()) {
            // Button A: X+94, Y+34
            // Button B: X+22, Y+67
            // Prize: X=8400, Y=5400
            val buttonALine = lines.next()
            val buttonBLine = lines.next()
            val prizeLine = lines.next()
            if (lines.hasNext()) lines.next() // empty line

            // pX = a * aX + b * bX
            // pY = a * aY + b * bY
            // e.g.,
            // 8400 = 94a + 22b
            // 5400 = 34a + 67b
            val (aX, aY) = buttonALine
                .substring("Button A:".length)
                .split(",")
                .map { it.substring(" X".length) }
                .map { it.toInt() }

            val (bX, bY) = buttonBLine
                .substring("Button B:".length)
                .split(",")
                .map { it.substring(" X".length) }
                .map { it.toInt() }

            val (pX, pY) = prizeLine
                .substring("Prize:".length)
                .split(",")
                .map { it.substring(" X=".length) }
                .map { it.toLong() }

            // b = (pX - a * aX) / bX
            //
            // pY = a * aY + (pX - a * aX) / bX * bY

            // pY * bX = a * aY * bX + (pX - a * aX) * bY
            // pY * bX = a * aY * bX + bY * pX - a * aX * bY
            // pY * bX - bY * pX = a * aY * bX - a * aX * bY
            // pY * bX - bY * pX = a * (aY * bX - aX * bY)
            // a = (pY * bX - bY * pX) / (aY * bX - aX * bY)
            // b = (pX - a * aX) / bX
            //
            // e.g.
            //
            // b = (8400 - 94a) / 22
            //
            // 5400 = 34a + 67 (8400 - 94a) / 22
            // 118 800 = 748a + 67 (8400 - 94a)
            // 118 800 = 748a + 562 800 - 6298a
            // -444 000 = -5550a
            // a = 80
            // b = (8400 - 94 * 80) / 22
            // b = 40

            var a = (pY * bX - bY * pX) / (aY * bX - aX * bY)
            var b = (pX - a * aX) / bX

            if (a < 0 || b < 0) {
                b = (pY * aX - aY * pX) / (bY * aX - bX * aY)
                a = (pX - b * bX) / aX
            }

            // Check due to integer division errors
            if (
                aX * a + bX * b == pX &&
                aY * a + bY * b == pY
            ) {
                result += a * 3 + b
            }
        }

        return result
    }

    fun part2(input: List<String>): Long {
        val lines = input.iterator()

        var result = 0L

        while (lines.hasNext()) {
            // Button A: X+94, Y+34
            // Button B: X+22, Y+67
            // Prize: X=8400, Y=5400
            val buttonALine = lines.next()
            val buttonBLine = lines.next()
            val prizeLine = lines.next()
            if (lines.hasNext()) lines.next() // empty line

            // pX = a * aX + b * bX
            // pY = a * aY + b * bY
            // e.g.,
            // 8400 = 94a + 22b
            // 5400 = 34a + 67b
            val (aX, aY) = buttonALine
                .substring("Button A:".length)
                .split(",")
                .map { it.substring(" X".length) }
                .map { it.toInt() }

            val (bX, bY) = buttonBLine
                .substring("Button B:".length)
                .split(",")
                .map { it.substring(" X".length) }
                .map { it.toInt() }

            val (pX, pY) = prizeLine
                .substring("Prize:".length)
                .split(",")
                .map { it.substring(" X=".length) }
                .map { it.toLong() + 10_000_000_000_000 }

            // b = (pX - a * aX) / bX
            //
            // pY = a * aY + (pX - a * aX) / bX * bY

            // pY * bX = a * aY * bX + (pX - a * aX) * bY
            // pY * bX = a * aY * bX + bY * pX - a * aX * bY
            // pY * bX - bY * pX = a * aY * bX - a * aX * bY
            // pY * bX - bY * pX = a * (aY * bX - aX * bY)
            // a = (pY * bX - bY * pX) / (aY * bX - aX * bY)
            // b = (pX - a * aX) / bX
            //
            // e.g.
            //
            // b = (8400 - 94a) / 22
            //
            // 5400 = 34a + 67 (8400 - 94a) / 22
            // 118 800 = 748a + 67 (8400 - 94a)
            // 118 800 = 748a + 562 800 - 6298a
            // -444 000 = -5550a
            // a = 80
            // b = (8400 - 94 * 80) / 22
            // b = 40

            var a = (pY * bX - bY * pX) / (aY * bX - aX * bY)
            var b = (pX - a * aX) / bX

            if (a < 0 || b < 0) {
                b = (pY * aX - aY * pX) / (bY * aX - bX * aY)
                a = (pX - b * bX) / aX
            }

            // Check due to integer division errors
            if (
                aX * a + bX * b == pX &&
                aY * a + bY * b == pY
            ) {
                result += a * 3 + b
            }
        }

        return result
    }

    val input = readInput("Day13")
    part1(input).println()
    part2(input).println()
}
