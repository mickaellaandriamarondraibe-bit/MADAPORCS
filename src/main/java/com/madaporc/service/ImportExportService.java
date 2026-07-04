package com.madaporc.service;

import com.madaporc.model.*;
import com.madaporc.repository.*;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ImportExportService {

    // separateur CSV (le point-virgule est celui utilise par Excel en francais)
    private static final String SEP = ";";

    private final ImportExportRepository importExportRepository;
    private final ClientRepository clientRepository;
    private final IngredientRepository ingredientRepository;
    private final LotPorcRepository lotPorcRepository;
    private final VenteRepository venteRepository;
    private final DepenseRepository depenseRepository;

    public ImportExportService(ImportExportRepository importExportRepository,
                               ClientRepository clientRepository,
                               IngredientRepository ingredientRepository,
                               LotPorcRepository lotPorcRepository,
                               VenteRepository venteRepository,
                               DepenseRepository depenseRepository) {
        this.importExportRepository = importExportRepository;
        this.clientRepository = clientRepository;
        this.ingredientRepository = ingredientRepository;
        this.lotPorcRepository = lotPorcRepository;
        this.venteRepository = venteRepository;
        this.depenseRepository = depenseRepository;
    }

    // ================= HISTORIQUE =================

    public List<ImportExport> historique() {
        return importExportRepository.findAllByOrderByDateOperationDesc();
    }

    private void tracer(String type, String format, String module, String nomFichier,
                        String statut, String message) {
        ImportExport op = new ImportExport();
        op.setTypeOperation(type);
        op.setFormatFichier(format);
        op.setModule(module);
        op.setNomFichier(nomFichier);
        op.setStatut(statut);
        op.setMessage(message);
        importExportRepository.save(op);
    }

    // ================= MODELES TELECHARGEABLES =================
    // En-tetes attendus a l'import : le gestionnaire telecharge ce modele,
    // voit les colonnes exactes + un exemple, remplit puis reimporte.

    public String enteteModule(String module) {
        switch (module) {
            case "CLIENTS":     return "nom;telephone;adresse";
            case "INGREDIENTS": return "nom;unite;stock_actuel;seuil_alerte";
            default:            return null;
        }
    }

    private String exempleModule(String module) {
        switch (module) {
            case "CLIENTS":     return "Rakoto Jean;0341234567;Lot II Antananarivo";
            case "INGREDIENTS": return "Mais;kg;100;20";
            default:            return "";
        }
    }

    public byte[] modeleCsv(String module) {
        String entete = enteteModule(module);
        if (entete == null) return null;
        String contenu = entete + "\n" + exempleModule(module) + "\n";
        return contenu.getBytes(StandardCharsets.UTF_8);
    }

    // ================= IMPORT =================

    public String importerCsv(MultipartFile file, String module) {
        if (file == null || file.isEmpty()) {
            return "Aucun fichier fourni.";
        }
        String nom = file.getOriginalFilename();
        try {
            List<String[]> lignes = lireCsv(file);
            int n;
            switch (module) {
                case "CLIENTS":     n = importerClients(lignes);     break;
                case "INGREDIENTS": n = importerIngredients(lignes); break;
                default:
                    tracer("IMPORT", "EXCEL", module, nom, "ECHEC", "Import non supporte pour ce module.");
                    return "Import non supporte pour le module " + module + ".";
            }
            String msg = n + " ligne(s) importee(s).";
            tracer("IMPORT", "EXCEL", module, nom, "SUCCES", msg);
            return msg;
        } catch (Exception e) {
            tracer("IMPORT", "EXCEL", module, nom, "ECHEC", e.getMessage());
            return "Erreur d'import : " + e.getMessage();
        }
    }

    private int importerClients(List<String[]> lignes) {
        int n = 0;
        for (String[] c : lignes) {
            Client client = new Client();
            client.setNom(valeur(c, 0));
            client.setTelephone(valeur(c, 1));
            client.setAdresse(valeur(c, 2));
            clientRepository.save(client);
            n++;
        }
        return n;
    }

    private int importerIngredients(List<String[]> lignes) {
        int n = 0;
        for (String[] c : lignes) {
            Ingredient i = new Ingredient();
            i.setNom(valeur(c, 0));
            i.setUnite(valeur(c, 1));
            i.setStockActuel(nombre(valeur(c, 2)));
            i.setSeuilAlerte(nombre(valeur(c, 3)));
            i.setCreatedAt(LocalDateTime.now());
            i.setUpdatedAt(LocalDateTime.now());
            ingredientRepository.save(i);
            n++;
        }
        return n;
    }

    // ================= EXPORT EXCEL (CSV) =================

    public byte[] exporterCsv(String module) {
        List<String[]> lignes = donneesExport(module);
        if (lignes == null) {
            tracer("EXPORT", "EXCEL", module, module + ".csv", "ECHEC", "Export non supporte.");
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (String[] ligne : lignes) {
            for (int i = 0; i < ligne.length; i++) {
                if (i > 0) sb.append(SEP);
                sb.append(echapper(ligne[i]));
            }
            sb.append("\n");
        }
        tracer("EXPORT", "EXCEL", module, module + ".csv", "SUCCES", "Export reussi.");
        return sb.toString().getBytes(StandardCharsets.UTF_8);
    }

    // ================= EXPORT PDF =================

    public byte[] exporterPdf(String module) {
        List<String[]> lignes = donneesExport(module);
        if (lignes == null) {
            tracer("EXPORT", "PDF", module, module + ".pdf", "ECHEC", "Export non supporte.");
            return null;
        }
        StringBuilder html = new StringBuilder();
        html.append("<html><head><style>")
            .append("body{font-family:sans-serif;font-size:11px;}")
            .append("h1{font-size:16px;}")
            .append("table{width:100%;border-collapse:collapse;}")
            .append("th,td{border:1px solid #999;padding:4px;text-align:left;}")
            .append("th{background:#eee;}")
            .append("</style></head><body>");
        html.append("<h1>Export ").append(module).append("</h1><table>");
        for (int r = 0; r < lignes.size(); r++) {
            String tag = (r == 0) ? "th" : "td";
            html.append("<tr>");
            for (String cell : lignes.get(r)) {
                html.append("<").append(tag).append(">").append(html(cell)).append("</").append(tag).append(">");
            }
            html.append("</tr>");
        }
        html.append("</table></body></html>");

        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.useFastMode();
            builder.withHtmlContent(html.toString(), null);
            builder.toStream(out);
            builder.run();
            tracer("EXPORT", "PDF", module, module + ".pdf", "SUCCES", "Export reussi.");
            return out.toByteArray();
        } catch (Exception e) {
            tracer("EXPORT", "PDF", module, module + ".pdf", "ECHEC", e.getMessage());
            return null;
        }
    }

    // ================= DONNEES A EXPORTER (1re ligne = en-tete) =================

    private List<String[]> donneesExport(String module) {
        List<String[]> lignes = new ArrayList<>();
        switch (module) {
            case "CLIENTS":
                lignes.add(new String[]{"nom", "telephone", "adresse"});
                for (Client c : clientRepository.findAll())
                    lignes.add(new String[]{c.getNom(), c.getTelephone(), c.getAdresse()});
                return lignes;
            case "INGREDIENTS":
                lignes.add(new String[]{"nom", "unite", "stock_actuel", "seuil_alerte"});
                for (Ingredient i : ingredientRepository.findAll())
                    lignes.add(new String[]{i.getNom(), i.getUnite(), texte(i.getStockActuel()), texte(i.getSeuilAlerte())});
                return lignes;
            case "LOTS":
                lignes.add(new String[]{"code", "sexe", "objectif", "effectif_actuel", "statut"});
                for (LotPorc l : lotPorcRepository.findAll())
                    lignes.add(new String[]{l.getCodeLot(), l.getSexe(), l.getObjectif(), texte(l.getEffectifActuel()), l.getStatut()});
                return lignes;
            case "VENTES":
                lignes.add(new String[]{"date", "client", "montant", "statut"});
                for (Vente v : venteRepository.findAll()) {
                    String client = v.getClient() != null ? v.getClient().getNom() : "";
                    lignes.add(new String[]{texte(v.getDateVente()), client, texte(v.getMontantTotal()), v.getStatut()});
                }
                return lignes;
            case "DEPENSES":
                lignes.add(new String[]{"date", "categorie", "montant", "description"});
                for (Depense d : depenseRepository.findAll()) {
                    String cat = d.getCategorie() != null ? d.getCategorie().getNom() : "";
                    lignes.add(new String[]{texte(d.getDateDepense()), cat, texte(d.getMontant()), d.getDescription()});
                }
                return lignes;
            default:
                return null;
        }
    }

    private List<String[]> lireCsv(MultipartFile file) throws Exception {
        List<String[]> lignes = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            boolean premiere = true;
            while ((line = br.readLine()) != null) {
                if (premiere) { premiere = false; continue; } // ignorer l'en-tete
                if (line.isBlank()) continue;
                lignes.add(line.split(SEP, -1));
            }
        }
        return lignes;
    }

    private String valeur(String[] c, int i) {
        return (i < c.length && c[i] != null) ? c[i].trim() : "";
    }

    private BigDecimal nombre(String v) {
        if (v == null || v.isBlank()) return BigDecimal.ZERO;
        return new BigDecimal(v.trim().replace(",", "."));
    }

    private String texte(Object o) {
        return o == null ? "" : o.toString();
    }

    private String echapper(String v) {
        if (v == null) return "";
        if (v.contains(SEP) || v.contains("\"") || v.contains("\n")) {
            return "\"" + v.replace("\"", "\"\"") + "\"";
        }
        return v;
    }

    private String html(String v) {
        if (v == null) return "";
        return v.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }
}
