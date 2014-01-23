package org.motechproject.mds.domain;

import org.motechproject.mds.dto.EntityDto;
import org.motechproject.mds.dto.LookupDto;

import javax.jdo.annotations.Discriminator;
import javax.jdo.annotations.DiscriminatorStrategy;
import javax.jdo.annotations.Element;
import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import javax.jdo.annotations.Version;
import javax.jdo.annotations.VersionStrategy;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import static org.apache.commons.lang.StringUtils.isNotBlank;

/**
 * The <code>EntityMapping</code> class contains basic information about an entity. This class is
 * related with table in database with the same name.
 */
@PersistenceCapable(identityType = IdentityType.DATASTORE, detachable = "true")
@Discriminator(strategy = DiscriminatorStrategy.CLASS_NAME)
@Version(strategy = VersionStrategy.VERSION_NUMBER, column = "VERSION",
        extensions = {@Extension(vendorName = "datanucleus", key = "field-name", value = "entityVersion")})
public class EntityMapping {

    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.INCREMENT)
    private Long id;

    @Persistent
    private String className;

    @Persistent
    private String module;

    @Persistent
    private String namespace;

    @Persistent(mappedBy = "entity")
    @Element(dependent = "true")
    private List<LookupMapping> lookups;

    @Persistent(mappedBy = "entity")
    @Element(dependent = "true")
    private List<FieldMapping> fields;

    @Persistent(mappedBy = "parentEntity")
    //@Join(deleteAction = ForeignKeyAction.CASCADE)
    @Element(dependent = "true")
    private List<EntityDraft> drafts;

    private Long entityVersion;

    public EntityMapping() {
        this(null);
    }

    public EntityMapping(String className) {
        this(className, null, null);
    }

    public EntityMapping(String className, String module, String namespace) {
        this.className = className;
        this.module = module;
        this.namespace = namespace;
    }

    public EntityDto toDto() {
        String simpleName = className.substring(className.lastIndexOf('.') + 1);

        return new EntityDto(id, simpleName, module, namespace);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public List<LookupDto> getLookupsDtos() {
        List<LookupDto> dtos = new ArrayList<>();

        for (LookupMapping mapping : lookups) {
            dtos.add(mapping.toDto());
        }
        return dtos;
    }

    public List<LookupMapping> getLookups() {
        return lookups;
    }

    public void setLookups(List<LookupMapping> lookups) {
        this.lookups = lookups;
    }

    public List<EntityDraft> getDrafts() {
        if (drafts == null) {
            drafts = new ArrayList<>();
        }
        return drafts;
    }

    public void setDrafts(List<EntityDraft> drafts) {
        this.drafts = drafts;
    }

    public Long getEntityVersion() {
        return entityVersion;
    }

    public void setEntityVersion(Long entityVersion) {
        this.entityVersion = entityVersion;
    }

    @NotPersistent
    public boolean isReadOnly() {
        return isNotBlank(module) || isNotBlank(namespace);
    }

    public List<FieldMapping> getFields() {
        if (fields == null) {
            fields = new ArrayList<>();
        }
        return fields;
    }

    public void setFields(List<FieldMapping> fields) {
        this.fields = fields;
    }

    public FieldMapping getField(Long id) {
        for (FieldMapping field : this.getFields()) {
            if (field.getId().equals(id)) {
                return field;
            }
        }
        return null;
    }

    public void removeField(Long fieldId) {
        for (Iterator<FieldMapping> it = getFields().iterator(); it.hasNext(); ) {
            FieldMapping field = it.next();
            if (Objects.equals(field.getId(), fieldId)) {
                it.remove();
                break;
            }
        }
    }

    public void addField(FieldMapping field) {
        getFields().add(field);
    }

    public void updateFromDraft(EntityDraft draft) {
        setFields(draft.getFields());
    }

    @NotPersistent
    public boolean isDraft() {
        return false;
    }
}