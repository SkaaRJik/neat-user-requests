package ru.filippov.neatvue.domain;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Table(name = "authority")
public class Auth {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "user_id")
    @NotBlank
    private Long userId;

    @Column(name = "refresh_token")
    @NotBlank
    private String refreshToken;

    @Column
    @NotBlank
    private String ip;

    @Column
    @NotBlank
    private String device;



}
