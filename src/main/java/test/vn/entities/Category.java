package test.vn.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "categories")
@NamedQuery(
    name = "Category.findAll",
    query = "SELECT c FROM Category c"
)
public class Category implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "categoryId")
    private int categoryid;

    @Column(
        name = "categoryname",
        columnDefinition = "NVARCHAR(255) NULL"
    )
    private String categoryname;

    @Column(
        name = "images",
        columnDefinition = "NVARCHAR(255) NULL"
    )
    private String images;

    @Column(name = "imagePublicId", length = 255)
    private String imagePublicId;

    @Column(name = "status")
    private int status;

    // 1 Category có nhiều Video
    @OneToMany(mappedBy = "categories")
    private List<Video> videos = new ArrayList<>();

    // 1 Category có nhiều Product
    @OneToMany(mappedBy = "category")
    private List<Product> products = new ArrayList<>();

    public Category() {
    }

    public Category(
            int categoryid,
            String categoryname,
            String images,
            int status) {

        this.categoryid = categoryid;
        this.categoryname = categoryname;
        this.images = images;
        this.status = status;
    }

    public int getCategoryid() {
        return categoryid;
    }

    public void setCategoryid(int categoryid) {
        this.categoryid = categoryid;
    }

    public String getCategoryname() {
        return categoryname;
    }

    public void setCategoryname(String categoryname) {
        this.categoryname = categoryname;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public String getImagePublicId() {
        return imagePublicId;
    }

    public void setImagePublicId(String imagePublicId) {
        this.imagePublicId = imagePublicId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<Video> getVideos() {
        return videos;
    }

    public void setVideos(List<Video> videos) {
        this.videos = videos;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public Video addVideo(Video video) {

        getVideos().add(video);

        video.setCategory(this);

        return video;
    }

    public Video removeVideo(Video video) {

        getVideos().remove(video);

        video.setCategory(null);

        return video;
    }
}
