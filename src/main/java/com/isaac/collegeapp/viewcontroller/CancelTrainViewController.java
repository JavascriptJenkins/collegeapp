package com.isaac.collegeapp.viewcontroller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.isaac.collegeapp.h2model.CancelTrainVO;
import com.isaac.collegeapp.jparepo.CancelTrainRepo;
import com.isaac.collegeapp.model.StudentDAO;
import com.isaac.collegeapp.service.StudentService;
import com.isaac.collegeapp.util.ControllerHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

@RequestMapping("/")
@Controller
public class CancelTrainViewController {

    @Autowired
    StudentService studentService;

    ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    ControllerHelper controllerHelper;

    @Autowired
    HttpServletRequest httpServletRequest;

    @Autowired
    CancelTrainRepo cancelTrainRepo;


    //default home mapping
    @GetMapping
    String viewHomePage(Model model){


        List<CancelTrainVO> cancelTrainVOList = calculateProgressBars();

//        controllerHelper.checkForLoggedInStudent(model, httpServletRequest); // this will check to see if a student has already loggede in
        model.addAttribute("canceltrains", cancelTrainVOList);
        model.addAttribute("canceltrain", new CancelTrainVO());

        model.addAttribute("canceltrainNewCancel", new CancelTrainVO());



//        model.addAttribute("students", studentService.getAllStudentData());
        return "index.html";
    }


    @PostMapping("/vote")
    String vote(@ModelAttribute( "canceltrain" ) CancelTrainVO cancelTrainVO, Model model){

        if(cancelTrainVO.getIncomingVote() ==0){
            Optional<CancelTrainVO> existing = cancelTrainRepo.findById(cancelTrainVO.getId());

            existing.get().setDownvotes(existing.get().getDownvotes() - 1);
            cancelTrainRepo.save(existing.get());

        } else if(cancelTrainVO.getIncomingVote() ==1){
            Optional<CancelTrainVO> existing = cancelTrainRepo.findById(cancelTrainVO.getId());
            existing.get().setUpvotes(existing.get().getUpvotes() + 1);
            cancelTrainRepo.save(existing.get());

        }



        model.addAttribute("canceltrain", new CancelTrainVO());
        model.addAttribute("canceltrainNewCancel", new CancelTrainVO());

        model.addAttribute("canceltrains",calculateProgressBars());


        return "index.html";
    }


    @PostMapping("/newCancel")
    String newCancel(@ModelAttribute( "canceltrainNewCancel" ) CancelTrainVO cancelTrainVO, Model model){


        if(cancelTrainVO.getFname() != null
        && cancelTrainVO.getLname() != null
        && cancelTrainVO.getWhy() != null

        ){

            cancelTrainVO.setId(null);
            cancelTrainVO.setUpvotes(0);
            cancelTrainVO.setDownvotes(0);
            cancelTrainVO.setImageurl("");
            cancelTrainVO.setCancelstatus(0); // will be set to 1 to show in grid
            cancelTrainVO.setUpdatedtimestamp(java.time.LocalTime.now());
            cancelTrainVO.setCreatetimestamp(java.time.LocalTime.now());

            cancelTrainRepo.save(cancelTrainVO);

            model.addAttribute("successMessage", "Thank you for submitting a new cancel candidate!");

            model.addAttribute("canceltrain", new CancelTrainVO());
            model.addAttribute("canceltrainNewCancel", new CancelTrainVO());
            model.addAttribute("canceltrains",cancelTrainRepo.findAll());

        } else {
            model.addAttribute("errorMessage", "Please fill out all data!");

            model.addAttribute("canceltrain", new CancelTrainVO());
            model.addAttribute("canceltrainNewCancel", new CancelTrainVO());
            model.addAttribute("canceltrains",calculateProgressBars());

        }




        return "index.html";
    }


    @GetMapping("/viewStudents")
    String viewStudents(Model model){

        controllerHelper.checkForLoggedInStudent(model, httpServletRequest); // this will check to see if a student has already loggede in


        model.addAttribute("students", studentService.getAllStudentData());
        return "viewStudents.html";
    }

    @GetMapping("/editStudent")
    String editStudent(Model model){
        controllerHelper.checkForLoggedInStudent(model, httpServletRequest); // this will check to see if a student has already loggede in


        model.addAttribute("student", new StudentDAO());
        model.addAttribute("students",studentService.getAllStudentData());
        return "editStudent.html";
    }

    @PostMapping("/submitEditStudent")
    String submitEditStudent(@ModelAttribute( "student" ) StudentDAO studentDAO, Model model){

        controllerHelper.checkForLoggedInStudent(model, httpServletRequest); // this will check to see if a student has already loggede in


        System.out.println(studentDAO);


        // now the edit works, just need to submit the edit to the database

        model.addAttribute("student", new StudentDAO());

        model.addAttribute("students",studentService.getAllStudentData());


        return "editStudent.html";
    }

    @GetMapping("/newStudent")
    String newStudent(Model model){
        controllerHelper.checkForLoggedInStudent(model, httpServletRequest); // this will check to see if a student has already loggede in


        model.addAttribute("student", new StudentDAO());
        model.addAttribute("students",studentService.getAllStudentData());
        return "newStudent.html";
    }

    @PostMapping ("/createStudent")
    String createStudent(@ModelAttribute( "student" ) StudentDAO studentDAO, Model model){

        controllerHelper.checkForLoggedInStudent(model, httpServletRequest); // this will check to see if a student has already loggede in


        // Validation on student name
        if(studentDAO.getStudentName().length() > 60){
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


    List<CancelTrainVO> calculateProgressBars(){

        List<CancelTrainVO> cancelTrainVOList = cancelTrainRepo.findByOrderByUpvotesDesc();

        // set progress bar value
        for(CancelTrainVO cancelTrainVO : cancelTrainVOList){

            int progress = 0;
            if(cancelTrainVO.getDownvotes() >= cancelTrainVO.getUpvotes()){
                progress = 0;
            } else {
                progress = cancelTrainVO.getUpvotes() - Math.abs(cancelTrainVO.getDownvotes());
            }
            cancelTrainVO.setProgressbar(progress);
        }
        return cancelTrainVOList;

    }


}
