package com.javaPpmTool.ppmtool.services;

import com.javaPpmTool.ppmtool.domain.Backlog;
import com.javaPpmTool.ppmtool.domain.Project;
import com.javaPpmTool.ppmtool.domain.ProjectTask;
import com.javaPpmTool.ppmtool.exceptions.ProjectNotFoundException;
import com.javaPpmTool.ppmtool.repositories.BacklogRepository;
import com.javaPpmTool.ppmtool.repositories.ProjectRepository;
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

    @Autowired
    private ProjectRepository projectRepository;

    public ProjectTask addProjectTask(String projectIdentifier, ProjectTask projectTask){

        try{
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
        }catch (Exception e){
            //Exceptions: Project not found
            throw new ProjectNotFoundException("Project not Found");
        }
    }

    public Iterable<ProjectTask> findBacklogById(String id) {

        Project project = projectRepository.findByProjectIdentifier(id);

        if(project == null){
            throw new ProjectNotFoundException("Project with ID: '"+ id + "' does not exist");
        }

        return projectTaskRepository.findByProjectIdentifierOrderByPriority(id);
    }

    public ProjectTask findPTByProjectSequence(String backlog_id, String pt_id){

        //make sure we are searching on the right backlog

        return projectTaskRepository.findByProjectSequence(pt_id);
    }
}
