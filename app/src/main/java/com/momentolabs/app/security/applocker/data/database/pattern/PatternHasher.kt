package com.momentolabs.app.security.applocker.data.database.pattern

import android.util.Base64
import java.security.MessageDigest
import java.security.SecureRandom
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec

/**
 * Hashes lock patterns with PBKDF2-HMAC-SHA256 and a random salt, so the pattern itself is
 * never stored.
 */
object PatternHasher {

    private const val ALGORITHM = "PBKDF2WithHmacSHA256"
    private const val ITERATIONS = 50_000
    private const val KEY_LENGTH_BITS = 256
    private const val SALT_LENGTH_BYTES = 16

    private val secureRandom = SecureRandom()

    fun hash(pattern: List<PatternDot>): PatternHashEntity {
        val salt = ByteArray(SALT_LENGTH_BYTES).also { secureRandom.nextBytes(it) }
        return PatternHashEntity(salt = encode(salt), hash = encode(derive(pattern, salt)))
    }

    fun matches(pattern: List<PatternDot>, stored: PatternHashEntity): Boolean {
        if (pattern.isEmpty()) {
            return false
        }
        val expected = decode(stored.hash)
        val actual = derive(pattern, decode(stored.salt))
        return MessageDigest.isEqual(expected, actual)
    }

    private fun derive(pattern: List<PatternDot>, salt: ByteArray): ByteArray {
        val spec = PBEKeySpec(serialize(pattern), salt, ITERATIONS, KEY_LENGTH_BITS)
        return try {
            SecretKeyFactory.getInstance(ALGORITHM).generateSecret(spec).encoded
        } finally {
            spec.clearPassword()
        }
    }

    /** A 3x3 grid, so every dot is a single digit from 0 to 8. */
    private fun serialize(pattern: List<PatternDot>): CharArray =
        CharArray(pattern.size) { index -> '0' + (pattern[index].row * 3 + pattern[index].column) }

    private fun encode(bytes: ByteArray): String = Base64.encodeToString(bytes, Base64.NO_WRAP)

    private fun decode(value: String): ByteArray = Base64.decode(value, Base64.NO_WRAP)
}
