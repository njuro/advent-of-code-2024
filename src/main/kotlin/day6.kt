import utils.Coordinate
import utils.Direction
import utils.readInputLines

/** [https://adventofcode.com/2024/day/6] */
class Day6 : AdventOfCodeTask {
    override fun run(part2: Boolean): Any {
        val map = readInputLines("6.txt").flatMapIndexed { y, line ->
            line.mapIndexed { x, c -> Coordinate(x, y) to c }
        }.toMap()

        val start = map.entries.first { it.value == '^' }.key

        fun evaluate(map: Map<Coordinate, Char>): List<Coordinate> {
            var current = start
            var direction = Direction.UP
            val seen = mutableSetOf(current to direction)
            while (true) {
                val next = current.move(direction, offset = true)
                when {
                    (next to direction) in seen -> return emptyList()
                    map[next] == '#' -> direction = direction.turnRight()
                    map[next] == null -> break
                    else -> current = next
                }
                seen.add(current to direction)
            }
            return seen.map { it.first }
        }

        val path = evaluate(map).distinct()

        return if (part2) path.count { candidate ->
            val newMap = map.toMutableMap().apply { put(candidate, '#') }
            evaluate(newMap).isEmpty()
        } - 1 else path.size
    }
}

fun main() {
    print(Day6().run(part2 = true))
}
