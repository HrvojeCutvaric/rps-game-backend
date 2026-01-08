package co.hrvoje.utils

interface HashingManager {

    fun hash(plain: String): String

    fun verify(plain: String, hashed: String): Boolean
}