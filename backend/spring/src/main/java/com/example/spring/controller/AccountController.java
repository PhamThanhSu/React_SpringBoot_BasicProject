package com.example.spring.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.spring.model.Account;
import com.example.spring.response.ApiResponse;
import com.example.spring.service.AccountService;

@RestController
@RequestMapping("/api/v1")
public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/account/{userid}")
    public ResponseEntity<ApiResponse<Account>> getAccountByUserid(@PathVariable String userid) {
        try {
            System.out.println("Userid: " + userid);
            Account account = accountService.getAccountByUserid(userid);
            System.out.println("Account: " + account);
            if (account != null) {
                return ResponseEntity.ok(new ApiResponse<>(200, "Lấy thông tin account thành công", 0, account));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>(1, "Không tìm thấy account"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(1, "Server error", 500));
        }
    }

}
