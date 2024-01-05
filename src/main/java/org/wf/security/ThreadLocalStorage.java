package org.wf.security;

import java.util.Optional;

/**
 * A utility class for managing thread-local storage of tenant identifiers.
 * This class provides a way to store and retrieve the tenant ID (representing
 * a user or a specific context) that is unique to each thread of execution.
 * Using thread-local storage ensures that the tenant ID is maintained correctly
 * even in a multi-threaded environment, where each thread may be handling a
 * different user or context. This is particularly useful in applications where
 * different users or tenants may be served concurrently.
 */
public class ThreadLocalStorage {

    static final String DEFAULT_TENANT = "demo_user";

    // ThreadLocal container for tenant IDs.
    // Each thread will have its own tenant ID independent of other threads.
    private static ThreadLocal<String> tenant = new ThreadLocal<>();

    /**
     * Sets the tenant ID for the current thread.
     * The tenant ID is stored in a thread-local variable, meaning it's specific
     * to the thread that sets this value.
     *
     * @param tenantID The tenant ID to set for the current thread.
     */
    public static void setTenantID(String tenantID) {
        tenant.set(tenantID);
    }

    /**
     * Retrieves the tenant ID associated with the current thread.
     * If the current thread has not set a tenant ID, this method will return null.
     *
     * @return The tenant ID for the current thread, or null if none is set.
     */
    public static String getTenantID() {
        return Optional.ofNullable(tenant.get()).orElse(DEFAULT_TENANT);
    }

}
