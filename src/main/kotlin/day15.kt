import utils.Coordinate
import utils.Direction
import utils.readInputBlock

/** [https://adventofcode.com/2024/day/15] */
class Boxes : AdventOfCodeTask {
    override fun run(part2: Boolean): Any {
        val (rawMap, rawDirections) = readInputBlock("15.txt").split("\n\n").map(String::trim)

        data class Block(var definition: Map<Coordinate, Char>) {
            constructor(position: Coordinate, mark: Char) : this(mapOf(position to mark))
        }

        data class Robot(var position: Coordinate)

        val blocks = mutableSetOf<Block>()
        val map = rawMap.lines().flatMapIndexed { y, line ->
            line.flatMapIndexed { x, c ->
                if (part2) {
                    val firstCoord = Coordinate(2 * x, y)
                    val secondCoord = Coordinate(2 * x + 1, y)
                    val (firstChar, secondChar) = when (c) {
                        '#' -> '#' to '#'
                        'O' -> '[' to ']'.also { blocks.add(Block(mapOf(firstCoord to '[', secondCoord to ']'))) }
                        '.' -> '.' to '.'
                        '@' -> '@' to '.'
                        else -> error("Unexpected character")
                    }
                    listOf(firstCoord to firstChar, secondCoord to secondChar)
                } else {
                    val coord = Coordinate(x, y).also { if (c == 'O') blocks.add(Block(it, 'O')) }
                    listOf(coord to c)
                }
            }
        }.toMap().toMutableMap()

        val directions = rawDirections.filterNot(Character::isWhitespace).map {
            when (it) {
                '^' -> Direction.UP
                'v' -> Direction.DOWN
                '>' -> Direction.RIGHT
                '<' -> Direction.LEFT
                else -> error("Unexpected direction")
            }
        }
        val robot = map.entries.first { (_, c) -> c == '@' }.key.let(::Robot)


        fun Block.move(direction: Direction) {
            definition.keys.forEach { map[it] = '.' }
            definition = definition.mapKeys { (position, mark) ->
                position.move(direction, offset = true).also { map[it] = mark }
            }
        }

        fun Block.findBlocksToMove(direction: Direction): List<Block> {
            val neighbourPositions =
                definition.keys.map { it.move(direction, offset = true) }.filter { it !in definition }
            val neighbourTiles = neighbourPositions.map(map::getValue)
            if ('#' in neighbourTiles) return emptyList()

            val connectedBlocks =
                neighbourPositions.mapNotNull { position -> blocks.find { position in it.definition } }
                    .map { it.findBlocksToMove(direction) }
            if (connectedBlocks.any { it.isEmpty() }) return emptyList()

            return connectedBlocks.flatten().distinct() + this
        }

        fun Robot.move(newPosition: Coordinate) {
            map[position] = '.'
            position = newPosition.also { map[it] = '@' }
        }

        fun Robot.process(direction: Direction) {
            val next = position.move(direction, offset = true)
            if (map[next] == '#') return
            if (map[next] == '.') {
                robot.move(next)
                return
            }

            val blocksToMove =
                blocks.first { next in it.definition }.findBlocksToMove(direction).takeIf { it.isNotEmpty() } ?: return
            blocksToMove.forEach { it.move(direction) }
            robot.move(next)
        }

        directions.forEach(robot::process)
        return blocks.map { block -> block.definition.keys.minBy { it.x } }.sumOf { (x, y) -> 100 * y + x }
    }
}

fun main() {
    print(Boxes().run(part2 = true))
}
