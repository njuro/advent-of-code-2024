import utils.Coordinate
import utils.readInputLines

/** [https://adventofcode.com/2024/day/4] */
class Letters : AdventOfCodeTask {
    override fun run(part2: Boolean): Any {
        val letters = readInputLines("4.txt").flatMapIndexed { y, line ->
            line.mapIndexed { x, c -> Coordinate(x, y) to c }
        }.toMap().withDefault { '.' }

        return if (part2) {
            val leftDeltas = listOf(1 to -1, -1 to 1)
            val rightDeltas = listOf(-1 to -1, 1 to 1)
            letters.filterValues { it == 'A' }.keys.count { (x, y) ->
                val left = leftDeltas.map { (dx, dy) -> letters.getValue(Coordinate(x + dx, y + dy)) }.toSet()
                val right = rightDeltas.map { (dx, dy) -> letters.getValue(Coordinate(x + dx, y + dy)) }.toSet()
                left == setOf('M', 'S') && left == right
            }
        } else {
            val deltas = listOf(-1 to -1, -1 to 0, -1 to 1, 0 to -1, 0 to 1, 1 to -1, 1 to 0, 1 to 1)
            letters.filterValues { it == 'X' }.keys.sumOf { start ->
                deltas.count { (dx, dy) ->
                    (1..3).fold(start to "X") { (position, letter), _ ->
                        val next = Coordinate(position.x + dx, position.y + dy)
                        next to letter + letters.getValue(next)
                    }.second == "XMAS"
                }
            }
        }
    }
}

fun main() {
    print(Letters().run(part2 = true))
}
