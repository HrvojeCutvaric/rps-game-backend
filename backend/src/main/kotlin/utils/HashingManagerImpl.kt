package co.hrvoje.utils

import org.mindrot.jbcrypt.BCrypt

class HashingManagerImpl : HashingManager {

    override fun hash(plain: String): String {
        return BCrypt.hashpw(plain, BCrypt.gensalt())
    }

    override fun verify(plain: String, hashed: String): Boolean {
        return BCrypt.checkpw(plain, hashed)
    }
}