package org.example.Lab4;

import org.apache.catalina.Context;
import org.apache.catalina.Wrapper;
import org.apache.catalina.startup.Tomcat;

import java.io.File;

public class lab4Main {

    private final static int PORT = 8080;
    private final static String WEB_PATH = "src/main/webapp";
    private final static String SEARCH_URL = "/search";

    public static void main(String[] args) throws Exception {
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(PORT);

        String webappDir = new File(WEB_PATH).getAbsolutePath();
        Context ctx = tomcat.addWebapp("", webappDir);
        ctx.setAddWebinfClassesResources(true);
        ctx.addWelcomeFile("index.html");

        Wrapper wrapper = ctx.createWrapper();
        SearchServlet searchServlet = new SearchServlet();
        wrapper.setName(searchServlet.getClass().getSimpleName());
        wrapper.setServletClass(searchServlet.getClass().getName());
        wrapper.setLoadOnStartup(1);

        ctx.addChild(wrapper);

        ctx.addServletMappingDecoded(SEARCH_URL, searchServlet.getClass().getSimpleName());

        tomcat.getConnector();
        tomcat.start();
        System.out.println("Embedded Tomcat started on port " + PORT);
        tomcat.getServer().await();
    }
}
