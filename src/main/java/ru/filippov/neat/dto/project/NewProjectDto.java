package ru.filippov.neat.dto.project;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class NewProjectDto {
    @NotBlank
    @Size(min=3, max = 60)
    private String projectName;

    private MultipartFile file;
}
