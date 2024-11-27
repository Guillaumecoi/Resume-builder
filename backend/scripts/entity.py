class Entity:
    def __init__(self, name, parent=None):
        if not name[0].isupper():
            raise ValueError("Entity name must start with uppercase letter")
        self.name = name
        self.fields = []
        self.package = name.lower()
        self.parent = parent

    def add_field(self, name, type_str):
        self.fields.append((name, type_str))

    def _generate_fields(self):
        return "\n".join(f"    private {type_} {name};" for name, type_ in self.fields)

    def get_entity_file_content(self):
        fields_content = self._generate_fields()
        parent_import = f"import com.coigniez.resumebuilder.model.{self.parent.lower()}.{self.parent};" if self.parent else ""
        relation_imports = """
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.FetchType;
""" if self.parent else ""
        
        parent_relation = f"""
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "{self.parent.lower()}_id", referencedColumnName = "id")
    private {self.parent} {self.parent.lower()};
""" if self.parent else ""

        template = f"""package com.coigniez.resumebuilder.model.{self.package};

import com.coigniez.resumebuilder.model.common.BaseEntity;
{parent_import}

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;{relation_imports}
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "{self.package}")
public class {self.name} implements BaseEntity {{

    @Id
    @GeneratedValue
    private Long id;

{fields_content}
{parent_relation}
}}"""
        return template
    

    def get_request_content(self):
        fields = ",\n    ".join(f"{type_} {field}" for field, type_ in self.fields)
        return f"""package com.coigniez.resumebuilder.model.{self.package};

public record {self.name}Request(
    {fields}
) {{}}"""

    def get_response_content(self):
        fields = "\n".join(f"    private {type_} {field};" for field, type_ in self.fields)
        return f"""package com.coigniez.resumebuilder.model.{self.package};

import lombok.Builder;
import lombok.Data;

@Data
@Builder 
public class {self.name}Response {{
    private Long id;
{fields}
}}"""

    def get_mapper_content(self):
        to_entity = "\n                ".join(f".{field}(request.{field}())" for field, type_ in self.fields)
        to_response = "\n                ".join(f".{field}(entity.get{field[0].upper() + field[1:]}())" for field, type_ in self.fields)
        
        return f"""package com.coigniez.resumebuilder.model.{self.package};

import org.springframework.stereotype.Service;
import com.coigniez.resumebuilder.interfaces.Mapper;

@Service
public class {self.name}Mapper implements Mapper<{self.name}, {self.name}Request, {self.name}Response> {{

    @Override
    public {self.name} toEntity({self.name}Request request) {{
        if (request == null) {{
            return null;
        }}

        return {self.name}.builder()
                {to_entity}
                .build();
    }}

    @Override
    public {self.name}Response toDto({self.name} entity) {{
        if (entity == null) {{
            return null;
        }}

        return {self.name}Response.builder()
                .id(entity.getId())
                {to_response}
                .build();
    }}
}}"""

    def get_repository_content(self):
        return f"""package com.coigniez.resumebuilder.model.{self.package};

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface {self.name}Repository extends JpaRepository<{self.name}, Long> {{
    
}}"""

    def get_service_content(self):
        return f"""package com.coigniez.resumebuilder.model.{self.package};

import org.springframework.beans.BeanUtils;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.coigniez.resumebuilder.interfaces.CrudService;
import com.coigniez.resumebuilder.model.{self.parent.lower()}.{self.parent};
import com.coigniez.resumebuilder.model.{self.parent.lower()}.{self.parent}Repository;
import com.coigniez.resumebuilder.model.{self.parent.lower()}.{self.parent}Service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class {self.name}Service implements CrudService<{self.name}Response, {self.name}Request> {{

    private final {self.name}Repository {self.package}Repository;
    private final {self.name}Mapper {self.package}Mapper;
    private final {self.parent}Service {self.parent.lower()}Service;
    private final {self.parent}Repository {self.parent.lower()}Repository;

    public Long create(Long parentId, {self.name}Request request, Authentication user) {{
        {self.parent.lower()}Service.hasAccess(parentId, user);
        {self.name} {self.package} = {self.package}Mapper.toEntity(request);
        {self.parent} {self.parent.lower()} = {self.parent.lower()}Repository.findById(parentId).orElseThrow();
        {self.package}.set{self.parent}({self.parent.lower()});
        return {self.package}Repository.save({self.package}).getId();
    }}

    public {self.name}Response get(Long id, Authentication user) {{
        hasAccess(id, user);
        {self.name} {self.package} = {self.package}Repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(""));
        return {self.package}Mapper.toDto({self.package});
    }}

    public void update(Long id, {self.name}Request request, Authentication user) {{
        hasAccess(id, user);
        {self.name} existing{self.name} = {self.package}Repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(""));
        {self.name} updated{self.name} = {self.package}Mapper.toEntity(request);
        BeanUtils.copyProperties(updated{self.name}, existing{self.name}, "id", "{self.parent.lower()}");
        {self.package}Repository.save(existing{self.name});
    }}

    public void delete(Long id, Authentication user) {{
        hasAccess(id, user);
        {self.package}Repository.deleteById(id);
    }}

    public void hasAccess(Long id, Authentication user) {{
        //TODO: Implement access control
    }}
}}"""
    
    def get_controller_content(self):
        return f"""package com.coigniez.resumebuilder.model.{self.package};

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Slf4j
@RestController
@RequestMapping("resumes/{{resumeId}}/{self.package}s")
@RequiredArgsConstructor
@Tag(name = "Resume {self.name}s")
public class {self.name}Controller {{

    private final {self.name}Service {self.package}Service;

    @PostMapping
    public ResponseEntity<Long> create(@PathVariable Long resumeId, @Valid @RequestBody {self.name}Request request, Authentication user) {{
        Long id = {self.package}Service.create(resumeId, request, user);
        URI location = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{{id}}")
            .buildAndExpand(id)
            .toUri();
        return ResponseEntity.created(location).body(id);
    }}

    @GetMapping("/{{id}}")
    public ResponseEntity<{self.name}Response> get{self.name}(@PathVariable Long resumeId, @PathVariable Long id, Authentication user) {{
        {self.name}Response {self.package} = {self.package}Service.get(id, user);
        return ResponseEntity.ok({self.package});
    }}

    @PostMapping("/{{id}}")
    public ResponseEntity<Void> update(@PathVariable Long resumeId, @PathVariable Long id, @Valid @RequestBody {self.name}Request request, Authentication user) {{
        {self.package}Service.update(id, request, user);
        return ResponseEntity.ok().build();
    }}

    @PostMapping("/{{id}}/delete") 
    public ResponseEntity<Void> delete(@PathVariable Long resumeId, @PathVariable Long id, Authentication user) {{
        {self.package}Service.delete(id, user);
        return ResponseEntity.noContent().build();
    }}
}}"""
    
    def save(self):
        """Save all entity files to the specified base path."""
        from pathlib import Path
        
        # Get script directory and build relative path
        script_dir = Path(__file__).parent.resolve()
        package_path = script_dir.parent / "src" / "main" / "java" / "com" / "coigniez" / "resumebuilder" / "model" / self.package
        package_path.mkdir(parents=True, exist_ok=True)
    
        files_to_create = {
            f"{self.name}.java": self.get_entity_file_content(),
            f"{self.name}Request.java": self.get_request_content(),
            f"{self.name}Response.java": self.get_response_content(),
            f"{self.name}Mapper.java": self.get_mapper_content(),
            f"{self.name}Repository.java": self.get_repository_content(),
            f"{self.name}Service.java": self.get_service_content(),
            f"{self.name}Controller.java": self.get_controller_content()
        }
        
        created_files = []
        
        created_files = []
        try:
            for filename, content in files_to_create.items():
                file_path = package_path / filename
                file_path.write_text(content)
                created_files.append(str(file_path))
                print(f"Created: {file_path}")
            return created_files
                
        except Exception as e:
            print(f"Error creating files: {e}")
            raise
        
