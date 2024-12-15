import java.math.BigInteger
import java.security.MessageDigest
import java.util.PriorityQueue
import kotlin.io.path.Path
import kotlin.io.path.readText

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = Path("src/$name.txt").readText().trim().lines()

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')

/**
 * The cleaner shorthand for printing output.
 */
fun Any?.println() = println(this)

/**
 * Up, down, left, right and diagonals
 */
val directions8 = listOf(
    listOf(1, 0),
    listOf(1, 1),
    listOf(0, 1),
    listOf(-1, 1),
    listOf(-1, 0),
    listOf(-1, -1),
    listOf(0, -1),
    listOf(1, -1),
)

/**
 * Up, down, left and right
 */
val directions4 = listOf(
    -1 to 0,
    0 to 1,
    1 to 0,
    0 to -1
)

fun List<List<*>>.inBounds(y: Int, x: Int) = y in indices && x in this[y].indices

@JvmName("inBoundsString")
fun List<CharSequence>.inBounds(y: Int, x: Int) = y in indices && x in this[y].indices

fun List<List<*>>.doubleLoop(iteration: (y: Int, x: Int) -> Unit) {
    for (y in this.indices) {
        for (x in this[y].indices) {
            iteration(y, x)
        }
    }
}

@JvmName("doubleLoopString")
fun List<CharSequence>.doubleLoop(iteration: (y: Int, x: Int) -> Unit) {
    for (y in this.indices) {
        for (x in this[y].indices) {
            iteration(y, x)
        }
    }
}

fun <T> List<List<T>>.findPosition(element: T): Pair<Int, Int> {
    for (y in this.indices) {
        for (x in this[y].indices) {
            if (this[y][x] == element) {
                return y to x
            }
        }
    }
    return -1 to -1
}

/**
 * More efficient implementation of `.sorted().take(count)` for small `count`
 */
fun <T: Comparable<T>> Collection<T>.takeSorted(count: Int): List<T> {
    val sorted = PriorityQueue<T>(count, compareByDescending { it })

    forEach {
        if (sorted.size < count) {
            sorted.add(it)
        } else if (it < sorted.peek()) {
            sorted.poll()
            sorted.add(it)
        }
    }

    val result = ArrayList<T>(count)
    for (i in 0 until count) {
        result.add(sorted.poll())
    }
    result.reverse()
    return result
}
