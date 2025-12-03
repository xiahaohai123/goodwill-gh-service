package com.wangkang.wangkangdataetlservice.dao.dataetlservice.security.repository;

import com.wangkang.wangkangdataetlservice.dao.dataetlservice.security.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    User findByUsernameIgnoreCase(String username);

    Boolean existsByUsernameIgnoreCase(String username);
}
