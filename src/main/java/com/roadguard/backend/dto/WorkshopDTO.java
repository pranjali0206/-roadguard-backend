package com.roadguard.backend.dto;

public class WorkshopDTO {
    private Long id;
    private String name;
    private String ownerName;
    private String phone;
    private String address;
    private String city;
    private String state;
    private Double latitude;
    private Double longitude;
    private String services;
    private Double rating;
    private Integer totalRatings;
    private Boolean isOpen;
    private String imageUrl;
    private Double distanceKm; // calculated at runtime — NOT stored in DB

    // Constructor
    public WorkshopDTO(Long id, String name, String ownerName, String phone,
                       String address, String city, String state,
                       Double latitude, Double longitude, String services,
                       Double rating, Integer totalRatings, Boolean isOpen,
                       String imageUrl, Double distanceKm) {
        this.id = id;
        this.name = name;
        this.ownerName = ownerName;
        this.phone = phone;
        this.address = address;
        this.city = city;
        this.state = state;
        this.latitude = latitude;
        this.longitude = longitude;
        this.services = services;
        this.rating = rating;
        this.totalRatings = totalRatings;
        this.isOpen = isOpen;
        this.imageUrl = imageUrl;
        this.distanceKm = distanceKm;
    }

    // Getters
    public Long getId() { return id; }
    public String getName() { return name; }
    public String getOwnerName() { return ownerName; }
    public String getPhone() { return phone; }
    public String getAddress() { return address; }
    public String getCity() { return city; }
    public String getState() { return state; }
    public Double getLatitude() { return latitude; }
    public Double getLongitude() { return longitude; }
    public String getServices() { return services; }
    public Double getRating() { return rating; }
    public Integer getTotalRatings() { return totalRatings; }
    public Boolean getIsOpen() { return isOpen; }
    public String getImageUrl() { return imageUrl; }
    public Double getDistanceKm() { return distanceKm; }
}