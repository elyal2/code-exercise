package org.wf.data.tracing;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.wf.security.ThreadLocalStorage;

import java.time.LocalDateTime;

/**
 * Abstract base class for audit functionality.
 * This class provides automatic tracking of creation and modification details
 * for entity classes that extend it. It includes fields for creation and
 * modification timestamps, as well as the identifiers of the users who
 * performed these actions.
 */
@MappedSuperclass
@Getter
@Setter
public abstract class Audit {

    @Column(name = "tenant_id", nullable = false, length = 1024)
    private String tenantID;

    public String getTenantID() {
        return ThreadLocalStorage.getTenantID();
    }

    public void setTenantID(String tenantID) {
        this.tenantID = ThreadLocalStorage.getTenantID();
    }

    // Timestamp when the record was created.
    // Automatically set by Hibernate at the moment of creation.
    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private LocalDateTime created;

    // Placeholder method to set the 'createdBy' field.
    // Intended to be overridden with actual logic to determine the creator's ID.
    public void setCreatedBy(Integer createdBy) {
        setCreatedBy();
    }

    // Sets the 'createdBy' field based on the current user context.
    // Currently, this is a stub and needs to be implemented to map the current user.
    public void setCreatedBy() {
        this.createdBy = ThreadLocalStorage.getTenantID();
    }

    // User identifier of who created the record.
    @Column(name = "created_by", length = 1024)
    private String createdBy;

    // Timestamp when the record was last modified.
    // Automatically updated by Hibernate whenever the record is updated.
    @UpdateTimestamp
    @Column(name = "last_modified_at")
    private LocalDateTime lastModified;

    // Placeholder method to set the 'lastModifiedBy' field.
    // Intended to be overridden with actual logic to determine the modifier's ID.
    public void setLastModifiedBy(String lastModifiedBy) {
        setLastModifiedBy();
    }

    // Sets the 'lastModifiedBy' field based on the current user context.
    // Currently, this is a stub and needs to be implemented to map the current user.
    public void setLastModifiedBy() {
        this.lastModifiedBy = ThreadLocalStorage.getTenantID();
    }

    // User identifier of who last modified the record.
    @Column(name = "last_modified_by", length = 1024)
    private String lastModifiedBy;
}
