package com.example.eventorganizer.model.entities;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class Event implements Serializable {
    private Integer id;
    private String name;
    private String description;
    private String location;
    private String time;
    private Integer category_id;
    private List<Image> images;

    public Event()
    {
        super();
    }
    public Event(String name, String description,String time, String location,Integer category_id)
    {//sta sa slikama?
       this.name=name;
       this.description=description;
       this.time=time;
       this.location=location;
       this.category_id=category_id;
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

    public Integer getCategory_id() {
        return category_id;
    }

    public void setCategory_id(Integer category_id) {
        this.category_id = category_id;
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
        return Objects.equals(id, event.id) && Objects.equals(name, event.name) && Objects.equals(description, event.description) && Objects.equals(location, event.location) && Objects.equals(time, event.time) && Objects.equals(category_id, event.category_id) && Objects.equals(images, event.images);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, location, time, category_id, images);
    }

    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", location='" + location + '\'' +
                ", time='" + time + '\'' +
                ", category_id=" + category_id +
                ", images=" + images +
                '}';
    }
}
