package uk.zootm.advent.intcode

interface Input {
    fun read(): Int

    companion object Inputs {
        fun empty(): Input = IteratorInput(emptyList<Int>().iterator())
        fun of(vararg inputs: Int) = IteratorInput(inputs.iterator())
    }
}

class IteratorInput(val input: Iterator<Int>): Input {
    override fun read(): Int = input.next()
}