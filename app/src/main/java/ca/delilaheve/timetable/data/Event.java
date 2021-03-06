package ca.delilaheve.timetable.data;

import java.io.Serializable;

public class Event implements Serializable {

    private int id;
    private int classID;

    private String teacher;

    private String className;
    private String courseCode;

    private String day;
    private String startTime;
    private String endTime;

    private String room;
    private String campus;

    public Event(int id, int classID, String teacher, String day, String startTime, String endTime, String room, String campus, String className, String courseCode) {
        this.id = id;
        this.classID = classID;
        this.teacher = teacher;
        this.day = day;
        this.startTime = startTime;
        this.endTime = endTime;
        this.room = room;
        this.campus = campus;

        this.className = className;
        this.courseCode = courseCode;
    }

    public int getId() {
        return id;
    }

    public int getClassID() {
        return classID;
    }

    public String getTeacher() {
        return teacher;
    }

    public String getDay() {
        return day;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getRoom() {
        return room;
    }

    public String getCampus() {
        return campus;
    }

    public String getClassName() {
        return className;
    }

    public String getCourseCode() {
        return courseCode;
    }
}
