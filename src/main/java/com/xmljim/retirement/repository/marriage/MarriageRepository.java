package com.xmljim.retirement.repository.marriage;

import com.xmljim.retirement.domain.marriage.Marriage;
import com.xmljim.retirement.domain.marriage.MarriageStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Spring Data JPA repository for {@link Marriage} entities.
 *
 * <p>Provides CRUD operations and custom queries for marriage management.</p>
 *
 * @since 1.0
 */
@Repository
public interface MarriageRepository extends JpaRepository<Marriage, UUID> {

    /**
     * Finds all marriages involving a specific person (as either spouse).
     *
     * @param personId the ID of the person
     * @return list of marriages involving the person
     */
    @Query("SELECT m FROM Marriage m WHERE m.person1.id = :personId OR m.person2.id = :personId")
    List<Marriage> findByPersonId(@Param("personId") UUID personId);

    /**
     * Finds all marriages with the given status.
     *
     * @param status the marriage status to search for
     * @return list of marriages with the matching status
     */
    List<Marriage> findByStatus(MarriageStatus status);

    /**
     * Finds all marriages between two specific persons.
     *
     * @param person1Id the ID of the first person
     * @param person2Id the ID of the second person
     * @return list of marriages between the two persons
     */
    @Query("SELECT m FROM Marriage m WHERE "
            + "(m.person1.id = :person1Id AND m.person2.id = :person2Id) "
            + "OR (m.person1.id = :person2Id AND m.person2.id = :person1Id)")
    List<Marriage> findByPersonPair(
            @Param("person1Id") UUID person1Id,
            @Param("person2Id") UUID person2Id);

    /**
     * Checks if a person has any active (MARRIED status) marriages.
     *
     * @param personId the ID of the person
     * @return true if the person has an active marriage
     */
    @Query("SELECT COUNT(m) > 0 FROM Marriage m "
            + "WHERE (m.person1.id = :personId OR m.person2.id = :personId) "
            + "AND m.status = 'MARRIED'")
    boolean hasActiveMarriage(@Param("personId") UUID personId);
}
