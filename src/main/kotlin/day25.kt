import utils.Coordinate
import utils.readInputBlock

/** [https://adventofcode.com/2024/day/25] */
class Locks : AdventOfCodeTask {
    override fun run(part2: Boolean): Any {
        val keys = mutableSetOf<List<Int>>()
        val locks = mutableSetOf<List<Int>>()
        readInputBlock("25.txt").split("\n\n").filter(String::isNotBlank).map(String::lines).forEach { schema ->
            val map = schema.flatMapIndexed { y, line -> line.mapIndexed { x, c -> Coordinate(x, y) to c } }.toMap()
            val heights = (0..4).map { x -> map.count { it.key.x == x && it.value == '#' } }
            if (map[Coordinate(0, 0)] == '#') locks.add(heights) else keys.add(heights)
        }

        return keys.sumOf { key ->
            locks.count { lock -> key.zip(lock).all { (a, b) -> a + b <= 7 } }
        }
    }
}

fun main() {
    print(Locks().run(part2 = false))
}
