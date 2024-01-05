package org.wf.data.tracing;

import io.quarkus.arc.Unremovable;
import jakarta.enterprise.context.ApplicationScoped;
import org.hibernate.envers.RevisionListener;

@ApplicationScoped
@Unremovable
public class AuditRevisionListener implements RevisionListener {
    @Override
    public void newRevision(Object revisionEntity) {
// no need to do anything, just extending to change int for long in reventity
    }
}
