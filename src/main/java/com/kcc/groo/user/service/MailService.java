package com.kcc.groo.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class MailService {
	
	@Autowired
	JavaMailSender javaMailSender;
	
	public void sendVerificationEmail (String toEmail, String code) {
		 String subject = "이메일 인증번호 안내";
		 String content = """
			        <div style="max-width: 600px; margin: 0 auto; font-family: Arial, sans-serif; color: #333;">
			            <h2 style="text-align: center; color: #4CAF50;">그루(Groo) 이메일 인증</h2>
			            <p style="font-size: 16px; text-align: center;">
			            <br/>
			                아래 인증번호를 입력하여 이메일 인증을 완료해 주세요.
			            </p>
			            <div style="margin: 30px auto; padding: 20px; border-radius: 8px; background: #f4f6f8; text-align: center; width: fit-content;">
			                <span style="font-size: 28px; font-weight: bold; color: #2c3e50; letter-spacing: 3px;">
			                    %s
			                </span>
			            </div>
			            <p style="text-align: center; font-size: 14px; color: #777;">
			                인증번호는 <b style="color:red;">5분간</b> 유효합니다.<br/>
			            </p>
			            <hr style="margin: 40px 0;"/>
			            <p style="text-align: center; font-size: 12px; color: #aaa;">
			                © 2025 Groo Project. All rights reserved.
			            </p>
			        </div>
			    """.formatted(code);
;
		 
		 sendHtmlMail (toEmail, subject, content);
	}
	
	private void sendHtmlMail (String toEmail, String subject, String content) {
		try {
			MimeMessage message = javaMailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
			
			helper.setTo(toEmail);
			helper.setSubject(subject);
			helper.setText(content, true);
			
			javaMailSender.send(message);
		} catch (MessagingException e) {
			throw new RuntimeException("failed send email ", e);
		}
	}
}
