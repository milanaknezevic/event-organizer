package com.example.eventorganizer.model.entities;


import java.util.Objects;

public class Image {
    private Integer id;
    private String image_url;
    private Integer event_id;


    public Image() {
        super();
    }

    public Image(String image_url) {
        this.image_url = image_url;

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public Integer getEvent_id() {
        return event_id;
    }

    public void setEvent_id(Integer event_id) {
        this.event_id = event_id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Image image = (Image) o;
        return Objects.equals(id, image.id) && Objects.equals(image_url, image.image_url) && Objects.equals(event_id, image.event_id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, image_url, event_id);
    }

    @Override
    public String toString() {
        return "Image{" +
                "id=" + id +
                ", image_url='" + image_url + '\'' +
                ", event_id=" + event_id +
                '}';
    }
}

