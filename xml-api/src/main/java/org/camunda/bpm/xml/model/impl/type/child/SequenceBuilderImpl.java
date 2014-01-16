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
package org.camunda.bpm.xml.model.impl.type.child;

import java.util.ArrayList;
import java.util.List;

import org.camunda.bpm.xml.model.Model;
import org.camunda.bpm.xml.model.impl.ModelBuildOperation;
import org.camunda.bpm.xml.model.impl.type.ModelElementTypeImpl;
import org.camunda.bpm.xml.model.instance.ModelElementInstance;
import org.camunda.bpm.xml.model.type.ChildElementBuilder;
import org.camunda.bpm.xml.model.type.ChildElementCollectionBuilder;
import org.camunda.bpm.xml.model.type.SequenceBuilder;

/**
 * @author Daniel Meyer
 *
 */
public class SequenceBuilderImpl implements SequenceBuilder, ModelBuildOperation {

  private final ModelElementTypeImpl elementType;

  private final List<ModelBuildOperation> modelBuildOperations = new ArrayList<ModelBuildOperation>();

  public SequenceBuilderImpl(ModelElementTypeImpl modelType) {
    this.elementType = modelType;
  }

  public <T extends ModelElementInstance> ChildElementBuilder<T> element(Class<T> childElementType, String localName) {
    return element(childElementType, localName, elementType.getTypeNamespace());
  }

  public <T extends ModelElementInstance> ChildElementBuilder<T> element(Class<T> childElementType, String localName, String namespaceUri) {
    ChildElementBuilderImpl<T> builder = new ChildElementBuilderImpl<T>(childElementType, localName, namespaceUri, elementType);
    modelBuildOperations.add(builder);
    return builder;
  }

  public <T extends ModelElementInstance> ChildElementCollectionBuilder<T> elementCollection(Class<T> childElementType, String localName) {
    return elementCollection(childElementType, localName, elementType.getTypeNamespace());
  }

  public <T extends ModelElementInstance> ChildElementCollectionBuilder<T> elementCollection(Class<T> childElementType, String localName, String namespaceUri) {
    ChildElementCollectionBuilderImpl<T> builder = new ChildElementCollectionBuilderImpl<T>(childElementType, localName, namespaceUri, elementType);
    modelBuildOperations.add(builder);
    return builder;
  }

  public <T extends ModelElementInstance> ChildElementCollectionBuilder<T> elementCollection(Class<T> childElementType) {
    ChildElementCollectionBuilderImpl<T> builder = new ChildElementCollectionBuilderImpl<T>(childElementType, elementType);
    modelBuildOperations.add(builder);
    return builder;
  }

  public void performModelBuild(Model model) {
    for (ModelBuildOperation operation : modelBuildOperations) {
      operation.performModelBuild(model);
    }
  }

}