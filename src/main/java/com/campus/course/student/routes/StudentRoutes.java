package com.campus.course.student.routes;

import com.campus.course.base.routes.BaseRoutes;

public class StudentRoutes {
    private final static String ROOT = BaseRoutes.API + "/student";

    public final static String REGISTRATION = BaseRoutes.NOT_SECURED + "/api/v1/registration";
    public final static String EDIT = ROOT;
    public final static String BY_ID = ROOT + "/{id}";
    public final static String SEARCH = ROOT;
}
