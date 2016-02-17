package ca.delilaheve.timetable.data;

public class Event {

    private int id;
    private int classID;
    private int teacherID;

    private String day;
    private String startTime;
    private String endTime;

    private String room;
    private String campus;

    public Event(int id, int classID, int teacherID, String day, String startTime, String endTime, String room, String campus) {
        this.id = id;
        this.classID = classID;
        this.teacherID = teacherID;
        this.day = day;
        this.startTime = startTime;
        this.endTime = endTime;
        this.room = room;
        this.campus = campus;
    }

    public int getId() {
        return id;
    }

    public int getClassID() {
        return classID;
    }

    public int getTeacherID() {
        return teacherID;
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
}
