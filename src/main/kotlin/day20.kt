import utils.Coordinate
import utils.readInputLines

/** [https://adventofcode.com/2024/day/20] */
class Cheats : AdventOfCodeTask {
    override fun run(part2: Boolean): Any {
        val map = readInputLines("20.txt").flatMapIndexed { y, line ->
            line.mapIndexed { x, c -> Coordinate(x, y) to c }
        }.toMap()
        val start = map.filterValues { it == 'S' }.keys.single()
        val target = map.filterValues { it == 'E' }.keys.single()

        val queue = mutableListOf(listOf(start))
        var targetDistances = emptyMap<Coordinate, Int>()
        while (queue.isNotEmpty()) {
            val path = queue.removeLast()
            val current = path.last()
            if (current == target) {
                targetDistances = path.reversed().withIndex().associate { it.value to it.index }
                break
            }
            current.adjacent(offset = true).filterValues { it !in path && map[it] != '#' }.values.forEach { next ->
                queue.add(path.toMutableList().apply { add(next) })
            }
        }

        val maxCheatingDistance = if (part2) 20 else 2
        return targetDistances.entries.sumOf { (cheatStart, cheatStartTargetDistance) ->
            targetDistances.count { (cheatEnd, cheatEndTargetDistance) ->
                val cheatDistance = cheatStart.distanceTo(cheatEnd)
                cheatDistance <= maxCheatingDistance && (cheatEndTargetDistance - cheatStartTargetDistance - cheatDistance >= 100)
            }
        }
    }
}

fun main() {
    print(Cheats().run(part2 = false))
}
