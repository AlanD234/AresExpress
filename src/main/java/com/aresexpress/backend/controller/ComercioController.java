package com.aresexpress.backend.controller;

import com.aresexpress.backend.model.Comercio;
import com.aresexpress.backend.service.ComercioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/api/comercios")
@CrossOrigin(origins = "*")
public class ComercioController {

    @Autowired
    private ComercioService comercioService;

    // --- CAMBIO CLAVE: Usar la ruta absoluta del workspace del proyecto ---
    private static final String UPLOAD_DIR = System.getProperty("user.dir") + File.separator + "uploads" + File.separator + "logos" + File.separator;

    // ==========================================
    // 0. ENDPOINT PARA LISTAR SOLO COMERCIOS ACTIVOS
    // ==========================================
    @GetMapping
    public List<Comercio> listarComerciosActivos() {
        return comercioService.obtenerActivos();
    }

    // ==========================================
    // 0.1. ENDPOINT PARA LISTAR COMERCIOS VETADOS (HISTORIAL)
    // ==========================================
    @GetMapping("/vetados")
    public List<Comercio> listarComerciosVetados() {
        return comercioService.obtenerVetados();
    }

    // ==========================================
    // 1. ENDPOINT PARA CREAR NUEVO COMERCIO
    // ==========================================
    @PostMapping
    public Comercio crearComercio(
            @RequestParam("nombre") String nombre,
            @RequestParam("categoria") String categoria,
            @RequestParam("razonSocial") String razonSocial,
            @RequestParam("nit") String nit,
            @RequestParam("regimenIsr") String regimenIsr,
            @RequestParam("agenteRetenedorIva") Boolean agenteRetenedorIva,
            @RequestParam("contactoPersona") String contactoPersona,
            @RequestParam("telefono") String telefono,
            @RequestParam("direccion") String direccion,
            @RequestParam("diasOperacion") String diasOperacion,
            @RequestParam("horaApertura") LocalTime horaApertura,
            @RequestParam("horaCierre") LocalTime horaCierre,
            @RequestParam("abierto") Boolean abierto,
            @RequestParam(value = "logo", required = false) MultipartFile logoFile) {

        Comercio comercio = new Comercio();
        comercio.setNombre(nombre);
        comercio.setCategoria(categoria);
        comercio.setRazonSocial(razonSocial);
        comercio.setNit(nit);
        comercio.setRegimenIsr(regimenIsr);
        comercio.setAgenteRetenedorIva(agenteRetenedorIva);
        comercio.setContactoPersona(contactoPersona);
        comercio.setTelefono(telefono);
        comercio.setDireccion(direccion);
        comercio.setDiasOperacion(diasOperacion);
        comercio.setHoraApertura(horaApertura);
        comercio.setHoraCierre(horaCierre);
        comercio.setAbierto(abierto);
        comercio.setActivo(true); // Todo comercio nuevo nace activo por defecto

        // Lógica automática para guardar el archivo de la imagen si fue enviado
        if (logoFile != null && !logoFile.isEmpty()) {
            try {
                File uploadDir = new File(UPLOAD_DIR);
                if (!uploadDir.exists()) {
                    boolean creado = uploadDir.mkdirs();
                    if (creado) {
                        System.out.println("-> Carpeta de logos creada automáticamente en: " + uploadDir.getAbsolutePath());
                    }
                }
                
                String fileName = System.currentTimeMillis() + "_" + logoFile.getOriginalFilename();
                Path filePath = Paths.get(UPLOAD_DIR + fileName);
                Files.write(filePath, logoFile.getBytes());
                
                comercio.setLogoUrl("/logos/" + fileName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return comercioService.guardarComercio(comercio);
    }

    // ==========================================
    // 2. ENDPOINT PARA ACTUALIZAR COMERCIO EXISTENTE
    // ==========================================
    @PutMapping("/{id}")
    public Comercio actualizarComercio(
            @PathVariable Long id,
            @RequestParam("nombre") String nombre,
            @RequestParam("categoria") String categoria,
            @RequestParam("razonSocial") String razonSocial,
            @RequestParam("nit") String nit,
            @RequestParam("regimenIsr") String regimenIsr,
            @RequestParam("agenteRetenedorIva") Boolean agenteRetenedorIva,
            @RequestParam("contactoPersona") String contactoPersona,
            @RequestParam("telefono") String telefono,
            @RequestParam("direccion") String direccion,
            @RequestParam("diasOperacion") String diasOperacion,
            @RequestParam("horaApertura") LocalTime horaApertura,
            @RequestParam("horaCierre") LocalTime horaCierre,
            @RequestParam("abierto") Boolean abierto,
            @RequestParam(value = "logo", required = false) MultipartFile logoFile) {

        Comercio comercio = comercioService.obtenerComercioPorId(id);
        
        if (comercio == null) {
            throw new RuntimeException("Comercio no encontrado con el ID: " + id);
        }

        comercio.setNombre(nombre);
        comercio.setCategoria(categoria);
        comercio.setRazonSocial(razonSocial);
        comercio.setNit(nit);
        comercio.setRegimenIsr(regimenIsr);
        comercio.setAgenteRetenedorIva(agenteRetenedorIva);
        comercio.setContactoPersona(contactoPersona);
        comercio.setTelefono(telefono);
        comercio.setDireccion(direccion);
        comercio.setDiasOperacion(diasOperacion);
        comercio.setHoraApertura(horaApertura);
        comercio.setHoraCierre(horaCierre);
        comercio.setAbierto(abierto);

        if (logoFile != null && !logoFile.isEmpty()) {
            try {
                File uploadDir = new File(UPLOAD_DIR);
                if (!uploadDir.exists()) {
                    uploadDir.mkdirs();
                }
                
                String fileName = System.currentTimeMillis() + "_" + logoFile.getOriginalFilename();
                Path filePath = Paths.get(UPLOAD_DIR + fileName);
                Files.write(filePath, logoFile.getBytes());
                
                comercio.setLogoUrl("/logos/" + fileName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return comercioService.guardarComercio(comercio);
    }

    // ==========================================
    // 3. ENDPOINT PARA VETAR (BORRADO LÓGICO: ACTIVO = FALSE)
    // ==========================================
    @DeleteMapping("/{id}")
    public void vetarComercio(@PathVariable Long id) {
        comercioService.cambiarEstadoComercio(id, false);
    }

    // ==========================================
    // 4. ENDPOINT PARA RESTAURAR COMERCIO (ACTIVO = TRUE)
    // ==========================================
    @PutMapping("/restaurar/{id}")
    public void restaurarComercio(@PathVariable Long id) {
        comercioService.cambiarEstadoComercio(id, true);
    }
}