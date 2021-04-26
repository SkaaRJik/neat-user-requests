package ru.filippov.neat.entity;

import com.fasterxml.jackson.annotation.JsonView;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
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
@Table(name = "experiment_results", schema = "public", catalog = "neat")
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class ExperimentResult {

    @Id
    @SequenceGenerator(name = "EXPERIMENT_RESULT_ID_GEN", sequenceName = "experiment_result_id_sequence", allocationSize = 1, schema = "public")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "EXPERIMENT_RESULT_ID_GEN")
    @Column(name = "id")
    private Long id;


    @Basic
    @Column(name = "model", columnDefinition = "jsonb")
    @Type(type = "jsonb")
    private Map model;


    @Column(name = "train_errors", columnDefinition = "jsonb")
    @Type(type = "jsonb")
    private List<Map<String, Object>> trainErrors;


    @Column(name = "test_errors", columnDefinition = "jsonb")
    @Type(type = "jsonb")
    private List<Map<String, Object>> testErrors;

    @Basic
    @Column(name = "prediction_error")
    private Double predictionError;

    @Basic
    @Column(name = "creation_date", nullable = false)
    private LocalDateTime creationDate;

    @Basic
    @Column(name = "prediction_report_file")
    private String predictionReportFile;

}
