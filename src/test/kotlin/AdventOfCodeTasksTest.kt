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

    private fun runTaskTest(task: AdventOfCodeTask, part1Result: Any, part2Result: Any) {
        assertEquals(part1Result, task.run())
        assertEquals(part2Result, task.run(part2 = true))
    }
}
