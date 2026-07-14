package com.madaporc.service;

import com.madaporc.model.*;
import com.madaporc.repository.*;
import com.madaporc.dto.ClientDTO;
import com.madaporc.dto.IngredientDTO;
import com.madaporc.dto.LotPorcDTO;
import com.madaporc.dto.VaccinationDTO;
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

    // Services métier : l'import passe par eux pour appliquer les mêmes règles
    // et effets de bord que la création via formulaire (dépenses, mouvements...).
    private final ClientService clientService;
    private final IngredientService ingredientService;
    private final LotPorcService lotPorcService;
    private final VaccinationService vaccinationService;

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
                               AnalyseReproductionLotRepository analyseReproductionLotRepository,
                               ClientService clientService,
                               IngredientService ingredientService,
                               LotPorcService lotPorcService,
                               VaccinationService vaccinationService) {
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
        this.clientService = clientService;
        this.ingredientService = ingredientService;
        this.lotPorcService = lotPorcService;
        this.vaccinationService = vaccinationService;
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
            case "LOTS":          return "sexe;objectif;origine;race;prix_achat;effectif";
            case "VACCINATIONS":  return "code_lot;vaccin;date_vaccination;date_rappel;observation";
            default:              return null;
        }
    }

    private String exempleModule(String module) {
        switch (module) {
            case "CLIENTS":       return "Rakoto Jean;0341234567;Lot II Antananarivo";
            case "INGREDIENTS":   return "Mais;kg;100;20";
            case "LOTS":          return "MALE;ENGRAISSEMENT;ACHAT;Large White;150000;12";
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
            String resultat;
            switch (module) {
                case "CLIENTS":      resultat = importerClients(lignes);       break;
                case "INGREDIENTS":  resultat = importerIngredients(lignes);   break;
                case "LOTS":         resultat = importerLots(lignes);          break;
                case "VACCINATIONS": resultat = importerVaccinations(lignes);  break;
                default:
                    tracer("IMPORT", "EXCEL", module, nom, "ECHEC", "Import non supporte pour ce module.");
                    return "Import non supporte pour le module " + module + ".";
            }
            tracer("IMPORT", "EXCEL", module, nom, "SUCCES", resultat);
            return resultat;
        } catch (Exception e) {
            tracer("IMPORT", "EXCEL", module, nom, "ECHEC", e.getMessage());
            return "Erreur d'import : " + e.getMessage();
        }
    }

    private String importerClients(List<String[]> lignes) {
        int ok = 0;
        List<String> erreurs = new ArrayList<>();
        for (int i = 0; i < lignes.size(); i++) {
            String[] c = lignes.get(i);
            try {
                ClientDTO dto = new ClientDTO();
                dto.setNom(valeur(c, 0));
                dto.setTelephone(valeur(c, 1));
                dto.setAdresse(valeur(c, 2));
                String err = clientService.valider(dto);
                if (err != null) {
                    erreurs.add(ligneErreur(i, err));
                } else {
                    clientService.creer(dto);
                    ok++;
                }
            } catch (Exception e) {
                erreurs.add(ligneErreur(i, messageErreur(e)));
            }
        }
        return rapport(ok, erreurs);
    }

    private String importerIngredients(List<String[]> lignes) {
        int ok = 0;
        List<String> erreurs = new ArrayList<>();
        for (int i = 0; i < lignes.size(); i++) {
            String[] c = lignes.get(i);
            try {
                IngredientDTO dto = new IngredientDTO();
                dto.setNom(valeur(c, 0));
                dto.setUnite(valeur(c, 1));
                dto.setStockActuel(nombre(valeur(c, 2)));
                dto.setSeuilAlerte(nombre(valeur(c, 3)));
                String res = ingredientService.creerIngredient(dto);
                if ("Ingredient created successfully".equals(res)) {
                    ok++;
                } else {
                    erreurs.add(ligneErreur(i, res));
                }
            } catch (Exception e) {
                erreurs.add(ligneErreur(i, messageErreur(e)));
            }
        }
        return rapport(ok, erreurs);
    }

    private String importerLots(List<String[]> lignes) {
        int ok = 0;
        List<String> erreurs = new ArrayList<>();
        for (int i = 0; i < lignes.size(); i++) {
            String[] c = lignes.get(i);
            try {
                LotPorcDTO dto = new LotPorcDTO();
                dto.setSexe(valeur(c, 0));
                dto.setObjectif(valeur(c, 1));
                String origine = valeur(c, 2);
                dto.setOrigine(origine.isBlank() ? "ACHAT" : origine);
                // Race renseignee mais introuvable : on rejette la ligne au lieu de
                // l'ignorer silencieusement (sinon le lot serait cree sans race et compte comme reussi).
                String raceNom = valeur(c, 3);
                if (!raceNom.isBlank()) {
                    var race = trouverRace(raceNom);
                    if (race.isEmpty()) {
                        erreurs.add(ligneErreur(i, "Race inconnue : " + raceNom));
                        continue;
                    }
                    dto.setRaceId(race.get().getId());
                }
                dto.setPrixAchat(nombre(valeur(c, 4)));
                dto.setEffectifInitial(entier(valeur(c, 5)));
                // creerLot génère le code, crée la dépense d'achat, le mouvement
                // initial et la répartition reproductive (comme au formulaire).
                String err = lotPorcService.creerLot(dto);
                if (err == null) {
                    ok++;
                } else {
                    erreurs.add(ligneErreur(i, err));
                }
            } catch (Exception e) {
                erreurs.add(ligneErreur(i, messageErreur(e)));
            }
        }
        return rapport(ok, erreurs);
    }

    private String importerVaccinations(List<String[]> lignes) {
        int ok = 0;
        List<String> erreurs = new ArrayList<>();
        for (int i = 0; i < lignes.size(); i++) {
            String[] c = lignes.get(i);
            try {
                VaccinationDTO dto = new VaccinationDTO();
                lotPorcRepository.findByCodeLot(valeur(c, 0)).ifPresent(lot -> dto.setLotId(lot.getId()));
                trouverVaccin(valeur(c, 1)).ifPresent(v -> dto.setVaccinId(v.getId()));
                dto.setDateVaccination(dateOuNull(valeur(c, 2)));
                dto.setDateRappel(dateOuNull(valeur(c, 3)));
                dto.setObservation(valeur(c, 4));
                String err = vaccinationService.enregistrer(dto);
                if (err == null) {
                    ok++;
                } else {
                    erreurs.add(ligneErreur(i, err));
                }
            } catch (Exception e) {
                erreurs.add(ligneErreur(i, messageErreur(e)));
            }
        }
        return rapport(ok, erreurs);
    }

    // Rapport d'import : lignes créées + détail des lignes ignorées (avec raison).
    private String rapport(int ok, List<String> erreurs) {
        StringBuilder sb = new StringBuilder(ok + " ligne(s) importée(s)");
        if (!erreurs.isEmpty()) {
            sb.append(", ").append(erreurs.size()).append(" ignorée(s) : ");
            sb.append(String.join(" | ", erreurs));
        }
        return sb.append(".").toString();
    }

    // L'en-tête est déjà retiré, donc la ligne i de données = ligne i+2 du fichier.
    private String ligneErreur(int i, String message) {
        return "ligne " + (i + 2) + " : " + message;
    }

    private String messageErreur(Exception e) {
        return e.getMessage() != null ? e.getMessage() : e.getClass().getSimpleName();
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
                html.append("<").append(tag).append(">").append(html(formaterMontant(cell))).append("</").append(tag).append(">");
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
        StringBuilder contenu = new StringBuilder();
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
            int ch;
            while ((ch = br.read()) != -1) {
                contenu.append((char) ch);
            }
        }
        List<String[]> lignes = parserCsv(contenu.toString());
        // On ignore l'en-tete et les lignes vides.
        if (!lignes.isEmpty()) {
            lignes.remove(0);
        }
        lignes.removeIf(c -> c.length == 0 || (c.length == 1 && c[0].isBlank()));
        return lignes;
    }

    // Parseur CSV robuste : gere les champs entre guillemets contenant le separateur,
    // des retours a la ligne, et les guillemets echappes ("" -> ").
    private List<String[]> parserCsv(String contenu) {
        char sep = SEP.charAt(0);
        List<String[]> lignes = new ArrayList<>();
        List<String> champs = new ArrayList<>();
        StringBuilder champ = new StringBuilder();
        boolean dansGuillemets = false;
        int n = contenu.length();
        for (int i = 0; i < n; i++) {
            char ch = contenu.charAt(i);
            if (dansGuillemets) {
                if (ch == '"') {
                    if (i + 1 < n && contenu.charAt(i + 1) == '"') { champ.append('"'); i++; }
                    else { dansGuillemets = false; }
                } else {
                    champ.append(ch);
                }
            } else if (ch == '"') {
                dansGuillemets = true;
            } else if (ch == sep) {
                champs.add(champ.toString()); champ.setLength(0);
            } else if (ch == '\n' || ch == '\r') {
                if (ch == '\r' && i + 1 < n && contenu.charAt(i + 1) == '\n') { i++; }
                champs.add(champ.toString()); champ.setLength(0);
                lignes.add(champs.toArray(new String[0])); champs = new ArrayList<>();
            } else {
                champ.append(ch);
            }
        }
        if (champ.length() > 0 || !champs.isEmpty()) {
            champs.add(champ.toString());
            lignes.add(champs.toArray(new String[0]));
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

    // Uniquement pour le PDF : un nombre decimal (ex. montant "1000000.00") est
    // affiche avec des separateurs de milliers -> "1 000 000". Le reste (dates,
    // codes, entiers, telephones) est laisse tel quel.
    private String formaterMontant(String cell) {
        if (cell != null && cell.matches("-?\\d+\\.\\d+")) {
            try {
                java.text.NumberFormat nf = java.text.NumberFormat.getInstance(java.util.Locale.FRANCE);
                nf.setMinimumFractionDigits(0);
                nf.setMaximumFractionDigits(2);
                return nf.format(new java.math.BigDecimal(cell));
            } catch (NumberFormatException e) {
                return cell;
            }
        }
        return cell;
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
