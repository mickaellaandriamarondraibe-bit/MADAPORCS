package com.madaporc.repository;

    import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
    import com.madaporc.model.RolePermission;

    public interface RolePermissionRepository extends JpaRepository<RolePermission, Long> {

        List<RolePermission> findByRoleId(Long roleId);

    void deleteByRoleId(Long roleId);
    }
