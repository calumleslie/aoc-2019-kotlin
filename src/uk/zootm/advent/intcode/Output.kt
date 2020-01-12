package uk.zootm.advent.intcode

interface Output {
    fun write(o: Int)
}

class NoopOutput: Output {
    override fun write(o: Int) {
        // Do nothing
    }
}

class CollectingOutput: Output {
    val data = mutableListOf<Int>()

    override fun write(o: Int) {
        data.add(o)
    }
}
