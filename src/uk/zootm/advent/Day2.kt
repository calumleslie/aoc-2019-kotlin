package uk.zootm.advent

import uk.zootm.advent.intcode.Computer
import java.io.File

fun main() {
    val base = Computer.fromFile(File("inputs/day2.1.txt"))

    // before running the program, replace position 1 with the value 12 and replace position 2 with the value 2
    val part1 = base.copy()
    part1.Addr(1).write(12)
    part1.Addr(2).write(2)
    part1.execute()
    println("Part 1: ${part1.Addr(0).read()}")

    for(noun in (0..100)) {
        for (verb in (0..100)) {
            val prog = base.copy()
            prog.Addr(1).write(noun)
            prog.Addr(2).write(verb)
            prog.execute()

            val result = prog.Addr(0).read()
            if(result == 19690720) {
                println("Part 2: noun=$noun, verb=$verb, solution=${100 * noun + verb}")
            }
        }
    }
}