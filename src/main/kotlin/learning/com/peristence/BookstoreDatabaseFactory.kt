package learning.com.peristence

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.server.application.ApplicationEnvironment
import javax.sql.DataSource

object BookstoreDatabaseFactory {
    fun createDataSource (environment: ApplicationEnvironment): DataSource {
        return HikariDataSource(HikariConfig().apply {
            jdbcUrl = environment.config.propertyOrNull("database.url")?.getString()
            driverClassName = environment.config.propertyOrNull("database.driver")?.getString()
            username = environment.config.propertyOrNull("database.user")?.getString()
            password = environment.config.propertyOrNull("database.password")?.getString()
            isAutoCommit = environment.config.propertyOrNull("database.isAutocommit")?.getString().toBoolean()
            transactionIsolation = environment.config.propertyOrNull("database.transactionIsolation")?.getString()
            maximumPoolSize = environment.config.propertyOrNull("database.maxPoolSize")?.getString()?.toInt() ?: 10
            validate()
        })
    }
}
