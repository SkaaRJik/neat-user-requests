package ru.filippov.neat.entity;

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
import ru.filippov.neat.entity.view.NeatConfigView;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Table(name = "neat_config", schema = "public", catalog = "neat")
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class NeatConfig {

    @Id
    @SequenceGenerator(name = "NEAT_CONFIG_ID_GEN", sequenceName = "neat_config_id_sequence", allocationSize = 1, schema = "public")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "NEAT_CONFIG_ID_GEN")
    @Column(name = "id")
    @JsonView(NeatConfigView.Id.class)
    private Long id;

    @Basic
    @Column(name = "normalized_data", columnDefinition = "jsonb")
    @Type(type = "jsonb")
    @JsonView(NeatConfigView.Settings.class)
    private ProjectConfigDto.NormalizedDataDto normalizedData;


    @Basic
    @Column(name = "neat_settings", columnDefinition = "jsonb")
    @Type(type = "jsonb")
    @JsonView(NeatConfigView.Settings.class)
    private List<Map<String, Object>> neatSettings;

    @Basic
    @Column(name = "prediction_window_size")
    @JsonView(NeatConfigView.Settings.class)
    private Short predictionWindowSize;

    @Basic
    @Column(name = "prediction_period")
    @JsonView(NeatConfigView.Settings.class)
    private Short predictionPeriod;

    @Basic
    @Column(name = "creation_date")
    private LocalDateTime creationDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_project_id", referencedColumnName = "id", nullable = false)
    private Project project;

    @Column(name = "training_error")
    @JsonView(NeatConfigView.Errors.class)
    private Double trainingError;

    @Column(name = "prediction_error")
    @JsonView(NeatConfigView.Errors.class)
    private Double predictionError;

    @Column(name = "test_error")
    @JsonView(NeatConfigView.Errors.class)
    private Double testError;

    public ExperimentData toExperimentData() {
        return new ExperimentData(
                id,
                project.getId(),
                normalizedData,
                neatSettings,
                predictionWindowSize,
                predictionPeriod
        );
    }


}
