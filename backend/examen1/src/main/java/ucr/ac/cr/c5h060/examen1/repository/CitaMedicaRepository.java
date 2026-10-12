package ucr.ac.cr.c5h060.examen1.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ucr.ac.cr.c5h060.examen1.model.CitaMedica;

@Repository
public interface CitaMedicaRepository extends JpaRepository<CitaMedica, Long> {

    List<CitaMedica> findByNivelPrioridadOrderByFechaCitaDesc(String prioridad);

    List<CitaMedica> findAllByOrderByFechaCitaDesc();

}