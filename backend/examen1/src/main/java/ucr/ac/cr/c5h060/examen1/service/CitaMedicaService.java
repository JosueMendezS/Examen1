package ucr.ac.cr.c5h060.examen1.service;

import ucr.ac.cr.c5h060.examen1.dto.CitaMedicaDTO;
import ucr.ac.cr.c5h060.examen1.dto.CrearCitaDTO;
import ucr.ac.cr.c5h060.examen1.model.CitaMedica;
import ucr.ac.cr.c5h060.examen1.model.Doctor;
import ucr.ac.cr.c5h060.examen1.model.Paciente;
import ucr.ac.cr.c5h060.examen1.repository.CitaMedicaRepository;
import ucr.ac.cr.c5h060.examen1.repository.DoctorRepository;
import ucr.ac.cr.c5h060.examen1.repository.PacienteRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CitaMedicaService {

    private final CitaMedicaRepository citaMedicaRepository;
    private final PacienteRepository pacienteRepository;
    private final DoctorRepository doctorRepository;

    public CitaMedicaService(
            CitaMedicaRepository citaMedicaRepository,
            PacienteRepository pacienteRepository,
            DoctorRepository doctorRepository) {
        this.citaMedicaRepository = citaMedicaRepository;
        this.pacienteRepository = pacienteRepository;
        this.doctorRepository = doctorRepository;
    }

    public List<CitaMedicaDTO> obtenerTodas() {
        return citaMedicaRepository.findAllByOrderByFechaCitaDesc()
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public List<CitaMedicaDTO> obtenerPorPrioridad(String prioridad) {
        return citaMedicaRepository.findByNivelPrioridadOrderByFechaCitaDesc(prioridad)
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public CitaMedicaDTO crearCita(CrearCitaDTO dto) {
        Doctor doctor = doctorRepository.findById(dto.getDoctorId())
                .orElseThrow(() -> new RuntimeException("Doctor no encontrado con ese id " + dto.getDoctorId()));

        if (!Boolean.TRUE.equals(doctor.getDisponible())) {
            throw new IllegalStateException("El doctor seleccionado no está disponible");
        }

        Paciente paciente = pacienteRepository.findById(dto.getPacienteId())
                .orElseThrow(
                        () -> new RuntimeException("Paciente no encontrado con el id dado " + dto.getPacienteId()));

        String codigoUnico = "CIT-2026-" + UUID.randomUUID().toString().substring(0, 4).toUpperCase();

        CitaMedica cita = new CitaMedica();
        cita.setCodigoCita(codigoUnico);
        cita.setPaciente(paciente);
        cita.setDoctor(doctor);
        cita.setNivelPrioridad(dto.getNivelPrioridad());
        cita.setMotivoConsulta(dto.getMotivoConsulta());
        cita.setEstado("PENDIENTE");
        cita.setMontoConsulta(dto.getMontoConsulta());
        cita.setFechaCita(LocalDateTime.now());

        CitaMedica citaGuardada = citaMedicaRepository.save(cita);
        return convertirADTO(citaGuardada);
    }

    public CitaMedicaDTO actualizarEstado(Long id, String nuevoEstado) {
        CitaMedica cita = citaMedicaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cita médica no encontrada con ese id " + id));

        cita.setEstado(nuevoEstado);
        CitaMedica citaActualizada = citaMedicaRepository.save(cita);
        return convertirADTO(citaActualizada);
    }

    private CitaMedicaDTO convertirADTO(CitaMedica cita) {
        CitaMedicaDTO dto = new CitaMedicaDTO();
        dto.setId(cita.getId() != null ? cita.getId().longValue() : null);
        dto.setCodigoCita(cita.getCodigoCita());
        dto.setNombrePaciente(cita.getPaciente().getNombreCompleto());
        dto.setNombreDoctor(cita.getDoctor().getNombre());
        dto.setNivelPrioridad(cita.getNivelPrioridad());
        dto.setMotivoConsulta(cita.getMotivoConsulta());
        dto.setEstado(cita.getEstado());
        dto.setMontoConsulta(cita.getMontoConsulta());
        dto.setFechaCita(cita.getFechaCita());
        return dto;
    }
}