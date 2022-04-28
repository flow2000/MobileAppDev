package com.ph.schedule.bean;

/**
 * 课程表实体类
 *
 */
public class Schedule {

    /**
     * 课程id
     */
    private Long scheduleId = -1L;

    /**
     * 课程名称
     */
    private String scheduleName;

    /**
     * 星期几
     */
    private Integer scheduleWeek;

    /**
     * 课程上课时间
     */
    private String startTime;

    /**
     * 课程下课时间
     */
    private String endTime;

    /**
     * 课程开始周数
     */
    private Integer startWeek;

    /**
     * 课程结束周数
     */
    private Integer endWeek;

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

    public Integer getScheduleWeek() {
        return scheduleWeek;
    }

    public void setScheduleWeek(Integer scheduleWeek) {
        this.scheduleWeek = scheduleWeek;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Integer getStartWeek() {
        return startWeek;
    }

    public void setStartWeek(Integer startWeek) {
        this.startWeek = startWeek;
    }

    public Integer getEndWeek() {
        return endWeek;
    }

    public void setEndWeek(Integer endWeek) {
        this.endWeek = endWeek;
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
                ", scheduleWeek='" + scheduleWeek + '\'' +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", startWeek=" + startWeek +
                ", endWeek=" + endWeek +
                ", address='" + address + '\'' +
                ", teacher='" + teacher + '\'' +
                '}';
    }
}
