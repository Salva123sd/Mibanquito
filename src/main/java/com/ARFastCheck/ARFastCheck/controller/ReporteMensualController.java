package com.ARFastCheck.ARFastCheck.controller;

import com.ARFastCheck.ARFastCheck.model.Prestamo;
import com.ARFastCheck.ARFastCheck.repository.PrestamoRepository;
import com.ARFastCheck.ARFastCheck.repository.PersonaRepository;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@Controller
@RequestMapping("/reportes")
public class ReporteMensualController {

    private final PrestamoRepository prestamoRepository;
    private final PersonaRepository personaRepository;
    private final SpringTemplateEngine templateEngine;

    public ReporteMensualController(PrestamoRepository prestamoRepository,
                                    PersonaRepository personaRepository,
                                    SpringTemplateEngine templateEngine) {
        this.prestamoRepository = prestamoRepository;
        this.personaRepository = personaRepository;
        this.templateEngine = templateEngine;
    }

    /**
     * /reportes/mensual/pdf                  -> PDF del mes actual
     * /reportes/mensual/pdf?year=2025&month=11 -> PDF noviembre 2025
     */
    @GetMapping("/mensual/pdf")
    public ResponseEntity<byte[]> reporteMensualPdf(
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer month) {

        // 1) Determinar mes (por defecto mes actual)
        YearMonth ym;
        if (year != null && month != null) {
            ym = YearMonth.of(year, month);
        } else {
            ym = YearMonth.now();
        }

        LocalDate inicio = ym.atDay(1);
        LocalDate fin = ym.atEndOfMonth();

        // 2) Datos para el reporte
        List<Prestamo> prestamosMes = prestamoRepository.findByFechaPrestamoBetween(inicio, fin);

        long totalClientes         = personaRepository.count();
        long prestamosActivos      = prestamoRepository.countByEstado("ACTIVO");
        long prestamosDevueltos    = prestamoRepository.countByEstado("DEVUELTO");
        long prestamosVencidos     = prestamoRepository.countByEstado("VENCIDO");
        long prestamosAtrasados    = prestamoRepository.countByEstado("ENTREGADO ATRASADO");
        long totalPrestamosMes     = prestamosMes.size();

        try {
            // 3) Armar contexto Thymeleaf
            Context ctx = new Context();
            ctx.setVariable("prestamosMes", prestamosMes);
            ctx.setVariable("totalClientes", totalClientes);
            ctx.setVariable("prestamosActivos", prestamosActivos);
            ctx.setVariable("prestamosDevueltos", prestamosDevueltos);
            ctx.setVariable("prestamosVencidos", prestamosVencidos);
            ctx.setVariable("prestamosAtrasados", prestamosAtrasados);
            ctx.setVariable("totalPrestamosMes", totalPrestamosMes);
            ctx.setVariable("mes", ym);
            ctx.setVariable("inicioMes", inicio);
            ctx.setVariable("finMes", fin);

            // usa la plantilla templates/reportes/reporte-mensual-pdf.html
            String html = templateEngine.process("reportes/reporte-mensual-pdf", ctx);

            // 4) HTML -> PDF
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.useFastMode();
            builder.withHtmlContent(html, null);
            builder.toStream(baos);
            builder.run();

            byte[] pdfBytes = baos.toByteArray();

            // 5) Headers de descarga
            String nombreArchivo = String.format(
                    "reporte-prestamos-%d-%02d.pdf",
                    ym.getYear(),
                    ym.getMonthValue()
            );

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDisposition(
                    ContentDisposition.attachment()
                            .filename(nombreArchivo)
                            .build()
            );

            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
