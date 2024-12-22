import utils.Coordinate
import utils.Direction
import utils.readInputLines
import java.util.concurrent.ConcurrentSkipListMap

/** [https://adventofcode.com/2024/day/21] */
class Codes : AdventOfCodeTask {
    override fun run(part2: Boolean): Any {
        fun List<List<Char?>>.toMap(): Map<Coordinate, Char?> =
            flatMapIndexed { y, list ->
                list.mapIndexed { x, c -> Coordinate(x, y) to c }
            }.toMap()

        val numericalKeypad = listOf(
            listOf('7', '8', '9'),
            listOf('4', '5', '6'),
            listOf('1', '2', '3'),
            listOf(null, '0', 'A')
        ).toMap()
        val directionalKeypad = listOf(
            listOf(null, '^', 'A'),
            listOf('<', 'v', '>')
        ).toMap()
        val codes = readInputLines("21.txt")

        val possiblePathsCache =
            ConcurrentSkipListMap<Pair<Char, Char>, Set<String>>(compareBy({ it.first }, { it.second }))

        fun Map<Coordinate, Char?>.discoverPath(fromButton: Char, toButton: Char): Set<String> =
            possiblePathsCache.computeIfAbsent(fromButton to toButton) {
                if (fromButton == toButton) {
                    return@computeIfAbsent setOf("A")
                }
                val seen = mutableSetOf<Coordinate>()
                val start = entries.single { it.value == fromButton }.key
                val queue = mutableListOf("" to start)
                val results = mutableSetOf<String>()
                var maxLength = Int.MAX_VALUE - 1
                while (queue.isNotEmpty()) {
                    val (sequence, position) = queue.removeFirst()
                    if (sequence.length > maxLength) {
                        continue
                    }
                    if (get(position) == toButton) {
                        results += sequence + 'A'
                        maxLength = sequence.length + 1
                        continue
                    }
                    seen.add(position)
                    position.adjacent(offset = true).filter { (_, next) -> next !in seen && get(next) != null }
                        .forEach { (direction, next) ->
                            val char = when (direction) {
                                Direction.UP -> '^'
                                Direction.RIGHT -> '>'
                                Direction.DOWN -> 'v'
                                Direction.LEFT -> '<'
                            }
                            queue.add(sequence + char to next)
                        }
                }
                results
            }

        fun Map<Coordinate, Char?>.discoverAllPaths() {
            values.forEach { button1 ->
                values.forEach { button2 ->
                    if (button1 != null && button2 != null) {
                        discoverPath(button1, button2)
                    }
                }
            }
        }
        directionalKeypad.discoverAllPaths()
        numericalKeypad.discoverAllPaths()

        val bestPathCache = mutableMapOf<Pair<Char, Char>, String>()
        fun getBestPath(fromButton: Char, toButton: Char): String =
            bestPathCache.computeIfAbsent(fromButton to toButton) {
                val candidates = possiblePathsCache[fromButton to toButton]!!
                candidates.singleOrNull() ?: candidates.minBy {
                    it.zipWithNext { a, b -> possiblePathsCache[a to b]!!.first().length }.sum()
                }
            }

        val codeLengthCache = ConcurrentSkipListMap<Pair<String, Int>, Long>(compareBy({ it.first }, { it.second }))
        fun calculateCodeLength(code: String, iterations: Int): Long =
            codeLengthCache.computeIfAbsent(code to iterations) {
                if (iterations == 0)
                    code.length.toLong()
                else
                    "A$code".zipWithNext { prev, next ->
                        calculateCodeLength(getBestPath(prev, next), iterations - 1)
                    }.sum()
            }

        return codes.sumOf { code ->
            calculateCodeLength(code, if (part2) 26 else 3) * code.dropLast(1).toInt()
        }
    }
}

fun main() {
    print(Codes().run(part2 = false))
}
