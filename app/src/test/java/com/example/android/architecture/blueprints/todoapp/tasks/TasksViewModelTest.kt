package com.example.android.architecture.blueprints.todoapp.tasks

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.android.architecture.blueprints.todoapp.Event
import com.example.android.architecture.blueprints.todoapp.getOrAwaitValue
import org.hamcrest.CoreMatchers.not
import org.hamcrest.CoreMatchers.nullValue
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TasksViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var taskViewModel: TasksViewModel

    @Before
    fun setupViewModel() {
        taskViewModel = TasksViewModel(ApplicationProvider.getApplicationContext())
    }

    @Test
    fun addNewTask_setsNewTaskEvent(){
        // Create observer - no need for it to do anything.
        val observer = Observer<Event<Unit>> {}
        try{
            //Observe the LiveData forever
            taskViewModel.newTaskEvent.observeForever(observer)

            // WHEN adding a new task
            taskViewModel.addNewTask()

            // THEN teh new task event is triggered
            val value = taskViewModel.newTaskEvent.value
            assertThat(value?.getContentIfNotHandled(), (not(nullValue())))
        } finally {
            taskViewModel.newTaskEvent.removeObserver(observer)
        }
    }

    @Test
    fun addNewTask_setsNewTaskEvent_withUtilFunction(){
        // WHEN adding a new task
        taskViewModel.addNewTask()

        // THEN the new task event is triggered
        val value = taskViewModel.newTaskEvent.getOrAwaitValue()
        assertThat(value.getContentIfNotHandled(), (not(nullValue())))
    }

    @Test
    fun setFilterAllTasks_tasksAddViewVisible(){
        // WHEN the filter type is ALL_TASKS
        taskViewModel.setFiltering(TasksFilterType.ALL_TASKS)

        // THEN the "Add task" action is visible
        val value = taskViewModel.tasksAddViewVisible.getOrAwaitValue()
        assertThat(value, (not(nullValue())))
    }
}