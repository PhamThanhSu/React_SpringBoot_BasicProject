package com.example.spring.exception;

import com.example.spring.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = APIException.class)
    public ResponseEntity<ApiResponse<Object>> handleAPIException(APIException exception) {
        // Tạo ApiResponse để trả về cho client
        ApiResponse<Object> response = new ApiResponse<>();
        response.setEC(1);
        response.setMessage(exception.getMessage());
        response.setStatus(exception.getStatus());
        response.setData(exception.getData());

        // Trả về ResponseEntity với mã trạng thái phù hợp
        return ResponseEntity.status(exception.getStatus()).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleGenericException(Exception ex) {
        ApiResponse<Object> response = new ApiResponse<>();
        response.setEC(1);
        response.setMessage("Server error: " + ex.getMessage());
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());

        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
    }
}
