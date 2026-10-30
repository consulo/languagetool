// Maintained by tools/jpms/descriptors.py; see tools/jpms/README.md.
open module org.languagetool.server {
    requires bcrypt;
    requires transitive ch.qos.logback.classic;
    requires transitive ch.qos.logback.core;
    requires com.fasterxml.jackson.annotation;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    requires transitive com.google.common;
    requires com.google.protobuf;
    requires com.nimbusds.jose.jwt;
    requires io.grpc;
    requires transitive io.grpc.stub;
    requires io.opentelemetry.api;
    requires io.opentelemetry.context;
    requires io.opentelemetry.semconv;
    requires java.management;
    requires transitive java.sql;
    requires java.xml;
    requires jdk.httpserver;
    requires lettuce.core;
    requires logback.ecs.encoder;
    requires org.apache.commons.cli;
    requires org.apache.commons.codec;
    requires org.apache.commons.lang3;
    requires org.apache.commons.pool2;
    requires static org.jetbrains.annotations;
    requires transitive org.languagetool.core;
    requires transitive org.languagetool.gui.commons;
    requires transitive org.languagetool.language.all;
    requires org.mariadb.jdbc;
    requires transitive org.mybatis;
    requires org.slf4j;
    requires simpleclient;
    requires simpleclient.guava;
    requires simpleclient.hotspot;
    requires simpleclient.httpserver;

    exports org.languagetool.server;
}
