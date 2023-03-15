package org.response.database;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
/**
 * @author Tomas Kozakas
 */
@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
}