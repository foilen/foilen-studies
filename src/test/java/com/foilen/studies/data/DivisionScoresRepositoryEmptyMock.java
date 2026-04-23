package com.foilen.studies.data;

import com.foilen.studies.data.vocabulary.DivisionScores;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class DivisionScoresRepositoryEmptyMock implements DivisionScoresRepository {
    @Override
    public <S extends DivisionScores> S insert(S entity) {
        return null;
    }

    @Override
    public <S extends DivisionScores> List<S> insert(Iterable<S> entities) {
        return null;
    }

    @Override
    public <S extends DivisionScores> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends DivisionScores> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends DivisionScores> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends DivisionScores> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends DivisionScores> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends DivisionScores> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends DivisionScores, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }

    @Override
    public <S extends DivisionScores> S save(S entity) {
        return null;
    }

    @Override
    public <S extends DivisionScores> List<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<DivisionScores> findById(String s) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(String s) {
        return false;
    }

    @Override
    public List<DivisionScores> findAll() {
        return null;
    }

    @Override
    public List<DivisionScores> findAllById(Iterable<String> strings) {
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
    public void delete(DivisionScores entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends String> strings) {

    }

    @Override
    public void deleteAll(Iterable<? extends DivisionScores> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public List<DivisionScores> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<DivisionScores> findAll(Pageable pageable) {
        return null;
    }
}
