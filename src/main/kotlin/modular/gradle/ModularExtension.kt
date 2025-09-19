/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.gradle

import modular.core.spec.GeneralSpec
import modular.core.spec.LinkTypesSpec
import modular.core.spec.ModulePathTransformSpec
import modular.core.spec.ModuleType
import modular.core.spec.OutputSpec
import modular.core.spec.Spec
import modular.graphviz.spec.GraphVizSpec
import modular.mermaid.spec.MermaidSpec
import org.gradle.api.Action
import org.gradle.api.NamedDomainObjectContainer

/**
 * Main entry point for configuring the plugin from your Gradle script.
 */
interface ModularExtension {
  val general: GeneralSpec
  @ModularDsl fun general(action: Action<GeneralSpec>)

  val modulePathTransforms: ModulePathTransformSpec
  @ModularDsl fun modulePathTransforms(action: Action<ModulePathTransformSpec>)

  val moduleTypes: NamedDomainObjectContainer<ModuleType>
  @ModularDsl fun moduleTypes(action: Action<NamedDomainObjectContainer<ModuleType>>)

  val linkTypes: LinkTypesSpec
  @ModularDsl fun linkTypes(action: Action<LinkTypesSpec>)

  val outputs: OutputSpec
  @ModularDsl fun outputs(action: Action<OutputSpec>)

  val specs: NamedDomainObjectContainer<Spec>
  @ModularDsl fun specs(action: Action<NamedDomainObjectContainer<Spec>>)

  val graphViz: GraphVizSpec
  @ModularDsl fun graphViz()
  @ModularDsl fun graphViz(action: Action<GraphVizSpec>)

  val mermaid: MermaidSpec
  @ModularDsl fun mermaid()
  @ModularDsl fun mermaid(action: Action<MermaidSpec>)
}
