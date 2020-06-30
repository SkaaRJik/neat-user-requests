package ru.filippov.neat.entity;


import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import ru.filippov.neat.dto.ProjectConfigDto;
import ru.filippov.neat.entity.view.NeatConfigView;
import ru.filippov.neat.entity.view.ProjectView;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

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
    @JsonView(ProjectView.Id.class)
    private Long id;

    @Column(name = "name", nullable = false)
    @JsonView(ProjectView.Info.class)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_user_id", referencedColumnName = "id", nullable = false)
    private User user;

    @Column(name = "status", nullable = false)
    @JsonView(ProjectView.Info.class)
    private ProjectStatus status;

    @Column(name = "created_date", updatable = false, nullable = false)
    @JsonView(ProjectView.Info.class)
    private LocalDateTime creationDate;

    @Column(name = "updated_date", nullable = false)
    @JsonView(ProjectView.Info.class)
    private LocalDateTime updatedDate;

    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY)
    private List<NeatConfig> neatConfigs;

    @Basic(fetch = FetchType.LAZY)
    @Column(name = "data", columnDefinition = "jsonb")
    @Type(type = "jsonb")
    @JsonView(ProjectView.Data.class)
    private Map<String, Object> data;



}
