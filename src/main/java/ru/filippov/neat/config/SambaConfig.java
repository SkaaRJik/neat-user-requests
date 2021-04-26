package ru.filippov.neat.config;

import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.filippov.neat.samba.SambaWorker;

import java.io.IOException;

@Configuration
@Data
@Log4j2
public class SambaConfig {
    @Value("${samba.host}")
    private String URL;
    @Value("${samba.username}")
    private String USER_NAME;
    @Value("${samba.password}")
    private String PASSWORD;
    @Value("${samba.shared_directory}")
    private String SHARED_DIRECTORY;


    @Value("${samba.source_file_folder:source}")
    private String FOLDER_FOR_SOURCE_FILES;
    @Value("${samba.verified_file_folder:verified}")
    private String FOLDER_FOR_VERIFIED_FILES;
    @Value("${samba.normalized_file_folder:normalized}")
    private String FOLDER_FOR_NORMALIZED_FILES;
    @Value("${samba.report_file_folder:report}")
    private String FOLDER_FOR_REPORT_FILES;


    @Bean
    public SambaWorker getSambaWorker() throws IOException {
        return new SambaWorker(
                URL,
                SHARED_DIRECTORY,
                USER_NAME,
                PASSWORD,
                FOLDER_FOR_SOURCE_FILES,
                FOLDER_FOR_VERIFIED_FILES,
                FOLDER_FOR_NORMALIZED_FILES,
                FOLDER_FOR_REPORT_FILES
                );
    }

}
