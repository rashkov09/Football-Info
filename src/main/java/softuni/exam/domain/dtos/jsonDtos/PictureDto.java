package softuni.exam.domain.dtos.jsonDtos;

import com.google.gson.annotations.Expose;

public class PictureDto {
    @Expose
    private String url;

    public PictureDto() {
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
