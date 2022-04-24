package com.ph.schedule.bean;

/**
 * 课程表实体类
 *
 */
public class Schedule {

    /**
     * 课程id
     */
    private Long scheduleId = -1L;;

    /**
     * 课程名称
     */
    private String scheduleName;

    /**
     * 课程上课时间
     */
    private String scheduleTime;

    /**
     * 上课地址
     */
    private String address;

    /**
     * 任课教师
     */
    private String teacher;

    public Long getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(Long scheduleId) {
        this.scheduleId = scheduleId;
    }

    public String getScheduleName() {
        return scheduleName;
    }

    public void setScheduleName(String scheduleName) {
        this.scheduleName = scheduleName;
    }

    public String getScheduleTime() {
        return scheduleTime;
    }

    public void setScheduleTime(String scheduleTime) {
        this.scheduleTime = scheduleTime;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    @Override
    public String toString() {
        return "Schedule{" +
                "scheduleId=" + scheduleId +
                ", scheduleName='" + scheduleName + '\'' +
                ", scheduleTime='" + scheduleTime + '\'' +
                ", address='" + address + '\'' +
                ", teacher='" + teacher + '\'' +
                '}';
    }
}
