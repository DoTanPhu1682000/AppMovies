package com.phudt7.movie.model;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.phudt7.movie.AlarmBroadcastReminder;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity(tableName = "reminder")
public class Reminder {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int idMovies;
    private String linkMovie;
    private String title;
    private String titleNameMovies;
    private String titleInfomation;
    private long time;

    public Reminder(int id, int idMovies, String linkMovie, String title, String titleNameMovies, String titleInfomation, long time) {
        this.id = id;
        this.idMovies = idMovies;
        this.linkMovie = linkMovie;
        this.title = title;
        this.titleNameMovies = titleNameMovies;
        this.titleInfomation = titleInfomation;
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdMovies() {
        return idMovies;
    }

    public void setIdMovies(int idMovies) {
        this.idMovies = idMovies;
    }

    public String getLinkMovie() {
        return linkMovie;
    }

    public void setLinkMovie(String linkMovie) {
        this.linkMovie = linkMovie;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitleNameMovies() {
        return titleNameMovies;
    }

    public void setTitleNameMovies(String titleNameMovies) {
        this.titleNameMovies = titleNameMovies;
    }

    public String getTitleInfomation() {
        return titleInfomation;
    }

    public void setTitleInfomation(String titleInfomation) {
        this.titleInfomation = titleInfomation;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "Reminder{" +
                "id=" + id +
                ", idMovies=" + idMovies +
                ", linkMovie='" + linkMovie + '\'' +
                ", title='" + title + '\'' +
                ", titleNameMovies='" + titleNameMovies + '\'' +
                ", titleInfomation='" + titleInfomation + '\'' +
                ", time=" + time +
                '}';
    }

    public String convertTime(){
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        Date date = new Date(time);
        return String.format(dateFormat.format(date));
    }

    public void setSchedule(Context context){
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmBroadcastReminder.class);
        intent.putExtra("TITLE1", titleNameMovies);
        intent.putExtra("TITLE2", titleInfomation);
        PendingIntent alarmPendingIntent = PendingIntent.getBroadcast(context, (int) time,intent,0);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP,time,alarmPendingIntent);
    }

    public void cancelSchedule(Context context){
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmBroadcastReminder.class);
        PendingIntent alarmPendingIntent = PendingIntent.getBroadcast(context, (int) time,intent,0);
        alarmManager.cancel(alarmPendingIntent);
    }
}
