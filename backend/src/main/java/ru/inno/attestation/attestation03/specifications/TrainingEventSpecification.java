package ru.inno.attestation.attestation03.specifications;

import jakarta.annotation.Nullable;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import ru.inno.attestation.attestation03.dto.TrainingEventFilterDto;
import ru.inno.attestation.attestation03.models.TrainingEvent;
import ru.inno.attestation.attestation03.models.User;

import java.util.ArrayList;
import java.util.List;

public class TrainingEventSpecification {
    public static Specification<TrainingEvent> getSpecification(@Nullable TrainingEventFilterDto filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.isFalse(root.get("deleted")));

            if (filter == null) {
                return  cb.and(predicates.toArray(new Predicate[0]));
            }
            if(filter.id() != null) {
                predicates.add(cb.equal(root.get("id"), filter.id()));
            }
            if(filter.description() != null) {
                predicates.add(cb.equal(root.get("description"), filter.description()));
            }
            if(filter.ownerId() != null) {
                predicates.add(cb.equal(root.get("ownerId"), filter.ownerId()));
            }
            if(filter.fromDate() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("startDate"), filter.fromDate()));
            }
            if(filter.fromDate() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("endDate"), filter.toDate()));
            }
            if (filter.studentIds() != null && !filter.studentIds().isEmpty()) {
                Join<TrainingEvent, User> studentsJoin = root.join("students");
                predicates.add(studentsJoin.get("id").in(filter.studentIds()));
            }


            return  cb.and(predicates.toArray(new Predicate[0]));

        };
    }
}
