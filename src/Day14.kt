import java.awt.image.BufferedImage
import java.awt.image.BufferedImage.TYPE_INT_RGB
import java.io.File
import javax.imageio.ImageIO

fun main() {

    val maxX = 101
    val maxY = 103

    data class Robot(
        var x: Int,
        var y: Int,
        val vX: Int,
        val vY: Int,
    )

    fun parseRobots(input: List<String>) = input
        .map { line ->
            val parsed = line.split(" ").map { part ->
                part
                    .substring("v=".length)
                    .split(",")
                    .map { it.toInt() }
            }
            Robot(
                x = parsed[0][0],
                y = parsed[0][1],
                vX = parsed[1][0],
                vY = parsed[1][1]
            )
        }

    fun Robot.move() {
        x = (x + vX) % maxX
        y = (y + vY) % maxY

        if (x < 0) x += maxX
        if (y < 0) y += maxY
    }

    fun part1(input: List<String>): Int {
        val robots = parseRobots(input)

        for (i in 1..100) {
            robots.forEach { it.move() }
        }

        val halfX = maxX / 2
        val halfY = maxY / 2
        val quadrants = robots.groupingBy {
            when {
                it.x < halfX && it.y < halfY -> 0
                it.x < halfX && it.y > halfY -> 1
                it.x > halfX && it.y < halfY -> 2
                it.x > halfX && it.y > halfY -> 3
                else -> -1
            }
        }.eachCount()

        return (quadrants[0] ?: 0) *
                (quadrants[1] ?: 0) *
                (quadrants[2] ?: 0) *
                (quadrants[3] ?: 0)
    }

    fun part2(input: List<String>) {
        val robots = parseRobots(input)

        for (i in 1..10_000) {
            val image = BufferedImage(maxX, maxY, TYPE_INT_RGB)

            robots
                .onEach { it.move() }
                // Drawing a single point didn't work, so we do with a circle of size 1
                .forEach { image.setRGB(it.x, it.y, (255 * 65536) + (255 * 256) + 255) }

            File("day14/$i.jpg").apply {
                createNewFile()
                ImageIO.write(image, "jpg", this)
            }
        }
    }

    val input = readInput("Day14")
    part1(input).println()
    part2(input).println()
}
