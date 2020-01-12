package uk.zootm.advent.intcode

import java.io.File
import java.io.FileInputStream

class Memory(val store: IntArray) {
    fun makeComputer(input: Input = Input.empty(), output: Output = NoopOutput()): Computer =
        Computer(this, input, output)

    fun copy(): Memory = Memory(store.clone())

    override fun toString() = store.joinToString(",")

    override fun hashCode() = store.contentHashCode()

    override fun equals(other: Any?): Boolean {
        if (other != null && other is Memory) {
            return other.store.contentEquals(this.store)
        } else {
            return false
        }
    }

    companion object Parser {
        fun fromString(input: String): Memory {
            val store = input.splitToSequence(",")
                .map { Integer.parseInt(it) }
                .toList()
                .toIntArray()

            return Memory(store)
        }

        fun fromFile(file: File): Memory {
            val text = FileInputStream(file).bufferedReader().use { it.readLine() }
            return fromString(text)
        }
    }

    inner class Addr(private val i: Int) {
        init {
            assert(i < store.size, lazyMessage = { "Found invalid location $i (memory size is ${store.size}" })
        }

        fun write(value: Int) {
            store[i] = value
        }

        fun readPtr() = Addr(store[i])

        fun read() = store[i]

        operator fun plus(incr: Int) = Addr(i + incr)

        fun arg(index: Int) = this + (1 + index)

        override fun toString() = "Addr($i)"
    }
}