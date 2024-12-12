import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class AdventOfCodeTasksTest {

    @Test
    fun day01() {
        runTaskTest(Differences(), 1873376, 18997088)
    }

    @Test
    fun day02() {
        runTaskTest(Reports(), 371, 426)
    }

    @Test
    fun day03() {
        runTaskTest(Instructions(), 161085926, 82045421)
    }

    @Test
    fun day04() {
        runTaskTest(Letters(), 2575, 2041)
    }

    @Test
    fun day05() {
        runTaskTest(Rules(), 5129, 4077)
    }

    @Test
    fun day06() {
        runTaskTest(Day6(), 4602, 1703)
    }

    @Test
    fun day07() {
        runTaskTest(Equations(), 4364915411363, 38322057216320)
    }

    @Test
    fun day08() {
        runTaskTest(Antinodes(), 354, 1263)
    }

    @Test
    fun day09() {
        runTaskTest(Files(), 6435922584968, 6469636832766)
    }

    @Test
    fun day10() {
        runTaskTest(Trails(), 557, 1062)
    }

    @Test
    fun day11() {
        runTaskTest(Stones(), 183484L, 218817038947400L)
    }

    @Test
    fun day12() {
        runTaskTest(Fences(), 1489582, 914966)
    }

    private fun runTaskTest(task: AdventOfCodeTask, part1Result: Any, part2Result: Any) {
        assertEquals(part1Result, task.run())
        assertEquals(part2Result, task.run(part2 = true))
    }
}
