package com.deopraglabs.egrade.util;

import com.deopraglabs.egrade.model.Student;
import com.deopraglabs.egrade.model.Professor;
import com.deopraglabs.egrade.model.Coordinator;
import com.deopraglabs.egrade.model.Certificate;

import lombok.Getter;

public class DataHolder {

    @Getter
    private static final DataHolder instance = new DataHolder();
    @Getter
    private static Student student;
    @Getter
    private static Professor professor;
    @Getter
    private static Coordinator coordinator;
    @Getter
    private static Coordinator coordinatorEdit;
    @Getter
    private static Certificate certificate;

    public static void setStudent(Student student) {
        DataHolder.student = student;
    }

    public static void setProfessor(Professor professor) {
        DataHolder.professor = professor;
    }

    public static void setCoordinator(Coordinator coordinator) {
        DataHolder.coordinator = coordinator;
    }

    public static void setCoordinatorEdit(Coordinator coordinator) {
        DataHolder.coordinatorEdit = coordinator;
    }

    public static void setCertificate(Certificate certificate) {
        DataHolder.certificate = certificate;
    }
}

