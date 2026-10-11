package pe.edu.cibertec.evaluacion;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class EvaluacionServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(EvaluacionServiceApplication.class, args);
    }

    @RestController
    @RequestMapping("/api/v1/evaluaciones")
    public static class EvaluacionController {
        @GetMapping("/scoring/{clienteId}")
        public ScoringRiesgoDTO obtenerScoring(@PathVariable Long clienteId) {
            return buildScoring(clienteId);
        }

        @GetMapping("/cliente/{clienteId}")
        public Map<String, Object> obtenerEvaluacionCliente(@PathVariable Long clienteId) {
            Map<String, Object> response = new HashMap<>();
            response.put("clienteId", clienteId);
            response.put("scoring", buildScoring(clienteId));
            response.put("estadoEvaluacion", "ACTIVA");
            return response;
        }

        private ScoringRiesgoDTO buildScoring(Long clienteId) {
            int score = switch (clienteId.intValue()) {
                case 1001 -> 710;
                case 1002 -> 640;
                default -> 690;
            };

            String nivel;
            double capacidad;

            if (score >= 700) {
                nivel = "BAJO";
                capacidad = 45000.00;
            } else if (score >= 600) {
                nivel = "MEDIO";
                capacidad = 30000.00;
            } else if (score >= 500) {
                nivel = "ALTO";
                capacidad = 18000.00;
            } else {
                nivel = "CRITICO";
                capacidad = 10000.00;
            }

            ScoringRiesgoDTO dto = new ScoringRiesgoDTO();
            dto.setClienteId(clienteId);
            dto.setPuntajeCrediticio(score);
            dto.setNivelRiesgo(nivel);
            dto.setCapacidadMaxEndeudamiento(capacidad);
            dto.setDictamenRiesgo(score >= 650 ? "APTO" : "NO_APTO");
            return dto;
        }
    }

    public static class ScoringRiesgoDTO {
        private Long clienteId;
        private Integer puntajeCrediticio;
        private String nivelRiesgo;
        private Double capacidadMaxEndeudamiento;
        private String dictamenRiesgo;

        public Long getClienteId() {
            return clienteId;
        }

        public void setClienteId(Long clienteId) {
            this.clienteId = clienteId;
        }

        public Integer getPuntajeCrediticio() {
            return puntajeCrediticio;
        }

        public void setPuntajeCrediticio(Integer puntajeCrediticio) {
            this.puntajeCrediticio = puntajeCrediticio;
        }

        public String getNivelRiesgo() {
            return nivelRiesgo;
        }

        public void setNivelRiesgo(String nivelRiesgo) {
            this.nivelRiesgo = nivelRiesgo;
        }

        public Double getCapacidadMaxEndeudamiento() {
            return capacidadMaxEndeudamiento;
        }

        public void setCapacidadMaxEndeudamiento(Double capacidadMaxEndeudamiento) {
            this.capacidadMaxEndeudamiento = capacidadMaxEndeudamiento;
        }

        public String getDictamenRiesgo() {
            return dictamenRiesgo;
        }

        public void setDictamenRiesgo(String dictamenRiesgo) {
            this.dictamenRiesgo = dictamenRiesgo;
        }
    }
}
