package com.example.taskmanagementsystem;

import com.example.taskmanagementsystem.repository.TaskRepository;
import com.example.taskmanagementsystem.service.TaskService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.http.MediaType;

import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@AutoConfigureDataJpa
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@WithUserDetails(value = "admin@test.com", userDetailsServiceBeanName = "userDetailsService")
@SpringBootTest
public class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskRepository taskRepository;

    @AfterEach
    public void cleanup() {
        SecurityContextHolder.clearContext();
    }
    private String adminToken() throws Exception {
        String user = """
                {
                "email": "admin@test.com",
                "password": 1111
                }
                """;

        var response = mockMvc.perform(MockMvcRequestBuilders.post("/auth/authenticate")
                .content(user)
                .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse().getContentAsString();
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> map = objectMapper.readValue(response, Map.class);
        System.out.println(map.get("token"));

        return map.get("token");
    }

    @Test
    @Order(1)
    void createTask() throws Exception {

        String task = """
                {
                "title": "New Task",
                "description": "Test new Task",
                "status": "WAITING",
                "priority": "LOW",
                "performerEmails": ["admin@test.com"]
                }
                """;

        mockMvc.perform(MockMvcRequestBuilders.post("/tasks")
                        .content(task)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + adminToken())
                )
                .andExpect(status().isOk());
    }

    @Test
    @Order(2)
    void addPerformer_ShouldReturn200_WhenValidRequest() throws Exception {
        var taskId = taskRepository.findAll().get(0).getId();
        String task = String.format("""
        {
        "taskId": "%d",
        "performerName": "admin@test.com"
        }
        """, taskId);

        mockMvc.perform(MockMvcRequestBuilders.post("/tasks/performers")
                        .content(task)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }

    @Test
    @Order(3)
    void getTasksByAuthor_ShouldReturnTasks_WhenPerformerExists() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/tasks/authors/{authorName}", "admin@test.com")
                        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("New Task"))
                .andExpect(jsonPath("$[0].description").value("Test new Task"));
    }

    @Test
    @Order(4)
    void getTasksByPerformer_ShouldReturnTasks_WhenPerformerExists() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/tasks/performers/{performerName}", "admin@test.com")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("New Task"))
                .andExpect(jsonPath("$[0].description").value("Test new Task"));
    }

    @Test
    @Order(5)
    void updateTask() throws Exception {

        var taskId = taskRepository.findAll().get(0).getId();

        String task = """
                {
                "title": "New Task",
                "description": "Test new Task",
                "priority": "HIGH"
                }
                """;

        mockMvc.perform(MockMvcRequestBuilders.put("/tasks/{id}", taskId)
                        .content(task)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());
    }

    @Test
    @Order(6)
    void deleteTask() throws Exception {
        var taskId = taskRepository.findAll().get(0).getId();
        mockMvc.perform(MockMvcRequestBuilders.delete("/tasks/{id}", taskId))
                .andExpect(status().isOk());
    }
}
