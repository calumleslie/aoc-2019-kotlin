package uk.zootm.advent.intcode

import uk.zootm.advent.intcode.Memory.Addr

class Computer(val memory: Memory, val input: Input, val output: Output) {
    var instructionPointer = memory.Addr(0)
    var finished = false

    fun execute(trace: Boolean = false) {
        while (!finished) {
            step()
            if (trace) println(this)
        }
    }

    fun step() {
        val instruction = Instruction.from(instructionPointer)
        instruction.exec(input, output)

        val next = instruction.next()
        if( next != null ) {
            instructionPointer = next
        } else {
            finished = true
        }
    }

    override fun toString(): String = "Computer(instruction: $instructionPointer, finished: $finished, memory: $memory)"

}