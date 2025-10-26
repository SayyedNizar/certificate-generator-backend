package com.example.local_project.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.local_project.Entity.Courses;
import com.example.local_project.Entity.Users;
import com.example.local_project.Repository.UsersRepo;
import com.example.local_project.Services.CourseService;
import com.example.local_project.dto.CourseDto;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    @Autowired
    private CourseService courseService;
    @Autowired
    private UsersRepo usersRepo;

    
    /**
     * --- THIS IS THE FIX ---
     * We remove the @PreAuthorize annotation. This endpoint is now public,
     * allowing anyone (including logged-out visitors) to see the course list.
     */
    @GetMapping
    public Page<CourseDto> fetchCourses(Pageable pageable) {
        return courseService.getAllCourses(pageable);
    }

    /**
     * Fetches a paginated list of courses for the currently logged-in instructor.
     */
    @GetMapping("/my-courses")
    @PreAuthorize("hasAuthority('INSTRUCTOR')")
    public Page<CourseDto> getMyCourses(Authentication authentication, Pageable pageable) {
        String email = authentication.getName();
        return courseService.findCoursesByInstructor(email, pageable);
    }

    /**
     * Fetches a single course by its ID.
     * Now returns a CourseDto to prevent lazy loading errors.
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or @courseService.isOwner(#id, principal.username)")
    public CourseDto getCourseById(@PathVariable Long id) {
        return courseService.getCourseById(id);
    }

    /**
     * Creates a new course and assigns the logged-in user as the instructor.
     * Returns the newly created CourseDto.
     */
    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('INSTRUCTOR')")
    public CourseDto postCourse(@RequestBody Courses obj, Authentication authentication) {
        Users instructor = usersRepo.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Authenticated user not found"));
        return courseService.saveCourse(obj, instructor);
    }

    /**
     * Updates an existing course.
     * Returns the updated CourseDto.
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or @courseService.isOwner(#id, principal.username)")
    public CourseDto putCourse(@PathVariable Long id, @RequestBody Courses co) {
        return courseService.updateCourse(id, co);
    }

    /**
     * Deletes a course.
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or @courseService.isOwner(#id, principal.username)")
    public String delete_Course(@PathVariable Long id) {
        return courseService.deleteCourse(id);
    }
}

