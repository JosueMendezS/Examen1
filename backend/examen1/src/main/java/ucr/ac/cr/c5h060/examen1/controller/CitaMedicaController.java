package ucr.ac.cr.c5h060.examen1.controller;

import ucr.ac.cr.c5h060.examen1.dto.CitaMedicaDTO;
import ucr.ac.cr.c5h060.examen1.dto.CrearCitaDTO;
import ucr.ac.cr.c5h060.examen1.service.CitaMedicaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/citaMedica")
@CrossOrigin(origins = "http://localhost:4200")
public class CitaMedicaController {

    private final CitaMedicaService citaMedicaService;

    public CitaMedicaController(CitaMedicaService citaMedicaService) {
        this.citaMedicaService = citaMedicaService;
    }

    @GetMapping
    public ResponseEntity<List<CitaMedicaDTO>> obtenerTodas() {
        return ResponseEntity.ok(citaMedicaService.obtenerTodas());
    }

    @GetMapping("/prioridad/{prioridad}")
    public ResponseEntity<List<CitaMedicaDTO>> obtenerPorPrioridad(@PathVariable String prioridad) {
        return ResponseEntity.ok(citaMedicaService.obtenerPorPrioridad(prioridad));
    }

    @PostMapping
    public ResponseEntity<CitaMedicaDTO> crearCita(@RequestBody CrearCitaDTO dto) {
        CitaMedicaDTO nuevaCita = citaMedicaService.crearCita(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaCita);
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<CitaMedicaDTO> actualizarEstado(@PathVariable Long id,
            @RequestBody Map<String, String> body) {
        String nuevoEstado = body.get("estado");
        CitaMedicaDTO citaActualizada = citaMedicaService.actualizarEstado(id, nuevoEstado);
        return ResponseEntity.ok(citaActualizada);
    }
}