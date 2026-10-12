package ucr.ac.cr.c5h060.examen1.repository;

import java.math.BigInteger;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ucr.ac.cr.c5h060.examen1.model.Doctor;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, BigInteger> {
}