package co.hrvoje.data.db.dao

import co.hrvoje.data.db.tables.Users
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class UserDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<UserDAO>(Users)

    var username by Users.username
    var password by Users.password
}