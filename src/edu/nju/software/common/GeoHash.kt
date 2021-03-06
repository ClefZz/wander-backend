package edu.nju.software.common

import edu.nju.software.common.util.SimpleBitSet
import java.util.*

/**
 * @author <a href="mailto:tinker19981@hotmail.com">tinker</a>
 */

const private val DIGITS = "0123456789bcdefghjkmnpqrstuvwxyz"
private val lookup = mapOf(*DIGITS.mapIndexed { index, c -> c to index }.toTypedArray())

private fun base32Decode(encoded: String)
        = encoded.
        mapNotNull { lookup[it] }.
        takeIf { it.size == encoded.length }?.
        joinToString(separator = "") { Integer.toBinaryString(it).padStart(5, '0') }
        ?: throw IllegalArgumentException("illegal encoded string")

private fun appendBit(origin: Int, bit: Boolean) = (if (bit) 1 else 0) or (origin shl 1)

private fun unwind(winded: String) =
        winded.foldIndexed(arrayOf(0, 0))
        { index, buffer, c ->
            buffer[index and 1] = appendBit(buffer[index and 1], c == '1')
            buffer
        }.let { it[0] to it[1] }

// 根据二进制和范围解码
private fun integrate(bin: Int, floor: Double, ceiling: Double): Double {
    val bits = BitSet.valueOf(longArrayOf(bin.toLong()))
    var mid = 0.0
    var low = floor
    var high = ceiling

    for (i in (bits.length() - 1) downTo 0) {
        mid = (low + high) / 2.0
        if (bits.get(i)) low = mid
        else high = mid
    }
    return mid
}

const private val FLOOR_LAT = -90.0
const private val CEILING_LAT = 90.0
const private val FLOOR_LNG = -180.0
const private val CEILING_LNG = 180.0

fun decodeGeoHash(hash: String): Pair<Double, Double> {
    val (longitude, latitude) = unwind(base32Decode(hash))
    return integrate(latitude, FLOOR_LAT, CEILING_LAT) to integrate(longitude, FLOOR_LNG, CEILING_LNG)
}

private fun base32Encode(str: SimpleBitSet)
        = str.takeIf { it.length() % 5 == 0 }?.
        sliceBy(5)?.
        map { DIGITS[it] }?.
        joinToString(separator = "") ?: throw IllegalArgumentException()

private fun wind(lngBits: SimpleBitSet, latBits: SimpleBitSet)
        = lngBits.zip(latBits).
        fold(SimpleBitSet()) { buffer, (lngBit, latBit) -> buffer.append(lngBit).append(latBit) }.
        append(lngBits[0])

private fun decompose(value: Double, floor: Double, ceiling: Double, bitCount: Int): SimpleBitSet {
    val buffer = SimpleBitSet()
    var low = floor
    var high = ceiling
    var mid: Double
    for (i in 0 until bitCount) {
        mid = (low + high) / 2
        if (value >= mid) {
            buffer.append(true)
            low = mid
        } else {
            buffer.append(false)
            high = mid
        }
    }
    return buffer
}

const private val BITS_LAT = 22
const private val BITS_LNG = 23

//对经纬度进行编码
fun encodeGeoHash(latitude: Double, longitude: Double): String {
    val lngBits = decompose(longitude, FLOOR_LNG, CEILING_LNG, BITS_LNG)
    val latBits = decompose(latitude, FLOOR_LAT, CEILING_LAT, BITS_LAT)
    return base32Encode(wind(lngBits, latBits))
}

