package com.example.local_project.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.local_project.Entity.Courses;
import com.example.local_project.Entity.Users;
import com.example.local_project.Repository.CourseRepo;
import com.example.local_project.dto.CourseDto;
import com.example.local_project.dto.UserDto;

@Service
@Transactional(readOnly = true) // Good practice to make service read-only by default
public class CourseService {

    @Autowired
    private CourseRepo courseRepo;

    /**
     * Helper method to convert a Courses entity to a CourseDto.
     */
    private CourseDto convertToDto(Courses course) {
        CourseDto courseDto = new CourseDto();
        courseDto.setCourseId(course.getCourseId());
        courseDto.setCourseName(course.getCourseName());
        courseDto.setCourseCode(course.getCourseCode());

        // Create a simplified DTO for the instructor to avoid the proxy issue
        UserDto instructorDto = new UserDto();
        if (course.getInstructor() != null) {
            instructorDto.setId(course.getInstructor().getId());
            instructorDto.setName(course.getInstructor().getName());
            instructorDto.setEmail(course.getInstructor().getEmail());
            courseDto.setInstructor(instructorDto);
        }
        return courseDto;
    }

    public Page<CourseDto> getAllCourses(Pageable pageable) {
        return courseRepo.findAll(pageable).map(this::convertToDto);
    }
    
    public CourseDto getCourseById(Long id) {
        return courseRepo.findById(id)
                .map(this::convertToDto) // Convert the entity to a DTO
                .orElseThrow(() -> new RuntimeException("Course not found with id: " + id));
    }

    @Transactional // Override read-only for methods that write to the DB
    public CourseDto saveCourse(Courses course, Users instructor) {
        course.setInstructor(instructor);
        Courses savedCourse = courseRepo.save(course);
        return convertToDto(savedCourse); // Return DTO
    }

    @Transactional
    public String deleteCourse(Long id) {
        if (!courseRepo.existsById(id)) {
            return "Course Not Found";
        }
        try {
            courseRepo.deleteById(id);
            return "Course Deleted Successfully";
        } catch (DataIntegrityViolationException e) {
            return "Error: Cannot delete this course because it has active certificates linked to it.";
        }
    }

    @Transactional
    public CourseDto updateCourse(Long id, Courses co) {
        Courses existingCourse = courseRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found with id: " + id));
        existingCourse.setCourseName(co.getCourseName());
        existingCourse.setCourseCode(co.getCourseCode());
        Courses updatedCourse = courseRepo.save(existingCourse);
        return convertToDto(updatedCourse); // Return DTO
    }

    public Page<CourseDto> findCoursesByInstructor(String email, Pageable pageable) {
        Page<Courses> coursePage = courseRepo.findByInstructorEmail(email, pageable);
        return coursePage.map(this::convertToDto);
    }
    
    public boolean isOwner(Long courseId, String email) {
        Courses course = courseRepo.findById(courseId).orElse(null);
        if (course == null || course.getInstructor() == null) {
            return false;
        }
        return course.getInstructor().getEmail().equalsIgnoreCase(email);
    }
}

