package ca.delilaheve.timetable.data;

public class Course {

    private int id;
    private int listID;
    private String courseName;
    private String courseCode;

    public Course(int id, int listID, String courseName, String courseCode) {
        this.id = id;
        this.listID = listID;
        this.courseName = courseName;
        this.courseCode = courseCode;
    }

    public int getId() {
        return id;
    }

    public int getListID() {
        return listID;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getCourseCode() {
        return courseCode;
    }
}
