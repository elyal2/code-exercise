package org.wf.data;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.wf.data.tracing.Audit;

import java.util.UUID;

@Entity
@Cacheable @Cache(region = "jobs", usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "jobs")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder @EqualsAndHashCode(callSuper = false)
public class Job extends Audit {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false)
    @EqualsAndHashCode.Include
    private UUID id;
    // Nombre puesto de trabajo (Job Position name)
    @Column(name = "name", length = 1024)
    @Size(max = 1024) // Max length of 1024 characters
    private String name;
//    Descripción puesto de trabajo (Job Position description)
    @Column(name = "description", length = 2048)
    @Size(max = 2048) // Max length of 2048 characters
    private String description;
//    Categoría profesional (Professional Category)
    @Column(name = "category", length = 2048)
    @Size(max = 2048) // Max length of 2048 characters
    private String category;
//    Grupo profesional de convenio (Professional Group Agreement)
    @Column(name = "group_agreement")
    private String groupAgreement;
//    Personas en este puesto (People in this Position) -- minimum value of -32,768 and a maximum value of 32,767
    @Column(name = "people_managed")
    private Short peopleManaged;
//    Departamento (Department) -- should be an ID?
    @Column(name = "department")
    private String department;
//    Ubicación del puesto (Location of the Position) -- should be an ID?
    @Column(name = "location")
    private String location;
//    Centro de trabajo (Work Center)-- should be an ID?
    @Column(name = "work_center")
    private String workCenter;
//    Horario de trabajo (Work Schedule) -- should be an ID?
    @Column(name = "work_schedule")
    private String workSchedule;
    /** Teletrabajo (Teleworking) -- yes/no */
    @Column(name = "is_teleworking")
    private Boolean isTeleworking;
    /** Disponibilidad para viajar (Availability to Travel) -- yes/no */
    @Column(name = "has_travel_availability")
    private Boolean hasTravelAvailability;
}
