package ru.filippov.neat.domain;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@Entity
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "auth", schema = "public")
public class Auth {
    @Id
    @SequenceGenerator(name = "AUTH_ID_GEN", sequenceName = "auth_id_sequence", allocationSize = 1, schema = "public")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AUTH_ID_GEN")
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id", nullable = false)
    private User user;

    @Column(name = "refresh_token", nullable = false)
    private String refreshToken;

    @Column(name = "previous_refresh_token")
    private String previousToken;

    @Column(name = "expiration_date", nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private Date expirationDate;



}
