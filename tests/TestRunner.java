import com.vibhor.campusgrade.exception.ValidationException;
import com.vibhor.campusgrade.model.Assessment;
import com.vibhor.campusgrade.model.Course;
import com.vibhor.campusgrade.model.Student;
import com.vibhor.campusgrade.repository.StudentRepository;
import com.vibhor.campusgrade.service.AcademicService;
import com.vibhor.campusgrade.service.AnalyticsService;
import com.vibhor.campusgrade.service.PredictionService;
import com.vibhor.campusgrade.repository.FileDataStore;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class TestRunner {
    public static void main(String[] args) throws Exception {
        testWeightedPercentage();
        testGpa();
        testValidation();
        testDuplicateStudent();
        testTargetPrediction();
        testFilePersistence();
        System.out.println("ALL TESTS PASSED");
    }

    private static void testWeightedPercentage() throws Exception {
        AcademicService service = new AcademicService(new StudentRepository());
        service.addStudent(new Student("S1", "Test User", "test@example.com", "CSE"));
        service.addCourse(new Course("JAVA101", "Programming in Java", 4));
        service.addAssessment(new Assessment("S1", "JAVA101", "Quiz", 18, 20, 20));
        service.addAssessment(new Assessment("S1", "JAVA101", "Endterm", 80, 100, 80));
        double expected = 82.0;
        assertNear(expected, service.coursePercentage("S1", "JAVA101"), 0.001, "weighted percentage");
    }

    private static void testGpa() throws Exception {
        AcademicService service = new AcademicService(new StudentRepository());
        service.addStudent(new Student("S1", "Test User", "test@example.com", "CSE"));
        service.addCourse(new Course("JAVA101", "Programming in Java", 4));
        service.addAssessment(new Assessment("S1", "JAVA101", "Final", 90, 100, 100));
        double gpa = new AnalyticsService(service).calculateGpa("S1");
        assertNear(10.0, gpa, 0.001, "GPA");
    }

    private static void testValidation() throws Exception {
        boolean thrown = false;
        try {
            new Assessment("S1", "JAVA101", "Bad", 101, 100, 50);
        } catch (ValidationException e) {
            thrown = true;
        }
        if (!thrown) throw new AssertionError("Invalid score was accepted");
    }

    private static void testDuplicateStudent() throws Exception {
        StudentRepository repo = new StudentRepository();
        repo.add(new Student("S1", "One", "one@example.com", "CSE"));
        boolean thrown = false;
        try {
            repo.add(new Student("S1", "Two", "two@example.com", "CSE"));
        } catch (ValidationException e) {
            thrown = true;
        }
        if (!thrown) throw new AssertionError("Duplicate ID was accepted");
    }

    private static void testTargetPrediction() throws Exception {
        AcademicService service = new AcademicService(new StudentRepository());
        service.addStudent(new Student("S1", "Test User", "test@example.com", "CSE"));
        service.addCourse(new Course("JAVA101", "Programming in Java", 4));
        service.addAssessment(new Assessment("S1", "JAVA101", "Midterm", 40, 50, 40));
        double required = new PredictionService(service).requiredRemainingPercentage("S1", "JAVA101", 80);
        assertNear(80.0, required, 0.001, "target prediction");
    }

    private static void testFilePersistence() throws Exception {
        Path tempDir = Files.createTempDirectory("campusgrade-test-");
        FileDataStore store = new FileDataStore(tempDir);
        Student student = new Student("S1", "Persist User", "persist@example.com", "CSE");
        Course course = new Course("JAVA101", "Programming in Java", 4);
        Assessment assessment = new Assessment("S1", "JAVA101", "Final", 90, 100, 100);
        store.save(List.of(student), List.of(course), List.of(assessment));
        if (store.loadStudents().size() != 1 || store.loadCourses().size() != 1 || store.loadAssessments().size() != 1) {
            throw new AssertionError("File persistence failed");
        }
        Files.walk(tempDir).sorted(java.util.Comparator.reverseOrder()).forEach(path -> {
            try { Files.deleteIfExists(path); } catch (Exception ignored) {}
        });
    }

    private static void assertNear(double expected, double actual, double delta, String label) {
        if (Math.abs(expected - actual) > delta) {
            throw new AssertionError(label + ": expected " + expected + ", got " + actual);
        }
    }
}
