import utils.readInputLines
import kotlin.math.abs

/** [https://adventofcode.com/2024/day/1] */
class Differences : AdventOfCodeTask {
    override fun run(part2: Boolean): Any {
        val input = readInputLines("1.txt")
            .map { line -> line.split(Regex("\\s+")).map { it.toInt() } }
        val left = input.map { it.first() }.sorted()
        val right = input.map { it.last() }.sorted()

        return if (part2)
            left.sumOf { number -> number * right.count { it == number } }
        else
            left.zip(right).sumOf { abs(it.second - it.first) }
    }
}

fun main() {
    print(Differences().run(part2 = true))
}
