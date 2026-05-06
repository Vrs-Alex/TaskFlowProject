package vrsalex.task.domain

import vrsalex.item.domain.repository.SubItemRepository

interface TaskRepository : SubItemRepository<Task, TaskCreate, TaskUpdate>
