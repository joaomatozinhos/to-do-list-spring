package br.com.rocketseat.todolist.task;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.rocketseat.todolist.utils.Utils;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/tasks")
public class TaskController {

	@Autowired
	private ITaskRepository taskRepository;

	@PostMapping("/create")
	public ResponseEntity create(@RequestBody TaskModel taskModel, HttpServletRequest request) {
		var idUser = request.getAttribute("idUser");
		taskModel.setIdUser((Long) idUser);

		LocalDateTime currentDate = LocalDateTime.now();

		if (currentDate.isAfter(taskModel.getStartAt()) || currentDate.isAfter(taskModel.getEndAt())) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("A data de início / data de término deve ser maior do que a data atual.");
		}

		if (taskModel.getStartAt().isAfter(taskModel.getEndAt())) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("A data de término deve ser maior do que a data de início.");
		}

		TaskModel task = this.taskRepository.save(taskModel);
		return ResponseEntity.status(HttpStatus.OK).body(task);
	}

	@GetMapping("/list")
	public List<TaskModel> list(HttpServletRequest request) {
		var idUser = request.getAttribute("idUser");
		List<TaskModel> tasks = this.taskRepository.findByIdUser((Long) idUser);
		return tasks;
	}

	@PutMapping("/update/{id}")
	public TaskModel update(@PathVariable Long id, @RequestBody TaskModel taskModel, HttpServletRequest request) {
		var idUser = request.getAttribute("idUser");

		var task = this.taskRepository.findById(id).orElse(null);

		Utils.copyNonNullProperties(taskModel, task);

		return this.taskRepository.save(task);
	}
}
