package com.madaporc.service;

import com.madaporc.model.*;
import com.madaporc.repository.*;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
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
    private final RaceRepository raceRepository;
    private final VaccinRepository vaccinRepository;
    private final VaccinationRepository vaccinationRepository;
    private final GroupeReproductionRepository groupeReproductionRepository;
    private final AnalyseReproductionLotRepository analyseReproductionLotRepository;

    public ImportExportService(ImportExportRepository importExportRepository,
                               ClientRepository clientRepository,
                               IngredientRepository ingredientRepository,
                               LotPorcRepository lotPorcRepository,
                               VenteRepository venteRepository,
                               DepenseRepository depenseRepository,
                               RaceRepository raceRepository,
                               VaccinRepository vaccinRepository,
                               VaccinationRepository vaccinationRepository,
                               GroupeReproductionRepository groupeReproductionRepository,
                               AnalyseReproductionLotRepository analyseReproductionLotRepository) {
        this.importExportRepository = importExportRepository;
        this.clientRepository = clientRepository;
        this.ingredientRepository = ingredientRepository;
        this.lotPorcRepository = lotPorcRepository;
        this.venteRepository = venteRepository;
        this.depenseRepository = depenseRepository;
        this.raceRepository = raceRepository;
        this.vaccinRepository = vaccinRepository;
        this.vaccinationRepository = vaccinationRepository;
        this.groupeReproductionRepository = groupeReproductionRepository;
        this.analyseReproductionLotRepository = analyseReproductionLotRepository;
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
            case "CLIENTS":       return "nom;telephone;adresse";
            case "INGREDIENTS":   return "nom;unite;stock_actuel;seuil_alerte";
            case "LOTS":          return "code;sexe;objectif;origine;race;effectif";
            case "VACCINATIONS":  return "code_lot;vaccin;date_vaccination;date_rappel;observation";
            default:              return null;
        }
    }

    private String exempleModule(String module) {
        switch (module) {
            case "CLIENTS":       return "Rakoto Jean;0341234567;Lot II Antananarivo";
            case "INGREDIENTS":   return "Mais;kg;100;20";
            case "LOTS":          return "LOT-M-010;MALE;ENGRAISSEMENT;ACHAT;Large White;12";
            case "VACCINATIONS":  return "LOT-M-001;Peste Porcine;2026-06-01;2026-12-01;RAS";
            default:              return "";
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
                case "CLIENTS":      n = importerClients(lignes);       break;
                case "INGREDIENTS":  n = importerIngredients(lignes);   break;
                case "LOTS":         n = importerLots(lignes);          break;
                case "VACCINATIONS": n = importerVaccinations(lignes);  break;
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

    private int importerLots(List<String[]> lignes) {
        int n = 0;
        for (String[] c : lignes) {
            LotPorc l = new LotPorc();
            l.setCodeLot(valeur(c, 0));
            l.setSexe(valeur(c, 1));
            l.setObjectif(valeur(c, 2));
            String origine = valeur(c, 3);
            l.setOrigine(origine.isBlank() ? "ACHAT" : origine);
            trouverRace(valeur(c, 4)).ifPresent(l::setRace);
            int effectif = entier(valeur(c, 5));
            l.setEffectifInitial(effectif);
            l.setEffectifActuel(effectif);
            l.setStatut("ACTIF");
            l.setDateCreation(LocalDate.now());
            l.setCreatedAt(LocalDateTime.now());
            lotPorcRepository.save(l);
            n++;
        }
        return n;
    }

    private int importerVaccinations(List<String[]> lignes) {
        int n = 0;
        for (String[] c : lignes) {
            LotPorc lot = lotPorcRepository.findByCodeLot(valeur(c, 0)).orElse(null);
            Vaccin vaccin = trouverVaccin(valeur(c, 1)).orElse(null);
            // lot et vaccin sont obligatoires : on ignore les lignes introuvables
            if (lot == null || vaccin == null) continue;
            Vaccination v = new Vaccination();
            v.setLot(lot);
            v.setVaccin(vaccin);
            v.setDateVaccination(date(valeur(c, 2)));
            v.setDateRappel(dateOuNull(valeur(c, 3)));
            v.setObservation(valeur(c, 4));
            vaccinationRepository.save(v);
            n++;
        }
        return n;
    }

    // ================= EXPORT EXCEL (CSV) =================

    @Transactional
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

    @Transactional
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
            case "GROUPES":
                lignes.add(new String[]{"code", "lot_femelle", "lot_male", "date_saillie", "date_prevue_mise_bas", "statut", "porcelets_nes"});
                for (GroupeReproduction g : groupeReproductionRepository.findAll()) {
                    String femelle = g.getLotFemelle() != null ? g.getLotFemelle().getCodeLot() : "";
                    String male = g.getLotMale() != null ? g.getLotMale().getCodeLot() : "";
                    lignes.add(new String[]{g.getCodeGroupe(), femelle, male,
                            texte(g.getDateSaillie()), texte(g.getDatePrevueMiseBas()),
                            g.getStatut(), texte(g.getNbPorceletsNes())});
                }
                return lignes;
            case "ANALYSE":
                lignes.add(new String[]{"lot", "date_analyse", "pretes_jamais_saillies", "deja_reproductrices",
                        "en_cycle", "a_surveiller", "a_retirer", "femelles_total"});
                for (AnalyseReproductionLot a : analyseReproductionLotRepository.findAll()) {
                    String lot = a.getLotPorc() != null ? a.getLotPorc().getCodeLot() : "";
                    lignes.add(new String[]{lot, texte(a.getDateAnalyse()),
                            texte(a.getNbPretesJamaisSaillies()), texte(a.getNbDejaReproductricesAptes()),
                            texte(a.getNbEnCycle()), texte(a.getNbASurveiller()),
                            texte(a.getNbARetirerReproduction()), texte(a.getNbFemellesTotal())});
                }
                return lignes;
            case "SANITAIRE":
                lignes.add(new String[]{"lot", "vaccin", "date_vaccination", "date_rappel", "statut"});
                for (Vaccination vac : vaccinationRepository.findAll()) {
                    lignes.add(new String[]{texte(vac.getCodeLot()), texte(vac.getNomVaccin()),
                            texte(vac.getDateVaccination()), texte(vac.getDateRappel()), texte(vac.getStatut())});
                }
                return lignes;
            case "FINANCIER":
                BigDecimal totalV = BigDecimal.ZERO;
                for (Vente v : venteRepository.findAll()) {
                    if ("VALIDEE".equals(v.getStatut()) && v.getMontantTotal() != null) {
                        totalV = totalV.add(v.getMontantTotal());
                    }
                }
                BigDecimal totalD = BigDecimal.ZERO;
                for (Depense d : depenseRepository.findAll()) {
                    if (d.getMontant() != null) {
                        totalD = totalD.add(d.getMontant());
                    }
                }
                lignes.add(new String[]{"indicateur", "valeur"});
                lignes.add(new String[]{"Total ventes validees", texte(totalV)});
                lignes.add(new String[]{"Total depenses", texte(totalD)});
                lignes.add(new String[]{"Benefice net", texte(totalV.subtract(totalD))});
                return lignes;
            default:
                return null;
        }
    }

    // Export global : toutes les donnees dans un seul CSV, une section par module.
    @Transactional
    public byte[] exporterCsvGlobal() {
        String[] modules = {"LOTS", "GROUPES", "ANALYSE", "SANITAIRE", "VENTES", "DEPENSES",
                "CLIENTS", "INGREDIENTS", "FINANCIER"};
        StringBuilder sb = new StringBuilder();
        for (String module : modules) {
            List<String[]> lignes = donneesExport(module);
            if (lignes == null) {
                continue;
            }
            sb.append("### ").append(module).append("\n");
            for (String[] ligne : lignes) {
                for (int i = 0; i < ligne.length; i++) {
                    if (i > 0) sb.append(SEP);
                    sb.append(echapper(ligne[i]));
                }
                sb.append("\n");
            }
            sb.append("\n");
        }
        tracer("EXPORT", "EXCEL", "GLOBAL", "export_global.csv", "SUCCES", "Export global reussi.");
        return sb.toString().getBytes(StandardCharsets.UTF_8);
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

    private int entier(String v) {
        if (v == null || v.isBlank()) return 0;
        return Integer.parseInt(v.trim());
    }

    private LocalDate date(String v) {
        return LocalDate.parse(v.trim()); // format attendu : yyyy-MM-dd
    }

    private LocalDate dateOuNull(String v) {
        return (v == null || v.isBlank()) ? null : LocalDate.parse(v.trim());
    }

    private java.util.Optional<Race> trouverRace(String nom) {
        if (nom == null || nom.isBlank()) return java.util.Optional.empty();
        return raceRepository.findAll().stream()
                .filter(r -> nom.trim().equalsIgnoreCase(r.getNom()))
                .findFirst();
    }

    private java.util.Optional<Vaccin> trouverVaccin(String nom) {
        if (nom == null || nom.isBlank()) return java.util.Optional.empty();
        return vaccinRepository.findAll().stream()
                .filter(vac -> nom.trim().equalsIgnoreCase(vac.getNom()))
                .findFirst();
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
