package com.isaac.collegeapp.viewcontroller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.isaac.collegeapp.model.StudentDAO;
import com.isaac.collegeapp.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/studentView")
@Controller
public class StudentViewController {

    @Autowired
    StudentService studentService;

    ObjectMapper objectMapper = new ObjectMapper();



    @GetMapping("/viewStudents")
    String viewStudents(Model model){

        model.addAttribute("students", studentService.getAllStudentData());
        return "viewStudents.html";
    }

    @GetMapping("/newStudent")
    String newStudent(Model model){
        model.addAttribute("student", new StudentDAO());
        model.addAttribute("students",studentService.getAllStudentData());
        return "newStudent.html";
    }

    @PostMapping ("/createStudent")
    String createStudent(@ModelAttribute( "student" ) StudentDAO studentDAO, Model model){

        // Validation on student name
        if(studentDAO.getStudent_name().length() > 60){
            model.addAttribute("errorMessage","Student Name must be shorter than 60 characters");
        } else {
            // This code block will execute if there are no errors in the data that was inputed by the client

            // step 1) create the new student and attach the success message
            String result = studentService.createStudent(studentDAO);
            model.addAttribute("successMessage",result);

            // step 2) fetch the list of students from the database and bind the list onto the page
            model.addAttribute("students",studentService.getAllStudentData());

        }

        return "newStudent.html";
    }


}
