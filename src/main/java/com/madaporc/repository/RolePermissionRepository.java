package com.madaporc.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.madaporc.model.RolePermission;
import com.madaporc.model.RolePermissionId;

public interface RolePermissionRepository extends JpaRepository<RolePermission, RolePermissionId> {

    List<RolePermission> findByRoleId(Long roleId);

    void deleteByRoleId(Long roleId);
}
