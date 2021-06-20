package ru.filippov.neat.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.*;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import ru.filippov.neat.entity.view.ExperimentView;
import ru.filippov.neat.entity.view.ProjectView;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Table(name = "experiments", schema = "public")
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id"
)
@NamedEntityGraph(name = "Experiment.project",
        attributeNodes = {
            @NamedAttributeNode("project"),
            @NamedAttributeNode("experimentResult")
        }
)
@ToString
public class Experiment {

    @Id
    @SequenceGenerator(name = "EXPERIMENT_ID_GEN", sequenceName = "experiment_id_sequence", allocationSize = 1, schema = "public")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "EXPERIMENT_ID_GEN")
    @Column(name = "id")
    @JsonView({ ProjectView.Info.class, ExperimentView.Id.class})
    private Long id;

    @Basic
    @Column(name = "name")
    @JsonView({ProjectView.Info.class, ProjectView.FullInfo.class, ExperimentView.Info.class})
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_project_id", referencedColumnName = "id")
    @JsonIgnore
    private Project project;

    @Basic
    @Column(name = "normalized_file")
    @JsonIgnore
    private String normalizedDataFile;

    @Basic
    @Column(name = "enable_log_transform")
    @JsonView({ExperimentView.FullInfo.class})
    private Boolean enableLogTransform;

    @Basic
    @Column(name = "normalization_method")
    @JsonView(ExperimentView.FullInfo.class)
    private String normalizationMethod;

    @Basic
    @Column(name = "normalization_statistic", columnDefinition = "jsonb")
    @Type(type = "jsonb")
    @JsonView(ExperimentView.FullInfo.class)
    private HashMap<String, Object> normalizationStatistic;

    @Basic
    @Column(name = "neat_settings", columnDefinition = "jsonb")
    @Type(type = "jsonb")
    @JsonView(ExperimentView.FullInfo.class)
    private List<HashMap<String, Object>> neatSettings;

    @Basic
    @Column(name = "columns", columnDefinition = "jsonb")
    @Type(type = "jsonb")
    @JsonView(ExperimentView.FullInfo.class)
    private List<HashMap<String, Object>> columns;

    @Basic
    @Column(name = "prediction_window_size")
    @JsonView(ExperimentView.FullInfo.class)
    private Short predictionWindowSize;

    @Basic
    @Column(name = "prediction_period")
    @JsonView(ExperimentView.FullInfo.class)
    private Short predictionPeriod;

    @Basic
    @Column(name = "train_end_index")
    @JsonView(ExperimentView.FullInfo.class)
    private Integer trainEndIndex;

    @Basic
    @Column(name = "test_end_index")
    @JsonView(ExperimentView.FullInfo.class)
    private Integer testEndIndex;

    @Basic
    @Column(name = "creation_date")
    @JsonView({ ProjectView.Info.class, ExperimentView.Info.class})
    private LocalDateTime creationDate;

    @Basic
    @Column(name = "updated_date")
    @JsonView({ProjectView.Info.class,ExperimentView.Info.class})
    private LocalDateTime updatedDate;

    @OneToOne
    @JoinColumn(name = "fk_experiment_result_id", referencedColumnName = "id")
    @JsonView({ExperimentView.FullInfo.class})
    private ExperimentResult experimentResult;

    @Basic
    @Column(name = "status")
    @JsonView({ ProjectView.Info.class, ExperimentView.Info.class})
    private ExperimentStatus status;

    public Experiment copy(Long id){

        StringBuilder stringBuilder = new StringBuilder("Copy ");
        stringBuilder.append(this.name);

        String experimentName = stringBuilder.toString();

        return new Experiment(
                id,
                experimentName,
                this.project,
                this.normalizedDataFile,
                this.enableLogTransform,
                this.normalizationMethod,
                this.normalizationStatistic,
                this.neatSettings,
                this.columns,
                this.predictionWindowSize,
                this.predictionPeriod,
                this.trainEndIndex,
                this.testEndIndex,
                LocalDateTime.now(),
                LocalDateTime.now(),
                null,
                ExperimentStatus.NORMALIZED
        );
    }
}
