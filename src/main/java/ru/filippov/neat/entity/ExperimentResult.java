package ru.filippov.neat.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.*;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Table(name = "experiment_results", schema = "public")
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id"
)
@ToString
public class ExperimentResult {

    @Id
    @SequenceGenerator(name = "EXPERIMENT_RESULT_ID_GEN", sequenceName = "experiment_result_id_sequence", allocationSize = 1, schema = "public")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "EXPERIMENT_RESULT_ID_GEN")
    @Column(name = "id")
    private Long id;


    @Column(name = "model", columnDefinition = "jsonb")
    @Type(type = "jsonb")
    private String model;


    @Column(name = "train_errors", columnDefinition = "jsonb")
    @Type(type = "jsonb")
    private String trainErrors;


    @Column(name = "test_errors", columnDefinition = "jsonb")
    @Type(type = "jsonb")
    private String testErrors;

    @Basic
    @Column(name = "prediction_error")
    private Double predictionError;

    @Basic
    @Column(name = "prediction_result")
    private String predictionResult;

    @Basic
    @Column(name = "creation_date", nullable = false)
    private LocalDateTime creationDate;

    @Basic
    @Column(name = "prediction_result_file")
    private String predictionResultFile;

    @Basic
    @Column(name = "prediction_report_file")
    private String predictionReportFile;

    @Column(name = "window_train_statistic", columnDefinition = "jsonb")
    @Type(type = "jsonb")
    private String windowTrainStatistic;



}
