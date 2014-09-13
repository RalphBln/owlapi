package org.obolibrary.macro;

import static org.semanticweb.owlapi.model.parameters.Imports.*;
import static org.semanticweb.owlapi.search.Searcher.annotations;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.obolibrary.obo2owl.Obo2OWLConstants.Obo2OWLVocabulary;
import org.semanticweb.owlapi.manchestersyntax.renderer.ParserException;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLAnnotationPropertyDomainAxiom;
import org.semanticweb.owlapi.model.OWLAnnotationPropertyRangeAxiom;
import org.semanticweb.owlapi.model.OWLAnnotationValue;
import org.semanticweb.owlapi.model.OWLAsymmetricObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLAxiomVisitorEx;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLClassExpressionVisitorEx;
import org.semanticweb.owlapi.model.OWLDataAllValuesFrom;
import org.semanticweb.owlapi.model.OWLDataExactCardinality;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataHasValue;
import org.semanticweb.owlapi.model.OWLDataMaxCardinality;
import org.semanticweb.owlapi.model.OWLDataMinCardinality;
import org.semanticweb.owlapi.model.OWLDataPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDataPropertyDomainAxiom;
import org.semanticweb.owlapi.model.OWLDataPropertyRangeAxiom;
import org.semanticweb.owlapi.model.OWLDataRange;
import org.semanticweb.owlapi.model.OWLDataSomeValuesFrom;
import org.semanticweb.owlapi.model.OWLDataVisitorEx;
import org.semanticweb.owlapi.model.OWLDatatypeDefinitionAxiom;
import org.semanticweb.owlapi.model.OWLDeclarationAxiom;
import org.semanticweb.owlapi.model.OWLDifferentIndividualsAxiom;
import org.semanticweb.owlapi.model.OWLDisjointClassesAxiom;
import org.semanticweb.owlapi.model.OWLDisjointDataPropertiesAxiom;
import org.semanticweb.owlapi.model.OWLDisjointObjectPropertiesAxiom;
import org.semanticweb.owlapi.model.OWLDisjointUnionAxiom;
import org.semanticweb.owlapi.model.OWLEquivalentClassesAxiom;
import org.semanticweb.owlapi.model.OWLEquivalentDataPropertiesAxiom;
import org.semanticweb.owlapi.model.OWLEquivalentObjectPropertiesAxiom;
import org.semanticweb.owlapi.model.OWLFunctionalDataPropertyAxiom;
import org.semanticweb.owlapi.model.OWLFunctionalObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLHasKeyAxiom;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLInverseFunctionalObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLInverseObjectPropertiesAxiom;
import org.semanticweb.owlapi.model.OWLIrreflexiveObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLNamedObject;
import org.semanticweb.owlapi.model.OWLNegativeDataPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLNegativeObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLObjectAllValuesFrom;
import org.semanticweb.owlapi.model.OWLObjectComplementOf;
import org.semanticweb.owlapi.model.OWLObjectExactCardinality;
import org.semanticweb.owlapi.model.OWLObjectHasSelf;
import org.semanticweb.owlapi.model.OWLObjectHasValue;
import org.semanticweb.owlapi.model.OWLObjectIntersectionOf;
import org.semanticweb.owlapi.model.OWLObjectMaxCardinality;
import org.semanticweb.owlapi.model.OWLObjectMinCardinality;
import org.semanticweb.owlapi.model.OWLObjectOneOf;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLObjectPropertyDomainAxiom;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLObjectPropertyRangeAxiom;
import org.semanticweb.owlapi.model.OWLObjectSomeValuesFrom;
import org.semanticweb.owlapi.model.OWLObjectUnionOf;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLReflexiveObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLSameIndividualAxiom;
import org.semanticweb.owlapi.model.OWLSubAnnotationPropertyOfAxiom;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
import org.semanticweb.owlapi.model.OWLSubDataPropertyOfAxiom;
import org.semanticweb.owlapi.model.OWLSubObjectPropertyOfAxiom;
import org.semanticweb.owlapi.model.OWLSubPropertyChainOfAxiom;
import org.semanticweb.owlapi.model.OWLSymmetricObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLTransitiveObjectPropertyAxiom;
import org.semanticweb.owlapi.model.SWRLRule;
import org.semanticweb.owlapi.search.Filters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Empty abstract visitor for macro expansion. This class allows to minimize the
 * code in the actual visitors, as they only need to overwrite the relevant
 * methods.
 */
