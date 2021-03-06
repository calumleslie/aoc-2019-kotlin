package uk.zootm.advent.intcode

class Computer(val memory: Memory, val input: Input, val output: Output) {
    var instructionPointer = memory.Addr(0)
    var finished = false

    fun execute(trace: Boolean = false) {
        while (!finished) {
            step(trace)
            if (trace) println("STATE: $this")
        }
    }

    fun step(trace: Boolean) {
        val instruction = Instruction.from(instructionPointer)
        if(trace) println("INSTRUCTION: $instruction")
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