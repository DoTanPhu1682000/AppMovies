package com.phudt7.movie.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Comparator;

@Entity(tableName = "movies")
public class Result implements Parcelable, Comparable<Result> {
    @SerializedName("id")
    @Expose
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public int id;
    @SerializedName("overview")
    @Expose
    @ColumnInfo(name = "overview")
    public String overview;
    @SerializedName("poster_path")
    @Expose
    @ColumnInfo(name = "posterPath")
    public String posterPath;
    @SerializedName("release_date")
    @Expose
    @ColumnInfo(name = "releaseDate")
    public String releaseDate;
    @SerializedName("title")
    @Expose
    @ColumnInfo(name = "title")
    public String title;
    @SerializedName("vote_average")
    @Expose
    @ColumnInfo(name = "voteAverage")
    public Double voteAverage;
    @SerializedName("vote_count")
    @Expose
    @ColumnInfo(name = "voteCount")
    public int voteCount;
    @SerializedName("original_title")
    @Expose
    @ColumnInfo(name = "originalTitle")
    public String originalTitle;

    public Result() {

    }

    public Result(int id, String overview, String posterPath, String releaseDate, String title, Double voteAverage, int voteCount) {
        this.id = id;
        this.overview = overview;
        this.posterPath = posterPath;
        this.releaseDate = releaseDate;
        this.originalTitle = title;
        this.voteAverage = voteAverage;
        this.voteCount = voteCount;
    }

    protected Result(Parcel in) {
        id = in.readInt();
        overview = in.readString();
        posterPath = in.readString();
        releaseDate = in.readString();
        title = in.readString();
        if (in.readByte() == 0) {
            voteAverage = null;
        } else {
            voteAverage = in.readDouble();
        }
        voteCount = in.readInt();
    }

    public static final Creator<Result> CREATOR = new Creator<Result>() {
        @Override
        public Result createFromParcel(Parcel in) {
            return new Result(in);
        }

        @Override
        public Result[] newArray(int size) {
            return new Result[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(Double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public static Comparator<Result> getReleaseDateSettings() {
        return releaseDateSettings;
    }

    public static void setReleaseDateSettings(Comparator<Result> releaseDateSettings) {
        Result.releaseDateSettings = releaseDateSettings;
    }

    public static Comparator<Result> getRatingSettings() {
        return ratingSettings;
    }

    public static void setRatingSettings(Comparator<Result> ratingSettings) {
        Result.ratingSettings = ratingSettings;
    }

    @Override
    public String toString() {
        return "Result{" +
                "id=" + id +
                ", overview='" + overview + '\'' +
                ", posterPath='" + posterPath + '\'' +
                ", releaseDate='" + releaseDate + '\'' +
                ", title='" + title + '\'' +
                ", voteAverage=" + voteAverage +
                ", voteCount=" + voteCount +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(overview);
        dest.writeString(posterPath);
        dest.writeString(releaseDate);
        dest.writeString(title);
        if (voteAverage == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(voteAverage);
        }
        dest.writeInt(voteCount);
    }


    @Override
    public int compareTo(Result o) {
        return this.id - o.getId();
    }

    public static Comparator<Result> releaseDateSettings = new Comparator<Result>() {
        @Override
        public int compare(Result o1, Result o2) {
            return o2.getReleaseDate().compareTo(o1.getReleaseDate());
        }
    };

    public static Comparator<Result> ratingSettings = new Comparator<Result>() {
        @Override
        public int compare(Result o1, Result o2) {
            return o2.getVoteAverage().compareTo(o1.getVoteAverage());
        }
    };
}
