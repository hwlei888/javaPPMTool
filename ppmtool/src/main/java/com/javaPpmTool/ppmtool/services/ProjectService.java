package com.javaPpmTool.ppmtool.services;

import com.javaPpmTool.ppmtool.domain.Project;
import com.javaPpmTool.ppmtool.repositories.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    public Project saveOrUpdateProject(Project project){

        return projectRepository.save(project);
    }
}
