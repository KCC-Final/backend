package com.kcc.groo.user.dao;

import org.springframework.data.repository.CrudRepository;

import com.kcc.groo.user.data.model.EmailVerification;

public interface EmailVerificationRepository extends CrudRepository<EmailVerification, String>{

}
