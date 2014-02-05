/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.camunda.bpm.model.bpmn.impl.instance;

import org.camunda.bpm.model.bpmn.Query;
import org.camunda.bpm.model.bpmn.impl.QueryImpl;
import org.camunda.bpm.model.bpmn.instance.FlowElement;
import org.camunda.bpm.model.bpmn.instance.FlowNode;
import org.camunda.bpm.model.bpmn.instance.SequenceFlow;
import org.camunda.bpm.model.xml.ModelBuilder;
import org.camunda.bpm.model.xml.impl.instance.ModelTypeInstanceContext;
import org.camunda.bpm.model.xml.type.ModelElementTypeBuilder;
import org.camunda.bpm.model.xml.type.child.SequenceBuilder;
import org.camunda.bpm.model.xml.type.reference.ElementReferenceCollection;

import java.util.Collection;
import java.util.HashSet;

import static org.camunda.bpm.model.bpmn.impl.BpmnModelConstants.BPMN20_NS;
import static org.camunda.bpm.model.bpmn.impl.BpmnModelConstants.BPMN_ELEMENT_FLOW_NODE;

/**
 * The BPMN flowNode element
 *
 * @author Sebastian Menski
 */
public abstract class FlowNodeImpl extends FlowElementImpl implements FlowNode {

  private static ElementReferenceCollection<SequenceFlow, Incoming> incomingCollection;
  private static ElementReferenceCollection<SequenceFlow, Outgoing> outgoingCollection;

  public static void registerType(ModelBuilder modelBuilder) {
    ModelElementTypeBuilder typeBuilder = modelBuilder.defineType(FlowNode.class, BPMN_ELEMENT_FLOW_NODE)
      .namespaceUri(BPMN20_NS)
      .extendsType(FlowElement.class)
      .abstractType();

    SequenceBuilder sequenceBuilder = typeBuilder.sequence();

    incomingCollection = sequenceBuilder.elementCollection(Incoming.class)
      .qNameElementReferenceCollection(SequenceFlow.class)
      .build();

    outgoingCollection = sequenceBuilder.elementCollection(Outgoing.class)
      .qNameElementReferenceCollection(SequenceFlow.class)
      .build();

    typeBuilder.build();
  }

  public FlowNodeImpl(ModelTypeInstanceContext context) {
    super(context);
  }

  public Collection<SequenceFlow> getIncoming() {
    return incomingCollection.getReferenceTargetElements(this);
  }

  public Collection<SequenceFlow> getOutgoing() {
    return outgoingCollection.getReferenceTargetElements(this);
  }

  public Query<FlowNode> getPreviousNodes() {
    Collection<FlowNode> previousNodes = new HashSet<FlowNode>();
    for (SequenceFlow sequenceFlow : getIncoming()) {
      previousNodes.add(sequenceFlow.getSource());
    }
    return new QueryImpl<FlowNode>(previousNodes);
  }

  public Query<FlowNode> getSucceedingNodes() {
    Collection<FlowNode> succeedingNodes = new HashSet<FlowNode>();
    for (SequenceFlow sequenceFlow : getOutgoing()) {
      succeedingNodes.add(sequenceFlow.getTarget());
    }
    return new QueryImpl<FlowNode>(succeedingNodes);
  }
}
