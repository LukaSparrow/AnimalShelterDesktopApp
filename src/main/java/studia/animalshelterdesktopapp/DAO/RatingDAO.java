package studia.animalshelterdesktopapp.DAO;

import org.hibernate.Session;
import studia.animalshelterdesktopapp.Rating;
import studia.animalshelterdesktopapp.controllers.HibernateUtil;

import java.util.List;

public class RatingDAO implements IDAO<Rating> {
    @Override
    public Rating get(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Rating.class, id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Rating> getAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Rating", Rating.class).list();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int update(Rating rating) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.update(rating);
            session.getTransaction().commit();
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public int delete(Rating rating) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.delete(rating);
            session.getTransaction().commit();
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public int insert(Rating rating) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.save(rating);
            session.getTransaction().commit();
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public List<Rating> getAllRatingsForShelter(Long shelterId){
        try(Session session = HibernateUtil.getSessionFactory().openSession()){
            session.beginTransaction();
            String hql = "from Rating where shelter.id = :shelterId";
            return session.createQuery(hql, Rating.class)
                    .setParameter("shelterId", shelterId)
                    .list();
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
