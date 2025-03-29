package com.solcare.solar.service;
import java.security.SecureRandom;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.solcare.solar.entity.User;
import com.solcare.solar.repositories.UserRepository;

@Service
public class OtpService {

     @Autowired
    private UserRepository userRepository;

    private static final int OTP_EXPIRATION_TIME = 5 * 60 * 1000;  // 5 minutes
    private static final int OTP_REQUEST_LIMIT = 5;  // Max OTP requests per 10 minutes
    private static final long OTP_REQUEST_TIME_LIMIT = 10 * 60 * 1000;  // 10 minutes

    // OTP generation using SecureRandom
    public String generateOTP() {
        SecureRandom secureRandom = new SecureRandom();
        int otp = 100000 + secureRandom.nextInt(900000);  // 6-digit OTP
        return String.valueOf(otp);
    }

    // Send OTP (Email or SMS)
    // public void sendOTP(String phoneNumberOrEmail, String otp) {
    //     // Send OTP via Email (as shown earlier)
    //     SimpleMailMessage message = new SimpleMailMessage();
    //     message.setTo(phoneNumberOrEmail);
    //     message.setSubject("Your OTP Code");
    //     message.setText("Your OTP code is: " + otp);
    //     emailSender.send(message);
    // }

    // Validate OTP (with expiration check)
    public boolean validateOTP(String enteredOTP, String storedOTP, long otpCreationTime) {
        // Validate OTP expiration time (e.g., 5 minutes)
        long expirationTime = 5 * 60 * 1000;  // 5 minutes in milliseconds
        long currentTime = System.currentTimeMillis();
        
        if (currentTime - otpCreationTime > expirationTime) {
            return false; // OTP expired
        }
        
        return enteredOTP.equals(storedOTP);
    }

      // Send OTP with rate limiting and expiration checks
      public String sendOTP(Long phoneNumberOrEmail) {
        // Get the user by phone number or email
        Optional<User> user = userRepository.findByMobileNumber(phoneNumberOrEmail);
        
        // Check if user exists
        if (!user.isPresent()) {
            return "User not found!";
        }

        // Check if the user has exceeded the OTP request limit
        if (user.get().getOtpRequestCount() >= OTP_REQUEST_LIMIT) {
            long lastRequestTime = user.get().getOtpRequestTime();
            if (System.currentTimeMillis() - lastRequestTime < OTP_REQUEST_TIME_LIMIT) {
                return "Too many OTP requests. Please try again later.";
            } else {
                // Reset OTP request count after the time window has passed
                user.get().setOtpRequestCount(0l);
            }
        }

          // Generate OTP
          String otp = generateOTP();
          
          user.get().setOtp(otp);
          user.get().setOtpRequestTime(System.currentTimeMillis());
          user.get().setOtpCreationTime(System.currentTimeMillis());
          user.get().setOtpRequestCount(user.get().getOtpRequestCount() + 1);
          userRepository.save(user.get());
          //sendOTP(phoneNumberOrEmail, otp);

          return "OTP sent successfully! "+ user.get().getOtp();
    }
     // Validate OTP
     public boolean validateOTP(String enteredOTP, Long phoneNumberOrEmail) {
        Optional<User> user = userRepository.findByMobileNumber(phoneNumberOrEmail);

        // OTP not found or expired
        if (user == null || System.currentTimeMillis() - user.get().getOtpCreationTime() > OTP_EXPIRATION_TIME) {
            return false;
        }

        return enteredOTP.equals(user.get().getOtp());
    }

}

