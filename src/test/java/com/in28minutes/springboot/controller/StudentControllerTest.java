package com.in28minutes.springboot.controller;

import com.in28minutes.springboot.model.Course;
import com.in28minutes.springboot.service.StudentService;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import java.util.Arrays;

import static javax.ws.rs.core.Response.Status.CREATED;
import static org.hamcrest.Matchers.equalTo;

@QuarkusTest
@TestHTTPEndpoint(StudentController.class)
public class StudentControllerTest {


    @InjectMock
    private StudentService studentService;

    Course mockCourse = new Course("Course1", "Spring", "10 Steps",
            Arrays.asList("Learn Maven", "Import Project", "First Example",
                    "Second Example"));

    String exampleCourseJson = "{\"name\":\"Spring\",\"description\":\"10 Steps\",\"steps\":[\"Learn Maven\",\"Import Project\",\"First Example\",\"Second Example\"]}";

    @Test
    public void retrieveDetailsForCourse() throws Exception {

        Mockito.when(
                studentService.retrieveCourse(Mockito.anyString(),
                        Mockito.anyString())).thenReturn(mockCourse);

        Response result = RestAssured.get(
                "Student1/courses/Course1");

        System.out.println(result.getBody().peek().prettyPrint());

        result.then().body("id", equalTo("Course1"))
                .body("name", equalTo("Spring"))
                .body("description", equalTo("10 Steps"));
    }

    @Test
    public void createStudentCourse() throws Exception {
        Course mockCourse = new Course("1", "Smallest Number", "1",
                Arrays.asList("1", "2", "3", "4"));

        // studentService.addCourse to respond back with mockCourse
        Mockito.when(
                studentService.addCourse(Mockito.anyString(),
                        Mockito.any(Course.class))).thenReturn(mockCourse);

        Response response = RestAssured.with().contentType(MediaType.APPLICATION_JSON).body(exampleCourseJson).post(
                "Student1/courses");
        response.then().statusCode(CREATED.getStatusCode())
                .header(HttpHeaders.LOCATION, equalTo("http://localhost:8081/students/Student1/courses/1"));

    }

}
