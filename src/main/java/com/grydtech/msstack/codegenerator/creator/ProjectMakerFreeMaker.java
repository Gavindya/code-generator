package com.grydtech.msstack.codegenerator.creator;

import com.google.common.base.CaseFormat;
import com.grydtech.msstack.codegenerator.creator.templatehelpers.ToCamelCase;
import com.grydtech.msstack.codegenerator.creator.templatehelpers.ToHeadlessCamelCase;
import com.grydtech.msstack.codegenerator.creator.templatehelpers.ToKebabCase;
import com.grydtech.msstack.modelconverter.microservice.*;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProjectMakerFreeMaker extends ProjectMaker {
    private final static Configuration cfg;

    static {
        cfg = new Configuration(Configuration.VERSION_2_3_27);
        cfg.setClassForTemplateLoading(ProjectMakerFreeMaker.class, "/code-templates");
    }

    public ProjectMakerFreeMaker(String projectPath, String groupId, String artifactId, String version) {
        super(projectPath, groupId, artifactId, version);
    }

    @Override
    public void createProjectPom() throws IOException, TemplateException {
        Template template = cfg.getTemplate("pom-file-template.ftl");

        Map<String, Object> root = new HashMap<>();
        root.put("groupId", groupId);
        root.put("artifactId", artifactId);
        root.put("version", version);

        createFile(projectPath, "pom.xml", template, root);
    }

    @Override
    public void createEntityClass(EntityClassSchema entityClass) throws IOException, TemplateException {
        String packageName = basePackageName + ".entities";

        List<String> importPackages = new ArrayList<>();

        for (Attribute attribute : entityClass.getAttributes()) {
            if ("array".equals(attribute.getMultiplicity())) {
                importPackages.add("java.util.List");
                break;
            }
        }

        if (!entityClass.getEvents().isEmpty()) {
            importPackages.add(basePackageName + ".events.*");
        }

        String filePath = sourcePath + File.separator + packageName.replace(".", File.separator);
        String fileName = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, entityClass.getName()) + ".java";
        Template template = cfg.getTemplate("entity-class-template.ftl");

        Map<String, Object> root = new HashMap<>();
        root.put("packageName", packageName);
        root.put("importPackages", importPackages);
        root.put("className", entityClass.getName());
        root.put("attributes", entityClass.getAttributes());
        root.put("events", entityClass.getEvents());

        createFile(filePath, fileName, template, root);
    }

    @Override
    public void createEventClass(EventClassSchema eventClass) throws IOException, TemplateException {
        String packageName = basePackageName + ".events";

        List<String> importPackages = new ArrayList<>();

        String filePath = sourcePath + File.separator + packageName.replace(".", File.separator);
        String fileName = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, eventClass.getName()) + ".java";

        Template template = cfg.getTemplate("event-class-template.ftl");

        Map<String, Object> root = new HashMap<>();
        root.put("packageName", packageName);
        root.put("importPackages", importPackages);
        root.put("className", eventClass.getName());
        root.put("attributes", eventClass.getAttributes());

        createFile(filePath, fileName, template, root);
    }

    @Override
    public void createHandlerClass(HandlerClassSchema handlerClass) throws IOException, TemplateException {
        String packageName = basePackageName + ".handlers";

        List<String> importPackages = new ArrayList<>();

        importPackages.add(basePackageName + ".events.*");
        importPackages.add(basePackageName + ".requests.*");
        importPackages.add(basePackageName + ".responses.*");

        String filePath = sourcePath + File.separator + packageName.replace(".", File.separator);
        String fileName = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, handlerClass.getName()) + ".java";

        Template template = cfg.getTemplate("handler-class-template.ftl");

        Map<String, Object> root = new HashMap<>();
        root.put("packageName", packageName);
        root.put("importPackages", importPackages);
        root.put("className", handlerClass.getName());
        root.put("request", handlerClass.getConsume());
        root.put("response", handlerClass.getProduce());
        root.put("events", handlerClass.getEvents());

        createFile(filePath, fileName, template, root);
    }

    @Override
    public void createRequestClass(RequestClassSchema requestClass) throws IOException, TemplateException {
        String packageName = basePackageName + ".requests";

        List<String> importPackages = new ArrayList<>();

        String filePath = sourcePath + File.separator + packageName.replace(".", File.separator);
        String fileName = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, requestClass.getName()) + ".java";

        Template template = cfg.getTemplate("request-class-template.ftl");

        Map<String, Object> root = new HashMap<>();
        root.put("packageName", packageName);
        root.put("importPackages", importPackages);
        root.put("className", requestClass.getName());

        createFile(filePath, fileName, template, root);
    }

    @Override
    public void createResponseClass(ResponseClassSchema responseClass) throws IOException, TemplateException {
        String packageName = basePackageName + ".responses";

        List<String> importPackages = new ArrayList<>();

        String filePath = sourcePath + File.separator + packageName.replace(".", File.separator);
        String fileName = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, responseClass.getName()) + ".java";

        Template template = cfg.getTemplate("response-class-template.ftl");

        Map<String, Object> root = new HashMap<>();
        root.put("packageName", packageName);
        root.put("importPackages", importPackages);
        root.put("className", responseClass.getName());

        createFile(filePath, fileName, template, root);
    }

    private static void createFile(String path, String fileName, Template template, Map<String, Object> root) throws IOException, TemplateException {
        File filePath = new File(path);
        filePath.mkdirs();

        root.put("toCamel", new ToCamelCase());
        root.put("toHeadlessCamel", new ToHeadlessCamelCase());
        root.put("toKebab", new ToKebabCase());

        Writer fileWriter = new java.io.FileWriter(new File(path + File.separator + fileName));
        template.process(root, fileWriter);
    }
}
