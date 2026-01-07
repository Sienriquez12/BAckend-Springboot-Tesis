package com.especlub.match.shared.utils;

public class RolePermissions {
    public static final String ADMIN_GENERAL =
            "hasAnyAuthority(" +
                    "'ROLE_SUPERADMIN'," +
                    "'ROLE_ADMIN'," +
                    "'ROLE_PSYCHOLOGIST'," +
                    "'ROLE_TEACHER'," +
                    "'ROLE_PRESIDENT'" +
                    ")";

    public static final String ADMIN_CLUBS =
            "hasAnyAuthority(" +
                    "'ROLE_SUPERADMIN'," +
                    "'ROLE_ADMIN'," +
                    "'ROLE_PSYCHOLOGIST'," +
                    ")";


}