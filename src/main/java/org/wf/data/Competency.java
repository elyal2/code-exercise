package org.wf.data;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Embeddable
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder @EqualsAndHashCode(callSuper = false)
public class Competency {
    @Column(name = "name", nullable = false)
    @NotNull
    @Size(max = 1024) // Max length of 1024 characters
    private String name;

    @Enumerated(EnumType.STRING)
    @NotNull
    @Column(name = "level", nullable = false)
    private Expertise level;
}

enum Expertise {
    NONE, BASIC, INTERMEDIATE, ADVANCED, EXPERT
}
