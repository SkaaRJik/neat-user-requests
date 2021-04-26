package ru.filippov.neat.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import ru.filippov.neat.dto.ExperimentData;
import ru.filippov.neat.dto.ProjectConfigDto;
import ru.filippov.neat.entity.view.ExperimentView;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Table(name = "experiments", schema = "public", catalog = "neat")
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class Experiment {

    @Id
    @SequenceGenerator(name = "EXPERIMENT_ID_GEN", sequenceName = "experiment_id_sequence", allocationSize = 1, schema = "public")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "EXPERIMENT_ID_GEN")
    @Column(name = "id")
    @JsonView(ExperimentView.Id.class)
    private Long id;

    @Basic
    @Column(name = "name")
    @JsonView(ExperimentView.Info.class)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_project_id", referencedColumnName = "id", nullable = false)
    @JsonIgnore
    private Project project;

    @Basic
    @Column(name = "normalized_file")
    @JsonIgnore
    private String normalizedDataFile;

    @Basic
    @Column(name = "normalization_method")
    @JsonView(ExperimentView.FullInfo.class)
    private String normalizationMethod;

    @Basic
    @Column(name = "normalization_statistic", columnDefinition = "jsonb")
    @Type(type = "jsonb")
    @JsonView(ExperimentView.FullInfo.class)
    private Map<String, Object> normalization_statistic;

    @Basic
    @Column(name = "neat_settings", columnDefinition = "jsonb")
    @Type(type = "jsonb")
    @JsonView(ExperimentView.FullInfo.class)
    private List<Map<String, Object>> neatSettings;

    @Basic
    @Column(name = "columns", columnDefinition = "jsonb")
    @Type(type = "jsonb")
    @JsonView(ExperimentView.FullInfo.class)
    private List<Map<String, Object>> columns;

    @Basic
    @Column(name = "prediction_window_size")
    @JsonView(ExperimentView.FullInfo.class)
    private Short predictionWindowSize;

    @Basic
    @Column(name = "prediction_period")
    @JsonView(ExperimentView.FullInfo.class)
    private Short predictionPeriod;

    @Basic
    @Column(name = "creation_date", nullable = false)
    @JsonView(ExperimentView.Info.class)
    private LocalDateTime creationDate;

    @Basic
    @Column(name = "updated_date", nullable = false)
    @JsonView(ExperimentView.Info.class)
    private LocalDateTime updatedDate;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "fk_experiment_result_id", referencedColumnName = "id")
    private ExperimentResult experimentResult;

}
