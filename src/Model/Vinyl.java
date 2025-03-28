package Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Vinyl {
  @SerializedName("title")
  @Expose
  private String title;

  @SerializedName("artist")
  @Expose
  private String artist;

  @SerializedName("year")
  @Expose
  private int year;

  @SerializedName("borrower")
  @Expose
  private String borrower;

  @SerializedName("reserver")
  @Expose
  private String reserver;

  @SerializedName("status")
  @Expose
  private String status;

  @SerializedName("markedForRemoval")
  @Expose
  private boolean markedForRemoval;

  // Default constructor for Gson
  public Vinyl() {
    this.state = new AvailableState(this);
  }

  public Vinyl(String title, String artist, int year) {
    this.title = title;
    this.artist = artist;
    this.year = year;
    this.state = new AvailableState(this);
    this.observers = new ArrayList<>();
    this.markedForRemoval = false;
  }

  // Add getters for Gson serialization
  public String getTitle() { return title; }
  public String getArtist() { return artist; }
  public int getYear() { return year; }
  public String getBorrower() { return borrower; }
  public String getReserver() { return reserver; }
  public boolean isMarkedForRemoval() { return markedForRemoval; }

  // Rest of the existing Vinyl class implementation...

  // Additional method for Gson serialization
  public String getStatus() {
    return state != null ? state.getStatus() : "Unknown";
  }
}