package com.deopraglabs.egrade.util;

import com.deopraglabs.egrade.model.Certificate;
import com.deopraglabs.egrade.model.Coordinator;
import com.deopraglabs.egrade.model.Course;
import com.deopraglabs.egrade.model.Professor;
import com.deopraglabs.egrade.model.Student;
import com.deopraglabs.egrade.model.Grade;
import com.deopraglabs.egrade.model.Subject;

import lombok.Getter;
import lombok.Setter;

public class DataHolder {

    @Getter
    private static final DataHolder instance = new DataHolder();
    @Getter
    @Setter
    private static Student student;
    @Getter
    @Setter
    private static Course course;
    @Getter
    @Setter
    private static Grade grade;
    @Getter
    @Setter
    private static Subject subject;
    @Getter
    @Setter
    private static Professor professor;
    @Getter
    @Setter
    private static Coordinator coordinator;
    @Getter
    @Setter
    private static Coordinator coordinatorEdit;
    @Getter
    @Setter
    private static Certificate certificate;
}