public abstract class AbstractMacroExpansionVisitor implements
        OWLAxiomVisitorEx<OWLAxiom> {

    /**
     * class expression visitor
     */
    public abstract class AbstractClassExpressionVisitorEx implements
            OWLClassExpressionVisitorEx<OWLClassExpression> {

        protected AbstractClassExpressionVisitorEx() {}

        @Override
        public OWLClassExpression visit(@Nonnull OWLObjectIntersectionOf ce) {
            Set<OWLClassExpression> ops = new HashSet<>();
            for (OWLClassExpression op : ce.getOperands()) {
                ops.add(op.accept(this));
            }
            return dataFactory.getOWLObjectIntersectionOf(ops);
        }

        @Override
        public OWLClassExpression visit(@Nonnull OWLObjectUnionOf ce) {
            Set<OWLClassExpression> ops = new HashSet<>();
            for (OWLClassExpression op : ce.getOperands()) {
                ops.add(op.accept(this));
            }
            return dataFactory.getOWLObjectUnionOf(ops);
        }

        @Override
        public OWLClassExpression visit(@Nonnull OWLObjectComplementOf ce) {
            return dataFactory.getOWLObjectComplementOf(ce.getOperand().accept(
                    this));
        }

        @Nonnull
        @Override
        public OWLClassExpression visit(@Nonnull OWLObjectSomeValuesFrom ce) {
            OWLClassExpression filler = ce.getFiller();
            OWLObjectPropertyExpression p = ce.getProperty();
            OWLClassExpression result = null;
            if (p instanceof OWLObjectProperty) {
                result = expandOWLObjSomeVal(filler, p);
            }
            if (result == null) {
                result = dataFactory.getOWLObjectSomeValuesFrom(
                        ce.getProperty(), filler.accept(this));
            }
            return result;
        }

        @Nullable
        protected abstract OWLClassExpression expandOWLObjSomeVal(
                @Nonnull OWLClassExpression filler,
                @Nonnull OWLObjectPropertyExpression p);

        @Nonnull
        @Override
        public OWLClassExpression visit(@Nonnull OWLObjectHasValue ce) {
            OWLClassExpression result = null;
            OWLIndividual filler = ce.getFiller();
            OWLObjectPropertyExpression p = ce.getProperty();
            if (p instanceof OWLObjectProperty) {
                result = expandOWLObjHasVal(ce, filler, p);
            }
            if (result == null) {
                result = dataFactory.getOWLObjectHasValue(ce.getProperty(),
                        filler);
            }
            return result;
        }

        @Nullable
        protected abstract OWLClassExpression expandOWLObjHasVal(
                @Nonnull OWLObjectHasValue desc, @Nonnull OWLIndividual filler,
                @Nonnull OWLObjectPropertyExpression p);

        @Override
        public OWLClassExpression visit(@Nonnull OWLObjectAllValuesFrom ce) {
            return ce.getFiller().accept(this);
        }

        @Override
        public OWLClassExpression visit(@Nonnull OWLObjectMinCardinality ce) {
            OWLClassExpression filler = ce.getFiller().accept(this);
            return dataFactory.getOWLObjectMinCardinality(ce.getCardinality(),
                    ce.getProperty(), filler);
        }

        @Override
        public OWLClassExpression visit(@Nonnull OWLObjectExactCardinality ce) {
            return ce.asIntersectionOfMinMax().accept(this);
        }

        @Override
        public OWLClassExpression visit(@Nonnull OWLObjectMaxCardinality ce) {
            OWLClassExpression filler = ce.getFiller().accept(this);
            return dataFactory.getOWLObjectMaxCardinality(ce.getCardinality(),
                    ce.getProperty(), filler);
        }

        @Override
        public OWLClassExpression visit(@Nonnull OWLDataSomeValuesFrom ce) {
            OWLDataRange filler = ce.getFiller().accept(rangeVisitor);
            return dataFactory.getOWLDataSomeValuesFrom(ce.getProperty(),
                    filler);
        }

        @Override
        public OWLClassExpression visit(@Nonnull OWLDataAllValuesFrom ce) {
            OWLDataRange filler = ce.getFiller().accept(rangeVisitor);
            return dataFactory
                    .getOWLDataAllValuesFrom(ce.getProperty(), filler);
        }

        @Override
        public OWLClassExpression visit(@Nonnull OWLDataHasValue ce) {
            return ce.asSomeValuesFrom().accept(this);
        }

        @Override
        public OWLClassExpression visit(@Nonnull OWLDataExactCardinality ce) {
            return ce.asIntersectionOfMinMax().accept(this);
        }

        @Override
        public OWLClassExpression visit(@Nonnull OWLDataMaxCardinality ce) {
            int card = ce.getCardinality();
            OWLDataRange filler = ce.getFiller().accept(rangeVisitor);
            return dataFactory.getOWLDataMaxCardinality(card, ce.getProperty(),
                    filler);
        }

        @Override
        public OWLClassExpression visit(@Nonnull OWLDataMinCardinality ce) {
            int card = ce.getCardinality();
            OWLDataRange filler = ce.getFiller().accept(rangeVisitor);
            return dataFactory.getOWLDataMinCardinality(card, ce.getProperty(),
                    filler);
        }

        @Override
        public OWLClassExpression visit(OWLClass ce) {
            return ce;
        }

        @Override
        public OWLClassExpression visit(OWLObjectHasSelf ce) {
            return ce;
        }

        @Override
        public OWLClassExpression visit(OWLObjectOneOf ce) {
            return ce;
        }
    }

    static final Logger LOG = LoggerFactory
            .getLogger(AbstractMacroExpansionVisitor.class);
    final OWLDataFactory dataFactory;
    @Nonnull
    final Map<IRI, String> expandAssertionToMap;
    @Nonnull
    protected final Map<IRI, String> expandExpressionMap;
    protected OWLDataVisitorEx<OWLDataRange> rangeVisitor;
    protected OWLClassExpressionVisitorEx<OWLClassExpression> classVisitor;
    protected ManchesterSyntaxTool manchesterSyntaxTool;

    /**
     * @param input
     *        ontology
     */
    public void rebuild(OWLOntology input) {
        manchesterSyntaxTool = new ManchesterSyntaxTool(input);
    }

    /**
     * @return Manchester syntax tool
     */
    public ManchesterSyntaxTool getTool() {
        return manchesterSyntaxTool;
    }

    protected AbstractMacroExpansionVisitor(@Nonnull OWLOntology inputOntology) {
        dataFactory = inputOntology.getOWLOntologyManager().getOWLDataFactory();
        expandExpressionMap = new HashMap<>();
        expandAssertionToMap = new HashMap<>();
        OWLAnnotationProperty expandExpressionAP = dataFactory
                .getOWLAnnotationProperty(Obo2OWLVocabulary.IRI_IAO_0000424
                        .getIRI());
        OWLAnnotationProperty expandAssertionAP = dataFactory
                .getOWLAnnotationProperty(Obo2OWLVocabulary.IRI_IAO_0000425
                        .getIRI());
        for (OWLObjectProperty p : inputOntology
                .getObjectPropertiesInSignature()) {
            for (OWLAnnotation a : annotations(inputOntology.filterAxioms(
                    Filters.annotations, p.getIRI(), INCLUDED),
                    expandExpressionAP)) {
                OWLAnnotationValue v = a.getValue();
                if (v instanceof OWLLiteral) {
                    String str = ((OWLLiteral) v).getLiteral();
                    LOG.info("mapping {} to {}", p, str);
                    expandExpressionMap.put(p.getIRI(), str);
                }
            }
        }
        for (OWLAnnotationProperty p : inputOntology
                .getAnnotationPropertiesInSignature(EXCLUDED)) {
            for (OWLAnnotation a : annotations(inputOntology.filterAxioms(
                    Filters.annotations, p.getIRI(), INCLUDED),
                    expandAssertionAP)) {
                OWLAnnotationValue v = a.getValue();
                if (v instanceof OWLLiteral) {
                    String str = ((OWLLiteral) v).getLiteral();
                    LOG.info("assertion mapping {} to {}", p, str);
                    expandAssertionToMap.put(p.getIRI(), str);
                }
            }
        }
    }

    @Nullable
    protected OWLClassExpression expandObject(Object filler,
            @Nonnull OWLObjectPropertyExpression p) {
        OWLClassExpression result = null;
        IRI iri = ((OWLObjectProperty) p).getIRI();
        IRI templateVal = null;
        if (expandExpressionMap.containsKey(iri)) {
            if (filler instanceof OWLObjectOneOf) {
                Set<OWLIndividual> inds = ((OWLObjectOneOf) filler)
                        .getIndividuals();
                if (inds.size() == 1) {
                    OWLIndividual ind = inds.iterator().next();
                    if (ind instanceof OWLNamedIndividual) {
                        templateVal = ((OWLNamedObject) ind).getIRI();
                    }
                }
            }
            if (filler instanceof OWLNamedObject) {
                templateVal = ((OWLNamedObject) filler).getIRI();
            }
            if (templateVal != null) {
                String tStr = expandExpressionMap.get(iri);
                String exStr = tStr.replaceAll("\\?Y",
                        manchesterSyntaxTool.getId(templateVal));
                try {
                    result = manchesterSyntaxTool
                            .parseManchesterExpression(exStr);
                } catch (ParserException e) {
                    LOG.error(e.getMessage(), e);
                }
            }
        }
        return result;
    }

    // Conversion of non-class expressions to MacroExpansionVisitor
    @Override
    public OWLAxiom visit(@Nonnull OWLSubClassOfAxiom axiom) {
        return dataFactory.getOWLSubClassOfAxiom(
                axiom.getSubClass().accept(classVisitor), axiom.getSuperClass()
                        .accept(classVisitor));
    }

    @Override
    public OWLAxiom visit(@Nonnull OWLDisjointClassesAxiom axiom) {
        Set<OWLClassExpression> ops = new HashSet<>();
        for (OWLClassExpression op : axiom.getClassExpressions()) {
            ops.add(op.accept(classVisitor));
        }
        return dataFactory.getOWLDisjointClassesAxiom(ops);
    }

    @Override
    public OWLAxiom visit(@Nonnull OWLDataPropertyDomainAxiom axiom) {
        return dataFactory.getOWLDataPropertyDomainAxiom(axiom.getProperty(),
                axiom.getDomain().accept(classVisitor));
    }

    @Override
    public OWLAxiom visit(@Nonnull OWLObjectPropertyDomainAxiom axiom) {
        return dataFactory.getOWLObjectPropertyDomainAxiom(axiom.getProperty(),
                axiom.getDomain().accept(classVisitor));
    }

    @Override
    public OWLAxiom visit(@Nonnull OWLObjectPropertyRangeAxiom axiom) {
        return dataFactory.getOWLObjectPropertyRangeAxiom(axiom.getProperty(),
                axiom.getRange().accept(classVisitor));
    }

    @Override
    public OWLAxiom visit(@Nonnull OWLDisjointUnionAxiom axiom) {
        Set<OWLClassExpression> descs = new HashSet<>();
        for (OWLClassExpression op : axiom.getClassExpressions()) {
            descs.add(op.accept(classVisitor));
        }
        return dataFactory.getOWLDisjointUnionAxiom(axiom.getOWLClass(), descs);
    }

    @Override
    public OWLAxiom visit(@Nonnull OWLDataPropertyRangeAxiom axiom) {
        return dataFactory.getOWLDataPropertyRangeAxiom(axiom.getProperty(),
                axiom.getRange().accept(rangeVisitor));
    }

    @Override
    public OWLAxiom visit(@Nonnull OWLClassAssertionAxiom axiom) {
        if (axiom.getClassExpression().isAnonymous()) {
            return dataFactory.getOWLClassAssertionAxiom(axiom
                    .getClassExpression().accept(classVisitor), axiom
                    .getIndividual());
        }
        return axiom;
    }

    @Override
    public OWLAxiom visit(@Nonnull OWLEquivalentClassesAxiom axiom) {
        Set<OWLClassExpression> ops = new HashSet<>();
        for (OWLClassExpression op : axiom.getClassExpressions()) {
            ops.add(op.accept(classVisitor));
        }
        return dataFactory.getOWLEquivalentClassesAxiom(ops);
    }

    @Override
    public OWLAxiom visit(OWLHasKeyAxiom axiom) {
        return axiom;
    }

    @Override
    public OWLAxiom visit(OWLNegativeObjectPropertyAssertionAxiom axiom) {
        return axiom;
    }

    @Override
    public OWLAxiom visit(OWLAsymmetricObjectPropertyAxiom axiom) {
        return axiom;
    }

    @Override
    public OWLAxiom visit(OWLReflexiveObjectPropertyAxiom axiom) {
        return axiom;
    }

    @Override
    public OWLAxiom visit(OWLEquivalentObjectPropertiesAxiom axiom) {
        return axiom;
    }

    @Override
    public OWLAxiom visit(OWLNegativeDataPropertyAssertionAxiom axiom) {
        return axiom;
    }

    @Override
    public OWLAxiom visit(OWLDifferentIndividualsAxiom axiom) {
        return axiom;
    }

    @Override
    public OWLAxiom visit(OWLDisjointDataPropertiesAxiom axiom) {
        return axiom;
    }

    @Override
    public OWLAxiom visit(OWLDisjointObjectPropertiesAxiom axiom) {
        return axiom;
    }

    @Override
    public OWLAxiom visit(OWLObjectPropertyAssertionAxiom axiom) {
        return axiom;
    }

    @Override
    public OWLAxiom visit(OWLFunctionalObjectPropertyAxiom axiom) {
        return axiom;
    }

    @Override
    public OWLAxiom visit(OWLSubObjectPropertyOfAxiom axiom) {
        return axiom;
    }

    @Override
    public OWLAxiom visit(OWLDeclarationAxiom axiom) {
        return axiom;
    }

    @Override
    public OWLAxiom visit(OWLAnnotationAssertionAxiom axiom) {
        return axiom;
    }

    @Override
    public OWLAxiom visit(OWLSymmetricObjectPropertyAxiom axiom) {
        return axiom;
    }

    @Override
    public OWLAxiom visit(OWLFunctionalDataPropertyAxiom axiom) {
        return axiom;
    }

    @Override
    public OWLAxiom visit(OWLEquivalentDataPropertiesAxiom axiom) {
        return axiom;
    }

    @Override
    public OWLAxiom visit(OWLDataPropertyAssertionAxiom axiom) {
        return axiom;
    }

    @Override
    public OWLAxiom visit(OWLTransitiveObjectPropertyAxiom axiom) {
        return axiom;
    }

    @Override
    public OWLAxiom visit(OWLIrreflexiveObjectPropertyAxiom axiom) {
        return axiom;
    }

    @Override
    public OWLAxiom visit(OWLSubDataPropertyOfAxiom axiom) {
        return axiom;
    }

    @Override
    public OWLAxiom visit(OWLInverseFunctionalObjectPropertyAxiom axiom) {
        return axiom;
    }

    @Override
    public OWLAxiom visit(OWLSameIndividualAxiom axiom) {
        return axiom;
    }

    @Override
    public OWLAxiom visit(OWLSubPropertyChainOfAxiom axiom) {
        return axiom;
    }

    @Override
    public OWLAxiom visit(OWLInverseObjectPropertiesAxiom axiom) {
        return axiom;
    }

    @Override
    public OWLAxiom visit(SWRLRule rule) {
        return rule;
    }

    @Override
    public OWLAxiom visit(OWLAnnotationPropertyDomainAxiom axiom) {
        return axiom;
    }

    @Override
    public OWLAxiom visit(OWLAnnotationPropertyRangeAxiom axiom) {
        return axiom;
    }

    @Override
    public OWLAxiom visit(OWLSubAnnotationPropertyOfAxiom axiom) {
        return axiom;
    }

    @Override
    public OWLAxiom visit(OWLDatatypeDefinitionAxiom axiom) {
        return axiom;
    }
}
