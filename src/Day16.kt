import java.util.Comparator
import java.util.PriorityQueue
import java.util.Stack

fun main() {

    data class State(
        val score: Int,
        val direction: Int,
        val x: Int,
        val y: Int,
        val previous: State? = null,
    )

    fun part1(input: List<String>): Int {
        val map = input.map { it.toList() }
        val (sY, sX) = map.findPosition('S')

        val seen = mutableSetOf<Triple<Int, Int, Int>>().apply {
            add(Triple(sX, sY, 1))
        }
        val options = PriorityQueue(Comparator.comparingInt(State::score)).apply {
            add(
                State(
                    score = 0,
                    direction = 1,
                    x = sX,
                    y = sY
                )
            )
        }
        while (options.isNotEmpty()) {
            val (score, direction, x, y) = options.poll()

            val (yDiff, xDiff) = directions4[direction]
            val newY = y + yDiff
            val newX = x + xDiff
            if (map[newY][newX] == 'E') {
                return score + 1
            }
            if (map[newY][newX] != '#' && seen.add(Triple(newX, newY, direction))) {
                options.add(
                    State(
                        score = score + 1,
                        direction = direction,
                        x = newX,
                        y = newY
                    )
                )
            }

            val rightTurn = (direction + 1) % 4
            if (seen.add(Triple(x, y, rightTurn))) {
                options.add(
                    State(
                        score = score + 1000,
                        direction = rightTurn,
                        x = x,
                        y = y
                    )
                )
            }

            val leftTurn = (direction - 1).let { if (it < 0) 3 else it }
            if (seen.add(Triple(x, y, leftTurn))) {
                options.add(
                    State(
                        score = score + 1000,
                        direction = leftTurn,
                        x = x,
                        y = y
                    )
                )
            }
        }

        return -1
    }

    fun part2(input: List<String>): Int {
        val map = input.map { it.toList() }
        val (sY, sX) = map.findPosition('S')
        val (eY, eX) = map.findPosition('E')

        val optimalPathTo = mutableMapOf<Triple<Int, Int, Int>, MutableList<State>>()
        val seenScore = mutableMapOf<Triple<Int, Int, Int>, Int>()
        val options = PriorityQueue(Comparator.comparingInt(State::score)).apply {
            add(
                State(
                    score = 0,
                    direction = 1,
                    x = sX,
                    y = sY,
                    previous = null
                )
            )
        }
        var finalScore = -1
        while (options.isNotEmpty()) {
            val state = options.peek()
            val (score, direction, x, y) = state

            options.poll()

            val currentTriple = Triple(y, x, direction)
            if (currentTriple in seenScore) {
                if (seenScore[currentTriple] == score) {
                    optimalPathTo[currentTriple]!!.add(state.previous!!)
                }
                continue
            }
            optimalPathTo[currentTriple] = listOfNotNull(state.previous).toMutableList()
            seenScore[currentTriple] = score

            if (score > finalScore && finalScore != -1) {
                break
            }
            if (x == eX && y == eY && finalScore == -1) {
                finalScore = score
            }

            val (yDiff, xDiff) = directions4[direction]
            val newY = y + yDiff
            val newX = x + xDiff
            if (map[newY][newX] != '#') {
                options.add(
                    State(
                        score = score + 1,
                        direction = direction,
                        x = newX,
                        y = newY,
                        previous = state
                    )
                )
            }

            options.add(
                State(
                    score = score + 1000,
                    direction = (direction + 1) % 4,
                    x = x,
                    y = y,
                    previous = state
                )
            )

            options.add(
                State(
                    score = score + 1000,
                    direction = (direction - 1).let { if (it < 0) 3 else it },
                    x = x,
                    y = y,
                    previous = state
                )
            )
        }

        val onBestPath = mutableSetOf<Pair<Int, Int>>()

        (0..3)
            .map { direction -> State(0, direction, eX, eY) }
            .filter { seenScore[Triple(it.y, it.x, it.direction)] == finalScore }
            .forEach {
                val nodes = Stack<State>().apply {
                    add(it)
                }

                while (nodes.isNotEmpty()) {
                    val node = nodes.pop()
                    onBestPath.add(node.y to node.x)
                    nodes.addAll(optimalPathTo[Triple(node.y, node.x, node.direction)] ?: emptyList())
                }
            }

        return onBestPath.size
    }

    val input = readInput("Day16")
    part1(input).println()
    part2(input).println()
}
