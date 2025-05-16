package com.example.todo.controller;

import com.example.todo.entity.Todo;
import com.example.todo.service.TodoService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

// RestController : Kết hợp @Controller và  @ResponseBody, giúp trả về JSON thay vì view HTML - dùng để xây dựng REST API
// RequestMapping : Gắn mapping url cho controller/method
@RestController
@RequestMapping("/api/todos")       //Tất cả các endpoint trong controller này sẽ bắt đầu với /api/todos
public class TodoController {   //Dependency Injection: Spring sẽ tự inject dối tượng TodoService vào các controller này

    private final TodoService todoService;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }
    // @GetMapping, @PostMapping, @PutMapping, @DeleteMapping : Các dạng rút gọn của @RequestMapping
    // @PathVariable : Lấy giá trị từ URL
    // @RequestParam : Lấy giá trị từ query sang string
    // @RequestBody : Lấy dữ liệu JSON gửi từ client
    // @ResponseBody : Trả kết quả dưới dạng JSON
    @GetMapping
    public List<Todo> getAllTodos() {
        return todoService.getAllTodos();
    }   //Trả về toàn bộ danh sách công việc dưới dạng JSON

    @GetMapping("/{id}")
    public Optional<Todo> getTodoById(@PathVariable Long id) {
        return todoService.getTodoById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)     //Trả vê HTTP 201 khi tạo thành công
    public Todo createTodo(@RequestBody Todo todo) {
        return todoService.createTodo(todo);
    }

    @PutMapping("/{id}")
    public Todo updateTodo(@PathVariable Long id, @RequestBody Todo todo) {
        return todoService.updateTodo(id, todo);
    }

    @DeleteMapping("/{id}")
    public void deleteTodo(@PathVariable Long id) {
        if (!todoService.deleteTodo(id)) {
            throw new RuntimeException("Todo not found");
        }
    }
}


// Nếu muốn làm việc chuyên nghiệp hơn
// Dùng ResponseEntity thay vì @ResponseStatus để linh hoạt hơn trong việc trả về status và body
// Tạo custom exception class (VD: TodoNotFoundException ) với @ResponseStatus(HttpStatus.NOT_FOUND).
// Thêm xử lí validate đầu vào (@Valid) nếu có class DTO
// Thêm logging để dễ debug và theo dõi lỗi

