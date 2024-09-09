# Smart Contact Manager

## Overview

The **Smart Contact Manager** is a web-based application built with Java and Spring Boot, designed to help users securely manage their contacts. Users can register and log in using their credentials or through third-party services like Google and GitHub via OAuth2. Once authenticated, they can perform CRUD (Create, Read, Update, Delete) operations on their contacts, view their profiles, and track their activity.

The application focuses on simplicity and security, featuring email verification for new users and secure authentication methods. It also supports dark mode, providing an optimized user experience. The application uses MySQL as the backend database, and the frontend is built using HTML, Tailwind CSS, and Thymeleaf templates.

## Features

- **User Registration & Authentication**
  - Sign up, log in using credentials or via Google and GitHub
  - Email verification for new user registration
- **Contact Management**
  - Add, update, view, and delete contacts
  - Paginated contact list
- **Profile Management**
  - View and edit profile information
- **Dark Mode Support**
  - UI adapts to user preferences
- **User Activity Logging**
  - Track user actions within the application
- **Email Notifications**
  - Send verification and notification emails

## Technologies Used

- **Backend**: Java, Spring Boot, Spring Security, JPA, MySQL
- **Frontend**: HTML, Tailwind CSS, JavaScript, Thymeleaf
- **Database**: MySQL
- **Authentication**: OAuth2 (Google, GitHub)
- **Build Tool**: Maven
- **Others**: SMTP for email notifications, OAuth 2.0 for external login

## Installation

### Prerequisites

- Java 17 or later
- Maven
- MySQL
- IDE (e.g., IntelliJ IDEA, Eclipse)

### Steps

1. **Clone the Repository**:
   ```bash
   git clone https://github.com/your-username/smart-contact-manager.git
   cd smart-contact-manager
