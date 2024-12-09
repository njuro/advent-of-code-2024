import utils.readInputBlock

/** [https://adventofcode.com/2024/day/9] */
class Files : AdventOfCodeTask {
    override fun run(part2: Boolean): Any {
        val freeSpaces = mutableMapOf<Int, Int>().toSortedMap()
        var totalIndex = 0
        var files = readInputBlock("9.txt").trim().flatMapIndexed { index, c ->
            val size = c.digitToInt()
            val id = if (index % 2 == 0) index / 2 else null
            List(size) { id }.also {
                if (id == null) freeSpaces[totalIndex] = size
                totalIndex += size
            }
        }.toMutableList()

        if (part2) {
            (files.last { it != null }!! downTo 0).forEach { id ->
                val blockStart = files.indexOf(id)
                val blockEnd = files.lastIndexOf(id) + 1
                val block = files.subList(blockStart, blockEnd)
                val freeSpace = freeSpaces.entries.firstOrNull { (index, size) ->
                    index < blockStart && size >= block.size
                }
                if (freeSpace != null) {
                    val (freeSpaceStart, freeSpaceSize) = freeSpace
                    files = (files.subList(0, freeSpaceStart) + block + files.subList(
                        freeSpaceStart + block.size,
                        blockStart
                    ) + List(block.size) { null } + files.subList(blockEnd, files.size)).toMutableList()
                    freeSpaces[freeSpaceStart + block.size] = freeSpaceSize - block.size
                    freeSpaces.remove(freeSpaceStart)
                }
            }
        } else {
            while (null in files) {
                files[files.indexOf(null)] = files.removeLast()
            }
        }

        return files.foldIndexed(0L) { index, checksum, next ->
            checksum + index.toLong() * (next ?: 0)
        }
    }
}

fun main() {
    print(Files().run(part2 = true))
}
