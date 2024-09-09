package com.self.scm.forms;

import org.springframework.web.multipart.MultipartFile;

import com.self.scm.validators.ValidFile;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ContactForm {

    @NotBlank(message="Please enter the name")
    private String name;
    @Email(message = "Invalid email")
    @NotBlank(message = "Enter the email")
    private String email;
    @NotBlank(message="Please enter the phone number")
    @Size(min=10, max=10, message = "Invalid phone number")
    private String phoneNumber;
    private String address;
    private String description;
    private boolean favorite;
    private String instaLink;
    private String linkedInLink;

    @ValidFile()
    private MultipartFile contactImage;
    private String picture;
}
