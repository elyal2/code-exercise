package org.wf.data;

import jakarta.persistence.*;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.*;
import org.hibernate.annotations.Cache;
import org.wf.data.tracing.Audit;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Cacheable
@Cache(region = "employees", usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "employees")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder @EqualsAndHashCode(callSuper = false)
public class Employee extends Audit {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false)
    @EqualsAndHashCode.Include
    private UUID id;


    @Column(name = "name", length = 2048)
    @Size(max = 2048) // Max length of 2048 characters
    private String name;

    @Column(name = "surname", length = 2048)
    @Size(max = 2048) // Max length of 2048 characters
    private String surname;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @NotNull
    private EmployeeStatus status;

    @CreationTimestamp // Marks the field as automatically set to the current date and time upon creation
    @Column(name = "date_registered", nullable = false, updatable = false)
    @NotNull
    private LocalDateTime dateRegistered;

    @UpdateTimestamp // Marks the field as automatically updated to the current date and time upon modification
    @Column(name = "date_status_update", nullable = false)
    @NotNull
    private LocalDateTime dateStatusUpdate;

    @Column(name = "email", length = 1024)
    @Size(max = 1024) // Max length of 1024 characters
    @Email // Validates the field to be a valid email address
    private String email;

    @ManyToOne
    @JoinColumn(name = "reports_to_id")
    private Employee reportsTo;

    // Status check methods
    public boolean isRegistered() {
        return this.status == EmployeeStatus.REGISTERED;
    }

    public boolean isActive() {
        return this.status == EmployeeStatus.ACTIVE;
    }

    public boolean isSuspended() {
        return this.status == EmployeeStatus.SUSPENDED;
    }

    public boolean isDeleted() {
        return this.status == EmployeeStatus.DELETED;
    }

    // Custom setter for status to update the dateStatusUpdate field
    public void setStatus() {
        setStatus(EmployeeStatus.REGISTERED);
        this.dateStatusUpdate = LocalDateTime.now();
    }
    public void setActive(){
        setStatus(EmployeeStatus.ACTIVE);
        this.dateStatusUpdate = LocalDateTime.now();
    }
    public void setSuspended(){
        setStatus(EmployeeStatus.SUSPENDED);
        this.dateStatusUpdate = LocalDateTime.now();
    }
    public void setDeleted(){
        setStatus(EmployeeStatus.DELETED);
        this.dateStatusUpdate = LocalDateTime.now();
    }
//    @PreUpdate
//    protected void onUpdate() {
//        dateStatusUpdate = LocalDateTime.now();
//    }
    enum EmployeeStatus {
        REGISTERED, ACTIVE, SUSPENDED, DELETED
    }
}

// Enum for Employee Status
