package com.madaporc.DTO;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO conforme au PDF - placeholder à compléter si besoin.
 */
@Getter
@Setter
public class RolePermissionDTO {
    private Long roleId;
    private List<Long> permissionIds;
}
