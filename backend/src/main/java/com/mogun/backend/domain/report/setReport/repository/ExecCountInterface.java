package com.mogun.backend.domain.report.setReport.repository;

import com.mogun.backend.domain.exercise.Exercise;

import javax.persistence.Column;

public interface ExecCountInterface {

    int getExec_Key();

    int getExec_Count();
}
