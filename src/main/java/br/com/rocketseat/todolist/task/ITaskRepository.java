package br.com.rocketseat.todolist.task;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ITaskRepository extends JpaRepository<TaskModel, Long> {

}
