package studia.animalshelterdesktopapp.DAO;

import java.util.List;

public interface IDAO<T> {
    T get (Long id);
    List<T> getAll();
    int update(T t);
    int delete(T t);
    int insert(T t);
}

