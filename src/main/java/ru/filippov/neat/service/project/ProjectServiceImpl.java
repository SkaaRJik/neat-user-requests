package ru.filippov.neat.service.project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.filippov.neat.domain.Project;
import ru.filippov.neat.domain.User;
import ru.filippov.neat.repository.ProjectRepository;

import java.io.*;
import java.util.Map;

@Service
public class ProjectServiceImpl {
    final ProjectRepository projectRepository;

    @Value("${path.projects.data:./public/projects}")
    String projectsLocation;

    public ProjectServiceImpl(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public Project saveProject(Project project, User user, Map<String, Object> projectParams) throws IOException{

        File file = new File(String.format("%s/%s/data.ser", projectsLocation, user.getUsername()));
        if(!file.exists()){
            file.mkdirs();
        }

        FileOutputStream fos = new FileOutputStream(file);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(projectParams);
        oos.close();
        fos.close();

        project.setFilename(file.getName());

        Project newProject = projectRepository.save(project);



        return newProject;
    }

    public Project createProject(User user, Map<String, Object> projectParams) throws IOException {

        Project project = Project.builder()
                .name((String) projectParams.get("name"))
                .user(user)
                .build();

        Project newProject = saveProject(project, user, projectParams);
        return newProject;


    }
}
