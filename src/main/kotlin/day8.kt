import utils.Coordinate
import utils.readInputLines

/** [https://adventofcode.com/2024/day/8] */
class Antinodes : AdventOfCodeTask {
    override fun run(part2: Boolean): Any {
        val map = readInputLines("8.txt").flatMapIndexed { y, line ->
            line.mapIndexed { x, c -> Coordinate(x, y) to c }
        }.toMap().toMutableMap()

        val antinodes = map.filterValues { it != '.' }.flatMap { (start, sign) ->
            val targets = map.filter { it.value == sign && it.key != start }.keys
            targets.flatMap { target ->
                val diagonal1 = generateSequence(start) { prev ->
                    prev + Coordinate(
                        target.x - start.x,
                        target.y - start.y
                    )
                }.takeWhile { it in map }
                val diagonal2 = generateSequence(start) { prev ->
                    prev + Coordinate(
                        start.x - target.x,
                        start.y - target.y
                    )
                }.takeWhile { it in map }
                val diagonals = (diagonal1 + diagonal2).toList()

                if (part2) diagonals else diagonals.filter {
                    it != start && it != target &&
                            (it.distanceTo(start) == start.distanceTo(target) || it.distanceTo(target) == start.distanceTo(
                                target
                            ))
                }
            }
        }

        return antinodes.distinct().size
    }
}

fun main() {
    print(Antinodes().run(part2 = true))
}
