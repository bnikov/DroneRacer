package com.example.droneracer.web;

import com.example.droneracer.Model.Pilot;
import com.example.droneracer.Model.exceptions.PilotNotFoundException;
import com.example.droneracer.Service.PilotService;
import net.bytebuddy.utility.RandomString;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.Optional;

@Controller
public class ForgotPasswordController {

    private final JavaMailSender mailSender;

    private final PilotService pilotService;

    public ForgotPasswordController(JavaMailSender mailSender, PilotService pilotService) {
        this.mailSender = mailSender;
        this.pilotService = pilotService;
    }

    @GetMapping("/forgot_password")
    public String showForgotPasswordForm(Model model) {
        model.addAttribute("bodyContent", "forgotPassword");
        return "master-template";
    }

    @PostMapping("/forgot_password")
    public String processForgotPassword(@RequestParam String email, HttpServletRequest http, Model model) {
        String token = RandomString.make(30);
        model.addAttribute("bodyContent", "forgotPassword");
        try {
            pilotService.updateResetPasswordToken(token, email);
            String siteURL = http.getRequestURL().toString().replace(http.getServletPath(), "");
            String resetPasswordLink = siteURL + "/reset_password?token=" + token;
            sendEmail(email, resetPasswordLink);
            model.addAttribute("message", "We have sent a reset password link to your email. Please check.");
        } catch (PilotNotFoundException ex){
            model.addAttribute("error", "Pilot not found");
        } catch (UnsupportedEncodingException | MessagingException e){
            model.addAttribute("error", "Error while sending mail");
        }
        return "master-template";
    }

    public void sendEmail(String recipientEmail, String link)
            throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom("droneracer1234@gmail.com", "DroneRacer Support");
        helper.setTo(recipientEmail);

        String subject = "Here's the link to reset your password";

        String content = "<p>Hello,</p>"
                + "<p>You have requested to reset your password.</p>"
                + "<p>Click the link below to change your password:</p>"
                + "<p><a href=\"" + link + "\">Change my password</a></p>"
                + "<br>"
                + "<p>Ignore this email if you do remember your password, "
                + "or you have not made the request.</p>";

        helper.setSubject(subject);

        helper.setText(content, true);

        mailSender.send(message);
    }


    @GetMapping("/reset_password")
    public String showResetPasswordForm(@RequestParam String token, Model model) {
        model.addAttribute("token", token);
        model.addAttribute("bodyContent", "resetPassword");
        return "master-template";
    }

    @PostMapping("/reset_password")
    public String processResetPassword(HttpServletRequest request, Model model) {
        String token = request.getParameter("token");
        String password = request.getParameter("password");

        Optional<Pilot> pilot = pilotService.findByResetPasswordToken(token);         //TODO: add dual password input
        model.addAttribute("title", "Reset your password");

        if (pilot.isEmpty()) {
            model.addAttribute("message", "Invalid Token");
            return "redirect:/resetPassword";
        } else {
            pilotService.updatePassword(pilot.get(), password);

            model.addAttribute("message", "You have successfully changed your password.");
        }

        return "redirect:/login";
    }
}
