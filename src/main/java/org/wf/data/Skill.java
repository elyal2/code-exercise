package org.wf.data;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.wf.data.tracing.Audit;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Cacheable
@Cache(region = "skills", usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "skills")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder @EqualsAndHashCode(callSuper = false)
public class Skill extends Audit {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false)
    @EqualsAndHashCode.Include
    private UUID id;
//    Competencia (Competency)
    @Column(name = "name", length = 1024)
    @Size(max = 1024) // Max length of 1024 characters
    private String name;
//    Prioridad / Criticidad (Priority / Criticality)
    @Column(name = "priority", nullable = false)
    @Enumerated(EnumType.STRING)
    private Criticality priority;
//    Interacción Interna (Internal Interaction)
    @Column(name = "internal_interaction", nullable = false)
    @NotNull
    @Enumerated(EnumType.STRING)
    private Frequency internalInteraction;
    @Column(name = "external_interaction", nullable = false)
    @NotNull
    @Enumerated(EnumType.STRING)
    private Frequency externalInteraction;
//    Formación / Titulación (Education / Qualification)
    @Column(name = "education", length = 1024)
    @Size(max = 1024) // Max length of 1024 characters
    private String education;
//    Especialidad / Estudios de Postgrado (Specialization / Postgraduate Studies)
    @Column(name = "specialization", length = 1024)
    @Size(max = 1024) // Max length of 1024 characters
    private String specialization;
//    Experiencia laboral anterior (Previous Work Experience)
    @Column(name = "previous_work_experience", length = 1024)
    @Size(max = 1024) // Max length of 1024 characters
    private String previousWorkExperience;
//    Idiomas (Languages)
    @ElementCollection
    @CollectionTable(name = "language_skills", joinColumns = @JoinColumn(name = "skill_id"))
    private Set<Competency> languages = new HashSet<>();
//    Ofimática (Office Automation)
    @ElementCollection
    @CollectionTable(name = "office_automation_skills", joinColumns = @JoinColumn(name = "skill_id"))
    private Set<Competency> officeAutomation = new HashSet<>();
}

enum Criticality {
    LOW, MEDIUM, HIGH
}
enum Frequency {
    NONE, DAILY, FREQUENT, OCCASIONAL
}
