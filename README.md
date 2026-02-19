📚 Learning Management System (LMS)

A full-stack Learning Management System (LMS) built using Spring Boot, Thymeleaf, MySQL/PostgreSQL, and Firebase Storage.
This platform supports Admin, Instructor, and Student roles with secure authentication, course management, lesson uploads, quizzes, and student progress tracking.

This project is designed as a complete LMS solution where:

Admins manage users and approve courses

Instructors create courses, upload lessons, and add quizzes

Students enroll in courses, learn from lessons, take quizzes, and track progress

🧩 Project Overview

The LMS is a role-based web application that allows:

Admins to manage users and approve courses

Instructors to create and manage courses, upload lesson content (video/PDF/text/files), and create quizzes

Students to enroll in courses, view lessons, complete lessons, take quizzes, and track learning progress

The system includes:

Secure login and registration

Role-based access control using Spring Security

File uploads stored in Firebase Storage

Relational database using:

MySQL for local development

PostgreSQL for production/deployment

Progress tracking and quiz evaluation

Clean UI built with Thymeleaf + Tailwind CSS

Unit and integration tests using JUnit 5 and Mockito

🚀 Features

🔐 Authentication & Authorization

Login and registration system

Roles: ADMIN, INSTRUCTOR, STUDENT

Spring Security with BCrypt password encryption

Role-based access to pages and actions

👨‍💼 Admin Module

View all courses

Approve or reject instructor courses

Delete courses

View course lessons and quiz previews

Manage users:

View all users

Change user roles (Student / Instructor / Admin)

Delete users

👨‍🏫 Instructor Module

Create new courses

Upload lessons (Video, PDF, Text, or other files)

Lesson files are stored in Firebase Storage

Add quiz questions to courses

View enrolled students for each course

Preview course lessons and quizzes

See approval status of courses (Pending / Approved)

👨‍🎓 Student Module

View available approved courses

Enroll in courses (no duplicate enrollments allowed)

View enrolled courses

Track progress:

Not Started

In Progress

Completed

Watch lessons and mark them as completed

Take quizzes

Automatic progress update:

Lessons contribute up to 90%

Quiz completion completes the course (100%)

See progress bar and course status

📊 Additional Features

Progress tracking with percentage and status

Quiz evaluation with pass/fail (60% required to pass)

Exception handling with custom error pages

Responsive UI using Tailwind CSS

Unit tests for Controllers, Services, and Repositories

Ready for deployment with PostgreSQL

🛠️ Tech Stack

Backend

Spring Boot 3

Spring Security

Spring Data JPA

Hibernate

MySQL (Local Development)

PostgreSQL (Deployment)

Firebase Storage (for media files)

Frontend

Thymeleaf

Tailwind CSS

HTML

Testing

JUnit 5

Mockito

Spring Boot Test (MockMvc, DataJpaTest)

Build Tool

Maven

Deployment

Backend: Render

Database: MySQL/PostgreSQL 

Storage: Firebase Storage

📁 Project Structure
LMS
├── src
│   ├── main
│   │   ├── java/com/LMS
│   │   │   ├── config        
│   │   │   ├── controller    
│   │   │   ├── entity        
│   │   │   ├── exception    
│   │   │   ├── repository    
│   │   │   └── service       
│   │   └── resources
│   │       ├── templates     
│   │       ├── static
│   │       └── application.properties
│   └── test
│       └── java/com/LMS      
├── Dockerfile
├── mvnw / mvnw.cmd
└── README.md

⚙️ Database Configuration

✅ Local Development (MySQL)

In application.properties:

spring.datasource.url=jdbc:mysql://localhost:3306/lms_db
spring.datasource.username=root
spring.datasource.password=your_password

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

🚀 Production / Deployment (PostgreSQL)

spring.datasource.url=jdbc:postgresql://<host>:<port>/<db_name>
spring.datasource.username=<db_username>
spring.datasource.password=<db_password>

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect

☁️ Firebase Storage Setup

This project uses Firebase Storage to store lesson files (videos, PDFs, etc).

