package org.wf.data.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import org.wf.data.Employee;

import java.util.UUID;
@ApplicationScoped
public class EmployeeRepo implements PanacheRepositoryBase<Employee, UUID> {
}
