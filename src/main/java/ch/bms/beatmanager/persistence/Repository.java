package ch.bms.beatmanager.persistence;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * Generic persistence interface decoupling business logic from storage implementation.
 */
public interface Repository<T> {

    void saveAll(List<T> items) throws IOException;

    List<T> loadAll() throws IOException;

    Optional<T> findById(String id) throws IOException;
}