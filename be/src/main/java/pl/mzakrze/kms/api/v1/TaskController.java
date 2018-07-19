package pl.mzakrze.kms.api.v1;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.mzakrze.kms.task.Task;
import pl.mzakrze.kms.task.TaskRepository;
import pl.mzakrze.kms.user.UserProfile;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static pl.mzakrze.kms.api.ApiConstants.API_V1;

@RestController(API_V1 + "/task")
public class TaskController extends UserProfileAwareController {

    @Autowired
    private TaskRepository taskRepository;

    @GetMapping
    public List<Task> myTasks(){
        UserProfile user = getUserAuthenticationState();
        return taskRepository.findAll().stream()
                .filter(e -> e.getUserProfile().getId().equals(user.getId()))
                .collect(Collectors.toList());
    }

    @PostMapping
    public Integer createTask(@RequestBody CreateTaskIn in){
        Task task = new Task();
        task.setUserProfile(getUserAuthenticationState());
        task.setDescription(in.description);
        task.setTitle(in.title);
        task = taskRepository.save(task);
        return task.getId();
    }

    @DeleteMapping(API_V1 + "/task" + "/{taskId}") // TODO - czy nie jest prefiksowane z adnotacji z klasy?
    public ResponseEntity<Task> deleteTask(@PathVariable("taskId") Integer taskId){
        Task task = taskRepository.findOne(taskId);
        if(task == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        if(task.getUserProfile().getId().equals(getUserAuthenticationState().getId()) == false){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        taskRepository.delete(task);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Data
    static class CreateTaskIn {
        public String description;
        public String title;
    }

}