package ru.inno.attestation.attestation03.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.inno.attestation.attestation03.models.TrainingEvent;

public interface TrainingEventRepository extends JpaRepository<TrainingEvent, Long>, JpaSpecificationExecutor<TrainingEvent> {

}
