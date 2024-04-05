package learning.com.entities

import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction


object Users : IdTable<String>("users") {
    override val id: Column<EntityID<String>> = varchar("username", length = 20).entityId()
    val role = reference("role", Roles)
    val age = integer("age")
    override val primaryKey = PrimaryKey(id)
}

object Roles : IdTable<String>("roles") {
    override val id: Column<EntityID<String>> = Roles.varchar("role", length = 20).entityId()
    val name = varchar("description", 255)
    override val primaryKey = PrimaryKey(id)
}

class User(id: EntityID<String>) : Entity<String>(id) {
    companion object : EntityClass<String, User>(Users)

    var name by Users.id
    var role by Role referencedOn Users.role
    var age by Users.age
}

class Role(id: EntityID<String>) : Entity<String>(id) {
    companion object : EntityClass<String, Role>(Roles)

    var role by Roles.id
    var name by Roles.name
    val users by User referrersOn Users.role // back reference to the users table reflects many-to-one relationship
}

class UserServiceImpl : UserService {

    suspend fun <T> dbQuery(block: () -> T): T = newSuspendedTransaction(Dispatchers.IO) { block() }

    override suspend fun findUserByName(name: String) = dbQuery {
        User.find { Users.id eq name }.firstOrNull()
    }

    override suspend fun findRoleByName(name: String) = dbQuery {
        Role.find { Roles.id eq name }.firstOrNull()
    }

    override suspend fun findUsersByRoleName(roleName: String) = dbQuery {
        Role.findById(roleName)?.users?.toList() ?: emptyList()
    }

    override suspend fun isUserHasRole(username: String, roleName: String): Boolean = dbQuery {
        Role.findById(roleName)?.users?.any { it.name.value == username } ?: false
    }
}

interface UserService {
    suspend fun findUserByName(name: String): User?
    suspend fun findRoleByName(name: String): Role?
    suspend fun findUsersByRoleName(roleName: String): List<User>
    suspend fun isUserHasRole(username: String, roleName: String): Boolean
}