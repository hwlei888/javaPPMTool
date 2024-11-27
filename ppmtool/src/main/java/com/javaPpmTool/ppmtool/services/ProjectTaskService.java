package com.javaPpmTool.ppmtool.services;

import com.javaPpmTool.ppmtool.domain.ProjectTask;
import com.javaPpmTool.ppmtool.repositories.BacklogRepository;
import com.javaPpmTool.ppmtool.repositories.ProjectTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectTaskService {

    @Autowired
    private BacklogRepository backlogRepository;

    @Autowired
    private ProjectTaskRepository projectTaskRepository;

    public ProjectTask addProjectTask(){
        //ProjectTasks to be added to a specific project, project != null, BackLog exists
        //set the relationship backlog to projectTask
        //we want our project sequence to be like this: IDPRO-1 IDPRO-2 ... 100 101
        //Update the BackLog SEQUENCE

        //INITIAL priority when priority null
        //INITIAL status when status is null
    }


}
