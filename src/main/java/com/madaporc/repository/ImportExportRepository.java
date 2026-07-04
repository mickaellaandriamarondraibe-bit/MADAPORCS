package com.madaporc.repository;

import com.madaporc.model.ImportExport;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ImportExportRepository extends JpaRepository<ImportExport, Long> {

    List<ImportExport> findAllByOrderByDateOperationDesc();

    List<ImportExport> findByModuleOrderByDateOperationDesc(String module);

    List<ImportExport> findByStatutOrderByDateOperationDesc(String statut);
}
