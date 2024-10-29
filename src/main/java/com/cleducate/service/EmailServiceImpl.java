package com.cleducate.service;

import com.cleducate.constants.EmailConstants;
import com.cleducate.entity.Course;
import com.cleducate.entity.User;
import com.cleducate.entity.courseEnrollment.UserCourseEnrollment;
import com.cleducate.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.internet.MimeMessage;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@Slf4j
public class EmailServiceImpl  implements  EmailService{


    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;
    private final UserRepository userRepository;

    @Autowired
    public EmailServiceImpl(JavaMailSender javaMailSender, TemplateEngine templateEngine, UserRepository userRepository){
        this.javaMailSender = javaMailSender;
        this.templateEngine = templateEngine;
        this.userRepository = userRepository;
    }

    @Value("${spring.mail.username}")
    private String fromEamil;

    //custom Executor interface of superclass
    private  final ExecutorService emailExecutor = Executors.newFixedThreadPool(10);

    public void sendEmail(String email, Context context, String emailSubject, String template){
        CompletableFuture.runAsync(()->{
            try{
                String emailBody = templateEngine.process(template, context);
                MimeMessage mimeMessage = javaMailSender.createMimeMessage();
                MimeMessageHelper msg = new MimeMessageHelper(mimeMessage);
                msg.setSubject(emailSubject);
                msg.setText(emailBody, true);
                msg.setTo(email);
                msg.setFrom(fromEamil);
                javaMailSender.send(mimeMessage);
                log.info("MAIL SENT SUCCESSFULLY TO EMAIL_ID :: {} , EMAIL_SUBJECT :: {}, TEMPLATE_NAME :: {}", email, emailSubject, template);

            }catch (Exception e){
                log.error("EXCEPTION WHILE SENDING EMAIL TO - EMAIL_ID :: {}, EMAIL_SUBJECT :: {}, TEMPLATE_NAME :: {}, EXCEPTION :: {}", email, emailSubject, template, e.getMessage());
            }
        }, emailExecutor);

    }

    @Override
    public void sendEmailToClientOnRegistration(User user, String password) {
        try{
            Context context = new Context();
            context.setVariable("email", user.getEmail());
            context.setVariable("password", password);
            sendEmail(user.getEmail(), context, EmailConstants.CLIENT_REGISTRATION_SUBJECT, EmailConstants.CLIENT_REGISTRATION_TEMPLATE);
        }catch (Exception e){
            log.error("EXCEPTION WHILE SENDING THE EMAIL TO CLIENT ON REGISTRATION :: {} ", e.getMessage());
        }
    }

    @Override
    public void sendEmailOnProfileCompletion(User user) {
        try{
            Context context = new Context();
            context.setVariable("fullName", user.getFirstName()+" "+user.getLastName());
            context.setVariable("email", user.getEmail());
            context.setVariable("phone", user.getPhone());
            sendEmail(user.getEmail(), context, EmailConstants.CLIENT_PROFILE_SUCCESSFULLY_CREATED, EmailConstants.CLIENT_PROFILE_COMPLETION_TEMPLATE);
        }catch (Exception e){
            log.error("EXCEPTION WHILE SENDING THE EMAIL TO CLIENT ON REGISTRATION :: {} ", e.getMessage());
        }
    }

    @Override
    public void sendEmailToAdminOnCourseEnrollment(UserCourseEnrollment userCourseEnrollment) {
        try{
            User user = userCourseEnrollment.getUser();
            Course course = userCourseEnrollment.getCourse();
            String formattedDate = userCourseEnrollment.getAssignmentDate().format(DateTimeFormatter.ofPattern("MMM dd,yyyy"));

            Context context = new Context();
            context.setVariable("userName", user.getFirstName()+" "+user.getLastName());
            context.setVariable("userEmail", user.getEmail());
            context.setVariable("courseTitle", course.getTitle());
            context.setVariable("enrollmentDate", formattedDate);
            sendEmail("intkhabkhanofficial@gmail.com", context, EmailConstants.ADMIN_COURSE_ENROLLMENT_SUBJECT, EmailConstants.ADMIN_COURSE_ENROLLMENT_TEMPLATE);
        }catch (Exception e){
            log.error("EXCEPTION WHILE SENDING THE EMAIL TO ADMIN ON COURSE ENROLLMENT :: {} ", e.getMessage());
        }
    }

    @Override
    public void sendEmailToClientOnCourseEnrollment(UserCourseEnrollment userCourseEnrollment) {
        try{
            User user = userCourseEnrollment.getUser();
            Course course = userCourseEnrollment.getCourse();
            Context context = new Context();
            context.setVariable("userName", user.getFirstName()+" "+user.getLastName());
            context.setVariable("userEmail", user.getEmail());
            context.setVariable("courseTitle", course.getTitle());
            context.setVariable("courseDescription", course.getDescription());
            sendEmail(user.getEmail(), context, EmailConstants.CLIENT_COURSE_ENROLLMENT_SUBJECT, EmailConstants.CLIENT_COURSE_ENROLLMENT_TEMPLATE);
        }catch (Exception e){
            log.error("EXCEPTION WHILE SENDING THE EMAIL TO CLIENT ON COURSE ENROLLMENT :: {} ", e.getMessage());
        }
    }

    @Override
    public void sendEmailToUserOnForgetPassword(User user, String resetLink) {
        try{
            Context context = new Context();
            context.setVariable("userName", user.getFirstName()+" "+user.getLastName());
            context.setVariable("resetLink", resetLink);
            sendEmail(user.getEmail(), context, EmailConstants.PASSWORD_RESET_REQUEST_SUBJECT, EmailConstants.PASSWORD_RESET_REQUEST_TEMPLATE);
        }catch (Exception e){
            log.error("EXCEPTION WHILE SENDING THE EMAIL TO CLIENT ON FORGET PASSWORD :: {} ", e.getMessage());
        }
    }

    @Override
    public void sendEmailOnAccountBlock(User user) {
        try{
            Context context = new Context();
            context.setVariable("userName", user.getFirstName()+" "+user.getLastName());
            sendEmail(user.getEmail(), context, EmailConstants.ACCOUNT_BLOCK_SUBJECT, EmailConstants.ACCOUNT_BLOCK_TEMPLATE);
        }catch (Exception e){
            log.error("EXCEPTION WHILE SENDING THE EMAIL TO USER ON ACCOUNT BLOCK :: {} ", e.getMessage());
        }
    }

    @Override
    public void sendEmailOnAccountUnBlock(User user, String resetLink) {
        try{
            Context context = new Context();
            context.setVariable("userName", user.getFirstName()+" "+user.getLastName());
            context.setVariable("resetLink", resetLink);
            sendEmail(user.getEmail(), context, EmailConstants.ACCOUNT_UNBLOCK_SUBJECT, EmailConstants.ACCOUNT_UNBLOCK_TEMPLATE);
        }catch (Exception e){
            log.error("EXCEPTION WHILE SENDING THE EMAIL TO USER ON ACCOUNT REVOKE :: {} ", e.getMessage());
        }
    }
}
