package com.madaporc.service;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import com.madaporc.model.LotPorc;

@Service
public class PdfExportService {

    private final SpringTemplateEngine templateEngine;

    public PdfExportService(SpringTemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
        System.setProperty("java.awt.headless", "true");
    }

    public byte[] renderLotExportPdf(List<LotPorc> lots,
                                     Map<Long, String> raceLibelles,
                                     Map<Long, String> statutLibelles,
                                     String title,
                                     String dateLabel,
                                     long total,
                                     long actifs,
                                     long alertes,
                                     String motCleLabel,
                                     String raceLabel,
                                     String statutLabel) {
        Context context = new Context();
        context.setVariable("lots", lots);
        context.setVariable("raceLibelles", raceLibelles);
        context.setVariable("statutLibelles", statutLibelles);
        context.setVariable("exportTitle", title);
        context.setVariable("filteredDate", dateLabel);
        context.setVariable("filteredTotalLots", total);
        context.setVariable("filteredTotalActifs", actifs);
        context.setVariable("filteredLotsEnAlerte", alertes);
        context.setVariable("motCleLabel", motCleLabel);
        context.setVariable("raceLabel", raceLabel);
        context.setVariable("statutLabel", statutLabel);

        String html = templateEngine.process("exports/pdf", context);

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.withHtmlContent(html, null);
            builder.toStream(outputStream);
            builder.run();
            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la génération du PDF.", e);
        }
    }
}
