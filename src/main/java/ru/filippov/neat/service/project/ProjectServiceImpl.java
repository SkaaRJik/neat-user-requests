package ru.filippov.neat.service.project;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.filippov.neat.domain.Project;
import ru.filippov.neat.domain.ProjectStatus;
import ru.filippov.neat.domain.User;
import ru.filippov.neat.exceptions.PermissionException;
import ru.filippov.neat.repository.ProjectRepository;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.NoSuchElementException;

@Service
public class ProjectServiceImpl {
    private final ProjectRepository projectRepository;

    @Value("${path.projects.data:./public/projects}")
    String projectsLocation;

    public ProjectServiceImpl(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public Project saveProject(User user, Map<String, Object> projectParams) throws IOException {

        this.saveProjectDataToFile(projectParams, user.getUsername());

        Project project = null;

        if (projectParams.containsKey("id")) {
            project = projectRepository
                    .findById((Long) projectParams.get("id"))
                    .orElseThrow(() -> new NoSuchElementException(String.format("Project with id %s does not exist", projectParams.get("id"))));

        } else {
            project = Project.builder()
                    .name((String) projectParams.get("name"))
                    .user(user)
                    .creationDate(LocalDateTime.now())
                    .status(ProjectStatus.NEW)
                    .build();
        }


        project.setUpdatedDate(LocalDateTime.now());
        this.saveProjectDataToFile(projectParams, user.getUsername());


        project = projectRepository.save(project);


        return project;
    }


    protected File saveProjectDataToFile(Map<String, Object> projectParams, String username) throws IOException {
        File file = new File(String.format("%s/%s/data.ser", projectsLocation, username));
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }

        FileOutputStream fos = new FileOutputStream(file);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(projectParams);
        oos.close();
        fos.close();
        return file;
    }

    public Page<Project> getProjectsByUser(User user, int curPage, int itemsPerPage) {
        Pageable pageable = PageRequest.of(curPage, itemsPerPage);

        Page<Project> projects = projectRepository.findAllByUserId(user.getId(), pageable);

        return projects;


    }

    public Project getProjectsById(Long id, User user) throws PermissionException, NoSuchElementException {

        Project project = projectRepository.findById(id).orElseThrow(() -> new NoSuchElementException("ERROR_PROJECT_DOES_NOT_EXISTS"));

        if(project.getUser().getId() != user.getId()){
            throw new PermissionException("ERROR_PROJECT_DOES_NOT_BELONG_TO_YOU");
        }

        return project;


    }

}
