package com.coigniez.resumebuilder.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import com.coigniez.resumebuilder.domain.column.ColumnRequest;
import com.coigniez.resumebuilder.domain.column.ColumnResponse;
import com.coigniez.resumebuilder.interfaces.CrudController;

import jakarta.validation.Valid;

public class ColumnController implements CrudController<ColumnRequest, ColumnResponse> {

    @Override
    public ResponseEntity<Long> create(@Valid ColumnRequest request, Authentication user) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'create'");
    }

    @Override
    public ResponseEntity<ColumnResponse> get(Long id, Authentication user) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'get'");
    }

    @Override
    public ResponseEntity<Void> update(Long id, @Valid ColumnRequest request, Authentication user) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    @Override
    public ResponseEntity<Void> delete(Long id, Authentication user) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }

    
}
