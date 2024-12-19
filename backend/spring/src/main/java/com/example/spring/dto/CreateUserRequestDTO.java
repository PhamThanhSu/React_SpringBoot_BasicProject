package com.example.spring.dto;

public class CreateUserRequestDTO {
    private UserDTO user;
    private AccountDTO account;

    // Getter và Setter
    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public AccountDTO getAccount() {
        return account;
    }

    public void setAccount(AccountDTO account) {
        this.account = account;
    }
}
