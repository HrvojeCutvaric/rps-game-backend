package co.hrvoje.data.db.tables

import org.jetbrains.exposed.dao.id.IntIdTable

object Users : IntIdTable("users") {
    val username = varchar("username", 50).uniqueIndex()
    val password = text("password")
}