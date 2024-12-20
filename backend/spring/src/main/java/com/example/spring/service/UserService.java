package com.example.spring.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.spring.dto.AccountDTO;
import com.example.spring.dto.UserDTO;
import com.example.spring.model.Account;
import com.example.spring.model.UserModel;
import com.example.spring.repository.AccountRepository;
import com.example.spring.repository.UserRepository;
import com.example.spring.response.ApiResponse;
import com.example.spring.exception.APIException;
import jakarta.transaction.Transactional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;

    public UserService(UserRepository userRepository, AccountRepository accountRepository) {
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
    }

    public List<UserModel> getAllUsers() {
        return userRepository.findAll();
    }

    public UserModel getUserById(String id) {
        return userRepository.findById(id).orElse(null);
    }

    public boolean validatePhoneNumber(String phone) {
        // Biểu thức kiểm tra số điện thoại hợp lệ
        String regex = "^0\\d{9}$";
        return phone.matches(regex);
    }

    public boolean validateEmail(String email) {
        // Kiểm tra định dạng email
        String regex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";
        return email.matches(regex);
    }

    // Tạo user mới tự động tạo account cho user đó
    @Transactional
    public ApiResponse<UserModel> createNewUser(UserDTO userDTO, AccountDTO accountDTO) {
        // Kiểm tra thông tin đầu vào
        if (userDTO.getEmail().isBlank() || userDTO.getPhone().isBlank() || userDTO.getUsername().isBlank()
                || accountDTO.getPassword().isBlank() || accountDTO.getRole().isBlank()) {
            throw new APIException(1, "Vui lòng nhập đầy đủ các thông tin!", 400);
        }

        // Kiểm tra email đã tồn tại
        if (userRepository.existsByEmail(userDTO.getEmail())) {
            throw new APIException(1, "Email đã tồn tại", 409);
        }

        // Kiểm tra số điện thoại đã tồn tại
        if (userRepository.existsByPhone(userDTO.getPhone())) {
            throw new APIException(1, "Số điện thoại đã tồn tại", 409);
        }

        // Kiểm tra độ hợp lệ của số điện thoại và email
        if (!validatePhoneNumber(userDTO.getPhone())) {
            throw new APIException(1, "Số điện thoại phải đủ 10 chữ số và bắt đầu bằng số 0", 400);
        }

        if (!validateEmail(userDTO.getEmail())) {
            throw new APIException(1, "Email không hợp lệ", 400);
        }

        // Chuyển UserDTO thành UserModel
        UserModel user = new UserModel();
        user.setUsername(userDTO.getUsername());
        user.setPhone(userDTO.getPhone());
        user.setEmail(userDTO.getEmail());
        user.setImage(userDTO.getImage());
        user.setBirthday(userDTO.getBirthday());

        // Lưu user vào database
        UserModel savedUser = userRepository.save(user);

        // Chuyển AccountDTO thành Account
        Account account = new Account();
        account.setUserid(savedUser.getUserid());
        account.setPassword(accountDTO.getPassword());
        account.setRole(accountDTO.getRole());

        // Lưu account vào database
        accountRepository.save(account);

        // Trả về ApiResponse thành công
        return new ApiResponse<>(201, "User created successfully", 0, savedUser);
    }

    @Transactional
    public ApiResponse<UserModel> updateUser(String userid, UserDTO userDTO, AccountDTO accountDTO) {
        // Kiểm tra sự tồn tại của người dùng theo userid
        UserModel existingUser = userRepository.findById(userid).orElse(null);
        if (existingUser == null) {
            throw new APIException(1, "User not found", 404); // 404 Not Found
        }

        // Kiểm tra thông tin đầu vào
        if (userDTO.getEmail().isBlank() || userDTO.getPhone().isBlank() || userDTO.getUsername().isBlank()
                || accountDTO.getRole().isBlank()) {
            throw new APIException(1, "Vui lòng nhập đầy đủ các thông tin!", 400);
        }

        // Kiểm tra độ hợp lệ của email và số điện thoại
        if (!validatePhoneNumber(userDTO.getPhone())) {
            throw new APIException(1, "Số điện thoại không hợp lệ", 400); // 400 Bad Request
        }

        if (!validateEmail(userDTO.getEmail())) {
            throw new APIException(1, "Email không hợp lệ", 400); // 400 Bad Request
        }

        // Kiểm tra nếu email được cung cấp và đã tồn tại trong hệ thống
        if (!userDTO.getEmail().equals(existingUser.getEmail())
                && userRepository.existsByEmail(userDTO.getEmail())) {
            throw new APIException(1, "Email đã tồn tại", 409); // 409 Conflict
        }

        // Kiểm tra nếu số điện thoại được cung cấp và đã tồn tại trong hệ thống
        if (!userDTO.getPhone().equals(existingUser.getPhone())
                && userRepository.existsByPhone(userDTO.getPhone())) {
            throw new APIException(1, "Số điện thoại đã tồn tại", 409); // 409 Conflict
        }

        // Cập nhật thông tin người dùng
        existingUser.setEmail(userDTO.getEmail());
        existingUser.setPhone(userDTO.getPhone());
        existingUser.setUsername(userDTO.getUsername());
        existingUser.setImage(userDTO.getImage() != null ? userDTO.getImage() : existingUser.getImage());
        existingUser.setBirthday(userDTO.getBirthday() != null ? userDTO.getBirthday() : existingUser.getBirthday());

        // Lưu thông tin người dùng đã cập nhật
        userRepository.save(existingUser);

        // Cập nhật thông tin tài khoản
        Account account = accountRepository.findByUserid(userid).orElse(null);
        System.out.println("id cua account" + account.getAccountid());
        System.out.println("role" + account.getRole());
        System.out.println("pass account" + account.getPassword());
        System.out.println("pass account - dto" + accountDTO.getPassword());
        if (account != null) {
            account.setPassword(accountDTO.getPassword().isBlank() ? account.getPassword() : accountDTO.getPassword());
            account.setRole(accountDTO.getRole());

            // Lưu tài khoản đã cập nhật
            accountRepository.save(account);
        }

        // Trả về kết quả thành công
        return new ApiResponse<>(200, "User updated successfully", 0, existingUser);
    }

    @Transactional
    public ApiResponse<UserModel> deleteUser(String userid) {
        UserModel existingUser = userRepository.findById(userid).orElse(null);
        if (existingUser == null) {
            throw new APIException(1, "User not found", 404); // 404 Not Found
        }

        // Xóa account khỏi CSDL
        Account account = accountRepository.findByUserid(userid).orElse(null);
        if (account != null) {
            accountRepository.delete(account);
        }

        // Xóa người dùng khỏi CSDL
        userRepository.delete(existingUser);

        // Trả về phản hồi thành công nếu xóa thành công
        return new ApiResponse<>(0, "User deleted successfully", 200);
    }

}
