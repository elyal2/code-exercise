package org.wf.data.tracing;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import org.hibernate.envers.RevisionEntity;
import org.hibernate.envers.RevisionNumber;
import org.hibernate.envers.RevisionTimestamp;

@Entity(name = "revinfo")
@RevisionEntity(AuditRevisionListener.class)
public class AuditRevisionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "revinfo_seq")
    @SequenceGenerator(
            name = "revinfo_seq",
            sequenceName = "hibernate_sequence",
            initialValue = 1,
            allocationSize = 1
    )
    @RevisionNumber
    private long rev;

    @RevisionTimestamp
    private long revtstmp;

    // Getters and setters
}
