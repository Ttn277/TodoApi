package com.example.todo.entity;
// Entity nên nằm trong 1 package riêng để dễ quản lỳ

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

// @Data : Annotation này thuộc Lombok, giúp tự động sinh ra:
// Getter / Setter
// toString()
// equals() và hashCode()
// Constructor mặc định
// -> Giúp bạn ko phải viết tay các phương thức còn lại
@Data
@Entity     // Đánh dấu class Todo là 1 entity(thực thể), được ánh xạ vào bảng trong cơ sở dữ liệu
public class Todo {
    // GenerationType.AUTO : JPA sẽ chọn chiến lược phù hợp theo loại Database bạn dùng
    @Id     // Đánh dấu primary key của bảng
    @GeneratedValue(strategy = GenerationType.AUTO)     // Tự động sinh gia trị cho id khi tạo mới (Tự tăng)
    private Long id;

    private String title;
    private String description;
    private boolean completed;
}


// Gợi ý mở rộng
// 1.Thêm ràng buộc validation nếu muốn kiểm tra đầu vào (Kết hợp với @Valid trong Controller
// @NotBlank
// private String title;

// @Size(max = 255)
// private String description;

// 2.Tùy chỉnh tên bảng hoặc cột nếu cần
// @Entity(name = "todo_items")
// @Column(name = "task_title")
