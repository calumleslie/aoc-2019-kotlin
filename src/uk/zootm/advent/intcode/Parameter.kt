package uk.zootm.advent.intcode

import uk.zootm.advent.intcode.Memory.Addr

abstract class Parameter(protected val addr: Addr) {
    abstract fun read(): Int
    abstract fun write(out: Int)
    fun readAsAddr() = addr.memory().Addr(read())
}

/**
 * Mode 0
 */
class PositionParameter(addr: Addr): Parameter(addr) {
    override fun read() = addr.readPtr().read()
    override fun write(out: Int) = addr.readPtr().write(out)
    override fun toString() = "PositionParameter($addr)"
}

/**
 * Mode 1
 */
class ImmediateParameter(addr: Addr): Parameter(addr) {
    override fun read() = addr.read()
    override fun write(out: Int) = throw IllegalStateException("Attempted to write to immediate-mode parameter at $addr")
    override fun toString() = "ImmediateParameter($addr)"
}