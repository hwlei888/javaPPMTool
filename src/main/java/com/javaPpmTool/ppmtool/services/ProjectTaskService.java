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

    @Autowired
    private ProjectService projectService;

    public ProjectTask addProjectTask(String projectIdentifier,
                                      ProjectTask projectTask,
                                      String username){

            //ProjectTasks to be added to a specific project, project != null, BackLog exists
            Backlog backlog = projectService.findProjectByIdentifier(projectIdentifier, username).getBacklog();

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
            //In the future we need if(projectTask.getPriority() == null || projectTask.getPriority() == 0) to handle the form
            //It needs to be checking null first then 0
            //In FE, <option value={0}>Select Priority</option>
            if(projectTask.getPriority() == null || projectTask.getPriority() == 0){
                projectTask.setPriority(3);
            }

            //INITIAL status when status is null
            if(projectTask.getStatus() == "" || projectTask.getStatus() == null){
                projectTask.setStatus("TO_DO");
            }

            return projectTaskRepository.save(projectTask);
    }

    public Iterable<ProjectTask> findBacklogById(String id, String username) {

//        Project project = projectRepository.findByProjectIdentifier(id);
//
//        if(project == null){
//            throw new ProjectNotFoundException("Project with ID: '"+ id + "' does not exist");
//        }

        projectService.findProjectByIdentifier(id, username);

        return projectTaskRepository.findByProjectIdentifierOrderByPriority(id);
    }

    //Find ProjectTask by projectSequence
    //backlog_id is projectIdentifier like ID01
    //pt_id is projectSequence like ID01-1, ID01-2
    public ProjectTask findPTByProjectSequence(String backlog_id, String pt_id){

        //make sure we are searching on an existing backlog
        Backlog backlog = backlogRepository.findByProjectIdentifier(backlog_id);
        if(backlog == null){
            throw new ProjectNotFoundException("Project with ID: '"+ backlog_id + "' does not exist");
        }

        //make sure that our task exists
        ProjectTask projectTask = projectTaskRepository.findByProjectSequence(pt_id);

        if(projectTask == null){
            throw new ProjectNotFoundException("Project Task '"+ pt_id + "' not found");
        }

        //make sure that the backlog/project id in the path corresponds to the right project
        if(!projectTask.getProjectIdentifier().equals(backlog_id)){
            throw new ProjectNotFoundException("Project Task '"+ pt_id + "' does not exist in project: '" + backlog_id + "'");
        }

        return projectTask;
    }

    //Update project task
    public ProjectTask updateByProjectSequence(ProjectTask updatedTask, String backlog_id, String pt_id){
        //find existing project task
        ProjectTask projectTask = findPTByProjectSequence(backlog_id, pt_id);

        //replace it with updated task
        projectTask = updatedTask;

        //save update
        return projectTaskRepository.save(projectTask);
    }

    //Delete project task
    public void deletePTByProjectSequence(String backlog_id, String pt_id){
        ProjectTask projectTask = findPTByProjectSequence(backlog_id, pt_id);

        projectTaskRepository.delete(projectTask);
    }
}
