package com.example.spring.controller;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.example.spring.dto.AccountDTO;
import com.example.spring.dto.UserDTO;
import com.example.spring.model.UserModel;
import com.example.spring.response.ApiResponse;
import com.example.spring.service.FileService;
import com.example.spring.service.UserService;

@RestController
@RequestMapping("/api/v1")
public class UserController {
    private final UserService userService;
    private final FileService fileService = new FileService(); // Khởi tạo đối tượng FileService

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // API lấy danh sách tất cả người dùng
    @GetMapping("/user")
    public List<UserModel> getAllUsers() {
        return userService.getAllUsers();
    }

    // API thêm người dùng mới
    @PostMapping("/create-user")
    public ResponseEntity<ApiResponse<UserModel>> createUser(
            @RequestParam("email") String email,
            @RequestParam("phone") String phone,
            @RequestParam("password") String password,
            @RequestParam("username") String username,
            @RequestParam("role") String role,
            @RequestParam(value = "birthday", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date birthday,
            @RequestParam(value = "userImage", required = false) MultipartFile userImage) {

        try {
            String imageName = null;

            // Kiểm tra nếu có ảnh, nếu không thì bỏ qua
            if (userImage != null && !userImage.isEmpty()) {
                // Lưu ảnh vào tệp và lấy tên tệp
                System.out.println("create-image" + userImage);
                imageName = fileService.saveImageToFile(userImage);
            }

            // Chuyển dữ liệu thành UserDTO và AccountDTO
            UserDTO userDTO = new UserDTO();
            userDTO.setEmail(email);
            userDTO.setPhone(phone);
            userDTO.setUsername(username);
            userDTO.setImage(imageName); // Lưu tên ảnh vào cơ sở dữ liệu (hoặc null nếu không có ảnh)
            userDTO.setBirthday(birthday);

            AccountDTO accountDTO = new AccountDTO();
            accountDTO.setPassword(password);
            accountDTO.setRole(role);

            // Gọi service để tạo người dùng và tài khoản
            ApiResponse<UserModel> response = userService.createNewUser(userDTO, accountDTO);

            // Nếu thành công, trả về 201 CREATED
            if (response.getEC() == 0) {
                return ResponseEntity.status(HttpStatus.CREATED).body(response);
            }

            if (response.getStatus() == 400) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            if (response.getStatus() == 409) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
            }
            // Trả về lỗi chung nếu có lỗi không mong muốn
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);

        } catch (Exception e) {
            // Trả về lỗi chung nếu có ngoại lệ không mong muốn
            ApiResponse<UserModel> errorResponse = new ApiResponse<>(1, "Server error: " + e.getMessage(), 500);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    // API lấy chi tiết một người dùng theo ID
    @GetMapping("/{id}")
    public ResponseEntity<UserModel> getUserById(@PathVariable String id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    // API để lấy ảnh người dùng
    @GetMapping("/user/image/{imageName}")
    public ResponseEntity<Resource> getUserImage(@PathVariable String imageName) throws IOException {
        @SuppressWarnings("static-access")
        Path imagePath = Paths.get(fileService.IMAGE_DIRECTORY, imageName);
        Resource imageResource = new UrlResource(imagePath.toUri());
        if (imageResource.exists() || imageResource.isReadable()) {
            return ResponseEntity.ok().body(imageResource);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // API cập nhật thông tin người dùng
    @PutMapping("/update-user/{userid}")
    public ResponseEntity<ApiResponse<UserModel>> updateUser(@PathVariable("userid") String userid,
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "phone", required = false) String phone,
            @RequestParam(value = "password", required = false) String password,
            @RequestParam(value = "username", required = false) String username,
            @RequestParam(value = "role", required = false) String role,
            @RequestParam(value = "birthday", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date birthday,
            @RequestParam(value = "userImage", required = false) MultipartFile userImage) {

        try {
            String imageName = null;

            // Kiểm tra nếu có ảnh, nếu không thì bỏ qua
            if (userImage != null && !userImage.isEmpty()) {
                // Lưu ảnh vào tệp và lấy tên tệp
                imageName = fileService.saveImageToFile(userImage);
            }

            // Chuyển dữ liệu thành UserDTO
            UserDTO userDTO = new UserDTO();
            userDTO.setEmail(email);
            userDTO.setPhone(phone);
            userDTO.setUsername(username);
            userDTO.setImage(imageName); // Lưu tên ảnh vào cơ sở dữ liệu (hoặc null nếu không có ảnh)
            userDTO.setBirthday(birthday);

            AccountDTO accountDTO = new AccountDTO();
            accountDTO.setPassword(password);
            accountDTO.setRole(role);

            // Gọi service để cập nhật người dùng
            ApiResponse<UserModel> response = userService.updateUser(userid, userDTO, accountDTO);
            System.out.println("response" + response.getStatus());
            // Trả về response phù hợp
            if (response.getEC() == 0) {
                return ResponseEntity.status(HttpStatus.OK).body(response);
            }

            if (response.getStatus() == 400) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            if (response.getStatus() == 404) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            if (response.getStatus() == 409) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
            }

            // Trả về lỗi chung nếu có lỗi không mong muốn
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);

        } catch (Exception e) {
            ApiResponse<UserModel> errorResponse = new ApiResponse<>(1, "Server error: " + e.getMessage(), 500);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    // API xóa người dùng
    @DeleteMapping("/delete-user/{userid}")
    public ResponseEntity<ApiResponse<UserModel>> deleteUser(@PathVariable String userid) { // Dùng request body
        try {
            ApiResponse<UserModel> response = userService.deleteUser(userid);

            if (response.getEC() == 0) {
                return ResponseEntity.ok(response); // Trả về OK nếu xóa thành công
            }

            if (response.getStatus() == 404) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response); // Trả về lỗi nếu không tìm thấy người dùng
            }

            // Trả về mã trạng thái lỗi 400 (Bad Request)
            if (response.getStatus() == 400) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            return ResponseEntity.status(HttpStatus.CREATED).body(response); // Trả về lỗi hệ thống nếu có sự cố

        } catch (Exception e) {
            ApiResponse<UserModel> errorResponse = new ApiResponse<>(1, "Server error: " + e.getMessage(), 500);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse); // Trả về lỗi server nếu có lỗi
        }
    }

}