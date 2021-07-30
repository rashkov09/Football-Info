package softuni.exam.domain.dtos.jsonDtos;

import com.google.gson.annotations.Expose;
import softuni.exam.domain.entities.Picture;

public class TeamDto {
    @Expose
    private String name;
    @Expose
    private PictureDto picture;

    public TeamDto() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PictureDto getPicture() {
        return picture;
    }

    public void setPicture(PictureDto picture) {
        this.picture = picture;
    }
}
