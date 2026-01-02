package com.rdbac.rdbac.Helpers;

import java.util.ArrayList;
import java.util.List;

public class DefaultRoles {
    public final List<String> CustomRoles = new ArrayList<>(List.of("ADMIN","VIEWER","EDITOR"));
    public final List<String> CustomPermissions =  new ArrayList<>(List.of("READ","WRITE","DELETE"));
}
