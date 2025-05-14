package com.example.todo;

import com.example.todo.entity.Todo;
import com.example.todo.repository.TodoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class TodoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        todoRepository.deleteAll();
    }

    @Test
    public void testGetAllTodos() throws Exception {
        Todo todo1 = new Todo();
        todo1.setTitle("Test Todo 1");
        todo1.setDescription("Description for test todo 1");
        todo1.setCompleted(false);
        todoRepository.save(todo1);

        mockMvc.perform(get("/api/todos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Test Todo 1"))
                .andExpect(jsonPath("$[0].description").value("Description for test todo 1"));
    }

    @Test
    public void testCreateTodo() throws Exception {
        Todo todo = new Todo();
        todo.setTitle("New Todo");
        todo.setDescription("This is a new todo");
        todo.setCompleted(false);

        mockMvc.perform(post("/api/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(todo)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("New Todo"))
                .andExpect(jsonPath("$.description").value("This is a new todo"));

        // DBに新しいTODOが作成されたか確認
        Todo createdTodo = todoRepository.findAll().get(0);
        assertEquals("New Todo", createdTodo.getTitle());
        assertEquals("This is a new todo", createdTodo.getDescription());
    }

    @Test
    public void testUpdateTodo() throws Exception {
        Todo todo = new Todo();
        todo.setTitle("Todo to update");
        todo.setDescription("Description of todo");
        todo.setCompleted(false);
        Todo savedTodo = todoRepository.save(todo);

        savedTodo.setTitle("Updated Todo");
        savedTodo.setDescription("Updated description");

        mockMvc.perform(put("/api/todos/" + savedTodo.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(savedTodo)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Todo"))
                .andExpect(jsonPath("$.description").value("Updated description"));

        // DBの値が更新されたことを確認
        Todo updatedTodo = todoRepository.findById(savedTodo.getId()).get();
        assertEquals("Updated Todo", updatedTodo.getTitle());
        assertEquals("Updated description", updatedTodo.getDescription());
    }

    @Test
    public void testDeleteTodo() throws Exception {
        Todo todo = new Todo();
        todo.setTitle("Todo to delete");
        todo.setDescription("Description of todo to delete");
        todo.setCompleted(false);
        Todo savedTodo = todoRepository.save(todo);

        mockMvc.perform(delete("/api/todos/" + savedTodo.getId()))
                .andExpect(status().isOk());

        // DBから削除されていることを確認
        assertTrue(todoRepository.findById(savedTodo.getId()).isEmpty());
    }
}

