package com.javaPpmTool.ppmtool.services;

import com.javaPpmTool.ppmtool.domain.Backlog;
import com.javaPpmTool.ppmtool.domain.ProjectTask;
import com.javaPpmTool.ppmtool.repositories.BacklogRepository;
import com.javaPpmTool.ppmtool.repositories.ProjectTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectTaskService {

    @Autowired
    private BacklogRepository backlogRepository;

    @Autowired
    private ProjectTaskRepository projectTaskRepository;

    public ProjectTask addProjectTask(String projectIdentifier, ProjectTask projectTask){

        //Exceptions: Project not found

        //ProjectTasks to be added to a specific project, project != null, BackLog exists
        Backlog backlog = backlogRepository.findByProjectIdentifier(projectIdentifier);

        //set the relationship backlog to projectTask
        projectTask.setBacklog(backlog);

        //we want our project sequence to be like this: IDPRO-1 IDPRO-2 ... IDPRO-100 IDPRO-101
        Integer BacklogSequence = backlog.getPTSequence();
        //Update the BackLog SEQUENCE
        BacklogSequence++;
        backlog.setPTSequence(BacklogSequence);

        //Add sequence to Project Task
        //projectTask.setProjectIdentifier(backlog.getProjectIdentifier()+"-"+BacklogSequence);
        projectTask.setProjectSequence(projectIdentifier+"-"+BacklogSequence);
        projectTask.setProjectIdentifier(projectIdentifier);

        //INITIAL priority when priority null
        //In the future we need if(projectTask.getPriority() == 0 || projectTask.getPriority() == null) to handle the form
        if(projectTask.getPriority() == null){
            projectTask.setPriority(3);
        }

        //INITIAL status when status is null
        if(projectTask.getStatus() == "" || projectTask.getStatus() == null){
            projectTask.setStatus("TO_DO");
        }

        return projectTaskRepository.save(projectTask);
    }

    public Iterable<ProjectTask> findBacklogById(String id) {
        return projectTaskRepository.findByProjectIdentifierOrderByPriority(id);
    }
}
