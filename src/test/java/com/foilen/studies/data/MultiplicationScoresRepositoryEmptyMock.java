package com.foilen.studies.data;

import com.foilen.studies.data.vocabulary.MultiplicationScores;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class MultiplicationScoresRepositoryEmptyMock implements MultiplicationScoresRepository {
    @Override
    public <S extends MultiplicationScores> S insert(S entity) {
        return null;
    }

    @Override
    public <S extends MultiplicationScores> List<S> insert(Iterable<S> entities) {
        return null;
    }

    @Override
    public <S extends MultiplicationScores> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends MultiplicationScores> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends MultiplicationScores> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends MultiplicationScores> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends MultiplicationScores> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends MultiplicationScores> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends MultiplicationScores, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }

    @Override
    public <S extends MultiplicationScores> S save(S entity) {
        return null;
    }

    @Override
    public <S extends MultiplicationScores> List<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<MultiplicationScores> findById(String s) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(String s) {
        return false;
    }

    @Override
    public List<MultiplicationScores> findAll() {
        return null;
    }

    @Override
    public List<MultiplicationScores> findAllById(Iterable<String> strings) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(String s) {

    }

    @Override
    public void delete(MultiplicationScores entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends String> strings) {

    }

    @Override
    public void deleteAll(Iterable<? extends MultiplicationScores> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public List<MultiplicationScores> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<MultiplicationScores> findAll(Pageable pageable) {
        return null;
    }
}
