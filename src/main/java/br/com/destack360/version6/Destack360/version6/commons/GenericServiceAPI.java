package br.com.destack360.version6.Destack360.version6.commons;

import com.google.cloud.firestore.CollectionReference;

import java.util.List;
import java.util.Map;

public interface GenericServiceAPI<T, T1> {

    CollectionReference getCollection() throws Exception;

    String update(T entity, String id) throws Exception;
    String save(T entity) throws Exception;

    String save(T entity, String id) throws Exception;

    void delete(String id) throws Exception;
    T1 get(String id) throws Exception;
    Map<String, Object> getAsMap(String id) throws Exception;
    List<T1> getAll() throws Exception;


}
