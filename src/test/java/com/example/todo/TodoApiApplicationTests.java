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

@SpringBootTest		//Khởi động toàn bộ ứng dụng Spring Boot để thực hiện test (giống như lúc chạy thật)
@AutoConfigureMockMvc	// Cho phép sử dụng MockMvc để giả lập HTTP request mà ko cần chạy trên server thật(Tomcat, v.v)
public class TodoApiApplicationTests {

	@Autowired
	private MockMvc mockMvc;	// MockMvc : Dùng để gửi request giả lập dến các API endpoint

	@Autowired
	private TodoRepository todoRepository;		// TodoRepository : Truy câp cơ sở dữ liệu để kiểm tra dữ liệu thâtj sau khi API chạy

	@Autowired
	private ObjectMapper objectMapper; 	// Chuyển đổi object Java <-> JSON

	@BeforeEach 	// Làm sạch dự liệu khi test
	public void setUp() {
		todoRepository.deleteAll();	// Đảm bảo mỗi test đều bắt đầu với database rỗng - tránh có quá nhiều kết quả test
	}

	@Test
	public void testGetAllTodos() throws Exception {
		Todo todo1 = new Todo();
		todo1.setTitle("Test Todo 1");
		todo1.setDescription("Description for test todo 1");
		todo1.setCompleted(false);
		todoRepository.save(todo1);

		mockMvc.perform(get("/api/todos"))	// Gủi GET request
				.andExpect(status().isOk())		// Kiểm tra HTTP 200 OK
				.andExpect(jsonPath("$[0].title").value("Test Todo 1")) 	// Dùng jsonPATH để kiểm tra nội dung JSON trả về
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
				.andExpect(status().isCreated()) 	// Kiểm tra HTTP 201 Created
				.andExpect(jsonPath("$.title").value("New Todo"))	// xác nhận dự liệu được lưu vào database
				.andExpect(jsonPath("$.description").value("This is a new todo"));

		//DBに新しいTODOが作成されたか確認
		Todo createTodo = todoRepository.findAll().get(0);
		assertEquals("New Todo", createTodo.getTitle());
		assertEquals("This is a new todo", createTodo.getDescription());
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

		mockMvc.perform(put("/api/todos/" + savedTodo.getId())	// Gửi PUT request để cập nhật cho todo
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
		todo.setTitle(("Todo to delete"));
		todo.setDescription("Description of todo to delete");
		todo.setCompleted(false);
		Todo savedTodo = todoRepository.save(todo);

		mockMvc.perform(delete("/api/todos/" + savedTodo.getId()))
				.andExpect(status().isOk());	// Kiểm tra trả về HTTP 200 OK

		//DBから削除されていることを確認
		assertTrue(todoRepository.findById(savedTodo.getId()).isEmpty());
	}
}

// Gợi ý mở rộng
// Thêm test cho GET /api/todos/{id}
// Thêm test cho trường hợp lỗi
// Sử dụng @TestMethodOrder để kiểm tra theo thứ tự nếu cần