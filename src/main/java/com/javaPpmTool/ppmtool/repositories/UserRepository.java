package com.javaPpmTool.ppmtool.repositories;

import com.javaPpmTool.ppmtool.domain.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository extends CrudRepository<User, Long> {
}

