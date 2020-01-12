package uk.zootm.advent

import uk.zootm.advent.intcode.Input
import uk.zootm.advent.intcode.Memory
import uk.zootm.advent.intcode.PrintingOutput
import java.io.File

fun main() {
    val base = Memory.fromFile(File("inputs/day5.1.txt"))

    println("# PART 1")
    base.copy().makeComputer(
        input = Input.of(1),
        output = PrintingOutput()
    ).execute()

    println()
    println("# PART 2")
    base.copy().makeComputer(
        input = Input.of(5),
        output = PrintingOutput()
    ).execute()
}