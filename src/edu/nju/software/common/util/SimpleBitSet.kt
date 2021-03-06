package edu.nju.software.common.util

/**
 * @author <a href="mailto:tinker19981@hotmail.com">tinker</a>
 */
const private val HEAD_MASK = Long.MAX_VALUE.inv()
const private val ALL_MASK = -1L

private fun rightMask(length: Int) = ALL_MASK ushr (java.lang.Long.SIZE - length)

class SimpleBitSet : Iterable<Boolean> {
    class BitIterator(private val bits: Long, length: Int) : Iterator<Boolean> {
        private var index = length
        override fun hasNext(): Boolean {
            return index > 0
        }

        override fun next() = at(--index)

        private fun at(index: Int) = (bits and (1L shl index)) != 0L
    }

    override fun iterator() = BitIterator(bits, length)

    private var bits = 0L
    private var length = 0

    fun append(value: Long, length: Int) {
        bits = (value and rightMask(length)) or (bits shl length)
        this.length += length
    }

    fun append(value: Boolean): SimpleBitSet {
        bits = (if (value) 1L else 0L) or (bits shl 1)
        length++

        return this
    }

    fun appendBit(value: Int) =
            value.takeIf { it == 0 || it == 1 }
                    ?.let { append(it == 1) } ?: throw IllegalArgumentException()

    operator fun get(index: Int) =
            index.takeIf { it < Integer.SIZE }?.
                    let { (bits and (1L shl it)) != 0L } ?: throw IndexOutOfBoundsException()

    fun sliceBy(size: Int): List<Int> {
        if (length % size != 0) throw IllegalArgumentException()
        var value = bits
        val list = mutableListOf<Int>()
        val mask = rightMask(size)
        while (value != 0L) {
            list.add((value and mask).toInt())
            value = value ushr size
        }
        return list.reversed()
    }

    fun length() = length

    override fun toString(): String = java.lang.Long.toBinaryString(bits)
}