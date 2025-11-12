package ru.inno.attestation.attestation03.specifications;

import jakarta.annotation.Nullable;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import ru.inno.attestation.attestation03.dto.UserFilterDto;
import ru.inno.attestation.attestation03.models.User;

import java.util.ArrayList;
import java.util.List;

public class UserSpecifications {

    public static Specification<User> getSpecification(@Nullable UserFilterDto filter) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(criteriaBuilder.isFalse(root.get("deleted")));

            if (filter == null) {
                return  criteriaBuilder.and(predicates.toArray(new Predicate[0]));
            }
            if (filter.role() != null) {
                predicates.add(criteriaBuilder.equal(root.get("role"), filter.role()));
            }
            if (filter.id() != null) {
                predicates.add(criteriaBuilder.equal(root.get("deleted"), filter.id()));
            }
            if (filter.userName() != null) {
                predicates.add(criteriaBuilder.equal(root.get("userName"), filter.userName()));
            }

            return  criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

    }
}
