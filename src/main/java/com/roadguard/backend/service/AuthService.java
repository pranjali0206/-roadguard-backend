package com.roadguard.backend.service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.roadguard.backend.model.OtpVerification;
import com.roadguard.backend.model.User;
import com.roadguard.backend.repository.OtpRepository;
import com.roadguard.backend.repository.UserRepository;
import com.roadguard.backend.utils.JwtUtils;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OtpRepository otpRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    @Autowired
    private JwtUtils jwtUtils;

    public String registerUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            return "Email already registered";
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setVerified(false);

        String otp = generateOtp();

        OtpVerification otpVerification = new OtpVerification();
        otpVerification.setEmail(user.getEmail());
        otpVerification.setOtp(otp);
        otpVerification.setExpiryTime(LocalDateTime.now().plusMinutes(5));
        otpVerification.setUsed(false);
        otpRepository.save(otpVerification);

        try {
            emailService.sendOtpEmail(user.getEmail(), otp);
        } catch (Exception e) {
            System.out.println("Email sending failed: " + e.getMessage());
            return "Registration failed. Could not send OTP email.";
        }

        userRepository.save(user);

        return "User registered successfully. Please verify your email.";
    }

    public String verifyOtp(String email, String otp) {
        Optional<OtpVerification> otpRecord = otpRepository
            .findTopByEmailOrderByExpiryTimeDesc(email);

        if (otpRecord.isEmpty()) {
            return "OTP not found";
        }

        OtpVerification otpVerification = otpRecord.get();

        if (otpVerification.isUsed()) {
            return "OTP already used";
        }

        if (otpVerification.getExpiryTime().isBefore(LocalDateTime.now())) {
            return "OTP expired";
        }

        if (!otpVerification.getOtp().equals(otp)) {
            return "Invalid OTP";
        }

        otpVerification.setUsed(true);
        otpRepository.save(otpVerification);

        Optional<User> user = userRepository.findByEmail(email);
        user.get().setVerified(true);
        userRepository.save(user.get());

        return "Email verified successfully";
    }

    public String loginUser(String email, String password) {
        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isEmpty()) {
            return "User not found";
        }

        User user = userOptional.get();

        if (!user.isVerified()) {
            return "Please verify your email first";
        }

        if (!passwordEncoder.matches(password, user.getPassword())) {
            return "Invalid password";
        }

        return jwtUtils.generateToken(email, user.getRole().name());
    }

    private String generateOtp() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }
}