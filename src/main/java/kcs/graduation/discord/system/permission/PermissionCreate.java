package kcs.graduation.discord.system.permission;

import org.javacord.api.entity.permission.PermissionType;
import org.javacord.api.entity.permission.PermissionsBuilder;

public class PermissionCreate {
    public PermissionsBuilder createHidePermission(boolean hide_by) {
        PermissionsBuilder permissionsBuilder = new PermissionsBuilder();
        if (hide_by) {
            permissionsBuilder.setDenied(PermissionType.VIEW_CHANNEL);
        } else {
            permissionsBuilder.setAllowed(PermissionType.VIEW_CHANNEL);
        }
        return permissionsBuilder;
    }
}
