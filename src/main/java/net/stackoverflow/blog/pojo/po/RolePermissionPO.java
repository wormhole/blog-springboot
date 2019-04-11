package net.stackoverflow.blog.pojo.po;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RolePermissionPO implements Serializable {

    private String id;
    private String roleId;
    private String permissionId;

}
