package grails.plugin.scaffolding.registry.input

import grails.plugin.scaffolding.model.property.DomainProperty
import grails.plugin.scaffolding.registry.DomainInputRenderer
import grails.util.GrailsNameUtils
import grails.web.mapping.LinkGenerator
import org.grails.datastore.mapping.model.PersistentProperty
import org.grails.datastore.mapping.model.types.ToMany


class BidirectionalToManyInputRenderer implements DomainInputRenderer {

    protected LinkGenerator linkGenerator

    BidirectionalToManyInputRenderer(LinkGenerator linkGenerator) {
        this.linkGenerator = linkGenerator
    }

    @Override
    boolean supports(DomainProperty property) {
        PersistentProperty persistentProperty = property.persistentProperty
        persistentProperty instanceof ToMany && persistentProperty.bidirectional
    }

    protected String getPropertyName(DomainProperty property) {
        GrailsNameUtils.getPropertyName(property.rootBeanType)
    }

    protected String getAssociatedClassName(DomainProperty property) {
        property.associatedType.simpleName
    }

    @Override
    Closure renderInput(Map defaultAttributes, DomainProperty property) {
        final String objectName = "${GrailsNameUtils.getPropertyName(property.rootBeanType)}.id"
        defaultAttributes.remove('required')
        defaultAttributes.remove('readonly')
        defaultAttributes.href = linkGenerator.link(resource: property.associatedType, action: "create", params: [(objectName): ""])
        return { ->
            a("Add ${getAssociatedClassName(property)}", defaultAttributes)
        }
    }
}