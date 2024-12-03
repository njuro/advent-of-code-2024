import utils.readInputBlock

/** [https://adventofcode.com/2024/day/3] */
class Instructions : AdventOfCodeTask {
    override fun run(part2: Boolean): Any {
        val pattern = Regex("mul\\((\\d+),(\\d+)\\)|do\\(\\)|don't\\(\\)")

        return pattern.findAll((readInputBlock("3.txt"))).fold(0 to true) { (acc, enabled), instruction ->
            when (instruction.value) {
                "do()" -> acc to true
                "don't()" -> acc to !part2
                else -> acc + (if (enabled) instruction.groupValues[1].toInt() * instruction.groupValues[2].toInt() else 0) to enabled
            }
        }.first
    }
}

fun main() {
    print(Instructions().run(part2 = false))
}
