package studia.animalshelterdesktopapp.DAO;

import org.hibernate.Session;
import studia.animalshelterdesktopapp.Animal;
import studia.animalshelterdesktopapp.AnimalCondition;
import studia.animalshelterdesktopapp.controllers.HibernateUtil;

import java.util.List;

public class AnimalDAO implements IDAO<Animal> {
    @Override
    public Animal get(Long id) {
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Animal.class, id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Animal> getAll() {
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Animal", Animal.class).list();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Animal> getAllInShelter(Long shelterId) {
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "from Animal where shelter.id = :shelterId";
            return session.createQuery(hql, Animal.class).setParameter("shelterId", shelterId).list();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Animal> getAllInShelter(Long shelterId, String filter, AnimalCondition condition) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "from Animal where shelter.id = :shelterId and " +
                    "(:filter is null or animalName like :filter or animalSpecies like :filter) and " +
                    "(:condition is null or animalCondition = :condition)";
            return session.createQuery(hql, Animal.class)
                    .setParameter("shelterId", shelterId)
                    .setParameter("filter", filter == null ? null : "%" + filter + "%")
                    .setParameter("condition", condition)
                    .list();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int insert(Animal animal) {
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.save(animal);
            session.getTransaction().commit();
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public int update(Animal animal) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.update(animal);
            session.getTransaction().commit();
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public int delete(Animal animal) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.delete(animal);
            session.getTransaction().commit();
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
