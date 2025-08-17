import { Component, OnInit } from '@angular/core';
import { Task } from '../../models/task.model';
import { TaskService } from '../../services/task.service';

@Component({
  selector: 'app-task-list',
  templateUrl: './task-list.component.html'
})
export class TaskListComponent implements OnInit {
  tasks: Task[] = [];
  page = 0;
  size = 5;
  totalElements = 0;
  statusFilter: string = '';
  selectedTask: Task | null = null;

  constructor(private taskService: TaskService) {}

  ngOnInit(): void {
    this.fetchTasks();
  }

  fetchTasks(): void {
    this.taskService.getTasks(this.page, this.size, this.statusFilter).subscribe(data => {
      this.tasks = data.content;
      this.totalElements = data.totalElements;
    });
  }

  onPageChange(newPage: number): void {
    this.page = newPage;
    this.fetchTasks();
  }

  applyFilter(status: string): void {
    this.statusFilter = status;
    this.page = 0; // Reset to first page
    this.fetchTasks();
  }

  editTask(task: Task): void {
    this.selectedTask = task;
  }

  deleteTask(id: number): void {
    this.taskService.deleteTask(id).subscribe(() => this.fetchTasks());
  }

  saveTask(task: Task): void {
    if (task.id) {
      this.taskService.updateTask(task.id, task).subscribe(() => {
        this.selectedTask = null;
        this.fetchTasks();
      });
    } else {
      this.taskService.createTask(task).subscribe(() => {
        this.fetchTasks();
      });
    }
  }

  cancelEdit(): void {
    this.selectedTask = null;
  }
}
