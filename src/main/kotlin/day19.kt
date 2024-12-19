import utils.readInputBlock
import java.util.concurrent.ConcurrentSkipListMap

/** [https://adventofcode.com/2024/day/19] */
class Towels : AdventOfCodeTask {
    override fun run(part2: Boolean): Any {
        val (towels, designs) = readInputBlock("19.txt").split("\n\n").map(String::trim).let {
            it.first().split(", ") to it.last().lines()
        }

        val cache = ConcurrentSkipListMap<String, Long>().apply { put("", 1) }
        fun compose(design: String): Long =
            cache.computeIfAbsent(design) {
                towels.filter { design.startsWith(it) }.sumOf { compose(design.removePrefix(it)) }
            }


        return if (part2) designs.sumOf(::compose) else designs.count { compose(it) > 0 }
    }
}

fun main() {
    print(Towels().run(part2 = false))
}
