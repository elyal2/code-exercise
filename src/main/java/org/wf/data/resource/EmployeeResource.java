package org.wf.data.resource;

import io.quarkus.hibernate.orm.rest.data.panache.PanacheRepositoryResource;
import org.wf.data.Employee;
import org.wf.data.repository.EmployeeRepo;

import java.util.UUID;

public interface EmployeeResource extends PanacheRepositoryResource<EmployeeRepo, Employee, UUID> {

}
