package ru.filippov.neat.domain;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "projects", schema = "public")
public class Project {
    @Id
    @SequenceGenerator(name = "PROJECT_ID_GEN", sequenceName = "project_id_sequence", allocationSize = 1, schema = "public")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PROJECT_ID_GEN")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    @Column(name = "training_error")
    private Double trainingError;

    @Column(name = "prediction_error")
    private Double predictionError;

    @Column(name = "status", nullable = false)
    private ProjectStatus status;

    @Column(name = "created_date", updatable = false, nullable = false)
    private LocalDateTime creationDate;

    @Column(name = "updated_date", nullable = false)
    private LocalDateTime updatedDate;



}
