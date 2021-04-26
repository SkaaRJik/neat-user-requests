package ru.filippov.neat.entity;


import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
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
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id"
)
public class Project {
    @Id
    @SequenceGenerator(name = "PROJECT_ID_GEN", sequenceName = "project_id_sequence", allocationSize = 1, schema = "public")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PROJECT_ID_GEN")
    @JsonView(ProjectView.Id.class)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    @JsonView(ProjectView.Info.class)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_user_id", referencedColumnName = "id", nullable = false)
    @JsonIgnore
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

    @JsonView(ProjectView.FullInfo.class)
    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY)
    private List<Experiment> experiments;

    @JsonView(ProjectView.FullInfo.class)
    @Column(name = "source_file")
    private String sourceFile;

    @JsonView(ProjectView.FullInfo.class)
    @Column(name = "verified_file")
    private String verifiedFile;

    @JsonView(ProjectView.FullInfo.class)
    @Column(name = "verification_errors", columnDefinition = "jsonb")
    @Type(type = "jsonb")
    private Map<String, Object> verificationErrors;

    @JsonView(ProjectView.FullInfo.class)
    @Column(name = "verification_info", columnDefinition = "jsonb")
    @Type(type = "jsonb")
    private Map<String, Object> verificationInfo;


    @JsonView(ProjectView.FullInfo.class)
    @Column(name = "legend", columnDefinition = "jsonb")
    @Type(type = "jsonb")
    private Map<String, Object> legend;

    @JsonView(ProjectView.FullInfo.class)
    @Column(name = "log_is_allowed")
    private Boolean logIsAllowed;

    @JsonView(ProjectView.FullInfo.class)
    @Column(name = "headers", columnDefinition = "jsonb")
    @Type(type = "jsonb")
    private List headers;



}
