package learning.com.entities

import learning.com.peristence.DataManagerPostgres
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

const val LOG_EVENT_TABLE = "log_events"

object LogEvents : UUIDTable(LOG_EVENT_TABLE, "uuid") {
    val event = varchar("event", 50)
    val severity = varchar("severity", 255)
    val message = text("msg", eagerLoading = false).nullable()
}

class LogEventMigration(val dbHelper :DataManagerPostgres) {

    private fun migrate() {
        transaction(db = dbHelper.connect()) {
            SchemaUtils.create(LogEvents)
        }
    }

    fun migrateIfNotExists() {
        transaction(db = dbHelper.connect()) {
            if (!db.dialect.tableExists(LogEvents)) {
                migrate()
            }
        }
    }
}