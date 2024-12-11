import utils.readInputBlock
import java.util.concurrent.ConcurrentSkipListMap
import kotlin.math.floor
import kotlin.math.log10
import kotlin.math.pow

/** [https://adventofcode.com/2024/day/11] */
class Stones : AdventOfCodeTask {
    override fun run(part2: Boolean): Any {
        val stones = readInputBlock("11.txt").trim().split(" ").map(String::toLong)
        val cache = ConcurrentSkipListMap<Pair<Long, Int>, Long>(compareBy({ it.first }, { it.second }))

        fun Long.transform(times: Int): Long =
            if (times == 0) 1L else cache.computeIfAbsent(this to times) {
                when {
                    this == 0L -> 1L.transform(times - 1)
                    digitCount % 2 == 0 -> (this / splitBase).transform(times - 1) + (this % splitBase).transform(times - 1)
                    else -> (this * 2024).transform(times - 1)
                }
            }

        return stones.sumOf { it.transform(if (part2) 75 else 25) }
    }

    private val Long.digitCount: Int
        get() = floor(log10(toDouble())).toInt() + 1

    private val Long.splitBase: Int
        get() = 10.0.pow(digitCount / 2).toInt()
}

fun main() {
    print(Stones().run(part2 = true))
}
