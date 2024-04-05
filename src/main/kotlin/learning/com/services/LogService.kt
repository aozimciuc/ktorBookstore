package learning.com.services

import kotlinx.coroutines.Dispatchers
import learning.com.entities.EventType
import learning.com.entities.EventTypes
import learning.com.entities.LogEvent
import learning.com.entities.LogEvents
import learning.com.entities.Severity
import learning.com.routes.log
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.count
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.kotlin.datetime.date
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import java.time.LocalDate
import java.util.*
import javax.sql.DataSource

class LogServiceImpl(val dataSource: DataSource) : LogService {

    override fun logEvent(event: String, severity: Severity, message: String?): UUID? {
        log.info("Logging event")
        var entityId: EntityID<UUID>? = null
        transaction(db = Database.connect(dataSource)) {
            addLogger(StdOutSqlLogger)
            entityId = LogEvents.insert {
                it[LogEvents.event] =
                    EventTypes.select(EventTypes.id).where { EventTypes.value eq event }.single()[EventTypes.id]
                it[LogEvents.severity] = severity
                it[LogEvents.message] = message
            } get LogEvents.id
        }
        log.info("Inserted in log table id: ${entityId?.value}")
        return entityId?.value
    }

    suspend fun <T> dbQuery(block: () -> T): T = newSuspendedTransaction(Dispatchers.IO) { block() }

    override suspend fun create(logEvent: LogEvent): UUID = dbQuery {
        LogEvents.insert {
            it[event] = logEvent.event.id
            it[severity] = logEvent.severity
            it[message] = logEvent.message
        }[LogEvents.id].value
    }

    // select all log events and map them to LogEvent objects.
    // Uses subselects to get the event type value and description
    // Inefficient, but demonstrates how to use subselects
    override suspend fun getAllWithSubselect(): List<LogEvent> = dbQuery {
        LogEvents.selectAll().map { row ->
            LogEvent(
                id = row[LogEvents.id].value.toString(),
                timestamp = row[LogEvents.timestamp],
                event = EventType(
                    id = row[LogEvents.event],
                    value = EventTypes.select(EventTypes.value).where { EventTypes.id eq row[LogEvents.event] }
                        .single()[EventTypes.value],
                    description = EventTypes.select(EventTypes.description)
                        .where { EventTypes.id eq row[LogEvents.event] }
                        .singleOrNull()?.get(EventTypes.description)
                ),
                severity = row[LogEvents.severity],
                message = row[LogEvents.message]
            )
        }
    }

    override suspend fun getAllWithJoin(): List<LogEvent> = dbQuery {
        (LogEvents innerJoin EventTypes).selectAll().map { row ->
            LogEvent(
                id = row[LogEvents.id].value.toString(),
                timestamp = row[LogEvents.timestamp],
                event = EventType(
                    id = row[LogEvents.event],
                    value = row[EventTypes.value],
                    description = row[EventTypes.description]
                ),
                severity = row[LogEvents.severity],
                message = row[LogEvents.message]
            )
        }
    }

    override suspend fun getEventsByType() = dbQuery {

        (LogEvents innerJoin EventTypes).selectAll()
            .map {
                EventType(
                    id = it[EventTypes.id],
                    value = it[EventTypes.value],
                    description = it[EventTypes.description]
                )
            }
    }

    override suspend fun getAllByDate(date: LocalDate): List<LogEvent> = dbQuery {

        (LogEvents innerJoin EventTypes).selectAll()
            .where { LogEvents.timestamp.between(from = date, to = date.plusDays(1)) }
            .groupBy(LogEvents.event)
            .map {
                LogEvent(
                    id = it[LogEvents.id].value.toString(),
                    timestamp = it[LogEvents.timestamp],
                    event = EventType(
                        id = it[LogEvents.event],
                        value = it[EventTypes.value],
                        description = it[EventTypes.description]
                    ),
                    severity = it[LogEvents.severity],
                    message = it[LogEvents.message]
                )
            }

    }

    override suspend fun updateMessage(id: UUID?, message: String) = dbQuery {
        LogEvents.update({ LogEvents.id eq id }) {
            it[LogEvents.message] = message
        }
    }

    override suspend fun getAllGroupedByDate(): List<Map<String, Any?>> = dbQuery {
        (LogEvents innerJoin EventTypes).select(
            LogEvents.id.count(),
            LogEvents.event,
            LogEvents.timestamp.date(),
            EventTypes.description,
            EventTypes.value,
            LogEvents.severity,
            LogEvents.message,
        )
            .groupBy(
                LogEvents.event,
                LogEvents.timestamp.date(),
                EventTypes.description,
                EventTypes.value,
                LogEvents.severity,
                LogEvents.message
            )
            .map { row ->
                mapOf(
                    "count" to row[LogEvents.id.count()],
                    "event" to mapOf(
                        "id" to row[LogEvents.event],
                        "value" to row[EventTypes.value],
                        "description" to row[EventTypes.description]
                    ),
                    "severity" to row[LogEvents.severity],
                    "message" to row[LogEvents.message]
                )
            }
    }

}

interface LogService {
    fun logEvent(event: String, severity: Severity, message: String?): UUID?
    suspend fun create(logEvent: LogEvent): UUID
    suspend fun getAllWithSubselect(): List<LogEvent>
    suspend fun getAllWithJoin(): List<LogEvent>
    suspend fun getEventsByType(): List<EventType>
    suspend fun getAllGroupedByDate(): List<Map<String, Any?>>
    suspend fun getAllByDate(date: LocalDate): List<LogEvent>
    suspend fun updateMessage(id: UUID?, message: String): Int
}
