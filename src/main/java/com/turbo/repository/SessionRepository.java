package com.turbo.repository;

import com.turbo.model.Session;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by ermolaev on 5/9/17.
 */
public interface SessionRepository extends CrudRepository<Session, Long> {
}
