package com.example.eventorganizer.model.entities;

import com.example.eventorganizer.enums.Category;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class Event implements Serializable {
    private Integer id;
    private String name;
    private String description;
    private String location;
    private String time;
    private Category category;
    private List<Image> images;

    public Event()
    {
        super();
    }
    public Event(String name, String description,String time, String location,Category category)
    {//sta sa slikama?
       this.name=name;
       this.description=description;
       this.time=time;
       this.location=location;
       this.category=category;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return Objects.equals(id, event.id) && Objects.equals(name, event.name) && Objects.equals(description, event.description) && Objects.equals(location, event.location) && Objects.equals(time, event.time) && Objects.equals(category, event.category) && Objects.equals(images, event.images);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, location, time, category, images);
    }

    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", location='" + location + '\'' +
                ", time='" + time + '\'' +
                ", category=" + category +
                ", images=" + images +
                '}';
    }
}
