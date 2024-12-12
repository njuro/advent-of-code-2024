import utils.Coordinate
import utils.Direction
import utils.readInputLines

/** [https://adventofcode.com/2024/day/12] */
class Fences : AdventOfCodeTask {
    override fun run(part2: Boolean): Any {
        val map = readInputLines("12.txt").flatMapIndexed { y, line ->
            line.mapIndexed { x, c -> Coordinate(x, y) to c }
        }.toMap()

        val seen = mutableSetOf<Coordinate>()
        val regions = mutableSetOf<Set<Coordinate>>()
        while (seen.size != map.size) {
            val queue = mutableListOf(map.keys.first { it !in seen })
            val region = mutableSetOf<Coordinate>()
            while (queue.isNotEmpty()) {
                val current = queue.removeFirst()
                if (current in seen) continue
                seen.add(current)
                region.add(current)
                val neighbours = current.adjacent().values.filter {
                    map[it] == map[current]
                }
                queue.addAll(neighbours)
            }
            regions.add(region)
        }

        return regions.sumOf { region ->
            if (part2) {
                val sides = region.flatMap { block -> block.adjacent().entries.filter { it.value !in region } }
                    .groupBy { it.key }.mapValues { (_, blocks) -> blocks.map { it.value } }

                sides.entries.sumOf { (direction, blocks) ->
                    blocks.sortedWith(
                        if (direction in setOf(Direction.UP, Direction.DOWN))
                            compareBy({ it.y }, { it.x }) else compareBy({ it.x }, { it.y })
                    ).zipWithNext().count { (prev, next) -> prev.distanceTo(next) != 1 } + 1
                }
            } else {
                region.sumOf { block -> block.adjacent().values.count { it !in region } }
            } * region.size
        }

    }
}

fun main() {
    print(Fences().run(part2 = true))
}
