package uk.zootm.advent

import uk.zootm.advent.intcode.Input
import uk.zootm.advent.intcode.Memory
import uk.zootm.advent.intcode.PrintingOutput
import java.io.File

fun main() {
    val base = Memory.fromFile(File("inputs/day5.1.txt"))

    base.makeComputer(
        input = Input.of(1),
        output = PrintingOutput()
    ).execute()
}