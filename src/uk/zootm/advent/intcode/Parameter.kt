package uk.zootm.advent.intcode

import uk.zootm.advent.intcode.Memory.Addr

interface Parameter {
    fun read(): Int
    fun write(out: Int)
}

/**
 * Mode 0
 */
class PositionParameter(private val addr: Addr): Parameter {
    override fun read() = addr.readPtr().read()
    override fun write(out: Int) = addr.readPtr().write(out)
    override fun toString() = "PositionParameter($addr)"
}

/**
 * Mode 1
 */
class ImmediateParameter(private val addr: Addr): Parameter {
    override fun read() = addr.read()
    override fun write(out: Int) = throw IllegalStateException("Attempted to write to immediate-mode parameter at $addr")
    override fun toString() = "ImmediateParameter($addr)"
}