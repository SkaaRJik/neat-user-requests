package ru.filippov.neat.dto.services.preprocessing;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.filippov.neat.entity.ProjectStatus;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class VerificationResult {
    private Long projectId;
    private Map<String, Object> errors;
    private Map<String, Object> info;
    private String verifiedFile;
    private Map<String, Object> legend;
    private boolean logIsAllowed;
    private Integer rows;
    private List headers;
    private ProjectStatus status;
}
