package ru.filippov.neat.service.project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.filippov.neat.domain.Project;
import ru.filippov.neat.domain.User;
import ru.filippov.neat.repository.ProjectRepository;

import java.io.*;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class ProjectServiceImpl {
    private final ProjectRepository projectRepository;

    @Value("${path.projects.data:./public/projects}")
    String projectsLocation;

    public ProjectServiceImpl(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public Project saveProject(User user, Map<String, Object> projectParams) throws IOException{

        this.saveProjectDataToFile(projectParams, user.getUsername());

        Project project = null;

        if (projectParams.containsKey("id")){
            project = projectRepository
                    .findById((Long) projectParams.get("id"))
                    .orElseThrow(() ->  new NoSuchElementException(String.format("Project with id %s does not exist", projectParams.get("id"))));

        } else {
            project = Project.builder()
                    .name((String) projectParams.get("name"))
                    .user(user)
                    .build();
        }




        this.saveProjectDataToFile(projectParams, user.getUsername());




        project = projectRepository.save(project);



        return project;
    }


    protected File saveProjectDataToFile(Map<String, Object> projectParams, String username) throws IOException {
        File file = new File(String.format("%s/%s/data.ser", projectsLocation, username));
        if(!file.getParentFile().exists()){
            file.getParentFile().mkdirs();
        }

        FileOutputStream fos = new FileOutputStream(file);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(projectParams);
        oos.close();
        fos.close();
        return file;
    }



}
