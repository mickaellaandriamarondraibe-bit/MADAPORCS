package com.madaporc.repository;

    import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
    import com.madaporc.model.Permission;

    public interface PermissionRepository extends JpaRepository<Permission, Long> {

        Optional<Permission> findByCode(String code);

    List<Permission> findByModule(String module);
    }
