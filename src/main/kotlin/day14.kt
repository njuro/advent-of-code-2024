import utils.Coordinate
import utils.readInputBlock
import kotlin.math.abs

/** [https://adventofcode.com/2024/day/14] */
class Robots : AdventOfCodeTask {
    override fun run(part2: Boolean): Any {
        val pattern = Regex("p=(-?\\d+),(-?\\d+) v=(-?\\d+),(-?\\d+)")
        val robots = pattern.findAll(readInputBlock("14.txt"))
            .map { it.groupValues.drop(1).map(String::toInt) }
            .map { (x, y, dx, dy) -> Coordinate(x, y) to Coordinate(dx, dy) }.toList()
        val width = 101
        val height = 103

        val (finalSeconds, finalRobots) = generateSequence(0 to robots) { (seconds, current) ->
            seconds + 1 to current.map { (position, vector) ->
                val newX = (position.x + vector.x).let {
                    if (it < 0) width - abs(it) else if (it >= width) it - width else it
                }
                val newY = (position.y + vector.y).let {
                    if (it < 0) height - abs(it) else if (it >= height) it - height else it
                }
                Coordinate(newX, newY) to vector
            }
        }.first { (seconds, current) ->
            if (part2)
                current.map { (position, _) -> position }.distinct().size == robots.size
            else
                seconds == 100
        }

        return if (part2)
            finalSeconds
        else
            listOf(
                finalRobots.count { (position, _) -> position.x < width / 2 && position.y < height / 2 },
                finalRobots.count { (position, _) -> position.x > width / 2 && position.y < height / 2 },
                finalRobots.count { (position, _) -> position.x < width / 2 && position.y > height / 2 },
                finalRobots.count { (position, _) -> position.x > width / 2 && position.y > height / 2 }
            ).fold(1) { acc, next -> acc * next }
    }
}

fun main() {
    print(Robots().run(part2 = true))
}
