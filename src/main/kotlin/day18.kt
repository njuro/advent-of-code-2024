import utils.Coordinate
import utils.readInputLines

/** [https://adventofcode.com/2024/day/18] */
class Paths : AdventOfCodeTask {
    override fun run(part2: Boolean): Any {
        val size = 70
        val bytes = readInputLines("18.txt").map { it.split(",").let { (x, y) -> Coordinate(x.toInt(), y.toInt()) } }
        val startingBytes = bytes.take(1024).toSet()
        val map = (0..size).flatMap { y ->
            (0..size).map { x ->
                Coordinate(x, y).let {
                    it to if (it in startingBytes) '#' else '.'
                }
            }
        }.toMap().toMutableMap()

        fun findPath(): Int {
            val seen = mutableSetOf<Coordinate>()
            val queue = mutableListOf(Coordinate(0, 0) to 0)
            while (queue.isNotEmpty()) {
                val (current, steps) = queue.removeFirst()
                if (current in seen) continue
                seen.add(current)
                if (current == Coordinate(size, size)) return steps
                current.adjacent().values.filter { map[it] == '.' }.forEach { queue.add(it to steps + 1) }
            }

            return -1
        }

        return if (part2) bytes.drop(startingBytes.size).first { nextByte ->
            map[nextByte] = '#'
            findPath() == -1
        }.let { "${it.x},${it.y}" } else findPath()
    }
}

fun main() {
    print(Paths().run(part2 = true))
}
