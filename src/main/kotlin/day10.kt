import utils.Coordinate
import utils.readInputLines

/** [https://adventofcode.com/2024/day/10] */
class Trails : AdventOfCodeTask {
    override fun run(part2: Boolean): Any {
        val map = readInputLines("10.txt").flatMapIndexed { y, line ->
            line.mapIndexed { x, c -> Coordinate(x, y) to if (c == '.') -1 else c.digitToInt() }
        }.toMap()

        fun discoverTrail(path: List<Coordinate>, height: Int): Set<List<Coordinate>> {
            if (height == 9) return setOf(if (part2) path else listOf(path.last()))

            return path.last().adjacent().values.filter { map[it] == height + 1 }
                .flatMap { discoverTrail(path + it, height + 1) }.toSet()
        }

        return map.filterValues { it == 0 }.keys.flatMap { discoverTrail(listOf(it), 0) }.size
    }
}

fun main() {
    print(Trails().run(part2 = true))
}
