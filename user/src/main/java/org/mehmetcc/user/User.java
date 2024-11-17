package org.mehmetcc.user;

import io.quarkus.hibernate.reactive.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

/**
 * Public fields used here are called the Active Record Pattern and thus is a best practice. For more information,
 * please refer to: https://thorben-janssen.com/panache-active-record-pattern/
 * <p>
 * Since we don't have accessor methods (like getters and setters) we can define our own logic predetermined for objects,
 * i.e. if there was a method defined with the name setFirstName that permutates some data transformation, for every
 * object creation that method will be called.
 * <p>
 * Active Record Pattern also states logic usually programmed under a separate service (usually called a Repository)
 * should be encapsulated within the Entity object as well (in this case, the User class) This kind of behaviour
 * also supplants Domain Driven Design(DDD) since it tackles Anemic Object Anti-Pattern.
 * <p>
 * For more:
 * [1] https://martinfowler.com/bliki/AnemicDomainModel.html
 * [2] https://www.martinfowler.com/eaaCatalog/activeRecord.html
 */

@Entity
@Table(name = "users")
public class User extends PanacheEntity {
    @Column(name = "first_name", nullable = false)
    public String firstName;

    @Column(name = "last_name", nullable = false)
    public String lastName;

    @Column(name = "created_at", nullable = false)
    public java.time.LocalDateTime createdAt;

    @CreationTimestamp
    @Column(name = "updated_at", nullable = false)
    public java.time.LocalDateTime updatedAt;

    @UpdateTimestamp
    @Column(name = "is_active", nullable = false)
    public boolean isActive = true;
}
