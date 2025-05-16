// Package chứa các lớp truy cập dưx liệu(repository).
// Theo đúng cấu trúc Spring Boot, các repository nên đc tách riêng để tổ chức và mở rộng sau này
package com.example.todo.repository;

import com.example.todo.entity.Todo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TodoRepository extends JpaRepository<Todo, Long>{
}
// TodoRepository : Là interface đại diện cho lớp truy vấn dữ liệu liên quan đến entity Todo
// extends JpaRepository<Todo, Long> :
// Todo : Tên entity bạn muốn thao tác
// Long : Kiểu dữ liệu của khóa chính (id) trong entity Todo


// Những gì nhận được từ JpaRepository (mà ko cần viết code)
// Spring Data JPA cung cấp sẵn các phương thức CRUD phổ biến, ví dụ :
// findAll() : Lấy tất cả các bản ghi (List<Todo>)
// findById(Long id) : Tìm 1 bản ghi theo ID
// save(Todo todo) : Lưu mới hoặc cập nhật 1 bản ghi
// deleteById(Long id) : xóa theo id
// existsById(Long id) : Kiểm tra có tồn tại id không
// count() : Đếm số lượng bản ghi

// Có thể mở rông thêm bằng phương thức tùy chỉnh
// Có thể thêm các phương thức như sau và Spring sẽ tự động sinh ra truy vấn
// List<Todo> findByCompleted(boolean completed);  //Tìm theo trạng thái hoàn thành
// List<Todo> findByTitleContaining(Sting keyword); // Tìm tiêu đề có chứa từ khóa

// Spring sẽ tự động
// Quét @Entity
// Nhận biết TodoRepository nhờ annotation @Repository nội bộ (Ko cần viết tay)
// Inject repository này vào service layer bằng @Autowired hoặc constructor

