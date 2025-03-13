package dao;

import java.util.List;

public interface GenericDAO<T, ID> {
    T find(ID id);
    List<T> findAll();
    void save(T entity);
    void update(T entity);
    void delete(ID id);
}