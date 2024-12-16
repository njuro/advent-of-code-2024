import utils.Coordinate
import utils.Direction
import utils.readInputLines

/** [https://adventofcode.com/2024/day/16] */
class Maze : AdventOfCodeTask {
    override fun run(part2: Boolean): Any {
        val map = readInputLines("16.txt").flatMapIndexed { y, line ->
            line.mapIndexed { x, c -> Coordinate(x, y) to c }
        }.toMap()
        val start = map.filterValues { it == 'S' }.keys.single()
        val end = map.filterValues { it == 'E' }.keys.single()

        val scores = mutableMapOf<Pair<Coordinate, Direction>, Int>()
        val queue = mutableListOf(Triple(listOf(start), Direction.RIGHT, 0))
        var minScore = Int.MAX_VALUE
        val bestPaths = mutableMapOf<List<Coordinate>, Int>()
        while (queue.isNotEmpty()) {
            val (path, direction, score) = queue.removeFirst()
            if (score > minScore) continue
            val position = path.last()
            val oldScore = scores[position to direction] ?: Int.MAX_VALUE
            if (oldScore < score) continue
            scores[position to direction] = score
            if (position == end) {
                minScore = score
                bestPaths[path] = score
                continue
            }

            position.move(direction, offset = true).takeUnless { map[it] == '#' }?.also {
                queue.add(Triple(path + it, direction, score + 1))
            }
            queue.add(Triple(path, direction.turnRight(), score + 1000))
            queue.add(Triple(path, direction.turnLeft(), score + 1000))
        }

        return if (part2) bestPaths.filterValues { it == minScore }.keys.flatten().distinct().size else minScore

    }
}

fun main() {
    print(Maze().run(part2 = true))
}
