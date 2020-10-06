package com.in28minutes.springboot.controller;

import com.in28minutes.springboot.model.Course;
import com.in28minutes.springboot.service.StudentService;
import org.jboss.resteasy.annotations.jaxrs.PathParam;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.Collections;
import java.util.List;

@Path("students")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class StudentController {

    @Inject
    private StudentService studentService;

    @GET
    @Path("/{studentId}/courses")
    public List<Course> retrieveCoursesForStudent(@PathParam String studentId) {
        return studentService.retrieveCourses(studentId);
    }

    @GET
    @Path("/{studentId}/courses/{courseId}")
    public Course retrieveDetailsForCourse(@PathParam String studentId,
                                           @PathParam String courseId) {
        return studentService.retrieveCourse(studentId, courseId);
    }

    @POST
    @Path("/{studentId}/courses")
    public Response registerStudentForCourse(
            @PathParam String studentId, Course newCourse, @Context UriInfo uriInfo) throws Exception {

        Course course = studentService.addCourse(studentId, newCourse);

        if (course == null)
            return Response.noContent().build();

        return Response.created(uriInfo.getRequestUriBuilder().path(course.getId()).build().toURL().toURI()).build();
    }

}
