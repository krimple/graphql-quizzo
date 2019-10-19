package com.chariot.quizzographql.repository;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface QuizDataRepository extends CrudRepository<QuizEntity, Long> {
}
