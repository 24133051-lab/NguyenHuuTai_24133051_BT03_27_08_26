package test.vn.configs;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import test.vn.entities.Category;
import test.vn.entities.Video;

public class Test {

    public static void main(String[] args) {

        EntityManager em =
                JPAConfig.getEntityManager();

        EntityTransaction trans =
                em.getTransaction();

        // Tạo Category
        Category cate =
                new Category();

        cate.setCategoryname("Iphone");
        cate.setImages("abc.jpg");
        cate.setStatus(1);

        // Tạo Video
        Video video =
                new Video();

        video.setVideoId("v01");
        video.setTitle("test");
        video.setCategory(cate);

        try {

            trans.begin();

            em.persist(cate);
            em.persist(video);

            trans.commit();

            System.out.println(
                    "Them Category va Video thanh cong!"
            );

        } catch (Exception e) {

            if (trans.isActive()) {
                trans.rollback();
            }

            e.printStackTrace();

        } finally {

            em.close();
        }
    }
}