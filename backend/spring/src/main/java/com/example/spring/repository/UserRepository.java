package com.example.spring.repository;

//import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.spring.model.UserModel;

@Repository
public interface UserRepository extends JpaRepository<UserModel, String> {
    //List<UserModel> findByUserName(String username);
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);
}
