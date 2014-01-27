/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.camunda.bpm.model.bpmn.impl.instance;

import org.camunda.bpm.model.bpmn.instance.FlowElement;
import org.camunda.bpm.model.bpmn.instance.FlowNode;
import org.camunda.bpm.model.bpmn.instance.SequenceFlow;
import org.camunda.bpm.model.xml.ModelBuilder;
import org.camunda.bpm.model.xml.impl.instance.ModelTypeInstanceContext;
import org.camunda.bpm.model.xml.type.ModelElementTypeBuilder;
import org.camunda.bpm.model.xml.type.attribute.Attribute;
import org.camunda.bpm.model.xml.type.child.ChildElement;
import org.camunda.bpm.model.xml.type.child.SequenceBuilder;
import org.camunda.bpm.model.xml.type.reference.AttributeReference;

import static org.camunda.bpm.model.bpmn.impl.BpmnModelConstants.*;
import static org.camunda.bpm.model.xml.type.ModelElementTypeBuilder.ModelTypeInstanceProvider;

/**
 * @author Sebastian Menski
 */
public class SequenceFlowImpl extends FlowElementImpl implements SequenceFlow {

  private static AttributeReference<FlowNode> sourceRefAttribute;
  private static AttributeReference<FlowNode> targetRefAttribute;
  private static Attribute<Boolean> isImmediateAttribute;
  private static ChildElement<ConditionExpression> conditionExpressionCollection;

  public static void registerType(ModelBuilder modelBuilder) {
    ModelElementTypeBuilder typeBuilder = modelBuilder.defineType(SequenceFlow.class, BPMN_ELEMENT_SEQUENCE_FLOW)
      .namespaceUri(BPMN20_NS)
      .extendsType(FlowElement.class)
      .instanceProvider(new ModelTypeInstanceProvider<SequenceFlow>() {
        public SequenceFlow newInstance(ModelTypeInstanceContext instanceContext) {
          return new SequenceFlowImpl(instanceContext);
        }
      });

    sourceRefAttribute = typeBuilder.stringAttribute(BPMN_ATTRIBUTE_SOURCE_REF)
      .required()
      .idAttributeReference(FlowNode.class)
      .build();

    targetRefAttribute = typeBuilder.stringAttribute(BPMN_ATTRIBUTE_TARGET_REF)
      .required()
      .idAttributeReference(FlowNode.class)
      .build();

    isImmediateAttribute = typeBuilder.booleanAttribute(BPMN_ATTRIBUTE_IS_IMMEDIATE)
      .build();

    SequenceBuilder sequenceBuilder = typeBuilder.sequence();

    conditionExpressionCollection = sequenceBuilder.element(ConditionExpression.class)
      .build();

    typeBuilder.build();
  }

  public SequenceFlowImpl(ModelTypeInstanceContext context) {
    super(context);
  }

  public FlowNode getSourceRef() {
    return sourceRefAttribute.getReferenceTargetElement(this);
  }

  public void setSourceRef(FlowNode sourceRef) {
    sourceRefAttribute.setReferenceTargetElement(this, sourceRef);
  }

  public FlowNode getTargetRef() {
    return targetRefAttribute.getReferenceTargetElement(this);
  }

  public void setTargetRef(FlowNode targetRef) {
    targetRefAttribute.setReferenceTargetElement(this, targetRef);
  }

  public boolean isImmediate() {
    return isImmediateAttribute.getValue(this);
  }

  public void setIsImmediate(boolean isImmediate) {
    isImmediateAttribute.setValue(this, isImmediate);
  }

  public ConditionExpression getConditionExpression() {
    return conditionExpressionCollection.getChild(this);
  }

  public void setConditionExpression(ConditionExpression conditionExpression) {
    conditionExpressionCollection.setChild(this, conditionExpression);
  }
}