package org.wf.data.security;

import io.agroal.api.AgroalPoolInterceptor;
import io.quarkus.runtime.configuration.ProfileManager;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;
import org.wf.security.ThreadLocalStorage;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

@Slf4j
//@DataSource("main") // Interceptor just for the 'main' data source.
@ApplicationScoped
public class PoolInterceptor implements AgroalPoolInterceptor {

    @Override
    public void onConnectionAcquire(Connection connection) {
        // Set the tenant ID when a connection is acquired.
        String tenantId = ThreadLocalStorage.getTenantID();
        setTenantId(connection, tenantId, "Setting");

        if (ProfileManager.getLaunchMode().isDevOrTest() && log.isInfoEnabled()) {
            log.info("⌨️ [DEV] Setting PostgreSQL session variable app.current_tenant = '{}'", tenantId);
        }

        AgroalPoolInterceptor.super.onConnectionAcquire(connection);
    }

    @Override
    public void onConnectionReturn(Connection connection) {
        // Clear the tenant ID when a connection is returned.
        setTenantId(connection, "", "Clearing");

        if (ProfileManager.getLaunchMode().isDevOrTest() && log.isInfoEnabled()) {
            log.info("⌨️ [DEV] Removing PostgreSQL session variable app.current_tenant");
        }

        AgroalPoolInterceptor.super.onConnectionReturn(connection);
    }

    private void setTenantId(Connection connection, String tenantId, String action) {
        // Ensure tenantId is safe to include in SQL query to prevent SQL injection
        // This is a critical step if tenantId can be influenced by user input
        // PostgreSQL does not support parameterized queries for setting session variables directly.
        tenantId = sanitizeTenantId(tenantId);

        String sql = "SET SESSION app.current_tenant = '" + tenantId + "'";
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            log.error("Failed to {} PostgreSQL session variable app.current_tenant", action, e);
        }
    }

    // @TODO: Example of a very basic sanitizer.
    private String sanitizeTenantId(String tenantId) {
        // Replace single quotes with an escape character
        return tenantId.replace("'", "''");
    }
}
