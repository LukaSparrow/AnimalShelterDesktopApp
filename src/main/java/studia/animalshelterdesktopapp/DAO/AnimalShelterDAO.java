package studia.animalshelterdesktopapp.DAO;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.query.Query;
import studia.animalshelterdesktopapp.Animal;
import studia.animalshelterdesktopapp.AnimalShelter;
import studia.animalshelterdesktopapp.Rating;
import studia.animalshelterdesktopapp.controllers.HibernateUtil;

import java.util.Collections;
import java.util.List;

public class AnimalShelterDAO implements IDAO<AnimalShelter> {
    @Override
    public AnimalShelter get(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(AnimalShelter.class, id);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<AnimalShelter> findByName(String filter){
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "from AnimalShelter where shelterName like :filter";
            Query<AnimalShelter> query = session.createQuery(hql, AnimalShelter.class);
            query.setParameter("filter", "%" + filter + "%");
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    @Override
    public List<AnimalShelter> getAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from AnimalShelter", AnimalShelter.class).list();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int update(AnimalShelter animalShelter) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.merge(animalShelter);
            session.getTransaction().commit();
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public int delete(AnimalShelter animalShelter) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.delete(animalShelter);
            session.getTransaction().commit();
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public int insert(AnimalShelter animalShelter) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();

            for(Animal animal : animalShelter.getAnimalList()) {
                animal.setShelter(animalShelter);
            }

            for(Rating rating : animalShelter.getRatings()) {
                rating.setShelter(animalShelter);
            }

            session.save(animalShelter);
            session.getTransaction().commit();
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public AnimalShelter findById(Long shelterId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(AnimalShelter.class, shelterId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<AnimalShelter> getAllSheltersWithAnimalsAndRatings() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "SELECT s FROM AnimalShelter s";
            Query<AnimalShelter> query = session.createQuery(hql, AnimalShelter.class);
            List<AnimalShelter> shelters = query.list();

            shelters.forEach(shelter -> {
                Hibernate.initialize(shelter.getAnimalList());
                Hibernate.initialize(shelter.getRatings());
            });

            return shelters;
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public List<Object[]> getShelterRatingsSummaryUsingCriteria() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Object[]> cq = cb.createQuery(Object[].class);
            Root<Rating> root = cq.from(Rating.class);

            cq.multiselect(
                    root.get("shelter").get("shelterName"),
                    cb.avg(root.get("rating")),
                    cb.count(root.get("rating"))
            ).groupBy(root.get("shelter").get("shelterName"));

            return session.createQuery(cq).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}
