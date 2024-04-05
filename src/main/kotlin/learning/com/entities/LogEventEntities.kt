package learning.com.entities

import kotlinx.datetime.LocalDateTime
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.kotlin.datetime.CurrentDateTime
import org.jetbrains.exposed.sql.kotlin.datetime.datetime


object EventTypes : Table("events") {
    val id = varchar("id", 20).uniqueIndex()
    val value = varchar("value", 50)
    val description = text("description").nullable()
    override val primaryKey = PrimaryKey(id)
}

object LogEvents : UUIDTable("log_events", "uuid") {
    val timestamp = datetime("timestamp").defaultExpression(CurrentDateTime)
    val event = varchar("event_id", 20) references (EventTypes.id)
    val severity = enumerationByName("severity", 20, Severity::class).default(Severity.UNKNOWN)
    val message = text("msg", eagerLoading = false).nullable()
}

enum class Severity {
    UNKNOWN, INFO, WARN, ERROR, DEBUG
}

data class LogEvent(
    val id: String,
    val timestamp: LocalDateTime,
    val event: EventType,
    val severity: Severity,
    val message: String?
)

data class EventType(
    val id: String,
    val value: String,
    val description: String?
)
