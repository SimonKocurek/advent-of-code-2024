import java.awt.image.BufferedImage
import java.awt.image.BufferedImage.TYPE_INT_RGB
import java.io.File
import java.util.*
import javax.imageio.ImageIO
import kotlin.math.sqrt

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

        fun Robot.nearestRobots(robots: List<Robot>) =
            PriorityQueue(
                robots.map { other ->
                    sqrt((x - other.x) * (x - other.x) + (y - other.y) * (y - other.y).toDouble()).toLong()
                }

                // When robots are clumped up, the closest ones will be very close
                // If they are scattered, the closest ones will be fairly far away
            ).take(10).sum()

        fun List<Robot>.sparsity() = PriorityQueue(
            map { it.nearestRobots(this) }
        )
            // To eliminate outliers and detect clumping, we only consider the closest 20%
            .take(size / 5).sum()

        var i = 0
        while (robots.sparsity() > 1300) {
            i++
            robots.forEach { it.move() }
        }

        val image = BufferedImage(maxX, maxY, TYPE_INT_RGB).apply {
            robots.forEach { setRGB(it.x, it.y, (255 * 65536) + (255 * 256) + 255) }
        }

        val file = File("day14/$i.jpg").apply {
            createNewFile()
        }
        ImageIO.write(image, "jpg", file)
    }

    val input = readInput("Day14")
    part1(input).println()
    part2(input)
}
